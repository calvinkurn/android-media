package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GopayData

class GopayWithdrawLimitBottomSheet: BottomSheetUnify() {
    private var childView: View? = null
    private var gopayData: GopayData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_GOPAY_DATA)) {
                it.getParcelable<GopayData>(ARG_GOPAY_DATA)?.let { gopay ->
                    gopayData = gopay
                }
                childView = LayoutInflater.from(context).inflate(R.layout.swd_dialog_gopay_withdraw_limit,
                    null, false)
                setChild(childView)
                setUpView()
            } else {
                dismiss()
            }
        }
    }

    private fun setUpView() {
        if (gopayData == null) {
            dismiss()
            return
        }

        val titleView = childView?.findViewById<Typography>(R.id.tvGopayDialogTitle)
        val descriptionView = childView?.findViewById<Typography>(R.id.tvGopayDialogDescription)
        val tickerTitleView = childView?.findViewById<Typography>(R.id.tvGopayLimitDialogTitle)
        val tickerDescriptionView = childView?.findViewById<Typography>(R.id.tvGopayTickerDescription)
        val tickerLimitView = childView?.findViewById<Typography>(R.id.tvGopayTickerLimit)

        titleView?.text = gopayData?.bottomsheetData?.title
        descriptionView?.text = gopayData?.bottomsheetData?.description
        tickerTitleView?.text = gopayData?.limitCopyWriting
        tickerLimitView?.text = gopayData?.limit
        tickerDescriptionView?.text = gopayData?.bottomsheetData?.balance
    }

    companion object {
        const val ARG_GOPAY_DATA = "arg_gopay_data"
        const val TAG = "gopay_withdraw_limit_tag"
        fun getInstance(gopayData: GopayData): GopayWithdrawLimitBottomSheet {
            return GopayWithdrawLimitBottomSheet().apply {
                val bundle = Bundle()
                bundle.putParcelable(ARG_GOPAY_DATA, gopayData)
                arguments = bundle
            }
        }
    }
}
