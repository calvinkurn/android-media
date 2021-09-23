package com.tokopedia.gopay_kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.analytics.GoPayKycConstants
import com.tokopedia.gopay_kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay_kyc.presentation.fragment.base.GoPayKycBaseFragment
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycNavigationListener
import com.tokopedia.gopay_kyc.presentation.viewholder.GoPayKycInstructionItemViewHolder
import kotlinx.android.synthetic.main.fragment_gopay_ktp_instructions_layout.*

class GoPayPlusKtpInstructionsFragment : GoPayKycBaseFragment() {

    private val instructionStringResList = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateInstructions()
    }

    private fun populateInstructions() {
        instructionStringResList.add(R.string.gopay_ktp_instruction_1)
        instructionStringResList.add(R.string.gopay_ktp_instruction_2)
        instructionStringResList.add(R.string.gopay_ktp_instruction_3)
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
            openKtpCamera()
        }
    }

    private fun initViews() {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        context?.let {
            goPayKtpStep1.text = it.getString(R.string.gopay_kyc_step_1)
            goPayUploadPhotoTitle.text = it.getString(R.string.gopay_kyc_ktp_upload_header_text)
            goPayKycTypeTitle.text = it.getString(R.string.gopay_kyc_ktp_text)
            goPayKycTypeDescription.text = it.getString(R.string.gopay_kyc_ktp_description_text)
            goPayPhotoTitle.text = it.getString(R.string.gopay_kyc_ktp_photo_front_text)
            takePhotoButton.text = it.getString(R.string.gopay_kyc_take_ktp_text)

            goPayIdImage.setImageDrawable(
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_gopay_kyc_upload_ktp
                )
            )
            for (instructionItem in instructionStringResList) {
                instructionLL.addView(
                    GoPayKycInstructionItemViewHolder(
                        it,
                        layoutParams
                    ).bindData(instructionItem)
                )
            }
        }
    }

    private fun openKtpCamera() {
        val event = GoPayKycEvent.Click.TakePhotoEvent(
            GoPayKycConstants.Label.KTP,
            GoPayKycConstants.ScreenNames.GOPAY_KYC_INSTRUCTION_PAGE
        )
        sendAnalytics(event)
        activity?.let { (it as GoPayKycNavigationListener).openKtpCameraScreen() }
    }

    override fun getScreenName() = null
    override fun initInjector() {}
    private fun sendAnalytics(event: GoPayKycEvent) =
        activity?.let { (it as GoPayKycNavigationListener).sendAnalytics(event) }

    override fun handleBackPressForGopay() {
        val event =
            GoPayKycEvent.Click.BackPressEvent(GoPayKycConstants.ScreenNames.GOPAY_KYC_INSTRUCTION_PAGE)
        sendAnalytics(event)
        activity?.finish()
    }

    override fun sendOpenScreenGopayEvent() {
        sendAnalytics(GoPayKycEvent.Impression.OpenScreenEvent(GoPayKycConstants.ScreenNames.GOPAY_KYC_INSTRUCTION_PAGE))
    }

    companion object {
        fun newInstance() = GoPayPlusKtpInstructionsFragment()
    }
}