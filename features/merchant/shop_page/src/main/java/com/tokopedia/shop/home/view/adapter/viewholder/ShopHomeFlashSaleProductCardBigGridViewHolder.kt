package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeFlashSaleProductCardBigGridViewHolder(
    itemView: View,
    listener: ShopHomeFlashSaleWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        // 12 + 12 + 4 + 8 + 4 + 12 + 12
        private const val PADDING_AND_MARGIN = 64
        private const val TWO = 2
    }

    private var uiModel: ShopHomeProductUiModel? = null
    private var productCardBigGrid: ProductCardGridView? = itemView.findViewById(R.id.fs_product_card_big_grid)

    init {
        adjustProductCardWidth(false)
        setupClickListener(listener)
        setupAddToCartListener(listener)
    }

    private fun setupAddToCartListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let{ shopHomeProductUiModel ->
            productCardBigGrid?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener.onProductAtcNonVariantQuantityEditorChanged(
                        shopHomeProductUiModel,
                        quantity
                    )
                }
            })

            productCardBigGrid?.setAddVariantClickListener {
                listener.onProductAtcVariantClick(shopHomeProductUiModel)
            }

            productCardBigGrid?.setAddToCartOnClickListener {
                listener.onProductAtcDefaultClick(
                    shopHomeProductUiModel,
                    shopHomeProductUiModel.minimumOrder
                )
            }
        }
    }

    fun bindData(uiModel: ShopHomeProductUiModel, fsUiModel: ShopHomeFlashSaleUiModel?) {
        this.uiModel = uiModel
        productCardBigGrid?.applyCarousel()
        productCardBigGrid?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        productCardBigGrid?.setProductModel(
            ShopPageHomeMapper.mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = uiModel,
                widgetName = fsUiModel?.name.orEmpty()
            )
        )
    }

    private fun setupClickListener(listener: ShopHomeFlashSaleWidgetListener) {
        productCardBigGrid?.setOnClickListener {
            uiModel?.run { listener.onFlashSaleProductClicked(this) }
        }
    }

    @Suppress("SameParameterValue")
    private fun adjustProductCardWidth(isTablet: Boolean) {
        val productCardWidth = (getScreenWidth() - PADDING_AND_MARGIN) / TWO
        if (!isTablet) { productCardBigGrid?.layoutParams = ViewGroup.LayoutParams(productCardWidth, ViewGroup.LayoutParams.WRAP_CONTENT) }
    }
}