package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeFlashSaleProductListViewHolder(
    itemView: View,
    private val listener: ShopHomeFlashSaleWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    private var uiModel: ShopHomeProductUiModel? = null
    private var productCardList: ProductCardListView? = itemView.findViewById(R.id.fs_product_card_list)
    private var flashSaleWidgetUiModel: ShopHomeFlashSaleUiModel? = null

    init {
        setupClickListener(listener)
    }

    fun bindData(uiModel: ShopHomeProductUiModel, fsUiModel: ShopHomeFlashSaleUiModel?) {
        this.uiModel = uiModel
        this.flashSaleWidgetUiModel = fsUiModel
        val productCardModel = ShopPageHomeMapper.mapToProductCardCampaignModel(
            isHasAddToCartButton = false,
            hasThreeDots = false,
            shopHomeProductViewModel = uiModel,
            widgetName = fsUiModel?.name.orEmpty()
        )
        productCardList?.setProductModel(productCardModel)
        setupAddToCartListener(listener)
        setProductImpressionListener(productCardModel, listener)
    }

    private fun setProductImpressionListener(
        productCardModel: ProductCardModel,
        listener: ShopHomeFlashSaleWidgetListener
    ) {
        uiModel?.let { productUiModel ->
            productCardList?.setImageProductViewHintListener(
                productUiModel,
                object : ViewHintListener {
                    override fun onViewHint() {
                        if (productCardModel.isButtonAtcShown()) {
                            listener.onImpressionProductAtc(
                                productUiModel,
                                adapterPosition,
                                flashSaleWidgetUiModel?.name.orEmpty()
                            )
                        }
                    }
                })
        }
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