package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.show
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
        const val ARGUMENT_DATA_DATE = "data_date"

        @JvmStatic
        fun newInstance(dateExpired: String): PowerMerchantCancelBottomSheet {
            return PowerMerchantCancelBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(ARGUMENT_DATA_DATE, dateExpired)
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

        initView(expiredDate)
    }

    fun setListener(listener: BottomSheetCancelListener) {
        this.listener = listener
    }

    private fun initView(expiredDate: String) {
        showWarningTicker(expiredDate)

        btnCancel.setOnClickListener {
            powerMerchantTracking.eventCancelMembershipBottomSheet()
            listener?.onClickCancelButton()
        }

        btnBack.setOnClickListener {
            dismiss()
        }
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