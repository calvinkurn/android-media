package ai.advance.liveness.view.activity

import ai.advance.common.utils.ScreenUtil
import ai.advance.liveness.view.OnBackListener
import ai.advance.liveness.R
import ai.advance.liveness.view.fragment.LivenessFragment
import ai.advance.liveness.lib.GuardianLivenessDetectionSDK
import ai.advance.liveness.lib.LivenessResult
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LivenessActivity : AppCompatActivity() {
    private var mLivenessFragment: LivenessFragment? = null
    private var mErrorDialog: AlertDialog? = null

    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        GuardianLivenessDetectionSDK.initOffLine(application)
        GuardianLivenessDetectionSDK.letSDKHandleCameraPermission()
        GuardianLivenessDetectionSDK.setDeviceType(if (Build.CPU_ABI == "x86") GuardianLivenessDetectionSDK.DeviceType.Emulator else GuardianLivenessDetectionSDK.DeviceType.RealPhone)

        setContentView(R.layout.activity_liveness)
        ScreenUtil.init(this)

        changeAppBrightness(255)
        if (GuardianLivenessDetectionSDK.isSDKHandleCameraPermission && !allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun changeAppBrightness(brightness: Int) {
        val window = this.window
        val lp = window.attributes
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        } else {
            lp.screenBrightness = (if (brightness <= 0) 1 else brightness) / 255f
        }
        window.attributes = lp
    }

    override fun onResume() {
        attachFragment()
        super.onResume()
    }

    private fun attachFragment() {
        if (allPermissionsGranted()) {
            if (GuardianLivenessDetectionSDK.isDeviceSupportLiveness) {
                mLivenessFragment = LivenessFragment.newInstance()
                if (mLivenessFragment?.isAdded == false) {
                    mLivenessFragment?.let {fragment ->
                        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss()
                    }
                }
            } else {
                val errorMsg = getString(R.string.liveness_device_not_support)
                mErrorDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(errorMsg)
                        .setPositiveButton(R.string.liveness_perform) { dialog, which ->
                            LivenessResult.errorMsg = errorMsg
                            setResult(Activity.RESULT_OK)
                            finish()
                        }.create()
                mErrorDialog?.show()
            }
        }
    }

    override fun onPause() {
        if (mLivenessFragment != null && mLivenessFragment?.isAdded == true) {
            mLivenessFragment?.release()
            mLivenessFragment?.let {fragment ->
                supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
            }
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (mErrorDialog != null && mErrorDialog?.isShowing == true) {
            mErrorDialog?.dismiss()
        }
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 0) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //----------request permission codes--------------
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

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (!allGranted(grantResults)) {
                onPermissionRefused()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mLivenessFragment != null && mLivenessFragment is OnBackListener) {
            (mLivenessFragment as OnBackListener).trackOnBackPressed()
        }
    }

    companion object {

        private const val PERMISSIONS_REQUEST_CODE = 2020
    }

}
