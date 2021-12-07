package com.tokopedia.updateinactivephone.features.onboarding.regular

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

        updateTitle(getString(R.string.text_title_selfie))
        updateDescription(getString(R.string.text_onboarding_selfie_description))

        viewBinding?.imgHeader?.let {
            ImageUtils.clearImage(it)
            context?.let { ctx ->
                it.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_sample_selfie))
            }
        }
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
}