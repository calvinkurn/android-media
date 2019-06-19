package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.bottom_sheet_pm_cancel.*

class PowerMerchantCancelBottomSheet : BottomSheets() {
    lateinit var buttonCancel: Button
    lateinit var txtExpiredDate: TextViewCompat
    private var listener: BottomSheetCancelListener? = null
    private var isTransitionPeriod: Boolean = false
    private var expiredDate: String = ""

    companion object {
        const val ARGUMENT_DATA_AUTO_EXTEND = "data_is_auto_extend"
        const val ARGUMENT_DATA_DATE = "data_date"

        @JvmStatic
        fun newInstance(isTransitionPeriod: Boolean, dateExpired: String): PowerMerchantCancelBottomSheet {
            return PowerMerchantCancelBottomSheet().apply {
                val bundle = Bundle()
                bundle.putBoolean(ARGUMENT_DATA_AUTO_EXTEND, isTransitionPeriod)
                bundle.putString(ARGUMENT_DATA_DATE, dateExpired)
                arguments = bundle
            }
        }
    }

    private fun initVar() {
        isTransitionPeriod = arguments?.getBoolean(ARGUMENT_DATA_AUTO_EXTEND) ?: false
        expiredDate = arguments?.getString(ARGUMENT_DATA_DATE) ?: ""
    }

    interface BottomSheetCancelListener {
        fun onclickButton()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_pm_cancel
    }

    fun setListener(listener: BottomSheetCancelListener) {
        this.listener = listener
    }

    override fun title(): String {
        super.title()
        return ""
    }

    override fun initView(view: View) {
        initVar()
        txtExpiredDate = view.findViewById(R.id.txt_ticker_yellow_bs)
        buttonCancel = view.findViewById(R.id.button_cancel_bs)

        if (!isTransitionPeriod) {
            ticker_yellow_cancellation_bs.visibility = View.GONE
        } else {
            showExpiredDateTickerYellow()
        }

        buttonCancel.setOnClickListener {
            listener?.onclickButton()
        }
    }

    private fun showExpiredDateTickerYellow() {
        txtExpiredDate.text = MethodChecker.fromHtml(getString(R.string.expired_label_bs, expiredDate))
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        updateHeight()
    }
}