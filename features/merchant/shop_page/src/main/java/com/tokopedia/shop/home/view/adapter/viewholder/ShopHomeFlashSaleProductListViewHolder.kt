package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeFlashSaleProductListViewHolder(
    itemView: View,
    listener: ShopHomeFlashSaleWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    private var uiModel: ShopHomeProductUiModel? = null
    private var productCardList: ProductCardListView? = itemView.findViewById(R.id.fs_product_card_list)

    init {
        setupClickListener(listener)
        setupAddToCartListener(listener)
    }

    fun bindData(uiModel: ShopHomeProductUiModel, fsUiModel: ShopHomeFlashSaleUiModel?) {
        this.uiModel = uiModel
        productCardList?.setProductModel(
            ShopPageHomeMapper.mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = uiModel,
                widgetName = fsUiModel?.name.orEmpty()
            )
        )
    }

    private fun setupAddToCartListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let{ shopHomeProductUiModel ->
            productCardList?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener.onProductAtcNonVariantQuantityEditorChanged(
                        shopHomeProductUiModel,
                        quantity
                    )
                }
            })

            productCardList?.setAddVariantClickListener {
                listener.onProductAtcVariantClick(shopHomeProductUiModel)
            }

            productCardList?.setAddToCartOnClickListener {
                listener.onProductAtcDefaultClick(
                    shopHomeProductUiModel,
                    shopHomeProductUiModel.minimumOrder
                )
            }
        }
    }

    private fun setupClickListener(listener: ShopHomeFlashSaleWidgetListener) {
        productCardList?.setOnClickListener {
            uiModel?.run { listener.onFlashSaleProductClicked(this) }
        }
    }
}