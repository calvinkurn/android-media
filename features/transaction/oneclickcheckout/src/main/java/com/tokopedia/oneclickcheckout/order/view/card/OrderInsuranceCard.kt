package com.tokopedia.oneclickcheckout.order.view.card

import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.databinding.FragmentOrderSummaryPageBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderInsuranceCard(private val binding: FragmentOrderSummaryPageBinding, private val listener: OrderInsuranceCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    fun setupInsurance(insuranceData: InsuranceData?, productId: String) {
        binding.apply {
            if (insuranceData != null) {
                if (insuranceData.insurancePrice > 0) {
                    tvInsurancePrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceData.insurancePrice, false).removeDecimalSuffix()
                    tvInsurancePrice.visible()
                } else {
                    tvInsurancePrice.gone()
                }
                setupListeners(insuranceData, productId)
                when (insuranceData.insuranceType) {
                    InsuranceConstant.INSURANCE_TYPE_MUST -> {
                        tvInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_must_insurance)
                        cbInsurance.isEnabled = false
                        forceSetChecked(cbInsurance, true)
                        listener.onInsuranceChecked(true)
                        groupInsurance.visible()
                    }
                    InsuranceConstant.INSURANCE_TYPE_NO -> {
                        listener.onInsuranceChecked(false)
                        groupInsurance.gone()
                    }
                    InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                        tvInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_shipment_insurance)
                        cbInsurance.isEnabled = true
                        if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                            forceSetChecked(cbInsurance, true)
                            listener.onInsuranceChecked(true)
                        } else if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                            forceSetChecked(cbInsurance, false)
                            listener.onInsuranceChecked(false)
                        }
                        groupInsurance.visible()
                    }
                }
            } else {
                groupInsurance.gone()
            }
        }
    }

    private fun setupListeners(insuranceData: InsuranceData, productId: String) {
        binding.apply {
            imgBtInsuranceInfo.let { iv ->
                iv.setOnClickListener {
                    listener.onClickInsuranceInfo(iv.context.getString(com.tokopedia.purchase_platform.common.R.string.title_bottomsheet_insurance),
                            insuranceData.insuranceUsedInfo,
                            com.tokopedia.purchase_platform.common.R.drawable.ic_pp_insurance)
                }
            }
            cbInsurance.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    orderSummaryAnalytics.eventClickOnInsurance(productId, "uncheck", insuranceData.insurancePrice.toString())
                } else {
                    orderSummaryAnalytics.eventClickOnInsurance(productId, "check", insuranceData.insurancePrice.toString())
                }
                listener.onInsuranceChecked(isChecked)
            }
        }
    }

    private fun forceSetChecked(checkBox: CheckboxUnify?, newCheck: Boolean) {
        checkBox?.apply {
            if (isChecked == newCheck) {
                isChecked = newCheck
                setIndeterminate(false)
            } else {
                isChecked = newCheck
            }
        }
    }

    interface OrderInsuranceCardListener {

        fun onInsuranceChecked(isChecked: Boolean)

        fun onClickInsuranceInfo(title: String, message: String, image: Int)
    }
}