package com.tokopedia.oneclickcheckout.order.view.card

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.databinding.CardOrderInsuranceBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderInsuranceCard(private val binding: CardOrderInsuranceBinding, private val listener: OrderInsuranceCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics): RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = 5
    }

    fun setupInsurance(shipment: OrderShipment, productId: String) {
        val insurance = shipment.insurance
        val insuranceData = insurance.insuranceData
        binding.apply {
            if (insuranceData != null && !shipment.isLoading) {
                setupListeners(insuranceData, productId)
                when (insuranceData.insuranceType) {
                    InsuranceConstant.INSURANCE_TYPE_MUST -> {
                        tvInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_must_insurance)
                        cbInsurance.isEnabled = false
                        forceSetChecked(cbInsurance, true)
                        listener.onInsuranceChecked(true)
                        setVisibility(View.VISIBLE)
                    }
                    InsuranceConstant.INSURANCE_TYPE_NO -> {
                        listener.onInsuranceChecked(false)
                        setVisibility(View.GONE)
                    }
                    InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                        tvInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_shipment_insurance)
                        cbInsurance.isEnabled = true
                        if (insurance.isFirstLoad) {
                            if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                                forceSetChecked(cbInsurance, true)
                                listener.onInsuranceChecked(true)
                            } else if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                                forceSetChecked(cbInsurance, false)
                                listener.onInsuranceChecked(false)
                            }
                        } else {
                            forceSetChecked(cbInsurance, insurance.isCheckInsurance)
                            listener.onInsuranceChecked(insurance.isCheckInsurance)
                        }
                        setVisibility(View.VISIBLE)
                    }
                }
                if (insuranceData.insurancePrice > 0) {
                    tvInsurancePrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceData.insurancePrice, false).removeDecimalSuffix()
                    tvInsurancePrice.visible()
                } else {
                    tvInsurancePrice.gone()
                }
            } else {
                setVisibility(View.INVISIBLE)
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
                    // TODO: 05/07/21 ProductId
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

    private fun setVisibility(visibility: Int) {
        binding.apply {
            if (visibility == View.INVISIBLE && cbInsurance.visibility == View.GONE) {
                return
            }
            cbInsurance.visibility = visibility
            tvInsurance.visibility = visibility
            imgBtInsuranceInfo.visibility = visibility
            tvInsurancePrice.visibility = visibility
            spaceInsurance.visibility = if (visibility == View.VISIBLE) View.INVISIBLE else visibility
        }
    }

    interface OrderInsuranceCardListener {

        fun onInsuranceChecked(isChecked: Boolean)

        fun onClickInsuranceInfo(title: String, message: String, image: Int)
    }
}