package com.tokopedia.updateinactivephone.features.onboarding.regular

import com.tokopedia.imageassets.ImageUrl

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.REQUEST_CAPTURE_SELFIE
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.features.imagepicker.InactivePhoneImagePickerActivity
import com.tokopedia.updateinactivephone.features.onboarding.BaseInactivePhoneOnboardingFragment
import com.tokopedia.utils.image.ImageUtils

class InactivePhoneCaptureSelfieFragment : BaseInactivePhoneOnboardingFragment() {

    override fun initView() {
        super.initView()

        updateImageHeader(IMAGE_SELFIE_SAMPLE)
        updateTitle(getString(R.string.text_title_selfie))
        updateDescription(getString(R.string.text_onboarding_selfie_description))
    }

    override fun onButtonNextClicked() {
        trackerRegular.clickOnNextButtonSelfiewOnboarding()

        val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.SELFIE)
        startActivityForResult(intent, REQUEST_CAPTURE_SELFIE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_SELFIE && resultCode == Activity.RESULT_OK) {
            context?.let {
                (it as InactivePhoneRegularActivity).gotoUploadData()
            }
        }
    }

    companion object {
        private const val IMAGE_SELFIE_SAMPLE = ImageUrl.IMAGE_SELFIE_SAMPLE
    }
}