package com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductShimmeringUiModel

class ShopDiscountSetupProductShimmeringViewHolder(
    itemView: View
) : AbstractViewHolder<ShopDiscountSetupProductShimmeringUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_discount_setup_product_shimmering_layout
    }

    override fun bind(uiModel: ShopDiscountSetupProductShimmeringUiModel) {
    }

}