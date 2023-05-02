package com.tokopedia.oneclickcheckout.order.view.card

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.databinding.CardOrderInsuranceBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderInsuranceCard(
    private val binding: CardOrderInsuranceBinding,
    private val listener: OrderInsuranceCardListener,
    private val orderSummaryAnalytics: OrderSummaryAnalytics
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = 5
    }

    fun setupInsurance(shipment: OrderShipment, profile: OrderProfile) {
        val insurance = shipment.insurance
        val insuranceData = insurance.insuranceData
        binding.apply {
            if (insuranceData != null && !shipment.isLoading && !shipment.isDisabled && profile.enable) {
                forceSetChecked(cbInsurance, insurance.isCheckInsurance)
                when (insuranceData.insuranceType) {
                    InsuranceConstant.INSURANCE_TYPE_MUST -> {
                        tvInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_must_insurance)
                        cbInsurance.isEnabled = false
                        setVisibility(View.VISIBLE)
                    }
                    InsuranceConstant.INSURANCE_TYPE_NO -> {
                        setVisibility(View.GONE)
                    }
                    InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                        tvInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_shipment_insurance)
                        cbInsurance.isEnabled = true
                        setVisibility(View.VISIBLE)
                    }
                }
                if (insuranceData.insurancePrice > 0) {
                    tvInsurancePrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        insuranceData.insurancePrice,
                        false
                    ).removeDecimalSuffix()
                    tvInsurancePrice.visible()
                } else {
                    tvInsurancePrice.gone()
                }
                setupListeners(insuranceData)
            } else if (insuranceData == null || shipment.isDisabled || !profile.enable) {
                setVisibility(View.GONE)
            } else {
                setVisibility(View.INVISIBLE)
            }
        }
    }

    private fun setupListeners(insuranceData: InsuranceData) {
        binding.apply {
            imgBtInsuranceInfo.let { iv ->
                iv.setOnClickListener {
                    listener.onClickInsuranceInfo(insuranceData.insuranceUsedInfo)
                }
            }
            cbInsurance.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    orderSummaryAnalytics.eventClickOnInsurance(
                        "uncheck",
                        insuranceData.insurancePrice.toString()
                    )
                } else {
                    orderSummaryAnalytics.eventClickOnInsurance(
                        "check",
                        insuranceData.insurancePrice.toString()
                    )
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

    private fun setVisibility(visibility: Int) {
        binding.apply {
            if (visibility != View.VISIBLE && cbInsurance.visibility == View.GONE) {
                return
            }
            cbInsurance.visibility = visibility
            tvInsurance.visibility = visibility
            imgBtInsuranceInfo.visibility = visibility
            tvInsurancePrice.visibility = visibility
            spaceInsurance.visibility =
                if (visibility == View.VISIBLE) {
                    View.INVISIBLE
                } else {
                    visibility
                }
        }
    }

    interface OrderInsuranceCardListener {

        fun onInsuranceChecked(isChecked: Boolean)

        fun onClickInsuranceInfo(message: String)
    }
}
