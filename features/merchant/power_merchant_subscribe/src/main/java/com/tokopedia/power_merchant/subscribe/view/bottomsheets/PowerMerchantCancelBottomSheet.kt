package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantDateFormatter.formatCancellationDate
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_power_merchant_cancellation.*

class PowerMerchantCancelBottomSheet : BottomSheetUnify() {
    private var listener: BottomSheetCancelListener? = null
    private val powerMerchantTracking: PowerMerchantTracking by lazy {
        PowerMerchantTracking()
    }

    companion object {
        private val TAG: String = PowerMerchantCancelBottomSheet::class.java.simpleName
        private const val ARGUMENT_DATA_DATE = "data_date"
        private const val EXTRA_FREE_SHIPPING_ENABLED = "extra_free_shipping_enabled"

        @JvmStatic
        fun newInstance(
            dateExpired: String,
            freeShippingEnabled: Boolean
        ): PowerMerchantCancelBottomSheet {
            return PowerMerchantCancelBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(ARGUMENT_DATA_DATE, dateExpired)
                bundle.putBoolean(EXTRA_FREE_SHIPPING_ENABLED, freeShippingEnabled)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemView = View.inflate(context,
            R.layout.bottom_sheet_power_merchant_cancellation, null)

        isFullpage = true
        setChild(itemView)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PmBottomSheet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val expiredDate = arguments?.getString(ARGUMENT_DATA_DATE) ?: ""
        val freeShippingEnabled = arguments?.getBoolean(EXTRA_FREE_SHIPPING_ENABLED) ?: false

        initView(expiredDate, freeShippingEnabled)
    }

    fun setListener(listener: BottomSheetCancelListener) {
        this.listener = listener
    }

    private fun initView(
        expiredDate: String,
        freeShippingEnabled: Boolean) {
        showWarningTicker(expiredDate)

        btnCancel.setOnClickListener {
            powerMerchantTracking.eventCancelMembershipBottomSheet()
            listener?.onClickCancelButton()
        }

        btnBack.setOnClickListener {
            dismiss()
        }

        imageFreeShipping.showWithCondition(freeShippingEnabled)
        textFreeShipping.showWithCondition(freeShippingEnabled)
    }

    private fun showWarningTicker(expiredDate: String) {
       context?.let {
           val descriptionText = formatCancellationDate(it, expiredDate)
           tickerWarning.setTextDescription(descriptionText)
           tickerWarning.show()
       }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    interface BottomSheetCancelListener {
        fun onClickCancelButton()
    }
}