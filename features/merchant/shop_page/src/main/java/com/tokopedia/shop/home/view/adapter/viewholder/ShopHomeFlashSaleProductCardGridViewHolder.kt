package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeFlashSaleProductCardGridViewHolder(
    itemView: View,
    listener: ShopHomeFlashSaleWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    private var uiModel: ShopHomeProductUiModel? = null
    private var fsUiModel: ShopHomeFlashSaleUiModel? = null
    private var productCardGrid: ProductCardGridView? = itemView.findViewById(R.id.fs_product_card_grid)

    init {
        setupClickListener(listener)
        setupImpressionListener(listener)
    }

    private fun setupImpressionListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let {
            productCardGrid?.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    listener.onFlashSaleProductImpression(it, fsUiModel, adapterPosition)
                }
            })
        }
    }

    fun bindData(uiModel: ShopHomeProductUiModel, fsUiModel: ShopHomeFlashSaleUiModel?) {
        this.uiModel = uiModel
        this.fsUiModel = fsUiModel
        productCardGrid?.applyCarousel()
        productCardGrid?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        productCardGrid?.setProductModel(
            ShopPageHomeMapper.mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = uiModel
            )
        )
    }

    private fun setupClickListener(listener: ShopHomeFlashSaleWidgetListener) {
        productCardGrid?.setOnClickListener {
            uiModel?.let { productModel ->
                fsUiModel?.let { widgetModel ->
                    listener.onFlashSaleProductClicked(
                        model = productModel,
                        widgetModel = widgetModel,
                        position = adapterPosition
                    )
                }
            }
        }
    }
}