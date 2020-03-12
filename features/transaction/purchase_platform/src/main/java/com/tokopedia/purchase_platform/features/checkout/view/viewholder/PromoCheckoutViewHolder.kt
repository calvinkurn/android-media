package com.tokopedia.purchase_platform.features.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckout.common.view.model.PromoCheckoutData
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentAdapterActionListener
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

    fun bindViewHolder(promoCheckoutData: PromoCheckoutData) {
        var title = itemView.context.getString(R.string.promo_funnel_label)
        if (promoCheckoutData.promoLabel.isNotEmpty()) {
            title = promoCheckoutData.promoLabel
            isApplied = true
            actionListener.onSendAnalyticsViewPromoCheckoutApplied()
        }
        itemView.promo_checkout_btn_shipment.title = title
        itemView.promo_checkout_btn_shipment.desc = promoCheckoutData.promoUsageInfo
        itemView.promo_checkout_btn_shipment.state = promoCheckoutData.state
        itemView.promo_checkout_btn_shipment.setOnClickListener {
            actionListener.onClickPromoCheckout(promoCheckoutData)
            actionListener.onSendAnalyticsClickPromoCheckout(isApplied, promoCheckoutData.listAllPromoCodes)}
    }
}