package com.tokopedia.oneclickcheckout.order.view.card

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import com.tokopedia.design.component.Tooltip
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderInsuranceCard(private val view: View, private val listener: OrderInsuranceCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private val groupInsurance by lazy { view.findViewById<Group>(R.id.group_insurance) }
    private val tvInsurance by lazy { view.findViewById<Typography>(R.id.tv_insurance) }
    private val cbInsurance by lazy { view.findViewById<CheckboxUnify>(R.id.cb_insurance) }
    private val imgBtInsuranceInfo by lazy { view.findViewById<ImageView>(R.id.img_bt_insurance_info) }
    private val tvInsurancePrice by lazy { view.findViewById<Typography>(R.id.tv_insurance_price) }

    fun setGroupInsuranceVisible(isVisible: Boolean) {
        groupInsurance?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setupInsurance(insuranceData: InsuranceData?, productId: String) {
        if (insuranceData != null) {
            if (insuranceData.insurancePrice > 0) {
                tvInsurancePrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceData.insurancePrice, false).removeDecimalSuffix()
                tvInsurancePrice?.visible()
            } else {
                tvInsurancePrice?.gone()
            }
            imgBtInsuranceInfo?.let { iv ->
                iv.setOnClickListener {
                    showBottomSheet(iv.context,
                            iv.context.getString(com.tokopedia.purchase_platform.common.R.string.title_bottomsheet_insurance),
                            insuranceData.insuranceUsedInfo,
                            com.tokopedia.purchase_platform.common.R.drawable.ic_pp_insurance)
                }
            }
            cbInsurance?.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    orderSummaryAnalytics.eventClickOnInsurance(productId, "uncheck", insuranceData.insurancePrice.toString())
                } else {
                    orderSummaryAnalytics.eventClickOnInsurance(productId, "check", insuranceData.insurancePrice.toString())
                }
                listener.onInsuranceChecked(isChecked)
            }
            if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_MUST) {
                tvInsurance?.setText(com.tokopedia.purchase_platform.common.R.string.label_must_insurance)
                cbInsurance?.isEnabled = false
                forceSetChecked(cbInsurance, true)
                listener.onInsuranceChecked(true)
                groupInsurance?.visible()
            } else if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_NO) {
                listener.onInsuranceChecked(false)
                groupInsurance?.gone()
            } else if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tvInsurance?.setText(com.tokopedia.purchase_platform.common.R.string.label_shipment_insurance)
                cbInsurance?.isEnabled = true
                if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                    forceSetChecked(cbInsurance, true)
                    listener.onInsuranceChecked(true)
                } else if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                    forceSetChecked(cbInsurance, false)
                    listener.onInsuranceChecked(false)
                }
                groupInsurance?.visible()
            }
        } else {
            groupInsurance?.gone()
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

    private fun showBottomSheet(context: Context, title: String, message: String, image: Int) {
        Tooltip(context).apply {
            setTitle(title)
            setDesc(message)
            setTextButton(context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setIcon(image)
            btnAction.setOnClickListener { this.dismiss() }
            show()
        }
    }

    interface OrderInsuranceCardListener {

        fun onInsuranceChecked(isChecked: Boolean)
    }
}