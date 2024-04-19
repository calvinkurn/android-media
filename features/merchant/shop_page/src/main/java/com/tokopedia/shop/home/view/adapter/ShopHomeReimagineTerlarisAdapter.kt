package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.shop.analytic.model.ShopHomeTerlarisWidgetTrackerDataModel
import com.tokopedia.shop.common.util.productcard.ShopProductCardColorHelper
import com.tokopedia.shop.databinding.ItemShopReimagineTerlarisWidgetScrollableBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeReimagineTerlarisViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeReimagineTerlarisViewHolder.Companion.PRODUCT_THREE
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifycomponents.toPx

class ShopHomeReimagineTerlarisAdapter(
    private val listener: ShopHomeReimagineTerlarisViewHolder.Listener,
    private val isOverrideTheme: Boolean,
    private val isFestivity: Boolean,
    private val backgroundColor: String,
    private val patternColorType: String,
    private val element: ShopHomeCarousellProductUiModel
) : RecyclerView.Adapter<ShopHomeReimagineTerlarisAdapter.ViewHolder>() {

    companion object {
        private const val LEFT_PEEK_MARGIN = 56
    }

    private val productCardColorHelper = ShopProductCardColorHelper()
    private val noLeftPeekProductCardWidth by lazy { getScreenWidth() }
    private val leftPeekProductCardWidth by lazy { getScreenWidth() - LEFT_PEEK_MARGIN.toPx() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopReimagineTerlarisWidgetScrollableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(element.productList[position])
    }
    override fun getItemCount() = element.productList.size

    inner class ViewHolder(
        private val binding: ItemShopReimagineTerlarisWidgetScrollableBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ShopHomeProductUiModel) {
            sendImpressionTracker(element.productList)
            renderProductCard(product)
            adjustProductCardWidthAppearance(element.productList.size)
        }

        private fun sendImpressionTracker(productList: List<ShopHomeProductUiModel>) {
            if (productList.size >= PRODUCT_THREE) {
                setupImpressionListener(element.productList)
            }
        }

        private fun renderProductCard(product: ShopHomeProductUiModel) {
            val productCardModel = ShopPageHomeMapper.mapToProductCardModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = product,
                isWideContent = false,
                productRating = product.averageRating,
                forceLightModeColor = false,
                patternColorType = patternColorType,
                backgroundColor = backgroundColor,
                isFestivity = isFestivity,
                makeProductCardTransparent = true
            )

            binding.productCard.setProductModel(productCardModel)

            val position = bindingAdapterPosition.inc()
            binding.tpgRank.text = position.toString()
            binding.productCard.setOnClickListener {
                listener.onProductClick(
                    ShopHomeTerlarisWidgetTrackerDataModel(
                        productId = product.id,
                        productName = product.name,
                        productPrice = product.displayedPrice,
                        position = position,
                        widgetId = element.widgetId
                    )
                )
            }
        }

        private fun adjustProductCardWidthAppearance(productCount: Int) {
            val productCardWidthPx = if (productCount == PRODUCT_THREE) {
                noLeftPeekProductCardWidth
            } else {
                leftPeekProductCardWidth
            }
            binding.productContainer.width(productCardWidthPx)
        }

        private fun setupImpressionListener(carouselData: List<ShopHomeProductUiModel>) {
            itemView.addOnImpressionListener(element.impressHolder) {
                listener.onProductImpression(carouselData, bindingAdapterPosition, element.widgetId)
            }
        }

        private fun FrameLayout.width(widthPx: Int) {
            val layoutParams = this.layoutParams
            layoutParams.width = widthPx

            this.layoutParams = layoutParams
        }
    }
}
