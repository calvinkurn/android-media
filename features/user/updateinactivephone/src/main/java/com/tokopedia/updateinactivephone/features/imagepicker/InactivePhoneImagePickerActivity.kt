package com.tokopedia.updateinactivephone.features.imagepicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode

class InactivePhoneImagePickerActivity : BaseSimpleActivity() {

    private var cameraViewMode: CameraViewMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent != null && intent.extras != null && intent.extras?.containsKey(EXTRA_CAMERA_MODE) == true) {
            val bundle = intent.getBundleExtra(EXTRA_CAMERA_MODE)
            cameraViewMode = bundle?.getParcelable(EXTRA_CAMERA_MODE)
        }
        title = getString(R.string.text_select_image)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return if (cameraViewMode != null) {
            cameraViewMode?.let {
                InactivePhoneCameraFragment.instance(it)
            }
        } else {
            setResult(Activity.RESULT_CANCELED)
            finish()
            null
        }
    }

    companion object {
        private const val EXTRA_CAMERA_MODE = "cameraMode"

        fun createIntentCamera(context: Context?, cameraViewMode: CameraViewMode): Intent {
            val bundle = Bundle()
            val intent = Intent(context, InactivePhoneImagePickerActivity::class.java)
            bundle.putParcelable(EXTRA_CAMERA_MODE, cameraViewMode)
            intent.putExtra(EXTRA_CAMERA_MODE, bundle)
            return intent
        }
    }
}