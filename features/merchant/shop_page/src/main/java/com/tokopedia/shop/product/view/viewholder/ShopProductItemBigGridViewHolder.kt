package com.tokopedia.shop.product.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.shop.R
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import kotlinx.android.synthetic.main.item_shop_newproduct_big_grid.view.*

class ShopProductItemBigGridViewHolder(
        itemView: View,
        private val shopProductClickedListener: ShopProductClickedListener?,
        private val shopProductImpressionListener: ShopProductImpressionListener?,
        private val shopTrackType: Int
) : AbstractViewHolder<ShopProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_shop_newproduct_big_grid
    }

    override fun bind(shopProductUiModel: ShopProductUiModel) {
        itemView.product_card?.setProductModel(
                ShopPageProductListMapper.mapToProductCardModel(shopProductUiModel, true)
        )
        itemView.product_card?.setThreeDotsOnClickListener {
            shopProductClickedListener?.onThreeDotsClicked(shopProductUiModel, adapterPosition)
        }

        itemView.product_card?.setOnClickListener {
            shopProductClickedListener?.onProductClicked(shopProductUiModel, shopTrackType, adapterPosition)
        }

        itemView.product_card?.setImageProductViewHintListener(shopProductUiModel, object : ViewHintListener {
            override fun onViewHint() {
                shopProductImpressionListener?.onProductImpression(shopProductUiModel, shopTrackType, adapterPosition)
            }
        })
    }

}