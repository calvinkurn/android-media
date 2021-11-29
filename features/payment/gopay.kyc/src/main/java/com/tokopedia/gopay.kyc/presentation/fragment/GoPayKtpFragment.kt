package com.tokopedia.gopay.kyc.presentation.fragment

import android.app.Activity.RESULT_OK
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
import com.tokopedia.gopay.kyc.presentation.activity.GoPayCameraKtpActivity.Companion.KTP_IMAGE_PATH
import com.tokopedia.gopay.kyc.presentation.fragment.base.GoPayKycBaseCameraFragment
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.pxToDp
import kotlinx.android.synthetic.main.fragment_gopay_ktp_layout.*
import kotlinx.android.synthetic.main.gopay_camera_action_layout.*

class GoPayKtpFragment : GoPayKycBaseCameraFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_ktp_layout, container, false)
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
                    GoPayKycConstants.Label.KTP,
                    GoPayKycConstants.ScreenNames.GOPAY_KYC_REVIEW_KTP_PAGE
                )
            )
            proceedToNextStep()
        }
    }

    private fun initViews() {
        kycHeader.title = getString(R.string.gopay_kyc_take_ktp_text)
        cameraView = camera
        cameraView?.facing = Facing.BACK
        cameraView?.zoom = 0f
        capturedImageView = capturedImage
        reverseCamera = cameraSwitchImage
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
            getString(R.string.gopay_kyc_ktp_capture_instruction_text)
    }

    override fun setVerificationInstruction() {
        photoInstructionTV.text =
            getString(R.string.gopay_kyc_ktp_capture_verification_text)
    }

    override fun getKycType() = GoPayKycConstants.Label.KTP
    override fun getPageSource() = GoPayKycConstants.ScreenNames.GOPAY_KYC_REVIEW_KTP_PAGE
    override fun changeHeaderTitle() {
        kycHeader.title = getString(R.string.gopay_kyc_check_ktp_title)
    }
    override fun proceedToNextStep() {
        val intent = Intent().apply { putExtra(KTP_IMAGE_PATH, getCapturedImagePath()) }
        activity?.setResult(RESULT_OK, intent)
        activity?.finish()
    }

    override fun getScreenName() = null

    companion object {
        const val TEXT_MARGIN = 16
        const val HEIGHT_RATIO_CUTOUT = 398 / 640f
        fun newInstance() = GoPayKtpFragment()
    }
}