package com.tokopedia.shop.product.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopNewproductBigGridBinding
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class ShopProductItemBigGridViewHolder(
        itemView: View,
        private val shopProductClickedListener: ShopProductClickedListener?,
        private val shopProductImpressionListener: ShopProductImpressionListener?,
        private val shopTrackType: Int,
        private val isShowTripleDot: Boolean
) : AbstractViewHolder<ShopProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_shop_newproduct_big_grid
    }

    private val viewBinding : ItemShopNewproductBigGridBinding? by viewBinding()
    private val productCard: ProductCardGridView? = viewBinding?.productCard

    override fun bind(shopProductUiModel: ShopProductUiModel) {
        productCard?.setProductModel(
                ShopPageProductListMapper.mapToProductCardModel(
                        shopProductUiModel = shopProductUiModel,
                        isWideContent = true,
                        isShowThreeDots = isShowTripleDot
                )
        )
        productCard?.setThreeDotsOnClickListener {
            shopProductClickedListener?.onThreeDotsClicked(shopProductUiModel, adapterPosition)
        }

        productCard?.setOnClickListener {
            shopProductClickedListener?.onProductClicked(shopProductUiModel, shopTrackType, adapterPosition)
        }

        productCard?.setImageProductViewHintListener(shopProductUiModel, object : ViewHintListener {
            override fun onViewHint() {
                shopProductImpressionListener?.onProductImpression(shopProductUiModel, shopTrackType, adapterPosition)
            }
        })
    }

}