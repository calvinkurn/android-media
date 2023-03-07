package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.databinding.ItemShopNewproductSmallGridBinding
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductViewHolder(
    itemView: View,
    private val shopProductClickedListener: ShopProductClickedListener?,
    private val shopProductImpressionListener: ShopProductImpressionListener?,
    private val isFixWidth: Boolean,
    private val deviceWidth: Int,
    @param:ShopTrackProductTypeDef @field:ShopTrackProductTypeDef
    private val shopTrackType: Int,
    private val layoutType: Int,
    private val isShowTripleDot: Boolean
) : AbstractViewHolder<ShopProductUiModel>(itemView) {
    private val totalReview: TextView? = null
    private val viewBinding: ItemShopNewproductSmallGridBinding? by viewBinding()
    private var productCard: ProductCardGridView? = null

    init {
        findViews()
    }

    companion object {
        @LayoutRes
        val GRID_LAYOUT = R.layout.item_shop_newproduct_small_grid
        const val RATIO_WITH_RELATIVE_TO_SCREEN = 2.3
    }

    private fun findViews() {
        productCard = viewBinding?.productCard
    }

    override fun bind(shopProductUiModel: ShopProductUiModel) {
        val productCardModel = ShopPageProductListMapper.mapToProductCardModel(
            shopProductUiModel = shopProductUiModel,
            isWideContent = false,
            isShowThreeDots = isShowTripleDot
        )
        productCard?.setProductModel(productCardModel)

        if (shopProductImpressionListener?.getSelectedEtalaseName().orEmpty().isNotEmpty()) {
            productCard?.setImageProductViewHintListener(
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
        }

        if (isFixWidth && deviceWidth > 0 && layoutType == ShopProductViewHolder.GRID_LAYOUT) {
            itemView.layoutParams.width = (deviceWidth / RATIO_WITH_RELATIVE_TO_SCREEN).toInt()
        }

        productCard?.setOnClickListener {
            shopProductClickedListener?.onProductClicked(shopProductUiModel, shopTrackType, adapterPosition)
        }

        productCard?.setThreeDotsOnClickListener {
            shopProductClickedListener?.onThreeDotsClicked(shopProductUiModel, shopTrackType)
        }

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

    override fun bind(shopProductUiModel: ShopProductUiModel, payloads: MutableList<Any>) {
        if (payloads.getOrNull(0) !is Boolean) return

        productCard?.setThreeDotsOnClickListener {
            shopProductClickedListener?.onThreeDotsClicked(shopProductUiModel, shopTrackType)
        }
    }
}
