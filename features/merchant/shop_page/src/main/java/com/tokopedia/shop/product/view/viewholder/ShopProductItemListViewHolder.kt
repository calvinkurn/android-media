package com.tokopedia.shop.product.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import kotlinx.android.synthetic.main.item_shop_product_card_list.view.*

class ShopProductItemListViewHolder(
        itemView: View,
        private val shopProductClickedListener: ShopProductClickedListener?,
        private val shopProductImpressionListener: ShopProductImpressionListener?,
        private val shopTrackType: Int
) : AbstractViewHolder<ShopProductViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_shop_product_card_list
    }

    override fun bind(shopProductViewModel: ShopProductViewModel) {
        itemView.productCardView?.setProductModel(
                ShopPageProductListMapper.mapToProductCardModel(shopProductViewModel)
        )
        itemView.productCardView?.setThreeDotsOnClickListener {
            shopProductClickedListener?.onThreeDotsClicked(shopProductViewModel, adapterPosition)
        }

        itemView.productCardView?.setOnClickListener {
            shopProductClickedListener?.onProductClicked(shopProductViewModel, shopTrackType, adapterPosition)
        }

        itemView.productCardView?.setImageProductViewHintListener(shopProductViewModel, object : ViewHintListener {
            override fun onViewHint() {
                shopProductImpressionListener?.onProductImpression(shopProductViewModel, shopTrackType, adapterPosition)
            }
        })
    }

}