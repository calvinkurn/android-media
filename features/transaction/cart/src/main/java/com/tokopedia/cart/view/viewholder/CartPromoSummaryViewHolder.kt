package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartPromoPriceSummaryBinding
import com.tokopedia.cart.domain.model.cartlist.PromoSummaryDetailData

/**
 * @author by furqan on 17/02/2021
 */
class CartPromoSummaryViewHolder(private val binding: ItemCartPromoPriceSummaryBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(element: PromoSummaryDetailData) {
        with(binding) {
            tgCartPromoCashbackTitle.text = element.description
            tgCartPromoCashbackValue.text = element.amountStr

            if (element.currencyDetailStr.isNotEmpty()) {
                tgCartPromoCashbackDescription.text = element.currencyDetailStr
                tgCartPromoCashbackDescription.visibility = View.VISIBLE
            } else {
                tgCartPromoCashbackDescription.visibility = View.GONE
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_cart_promo_price_summary
    }

}