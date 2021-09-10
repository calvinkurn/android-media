package com.tokopedia.gopay_kyc.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.otaliastudios.cameraview.controls.Facing
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.presentation.activity.GoPayCameraKtpActivity
import com.tokopedia.kotlin.extensions.view.getScreenHeight
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
            proceedToNextStep()
        }
    }

    private fun initViews() {
        cameraView = camera
        cameraView?.facing = Facing.FRONT
        cameraView?.zoom = 0f
        capturedImageView = capturedImage
        reverseCamera = cameraSwitchImage
        retakeButton = resetPhotoButton
        shutterImageView = cameraShutterImage
        cameraControlLayout = cameraControlGroup
        reviewPhotoLayout = reviewPhotoGroup
        ktpInstructionText = photoInstructionTV
        cameraLayout = gopayCameraLayout


        val calculatedMargin =
            context?.pxToDp((getScreenHeight().toFloat() * HEIGHT_RATIO_CUTOUT).toInt()) ?: 0

        val params = photoInstructionTV.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = calculatedMargin.toInt()
        photoInstructionTV.layoutParams = params
        setCaptureInstruction()
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


    override fun getScreenName() = null

    companion object {
        const val HEIGHT_RATIO_CUTOUT = 496 / 640f

        fun newInstance() = GoPaySelfieKtpFragment()
    }
}