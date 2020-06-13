package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantSpannableUtil.createSpannableString
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_power_merchant_cancellation.*

class PowerMerchantCancelBottomSheet : BottomSheetUnify() {
    private var listener: BottomSheetCancelListener? = null
    private val powerMerchantTracking: PowerMerchantTracking by lazy {
        PowerMerchantTracking()
    }

    companion object {
        private val TAG: String = PowerMerchantCancelBottomSheet::class.java.simpleName
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
        val isTransitionPeriod = arguments?.getBoolean(ARGUMENT_DATA_AUTO_EXTEND) ?: false
        val expiredDate = arguments?.getString(ARGUMENT_DATA_DATE) ?: ""

        initView(isTransitionPeriod, expiredDate)
    }

    fun setListener(listener: BottomSheetCancelListener) {
        this.listener = listener
    }

    private fun initView(isTransitionPeriod: Boolean, expiredDate: String) {
        if (isTransitionPeriod) {
            tickerWarning.hide()
        } else {
            showWarningTicker(expiredDate)
        }

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
           val cancellationDate = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD,
               DateFormatUtils.FORMAT_D_MMMM_YYYY, expiredDate)
           val warningText = it.getString(R.string.expired_label, cancellationDate)
           val highlightTextColor = ContextCompat.getColor(it, R.color.light_N700)

           tickerWarning.setTextDescription(createSpannableString(warningText, cancellationDate, highlightTextColor, true))
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