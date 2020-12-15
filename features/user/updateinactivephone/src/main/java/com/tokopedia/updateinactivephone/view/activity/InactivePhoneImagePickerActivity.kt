package com.tokopedia.updateinactivephone.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.imagepicker.common.EXTRA_IMAGE_PICKER_BUILDER
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.view.fragment.InactivePhoneCameraFragment

class InactivePhoneImagePickerActivity : BaseSimpleActivity() {

    private var cameraViewMode: CameraViewMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent != null && intent.extras != null && intent.extras?.containsKey(EXTRA_IMAGE_PICKER_BUILDER) as Boolean) {
            val bundle = intent.getBundleExtra(EXTRA_IMAGE_PICKER_BUILDER)
            cameraViewMode = bundle?.getParcelable<CameraViewMode>(EXTRA_CAMERA_MODE) as CameraViewMode
        }
        title = getString(R.string.text_select_image)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return cameraViewMode?.let {
            InactivePhoneCameraFragment.instance(it)
        } as Fragment
    }

    companion object {
        private const val EXTRA_CAMERA_MODE = "cameraMode"

        fun createIntentCamera(context: Context?, cameraViewMode: CameraViewMode): Intent {
            val bundle = Bundle()
            val intent = Intent(context, InactivePhoneImagePickerActivity::class.java)
            bundle.putParcelable(EXTRA_CAMERA_MODE, cameraViewMode)
            return intent
        }
    }
}