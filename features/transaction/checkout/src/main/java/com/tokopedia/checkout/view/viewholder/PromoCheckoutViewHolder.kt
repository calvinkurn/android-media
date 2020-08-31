package com.tokopedia.checkout.view.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by fwidjaja on 2020-02-26.
 */
class PromoCheckoutViewHolder(val view: View, val actionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(view) {

    private var isApplied = false

    companion object {
        @JvmStatic
        val ITEM_VIEW_PROMO_CHECKOUT = R.layout.item_promo_checkout
    }

    private val btnPromoCheckoutView by lazy {
        view.findViewById<ButtonPromoCheckoutView>(R.id.promo_checkout_btn_shipment)
    }

    private val llSummaryTransaction by lazy {
        view.findViewById<LinearLayout>(R.id.ll_summary_transaction)
    }

    fun bindViewHolder(lastApplyUiModel: LastApplyUiModel) {
        val titleValue: String

        when {
            lastApplyUiModel.additionalInfo.messageInfo.message.isNotEmpty() -> {
                titleValue = lastApplyUiModel.additionalInfo.messageInfo.message
                isApplied = true
                actionListener.onSendAnalyticsViewPromoCheckoutApplied()
            }
            lastApplyUiModel.defaultEmptyPromoMessage.isNotBlank() -> {
                titleValue = lastApplyUiModel.defaultEmptyPromoMessage
                isApplied = false
            }
            else -> {
                titleValue = itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
                isApplied = false
            }
        }

        btnPromoCheckoutView.apply {
            title = titleValue
            desc = lastApplyUiModel.additionalInfo.messageInfo.detail
            state = ButtonPromoCheckoutView.State.ACTIVE
            margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            setOnClickListener {
                actionListener.onClickPromoCheckout(lastApplyUiModel)
                actionListener.onSendAnalyticsClickPromoCheckout(isApplied, getAllPromosApplied(lastApplyUiModel))
            }
        }

        if (lastApplyUiModel.additionalInfo.usageSummaries.isEmpty()) {
            llSummaryTransaction.gone()
        } else {
            llSummaryTransaction.visible()
            if  (hasChildren(llSummaryTransaction)) llSummaryTransaction.removeAllViews()
            generateChildrenView(lastApplyUiModel)
        }
    }

    private fun getAllPromosApplied(lastApplyData: LastApplyUiModel): List<String> {
        val listPromos = arrayListOf<String>()
        lastApplyData.codes.forEach {
            listPromos.add(it)
        }
        lastApplyData.voucherOrders.forEach {
            listPromos.add(it.code)
        }
        return listPromos
    }

    private fun hasChildren(viewGroup: ViewGroup): Boolean {
        return viewGroup.childCount > 0
    }

    private fun generateChildrenView(lastApplyUiModel: LastApplyUiModel) {
        for ((i, lastApplyUsageSummary: LastApplyUsageSummariesUiModel) in lastApplyUiModel.additionalInfo.usageSummaries.withIndex()) {
            val params = LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            val displayMetrics = itemView.context?.resources?.displayMetrics
            val relativeLayout: RelativeLayout = RelativeLayout(itemView.context).apply {
                layoutParams = params

                if (i > 0) {
                    displayMetrics?.let {
                        setMargin(0, 4.dpToPx(it), 0, 0)
                    }
                } else if (i == 0) {
                    displayMetrics?.let {
                        setMargin(0, 0, 0, 0)
                    }
                }
            }

            val label: Typography = Typography(itemView.context).apply {
                setTextColor(resources.getColor(com.tokopedia.purchase_platform.common.R.color.text_black))
                setWeight(Typography.REGULAR)
                setType(Typography.BODY_3)
                text = lastApplyUsageSummary.description
            }

            val labelParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            labelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            label.layoutParams = labelParams

            val value: Typography = Typography(itemView.context).apply {
                setTextColor(resources.getColor(com.tokopedia.purchase_platform.common.R.color.text_black))
                setWeight(Typography.REGULAR)
                setType(Typography.BODY_3)
                text = lastApplyUsageSummary.amountStr
            }

            val valueParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            valueParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            value.layoutParams = valueParams

            if (label.parent != null) (label.parent as ViewGroup).removeView(label)
            relativeLayout.addView(label)

            if (value.parent != null) (value.parent as ViewGroup).removeView(value)
            relativeLayout.addView(value)

            llSummaryTransaction.addView(relativeLayout)
        }
    }
}