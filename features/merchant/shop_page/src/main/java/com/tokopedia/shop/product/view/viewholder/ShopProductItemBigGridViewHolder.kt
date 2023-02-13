package com.tokopedia.shop.product.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
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
        private const val RED_STOCK_BAR_LABEL_MATCH_VALUE = "segera habis"
    }

    private val viewBinding: ItemShopNewproductBigGridBinding? by viewBinding()
    private val productCard: ProductCardGridView? = viewBinding?.productCard

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
            isWideContent = true,
            isShowThreeDots = isShowTripleDot
        ).copy(
            stockBarLabelColor = stockBarLabelColor
        )
        productCard?.setProductModel(productCardModel)
        productCard?.setThreeDotsOnClickListener {
            shopProductClickedListener?.onThreeDotsClicked(shopProductUiModel, adapterPosition)
        }

        productCard?.setOnClickListener {
            shopProductClickedListener?.onProductClicked(shopProductUiModel, shopTrackType, adapterPosition)
        }

        productCard?.setImageProductViewHintListener(
            shopProductUiModel,
            object : ViewHintListener {
                override fun onViewHint() {
                    shopProductImpressionListener?.onProductImpression(
                        shopProductUiModel,
                        shopTrackType,
                        adapterPosition
                    )
                    if (productCardModel.isButtonAtcShown()) {
                        shopProductImpressionListener?.onImpressionProductAtc(
                            shopProductUiModel,
                            adapterPosition
                        )
                    }
                }
            }
        )

        productCard?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                shopProductClickedListener?.onProductAtcNonVariantQuantityEditorChanged(
                    shopProductUiModel,
                    quantity
                )
            }
        })

        productCard?.setAddVariantClickListener {
            shopProductClickedListener?.onProductAtcVariantClick(
                shopProductUiModel
            )
        }

        productCard?.setAddToCartOnClickListener {
            shopProductClickedListener?.onProductAtcDefaultClick(
                shopProductUiModel,
                shopProductUiModel.minimumOrder
            )
        }
    }
}
