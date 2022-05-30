package com.tokopedia.liveness.view

import ai.advance.common.utils.ScreenUtil
import ai.advance.core.PermissionActivity
import ai.advance.enums.DeviceType
import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.GuardianLivenessDetectionSDK as livenessSdk
import android.Manifest
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.liveness.R
import com.tokopedia.liveness.di.DaggerLivenessDetectionComponent
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.utils.LivenessConstants

open class LivenessActivity: PermissionActivity(), HasComponent<LivenessDetectionComponent> {

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

        setContentView(R.layout.activity_revamp_liveness)
        ScreenUtil.init(this)

        livenessSdk.initOffLine(application)
        livenessSdk.letSDKHandleCameraPermission()
        livenessSdk.setDeviceType(DeviceType.RealPhone)
        livenessSdk.setActionSequence(false,
            Detector.DetectionType.MOUTH,
            Detector.DetectionType.BLINK,
            Detector.DetectionType.POS_YAW
        )

        intent?.data?.let {
            val projectId = it.getQueryParameter(ApplinkConstInternalGlobal.PARAM_PROJECT_ID)

            intent?.extras?.apply {
                putString(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
            }
        }

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
            .replace(R.id.livenessParentView, fragment)
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
        return arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionGranted() { }

    override fun onPermissionRefused() {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.liveness_no_camera_permission))
                .setPositiveButton(getString(R.string.liveness_perform)) { _, _ ->
                    finish()
                }.create().show()
    }
}