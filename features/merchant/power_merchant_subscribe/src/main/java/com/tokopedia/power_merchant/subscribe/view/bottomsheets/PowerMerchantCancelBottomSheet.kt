package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.bottom_sheet_pm_cancel.*

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
        private const val ARGUMENT_DATA_AUTO_EXTEND = "data_is_auto_extend"
        private const val ARGUMENT_DATA_DATE = "data_date"
        private const val EXTRA_FREE_SHIPPING_ENABLED = "extra_free_shipping_enabled"

        @JvmStatic
        fun newInstance(
            isTransitionPeriod: Boolean,
            dateExpired: String,
            freeShippingEnabled: Boolean
        ): PowerMerchantCancelBottomSheet {
            return PowerMerchantCancelBottomSheet().apply {
                val bundle = Bundle()
                bundle.putBoolean(ARGUMENT_DATA_AUTO_EXTEND, isTransitionPeriod)
                bundle.putString(ARGUMENT_DATA_DATE, dateExpired)
                bundle.putBoolean(EXTRA_FREE_SHIPPING_ENABLED, freeShippingEnabled)
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
        activity?.run {
            windowManager.defaultDisplay.getMetrics(displaymetrics)
            val widthSpec = View.MeasureSpec.makeMeasureSpec(displaymetrics.widthPixels, View.MeasureSpec.EXACTLY)
            parentView?.post {
                parentView.measure(widthSpec, 0)
                updateHeight(parentView.measuredHeight)
            }
        }

    }

    override fun initView(view: View) {
        initVar()
        val freeShippingEnabled = arguments?.getBoolean(EXTRA_FREE_SHIPPING_ENABLED) ?: false
        val freeShippingImage = view.findViewById<TextViewCompat>(R.id.textViewCompat8)
        val freeShippingLabel = view.findViewById<ImageView>(R.id.imageView10)

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

        freeShippingLabel.showWithCondition(freeShippingEnabled)
        freeShippingImage.showWithCondition(freeShippingEnabled)
    }

    private fun showExpiredDateTickerYellow() {
        tickerContainer.visibility = View.VISIBLE
        txtExpiredDate.text = MethodChecker.fromHtml(getString(R.string.expired_label_bs, expiredDate))
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        updateHeight()
    }
}