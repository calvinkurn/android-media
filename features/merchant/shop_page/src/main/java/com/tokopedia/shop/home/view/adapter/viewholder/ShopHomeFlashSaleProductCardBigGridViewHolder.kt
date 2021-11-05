package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeFlashSaleProductCardBigGridViewHolder(
    itemView: View,
    listener: ShopHomeFlashSaleWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val PADDING_AND_MARGIN = 40
    }

    private var uiModel: ShopHomeProductUiModel? = null
    private var productCardBigGrid: ProductCardGridView? = itemView.findViewById(R.id.fs_product_card_big_grid)

    init {
        adjustProductCardWidth(false)
        setupClickListener(listener)
    }

    fun bindData(uiModel: ShopHomeProductUiModel) {
        this.uiModel = uiModel
        productCardBigGrid?.setProductModel(
            ShopPageHomeMapper.mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = uiModel
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
        val productCardWidth = (getScreenWidth() - PADDING_AND_MARGIN) / 2
        if (!isTablet) { productCardBigGrid?.layoutParams = ViewGroup.LayoutParams(productCardWidth, ViewGroup.LayoutParams.WRAP_CONTENT) }
    }
}