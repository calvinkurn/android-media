package com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailShimmeringUiModel

open class ShopDiscountProductDetailShimmeringViewHolder(
    itemView: View
) : AbstractViewHolder<ShopDiscountProductDetailShimmeringUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_discount_product_detail_shimmering_layout
    }

    override fun bind(uiModel: ShopDiscountProductDetailShimmeringUiModel) {
    }

}