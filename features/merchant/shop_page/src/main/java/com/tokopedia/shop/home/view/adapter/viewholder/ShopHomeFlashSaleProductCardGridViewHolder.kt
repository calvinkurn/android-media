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
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopHomeFlashSaleProductCardGridViewHolder(
    itemView: View,
    private val listener: ShopHomeFlashSaleWidgetListener,
    private val parentPosition: Int
) : RecyclerView.ViewHolder(itemView) {

    companion object{
        private const val RED_STOCK_BAR_LABEL_MATCH_VALUE = "segera habis"
    }

    private var uiModel: ShopHomeProductUiModel? = null
    private var fsUiModel: ShopHomeFlashSaleUiModel? = null
    private var productCardGrid: ProductCardGridView? = itemView.findViewById(R.id.fs_product_card_grid)
    private val paddingOffset = 6f.dpToPx()

    private fun setupImpressionListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let {
            productCardGrid?.setImageProductViewHintListener(
                it,
                object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onFlashSaleProductImpression(it, fsUiModel, ShopUtil.getActualPositionFromIndex(adapterPosition), parentPosition)
                    }
                }
            )
        }
    }

    fun bindData(uiModel: ShopHomeProductUiModel, fsUiModel: ShopHomeFlashSaleUiModel?) {
        this.uiModel = uiModel
        this.fsUiModel = fsUiModel
        setupClickListener(listener)
        setupImpressionListener(listener)
        productCardGrid?.applyCarousel()
        productCardGrid?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        val stockBarLabel = uiModel.stockLabel
        var stockBarLabelColor = ""
        if (stockBarLabel.equals(RED_STOCK_BAR_LABEL_MATCH_VALUE, ignoreCase = true)) {
            stockBarLabelColor = ShopUtil.getColorHexString(
                itemView.context,
                unifyprinciplesR.color.Unify_RN600
            )
        }
        val shopBadges = uiModel.shopBadgeList.map {
            ProductCardModel.ShopBadge(imageUrl = it.imageUrl, title = it.title)
        }
        val productCardModel = ShopPageHomeMapper.mapToProductCardCampaignModel(
            isHasAddToCartButton = false,
            hasThreeDots = false,
            shopHomeProductViewModel = uiModel,
            widgetName = fsUiModel?.name.orEmpty(),
            statusCampaign = fsUiModel?.data?.firstOrNull()?.statusCampaign.orEmpty(),
            forceLightModeColor = listener.isOverrideTheme(),
            patternColorType = listener.getPatternColorType(),
            backgroundColor = listener.getBackgroundColor(),
            isFestivity = fsUiModel?.isFestivity.orFalse()
        ).copy(
            stockBarLabelColor = stockBarLabelColor,
            isInBackground = true,
            shopBadgeList = shopBadges
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
                        position = ShopUtil.getActualPositionFromIndex(adapterPosition),
                        parentPosition = parentPosition
                    )
                }
            }
        }
    }
}
