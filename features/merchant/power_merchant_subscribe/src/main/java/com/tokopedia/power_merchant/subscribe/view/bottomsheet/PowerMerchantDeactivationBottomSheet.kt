package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantDateFormatter
import com.tokopedia.power_merchant.subscribe.databinding.BottomSheetPowerMerchantDeactivationBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PowerMerchantDeactivationBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomSheetPowerMerchantDeactivationBinding>()
    private var listener: BottomSheetCancelListener? = null
    private val powerMerchantTracking: PowerMerchantTracking by lazy {
        PowerMerchantTracking(UserSession(context))
    }

    companion object {
        private const val TAG: String = "PowerMerchantCancelBottomSheet"
        private const val ARGUMENT_DATA_DATE = "data_date"

        @JvmStatic
        fun newInstance(dateExpired: String): PowerMerchantDeactivationBottomSheet {
            return PowerMerchantDeactivationBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(ARGUMENT_DATA_DATE, dateExpired)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemView = View.inflate(
            context,
            R.layout.bottom_sheet_power_merchant_deactivation, null
        )
        binding = BottomSheetPowerMerchantDeactivationBinding.bind(itemView)
        setChild(itemView)
        setStyle(
            DialogFragment.STYLE_NORMAL,
            com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle
        )
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
        binding?.run {
            btnCancel.setOnClickListener {
                powerMerchantTracking.sendEventClickConfirmToStopPowerMerchant()
                listener?.onClickCancelButton()
            }

            btnBack.setOnClickListener {
                listener?.onClickBackButton()
                dismiss()
            }

            tvPmDeactivationTnC.movementMethod = LinkMovementMethod.getInstance()
            tvPmDeactivationTnC.text = getString(R.string.pm_pm_deactivation_be_rm_tnc)
        }
    }

    private fun showWarningTicker(expiredDate: String) {
        binding?.run {
            context?.let {
                val descriptionText = getString(
                    R.string.pm_bottom_sheet_expired_label, expiredDate
                ).parseAsHtml()
                tickerWarning.setTextDescription(descriptionText)
                tickerWarning.show()
            }
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    interface BottomSheetCancelListener {
        fun onClickCancelButton()
        fun onClickBackButton()
    }
}