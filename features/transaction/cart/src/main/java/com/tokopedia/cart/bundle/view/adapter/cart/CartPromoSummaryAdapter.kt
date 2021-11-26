package com.tokopedia.cart.bundle.view.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemCartPromoPriceSummaryBundleBinding
import com.tokopedia.cart.bundle.view.uimodel.PromoSummaryDetailData
import com.tokopedia.cart.bundle.view.viewholder.CartPromoSummaryViewHolder

/**
 * @author by furqan on 17/02/2021
 */
class CartPromoSummaryAdapter(private val promoSummaryList: List<PromoSummaryDetailData>)
    : RecyclerView.Adapter<CartPromoSummaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartPromoSummaryViewHolder =
            CartPromoSummaryViewHolder(ItemCartPromoPriceSummaryBundleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = promoSummaryList.size

    override fun onBindViewHolder(holder: CartPromoSummaryViewHolder, position: Int) {
        holder.bind(promoSummaryList[position])
    }

}