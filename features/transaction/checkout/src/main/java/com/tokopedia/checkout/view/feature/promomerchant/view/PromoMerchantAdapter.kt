package com.tokopedia.checkout.view.feature.promomerchant.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationAdapterListener
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationViewHolder

/**
 * Created by fwidjaja on 28/02/19.
 */
class PromoMerchantAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var shippingDurationViewModels: List<ShippingDurationViewModel>? = null
    private var promoMerchantAdapterListener: PromoMerchantAdapterListener? = null
    private var cartPosition: Int = 0
    // set true if has courier promo, whether own courier or other duration's courier
    private var hasCourierPromo: Boolean = false

    fun setShippingDurationViewModels(shippingDurationViewModels: List<ShippingDurationViewModel>) {
        this.shippingDurationViewModels = shippingDurationViewModels
    }

    fun setPromoMerchantAdapterListener(promoMerchantAdapterListener: PromoMerchantAdapterListener) {
        this.promoMerchantAdapterListener = promoMerchantAdapterListener
    }

    fun setCartPosition(cartPosition: Int) {
        this.cartPosition = cartPosition
    }

    fun setHasCourierPromo(hasCourierPromo: Boolean) {
        this.hasCourierPromo = hasCourierPromo
    }

    override fun getItemViewType(position: Int): Int {
        return ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ShippingDurationViewHolder(view, this, cartPosition, hasCourierPromo)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ShippingDurationViewHolder).bindData(shippingDurationViewModels!![position],
                promoMerchantAdapterListener)
    }

    override fun getItemCount(): Int {
        return shippingDurationViewModels!!.size
    }

}