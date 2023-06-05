package com.tokopedia.recharge_component.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.BottomsheetRechargeCheckBalanceOtpBinding
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceOTPBottomSheetModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RechargeCheckBalanceOTPBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetRechargeCheckBalanceOtpBinding>()
    private lateinit var bottomSheetModel: RechargeCheckBalanceOTPBottomSheetModel
    private var mListener: RechargeCheckBalanceOTPBottomSheetListener? = null

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG_CHECK_BALANCE_OTP_BOTTOM_SHEET)
    }

    fun setListener(listener: RechargeCheckBalanceOTPBottomSheetListener) {
        mListener = listener
    }

    fun setBottomSheetModel(model: RechargeCheckBalanceOTPBottomSheetModel) {
        bottomSheetModel = model
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        isFullpage = false
        isDragable = false
        showCloseIcon = true

        binding = BottomsheetRechargeCheckBalanceOtpBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)

        renderBottomSheet()
    }

    private fun renderBottomSheet() {
        binding?.run {
            setTitle(bottomSheetModel.title)
            bottomsheetOtpImage.loadImage(bottomSheetModel.mediaUrl)
            bottomsheetOtpDescription.text = bottomSheetModel.description
            bottomsheetOtpButton.text = bottomSheetModel.buttonText
            bottomsheetOtpButton.setOnClickListener {
                mListener?.onClickButton(bottomSheetModel.buttonAppLink)
                dismiss()
            }
            mListener?.onRenderCheckBalanceOTPBottomSheet()
        }
    }

    interface RechargeCheckBalanceOTPBottomSheetListener {
        fun onClickButton(applink: String)
        fun onRenderCheckBalanceOTPBottomSheet()
    }

    companion object {
        private const val TAG_CHECK_BALANCE_OTP_BOTTOM_SHEET = "check_balance_otp_bottom_sheet"
    }
}
