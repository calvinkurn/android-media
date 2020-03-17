package com.tokopedia.purchase_platform.features.checkout.view.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply.LastApplyUsageSummariesUiModel
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_promo_checkout.view.*


/**
 * Created by fwidjaja on 2020-02-26.
 */
class PromoCheckoutViewHolder(val view: View, val actionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(view) {

    private var isApplied = false
    companion object {
        @JvmStatic
        val ITEM_VIEW_PROMO_CHECKOUT = R.layout.item_promo_checkout
    }

    fun bindViewHolder(lastApplyUiModel: LastApplyUiModel) {
        var title = itemView.context.getString(R.string.promo_funnel_label)

        if (lastApplyUiModel.additionalInfo.messageInfo.message.isNotEmpty()) {
            title = lastApplyUiModel.additionalInfo.messageInfo.message
            isApplied = true
            actionListener.onSendAnalyticsViewPromoCheckoutApplied()
        }
        itemView.promo_checkout_btn_shipment.title = title
        itemView.promo_checkout_btn_shipment.desc = lastApplyUiModel.additionalInfo.messageInfo.detail
        itemView.promo_checkout_btn_shipment.state = ButtonPromoCheckoutView.State.ACTIVE
        itemView.promo_checkout_btn_shipment.setOnClickListener {
            actionListener.onClickPromoCheckout(lastApplyUiModel)
            actionListener.onSendAnalyticsClickPromoCheckout(isApplied, lastApplyUiModel.listAllPromoCodes)}

        val params = LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        val displayMetrics = itemView.context?.resources?.displayMetrics

        if (lastApplyUiModel.additionalInfo.usageSummaries.isEmpty()) {
            itemView.ll_summary_transaction.gone()
        } else {
            itemView.ll_summary_transaction.visible()
            for ((i, lastApplyUsageSummary : LastApplyUsageSummariesUiModel) in lastApplyUiModel.additionalInfo.usageSummaries.withIndex()) {
                val relativeLayout: RelativeLayout = RelativeLayout(itemView.context).apply {
                    layoutParams = params

                    if (i>0) {
                        displayMetrics?.let {
                            setMargin(0, 12.dpToPx(it), 0, 0)
                        }
                    }
                }

                val label: Typography = Typography(itemView.context).apply {
                    setTextColor(resources.getColor(R.color.text_black))
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
                    setTextColor(resources.getColor(R.color.text_black))
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

                itemView.ll_summary_transaction.addView(relativeLayout)
            }
        }
    }
}