package com.tokopedia.shop.product.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.databinding.ItemShopProductCardListBinding
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class ShopProductItemListViewHolder(
    itemView: View,
    private val shopProductClickedListener: ShopProductClickedListener?,
    private val shopProductImpressionListener: ShopProductImpressionListener?,
    private val shopTrackType: Int,
    private val isShowTripleDot: Boolean
) : AbstractViewHolder<ShopProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_shop_product_card_list
        private const val RED_STOCK_BAR_LABEL_MATCH_VALUE = "segera habis"
    }

    private val viewBinding: ItemShopProductCardListBinding? by viewBinding()
    private val productCardView: ProductCardListView? = viewBinding?.productCardView

    override fun bind(shopProductUiModel: ShopProductUiModel) {
        val stockBarLabel = shopProductUiModel.stockLabel
        var stockBarLabelColor = ""
        if (stockBarLabel.equals(RED_STOCK_BAR_LABEL_MATCH_VALUE, ignoreCase = true)) {
            stockBarLabelColor = ShopUtil.getColorHexString(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_RN600
            )
        }
        val productCardModel = ShopPageProductListMapper.mapToProductCardModel(
            shopProductUiModel = shopProductUiModel,
            isWideContent = false,
            isShowThreeDots = isShowTripleDot
        ).copy(
            stockBarLabelColor = stockBarLabelColor
        )
        productCardView?.setProductModel(productCardModel)
        productCardView?.setThreeDotsOnClickListener {
            shopProductClickedListener?.onThreeDotsClicked(shopProductUiModel, adapterPosition)
        }

        productCardView?.setOnClickListener {
            shopProductClickedListener?.onProductClicked(shopProductUiModel, shopTrackType, adapterPosition)
        }

        productCardView?.setImageProductViewHintListener(
            shopProductUiModel,
            object : ViewHintListener {
                override fun onViewHint() {
                    shopProductImpressionListener?.onProductImpression(shopProductUiModel, shopTrackType, adapterPosition)
                    if (productCardModel.isButtonAtcShown()) {
                        shopProductImpressionListener?.onImpressionProductAtc(
                            shopProductUiModel,
                            adapterPosition
                        )
                    }
                }
            }
        )

        productCardView?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                shopProductClickedListener?.onProductAtcNonVariantQuantityEditorChanged(
                    shopProductUiModel,
                    quantity
                )
            }
        })

        productCardView?.setAddVariantClickListener {
            shopProductClickedListener?.onProductAtcVariantClick(
                shopProductUiModel
            )
        }

        productCardView?.setAddToCartOnClickListener {
            shopProductClickedListener?.onProductAtcDefaultClick(
                shopProductUiModel,
                shopProductUiModel.minimumOrder
            )
        }
    }
}
