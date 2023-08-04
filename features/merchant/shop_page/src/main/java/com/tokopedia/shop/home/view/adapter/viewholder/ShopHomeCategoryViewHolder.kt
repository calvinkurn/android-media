package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.databinding.ItemShopHomeCategoryBannerLeftBinding
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeCategoryViewHolder(
    itemView: View
) : AbstractViewHolder<ShopHomeShowcaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_category_banner_left
    }

    private val viewBinding: ItemShopHomeCategoryBannerLeftBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseUiModel) {
        viewBinding?.tpgTitle?.text = model.categoryHeader.title
    }

}
