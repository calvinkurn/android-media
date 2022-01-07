package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductProductGridCardLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductProductGridUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

open class MvcLockedToProductProductGridViewHolder(
        itemView: View
) : AbstractViewHolder<MvcLockedToProductProductGridUiModel>(itemView) {
    private val viewBinding: ItemMvcLockedToProductProductGridCardLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_product_grid_card_layout
    }

    private fun findViews() {
    }

    override fun bind(mvcLockedToProductProductGridUiModel: MvcLockedToProductProductGridUiModel) {

//        this.shopHomeProductViewModel = shopHomeProductViewModel
//        productCard?.setProductModel(ShopPageHomeMapper.mapToProductCardModel(
//                isHasAddToCartButton = false,
//                hasThreeDots = isShowTripleDot,
//                shopHomeProductViewModel = shopHomeProductViewModel,
//                isWideContent = false
//        ))
    }

}