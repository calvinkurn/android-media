package com.tokopedia.gopay.kyc.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.otaliastudios.cameraview.controls.Facing
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.analytics.GoPayKycConstants
import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay.kyc.presentation.activity.GoPayCameraKtpActivity
import com.tokopedia.gopay.kyc.presentation.fragment.base.GoPayKycBaseCameraFragment
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.pxToDp
import kotlinx.android.synthetic.main.fragment_gopay_selfie_ktp_layout.*
import kotlinx.android.synthetic.main.gopay_camera_action_layout.*

class GoPaySelfieKtpFragment : GoPayKycBaseCameraFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_selfie_ktp_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        handleProceedButton()
    }

    private fun handleProceedButton() {
        proceedPhotoButton.setOnClickListener {
            sendAnalytics(
                GoPayKycEvent.Click.ConfirmPhotoEvent(
                    GoPayKycConstants.Label.SELFIE_KTP,
                    GoPayKycConstants.ScreenNames.GOPAY_KYC_SELFIE_KTP_INSTRUCTION_PAGE
                )
            )
            proceedToNextStep()
        }
    }

    private fun initViews() {
        kycHeader.title = getString(R.string.gopay_kyc_selfie_take_ktp_text)
        cameraView = camera
        cameraView?.facing = Facing.FRONT
        cameraView?.zoom = 0f
        capturedImageView = capturedImage
        cameraSwitchImage.gone()
        retakeButton = resetPhotoButton
        shutterImageView = cameraShutterImage
        reviewPhotoLayout = reviewPhotoGroup
        ktpInstructionText = photoInstructionTV
        cameraLayout = gopayCameraLayout

        val calculatedMargin =
            context?.pxToDp((getScreenHeight().toFloat() * HEIGHT_RATIO_CUTOUT).toInt()) ?: 0

        val params = photoInstructionTV.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = calculatedMargin.toInt()
        photoInstructionTV.layoutParams = params
        setCaptureInstruction()
        kycHeader.setNavigationOnClickListener { handleBackPressForGopay() }
    }

    override fun setCaptureInstruction() {
        photoInstructionTV.text =
            getString(R.string.gopay_kyc_selfie_ktp_capture_instruction_text)
    }

    override fun setVerificationInstruction() {
        photoInstructionTV.text =
            getString(R.string.gopay_kyc_selfie_ktp_capture_verification_text)
    }

    override fun proceedToNextStep() {
        val intent = Intent().apply {
            putExtra(
                GoPayCameraKtpActivity.SELFIE_KTP_IMAGE_PATH,
                getCapturedImagePath()
            )
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun getKycType() = GoPayKycConstants.Label.SELFIE_KTP
    override fun getPageSource() = GoPayKycConstants.ScreenNames.GOPAY_KYC_REVIEW_SELFIE_CAPTURE_PAGE
    override fun changeHeaderTitle() {
        kycHeader.title = getString(R.string.gopay_kyc_selfie_ktp_check_title)
    }
    override fun getScreenName() = null

    companion object {
        const val HEIGHT_RATIO_CUTOUT = 496 / 640f
        const val TEXT_MARGIN = 16

        fun newInstance() = GoPaySelfieKtpFragment()
    }
}