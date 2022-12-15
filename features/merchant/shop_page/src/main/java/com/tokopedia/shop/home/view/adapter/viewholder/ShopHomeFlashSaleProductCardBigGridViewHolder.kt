package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeFlashSaleProductCardBigGridViewHolder(
    itemView: View,
    private val listener: ShopHomeFlashSaleWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        // 12 + 12 + 4 + 8 + 4 + 12 + 12
        private const val PADDING_AND_MARGIN = 64
        private const val TWO = 2
    }

    private var uiModel: ShopHomeProductUiModel? = null
    private var fsUiModel: ShopHomeFlashSaleUiModel? = null
    private var productCardBigGrid: ProductCardGridView? = itemView.findViewById(R.id.fs_product_card_big_grid)

    init {
        adjustProductCardWidth(false)
        setupClickListener(listener)
        setupImpressionListener(listener)
    }

    private fun setupImpressionListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let {
            productCardBigGrid?.setImageProductViewHintListener(
                it,
                object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onFlashSaleProductImpression(it, fsUiModel, adapterPosition)
                    }
                }
            )
        }
    }

    private fun setupAddToCartListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let { shopHomeProductUiModel ->
            productCardBigGrid?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener.onProductAtcNonVariantQuantityEditorChanged(
                        shopHomeProductUiModel,
                        quantity,
                        fsUiModel?.name.orEmpty()
                    )
                }
            })

            productCardBigGrid?.setAddVariantClickListener {
                listener.onProductAtcVariantClick(shopHomeProductUiModel)
            }

            productCardBigGrid?.setAddToCartOnClickListener {
                listener.onProductAtcDefaultClick(
                    shopHomeProductUiModel,
                    shopHomeProductUiModel.minimumOrder,
                    fsUiModel?.name.orEmpty()
                )
            }
        }
    }

    fun bindData(uiModel: ShopHomeProductUiModel, fsUiModel: ShopHomeFlashSaleUiModel?) {
        this.uiModel = uiModel
        this.fsUiModel = fsUiModel
        productCardBigGrid?.applyCarousel()
        productCardBigGrid?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        val productCardModel = ShopPageHomeMapper.mapToProductCardCampaignModel(
            isHasAddToCartButton = false,
            hasThreeDots = false,
            shopHomeProductViewModel = uiModel,
            widgetName = fsUiModel?.name.orEmpty(),
            statusCampaign = fsUiModel?.data?.firstOrNull()?.statusCampaign.orEmpty()
        )
        productCardBigGrid?.setProductModel(productCardModel)
        setupAddToCartListener(listener)
        setProductImpressionListener(productCardModel, listener)
    }

    private fun setProductImpressionListener(
        productCardModel: ProductCardModel,
        listener: ShopHomeFlashSaleWidgetListener
    ) {
        uiModel?.let { productUiModel ->
            productCardBigGrid?.setImageProductViewHintListener(
                productUiModel,
                object : ViewHintListener {
                    override fun onViewHint() {
                        if (productCardModel.isButtonAtcShown()) {
                            listener.onImpressionProductAtc(
                                productUiModel,
                                adapterPosition,
                                fsUiModel?.name.orEmpty()
                            )
                        }
                    }
                }
            )
        }
    }

    private fun setupClickListener(listener: ShopHomeFlashSaleWidgetListener) {
        productCardBigGrid?.setOnClickListener {
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

    @Suppress("SameParameterValue")
    private fun adjustProductCardWidth(isTablet: Boolean) {
        val productCardWidth = (getScreenWidth() - PADDING_AND_MARGIN) / TWO
        if (!isTablet) { productCardBigGrid?.layoutParams = ViewGroup.LayoutParams(productCardWidth, ViewGroup.LayoutParams.WRAP_CONTENT) }
    }
}
