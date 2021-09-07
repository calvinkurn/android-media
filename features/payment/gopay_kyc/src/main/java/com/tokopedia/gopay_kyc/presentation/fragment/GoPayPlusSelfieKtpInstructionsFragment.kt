package com.tokopedia.gopay_kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.presentation.viewholder.GoPayKycInstructionItemViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import kotlinx.android.synthetic.main.fragment_gopay_ktp_instructions_layout.*

class GoPayPlusSelfieKtpInstructionsFragment : TkpdBaseV4Fragment() {

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
            goPayKycChange.gone()
            takePhotoButton.text = it.getString(R.string.gopay_kyc_selfie_take_ktp_text)

            goPayIdImage.setImageDrawable(
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_gopay_kyc_upload_selfie_ktp
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

    override fun getScreenName() = null

    companion object {

        fun newInstance() = GoPayPlusSelfieKtpInstructionsFragment()
    }
}