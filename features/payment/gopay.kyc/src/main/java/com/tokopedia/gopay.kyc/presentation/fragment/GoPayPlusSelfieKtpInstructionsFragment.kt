package com.tokopedia.gopay.kyc.presentation.fragment

import com.tokopedia.imageassets.ImageUrl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.analytics.GoPayKycConstants
import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay.kyc.presentation.fragment.base.GoPayKycBaseFragment
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycNavigationListener
import com.tokopedia.gopay.kyc.presentation.viewholder.GoPayKycInstructionItemViewHolder
import com.tokopedia.gopay.kyc.utils.ReviewCancelDialog
import kotlinx.android.synthetic.main.fragment_gopay_ktp_instructions_layout.*

class GoPayPlusSelfieKtpInstructionsFragment : GoPayKycBaseFragment() {

    private val instructionStringResList = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateInstructions()
    }

    private fun populateInstructions() {
        instructionStringResList.add(R.string.gopay_selfie_ktp_instruction_1)
        instructionStringResList.add(R.string.gopay_selfie_ktp_instruction_2)
        instructionStringResList.add(R.string.gopay_selfie_ktp_instruction_3)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_ktp_instructions_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        takePhotoButton.setOnClickListener {
            sendAnalytics(
                GoPayKycEvent.Click.TakePhotoEvent(
                    GoPayKycConstants.Label.SELFIE_KTP,
                    GoPayKycConstants.ScreenNames.GOPAY_KYC_SELFIE_KTP_INSTRUCTION_PAGE
                )
            )
            openSelfieKtpCamera()
        }
    }

    private fun initViews() {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        context?.let {
            stepDivider.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            goPayKtpStep1.text = it.getString(R.string.gopay_kyc_step_2)
            goPayUploadPhotoTitle.text =
                it.getString(R.string.gopay_kyc_selfie_ktp_upload_header_text)
            goPayKycTypeTitle.text = it.getString(R.string.gopay_kyc_selfie_ktp_text)
            goPayKycTypeDescription.text =
                it.getString(R.string.gopay_kyc_selfie_ktp_description_text)
            goPayPhotoTitle.text = it.getString(R.string.gopay_kyc_selfie_ktp_photo_front_text)
            takePhotoButton.text = it.getString(R.string.gopay_kyc_selfie_take_ktp_text)
            for (instructionItem in instructionStringResList) {
                instructionLL.addView(
                    GoPayKycInstructionItemViewHolder(
                        it,
                        layoutParams
                    ).bindData(instructionItem)
                )
            }
        }
        goPayIdImage.loadRemoteImageDrawable(INSTRUCTION_IMAGE_NAME, INSTRUCTION_IMAGE_PATH)
        goPayDoImage.loadRemoteImageDrawable(CORRECT_IMAGE_NAME, CORRECT_IMAGE_PATH)
        goPayDontImage.loadRemoteImageDrawable(INCORRECT_IMAGE_NAME, INCORRECT_IMAGE_PATH)
    }

    private fun openSelfieKtpCamera() {
        activity?.let { (it as GoPayKycNavigationListener).openSelfieKtpCameraScreen() }
    }

    override fun getScreenName() = null
    override fun initInjector() {}
    private fun sendAnalytics(event: GoPayKycEvent) =
        activity?.let { (it as GoPayKycNavigationListener).sendAnalytics(event) }


    override fun handleBackPressForGopay() {
        val event =
            GoPayKycEvent.Click.BackPressEvent(GoPayKycConstants.ScreenNames.GOPAY_KYC_SELFIE_KTP_INSTRUCTION_PAGE)
        sendAnalytics(event)
        activity?.let {
            ReviewCancelDialog.showReviewDialog(it,
                {
                    sendAnalytics(
                        GoPayKycEvent.Click.ConfirmOkDialogEvent(
                            "",
                            GoPayKycConstants.ScreenNames.GOPAY_KYC_REVIEW_SELFIE_CAPTURE_PAGE
                        )
                    )
                    openSelfieKtpCamera()
                },
                {
                    sendAnalytics(
                        GoPayKycEvent.Click.ExitKycDialogEvent(
                            "",
                            GoPayKycConstants.ScreenNames.GOPAY_KYC_REVIEW_SELFIE_CAPTURE_PAGE
                        )
                    )
                    (it as GoPayKycNavigationListener).exitKycFlow()
                }
            )
        }
    }

    override fun sendOpenScreenGopayEvent() {
        sendAnalytics(
            GoPayKycEvent.Impression.OpenScreenEvent(
                GoPayKycConstants.ScreenNames.GOPAY_KYC_SELFIE_KTP_INSTRUCTION_PAGE
            )
        )
    }

    companion object {
        fun newInstance() = GoPayPlusSelfieKtpInstructionsFragment()
        const val INSTRUCTION_IMAGE_NAME = "gopay_kyc_selfie_instruction_step.png"
        const val CORRECT_IMAGE_NAME = "gopay_kyc_selfie_ktp_correct_instruction.png"
        const val INCORRECT_IMAGE_NAME = "gopay_kyc_selfie_ktp_incorrect_instruction.png"
        const val INSTRUCTION_IMAGE_PATH = ImageUrl.INSTRUCTION_IMAGE_PATH
        const val CORRECT_IMAGE_PATH =
            "https://images.tokopedia.net/img/android/res/singleDpi/gopay_kyc_selfie_ktp_correct_instruction.png"
        const val INCORRECT_IMAGE_PATH =
            "https://images.tokopedia.net/img/android/res/singleDpi/gopay_kyc_selfie_ktp_incorrect_instruction.png"
    }
}