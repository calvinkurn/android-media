package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
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

class ShopHomeFlashSaleProductCardBigGridViewHolder(
    itemView: View,
    private val listener: ShopHomeFlashSaleWidgetListener,
    private val parentPosition: Int
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        // leftMargin + rightMargin + middleMargin
        // 12 + 12 + 2
        private const val PADDING_AND_MARGIN = 26
        private const val TWO = 2
        private val paddingOffset = 6f.dpToPx()
        private const val RED_STOCK_BAR_LABEL_MATCH_VALUE = "segera habis"
    }

    private var uiModel: ShopHomeProductUiModel? = null
    private var fsUiModel: ShopHomeFlashSaleUiModel? = null
    private var productCardBigGrid: ProductCardGridView? = itemView.findViewById(R.id.fs_product_card_big_grid)

    init {
        adjustProductCardWidth()
    }

    private fun setupImpressionListener(listener: ShopHomeFlashSaleWidgetListener) {
        uiModel?.let {
            productCardBigGrid?.setImageProductViewHintListener(
                it,
                object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onFlashSaleProductImpression(it, fsUiModel, ShopUtil.getActualPositionFromIndex(adapterPosition), parentPosition)
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
        setupClickListener(listener)
        setupImpressionListener(listener)
        productCardBigGrid?.applyCarousel()
        productCardBigGrid?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        val stockBarLabel = uiModel.stockLabel
        var stockBarLabelColor = ""
        if (stockBarLabel.equals(RED_STOCK_BAR_LABEL_MATCH_VALUE, ignoreCase = true)) {
            stockBarLabelColor = ShopUtil.getColorHexString(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_RN600
            )
        }
        val productCardModel = ShopPageHomeMapper.mapToProductCardCampaignModel(
            isHasAddToCartButton = false,
            hasThreeDots = false,
            shopHomeProductViewModel = uiModel,
            widgetName = fsUiModel?.name.orEmpty(),
            statusCampaign = fsUiModel?.data?.firstOrNull()?.statusCampaign.orEmpty()
        ).copy(
            stockBarLabelColor = stockBarLabelColor
        )
        productCardBigGrid?.setProductModel(productCardModel)
        setupAddToCartListener(listener)
        setProductImpressionListener(productCardModel, listener)
    }

    fun getHeightOfImageProduct(action: (Int) -> Unit){
        val productImageView = productCardBigGrid?.getProductImageView()
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
                        position = ShopUtil.getActualPositionFromIndex(adapterPosition),
                        parentPosition = parentPosition
                    )
                }
            }
        }
    }

    private fun adjustProductCardWidth() {
        val productCardBigGrid = productCardBigGrid ?: return
        val productCardWidth = (getScreenWidth() - PADDING_AND_MARGIN.toFloat().dpToPx()) / TWO
        val layoutParams = productCardBigGrid.layoutParams
        layoutParams.width = productCardWidth.toInt()
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        productCardBigGrid.layoutParams = layoutParams
    }
}
