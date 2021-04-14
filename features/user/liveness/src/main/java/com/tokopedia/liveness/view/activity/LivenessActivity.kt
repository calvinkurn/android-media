package com.tokopedia.liveness.view.activity

import ai.advance.common.utils.ScreenUtil
import ai.advance.liveness.lib.GuardianLivenessDetectionSDK
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.liveness.R
import com.tokopedia.liveness.di.DaggerLivenessDetectionComponent
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.utils.LivenessConstants.NOT_SUPPORT_LIVENESS
import com.tokopedia.liveness.view.OnBackListener
import com.tokopedia.liveness.view.fragment.LivenessFragment

class LivenessActivity : AppCompatActivity(), HasComponent<LivenessDetectionComponent> {

    override fun getComponent(): LivenessDetectionComponent {
        return DaggerLivenessDetectionComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private var livenessFragment: LivenessFragment? = null
    private var errorDialog: AlertDialog? = null
    private var extras = Bundle()

    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        GuardianLivenessDetectionSDK.initOffLine(application)
        GuardianLivenessDetectionSDK.letSDKHandleCameraPermission()
        GuardianLivenessDetectionSDK.setDeviceType(GuardianLivenessDetectionSDK.DeviceType.RealPhone)

        setContentView(R.layout.activity_liveness)
        ScreenUtil.init(this)

        if (GuardianLivenessDetectionSDK.isSDKHandleCameraPermission && !allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onResume() {
        attachFragment()
        super.onResume()
    }

    private fun attachFragment() {
        if (allPermissionsGranted()) {
            if (GuardianLivenessDetectionSDK.isDeviceSupportLiveness) {
                intent.extras?.let {
                    extras = it
                }
                livenessFragment = LivenessFragment.newInstance(extras)
                if (livenessFragment?.isAdded == false) {
                    livenessFragment?.let { fragment ->
                        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss()
                    }
                }
            } else {
                showNotSupportedDialog()
            }
        }
    }

    fun showNotSupportedDialog(){
        val dialog = DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.liveness_device_not_support_title))
        dialog.setDescription(getString(R.string.liveness_device_not_support))
        dialog.setPrimaryCTAText(getString(R.string.liveness_device_not_support_primary_button))
        dialog.setSecondaryCTAText(getString(R.string.liveness_device_not_support_secondary_button))

        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            setResult(NOT_SUPPORT_LIVENESS)
            finish()
        }

        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            setResult(RESULT_OK)
            finish()
        }
        dialog.show()
    }

    override fun onPause() {
        if (livenessFragment != null && livenessFragment?.isAdded == true) {
            livenessFragment?.release()
            livenessFragment?.let { fragment ->
                supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
            }
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (errorDialog != null && errorDialog?.isShowing == true) {
            errorDialog?.dismiss()
        }
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun allGranted(grantResults: IntArray): Boolean {
        var hasPermission = true
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false
            }
        }
        return hasPermission
    }

    private fun onPermissionRefused() {
        AlertDialog.Builder(this).setMessage(getString(R.string.liveness_no_camera_permission)).setPositiveButton(getString(R.string.liveness_perform)) { dialog, which -> finish() }.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (!allGranted(grantResults)) {
                onPermissionRefused()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (livenessFragment != null && livenessFragment is OnBackListener) {
            (livenessFragment as OnBackListener).trackOnBackPressed()
        }
    }

    companion object {

        private const val PERMISSIONS_REQUEST_CODE = 2020
    }

}
