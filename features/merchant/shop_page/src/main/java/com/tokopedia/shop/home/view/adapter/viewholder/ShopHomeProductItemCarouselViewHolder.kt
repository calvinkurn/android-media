package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeProductItemCarouselViewHolder(
        itemView: View,
        private val shopPageHomeProductClickListener: ShopPageHomeProductClickListener?,
        private val shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?
) : ShopHomeProductViewHolder(itemView, shopPageHomeProductClickListener) {

    companion object {
        const val RATIO_WITH_RELATIVE_TO_SCREEN = 2.3
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_card
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductViewModel) {
        super.bind(shopHomeProductViewModel)
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels;
        if (deviceWidth > 0) {
            itemView.layoutParams.width = (deviceWidth / RATIO_WITH_RELATIVE_TO_SCREEN).toInt()
        }
    }
}