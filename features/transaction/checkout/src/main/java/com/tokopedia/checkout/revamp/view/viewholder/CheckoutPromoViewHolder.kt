package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutPromoBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR

class CheckoutPromoViewHolder(private val binding: ItemCheckoutPromoBinding, private val listener: CheckoutAdapterListener) :
    RecyclerView.ViewHolder(binding.root) {

    private var isApplied = false

    fun bind(promoModel: CheckoutPromoModel) {
        if (!promoModel.isEnable) {
            binding.root.gone()
            return
        } else {
            binding.root.visible()
        }

        val titleValue: String
        val promo = promoModel.promo

        when {
            promo.additionalInfo.messageInfo.message.isNotEmpty() -> {
                titleValue = promo.additionalInfo.messageInfo.message
                if (promo.additionalInfo.usageSummaries.isNotEmpty()) {
                    isApplied = true
                    listener.onSendAnalyticsViewPromoCheckoutApplied()
                } else {
                    isApplied = false
                }
            }
            promo.defaultEmptyPromoMessage.isNotBlank() -> {
                titleValue = promo.defaultEmptyPromoMessage
                isApplied = false
            }
            else -> {
                titleValue = itemView.context.getString(purchase_platformcommonR.string.promo_funnel_label)
                isApplied = false
            }
        }

        if (isApplied) {
            binding.btnCheckoutPromo.showApplied(
                titleValue,
                promo.additionalInfo.messageInfo.detail,
                IconUnify.CHEVRON_RIGHT,
                promo.additionalInfo.usageSummaries.map { PromoEntryPointSummaryItem(it.description, it.amountStr, it.currencyDetailsStr) },
                showConfetti = true
            ) {
                listener.onClickPromoCheckout(promo)
                listener.onSendAnalyticsClickPromoCheckout(isApplied, getAllPromosApplied(promo))
            }
        } else {
            binding.btnCheckoutPromo.showActive(
                titleValue,
                IconUnify.CHEVRON_RIGHT
            ) {
                listener.onClickPromoCheckout(promo)
                listener.onSendAnalyticsClickPromoCheckout(isApplied, getAllPromosApplied(promo))
            }
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

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_promo
    }
}
