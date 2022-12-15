package com.tokopedia.liveness.view

import ai.advance.common.utils.ScreenUtil
import ai.advance.core.PermissionActivity
import ai.advance.enums.DeviceType
import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.GuardianLivenessDetectionSDK as livenessSdk
import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.liveness.R
import com.tokopedia.liveness.di.DaggerLivenessDetectionComponent
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.utils.LivenessConstants
import com.tokopedia.liveness.utils.LivenessConstants.REMOTE_CONFIG_KEY_LIVENESS_RANDOM_DETECTION
import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

open class LivenessActivity: PermissionActivity(), HasComponent<LivenessDetectionComponent> {

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var fragment: Fragment? = null

    override fun getComponent(): LivenessDetectionComponent {
        return DaggerLivenessDetectionComponent.builder()
            .baseAppComponent((this.application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        component.inject(this)

        livenessSdk.initOffLine(application)
        livenessSdk.letSDKHandleCameraPermission()
        livenessSdk.setDeviceType(DeviceType.RealPhone)
        livenessSdk.setActionSequence(
            isRandomDetection(),
            Detector.DetectionType.MOUTH,
            Detector.DetectionType.BLINK,
            Detector.DetectionType.POS_YAW
        )

        intent?.data?.let {
            val projectId = it.getQueryParameter(PARAM_PROJECT_ID)?.toIntOrNull()

            intent?.extras?.apply {
                projectId?.let { it1 -> putInt(PARAM_PROJECT_ID, it1) }
            }
        }

        setContentView(com.tokopedia.liveness.R.layout.activity_revamp_liveness)
        ScreenUtil.init(this)

        if (!allPermissionsGranted() && livenessSdk.isSDKHandleCameraPermission()) {
            requestPermissions()
        }
    }

    override fun onBackPressed() {
        if (fragment != null && fragment is OnBackListener) {
            (fragment as OnBackListener).trackOnBackPressed()
        }

        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        loadFragment()
    }
    
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    private fun loadFragment() {
        if (allPermissionsGranted()) {
            if (livenessSdk.isDeviceSupportLiveness()) {
                if (fragment == null) {
                    fragment = intent?.extras?.let {
                        LivenessFragment.newInstance(it)
                    }
                }

                if (fragment?.isAdded == false) {
                    fragment?.let { fragment ->
                        replaceFragment(fragment)
                    }
                }
            } else {
                alertDialogDeviceNotSupported()
            }
        }
    }

    open fun replaceFragment(fragment: Fragment) {
        if (fragment is LivenessErrorFragment) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(com.tokopedia.liveness.R.id.livenessParentView, fragment)
            .commitAllowingStateLoss()
    }

    open fun alertDialogDeviceNotSupported(){
        val dialog = DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.liveness_device_not_support_title))
        dialog.setDescription(getString(R.string.liveness_device_not_support))
        dialog.setPrimaryCTAText(getString(R.string.liveness_device_not_support_primary_button))
        dialog.setSecondaryCTAText(getString(R.string.liveness_device_not_support_secondary_button))

        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            setResult(LivenessConstants.NOT_SUPPORT_LIVENESS)
            finish()
        }

        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            setResult(RESULT_OK)
            finish()
        }
        dialog.show()
    }

    override fun getRequiredPermissions(): Array<String> {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        }else{
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

    }

    override fun onPermissionGranted() { }

    override fun onPermissionRefused() {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.liveness_no_camera_permission))
                .setPositiveButton(getString(R.string.liveness_perform)) { _, _ ->
                    finish()
                }.create().show()
    }

    private fun isRandomDetection(): Boolean = remoteConfig.getBoolean(
        REMOTE_CONFIG_KEY_LIVENESS_RANDOM_DETECTION,
        false
    )
}
