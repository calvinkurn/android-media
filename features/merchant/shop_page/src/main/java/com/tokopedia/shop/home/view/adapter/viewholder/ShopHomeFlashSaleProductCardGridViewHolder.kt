package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifycomponents.dpToPx

class ShopHomeFlashSaleProductCardGridViewHolder(
    itemView: View,
    private val listener: ShopHomeFlashSaleWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    private var uiModel: ShopHomeProductUiModel? = null
    private var fsUiModel: ShopHomeFlashSaleUiModel? = null
    private var productCardGrid: ProductCardGridView? = itemView.findViewById(R.id.fs_product_card_grid)
    private val paddingOffset = 6f.dpToPx()

    init {
        setupClickListener(listener)
        setupImpressionListener(listener)
    }

    private fun setupImpressionListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let {
            productCardGrid?.setImageProductViewHintListener(
                it,
                object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onFlashSaleProductImpression(it, fsUiModel, adapterPosition)
                    }
                }
            )
        }
    }

    fun bindData(uiModel: ShopHomeProductUiModel, fsUiModel: ShopHomeFlashSaleUiModel?) {
        this.uiModel = uiModel
        this.fsUiModel = fsUiModel
        productCardGrid?.applyCarousel()
        productCardGrid?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        val productCardModel = ShopPageHomeMapper.mapToProductCardCampaignModel(
            isHasAddToCartButton = false,
            hasThreeDots = false,
            shopHomeProductViewModel = uiModel,
            widgetName = fsUiModel?.name.orEmpty(),
            statusCampaign = fsUiModel?.data?.firstOrNull()?.statusCampaign.orEmpty()
        )
        productCardGrid?.setProductModel(productCardModel)
        setupAddToCartListener(listener)
        setProductImpressionListener(productCardModel, listener)
    }

    fun getHeightOfImageProduct(action: (Int) -> Unit) {
        val productImageView = productCardGrid?.getProductImageView()
        val viewTreeObserver: ViewTreeObserver? = productImageView?.viewTreeObserver
        if (viewTreeObserver?.isAlive.orFalse()) {
            viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    productImageView.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    action(productImageView.height + paddingOffset.toInt())
                }
            })
        }
    }

    private fun setProductImpressionListener(
        productCardModel: ProductCardModel,
        listener: ShopHomeFlashSaleWidgetListener
    ) {
        uiModel?.let { productUiModel ->
            productCardGrid?.setImageProductViewHintListener(
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

    private fun setupAddToCartListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let { shopHomeProductUiModel ->
            productCardGrid?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener.onProductAtcNonVariantQuantityEditorChanged(
                        shopHomeProductUiModel,
                        quantity,
                        fsUiModel?.name.orEmpty()
                    )
                }
            })

            productCardGrid?.setAddVariantClickListener {
                listener.onProductAtcVariantClick(shopHomeProductUiModel)
            }

            productCardGrid?.setAddToCartOnClickListener {
                listener.onProductAtcDefaultClick(
                    shopHomeProductUiModel,
                    shopHomeProductUiModel.minimumOrder,
                    fsUiModel?.name.orEmpty()
                )
            }
        }
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
