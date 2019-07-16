package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.R

class PowerMerchantCancelBottomSheet : BottomSheets() {
    lateinit var buttonCancel: Button
    lateinit var txtExpiredDate: TextViewCompat
    lateinit var tickerContainer: FrameLayout
    private var listener: BottomSheetCancelListener? = null
    private var isTransitionPeriod: Boolean = false
    private var expiredDate: String = ""
    private val powerMerchantTracking: PowerMerchantTracking by lazy {
        PowerMerchantTracking()
    }

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

    override fun configView(parentView: View?) {
        super.configView(parentView)

        val displaymetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(displaymetrics.widthPixels, View.MeasureSpec.EXACTLY)
        parentView?.post {
            parentView.measure(widthSpec, 0)
            updateHeight(parentView.measuredHeight)
        }
    }

    override fun initView(view: View) {
        initVar()
        tickerContainer = view.findViewById(R.id.ticker_yellow_cancellation_bs)
        txtExpiredDate = view.findViewById(R.id.txt_ticker_yellow_bs)
        buttonCancel = view.findViewById(R.id.button_cancel_bs)

        if (!isTransitionPeriod) {
            showExpiredDateTickerYellow()
        } else {
            tickerContainer.visibility = View.GONE

        }

        buttonCancel.setOnClickListener {
            powerMerchantTracking.eventCancelMembershipBottomSheet()
            listener?.onclickButton()
        }
    }

    private fun showExpiredDateTickerYellow() {
        tickerContainer.visibility = View.VISIBLE
        txtExpiredDate.text = MethodChecker.fromHtml(getString(R.string.expired_label_bs, expiredDate))
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        updateHeight()
    }
}