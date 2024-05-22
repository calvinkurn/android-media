package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopHomeTerlarisWidgetTrackerDataModel
import com.tokopedia.shop.databinding.ItemShopHomeReimagineTerlarisProductRankBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeReimagineTerlarisViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeReimagineTerlarisViewHolder.Companion.PRODUCT_THREE
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifycomponents.toPx

class ShopHomeReimagineTerlarisAdapter(
    private val listener: ShopHomeReimagineTerlarisViewHolder.Listener,
    private val element: ShopHomeCarousellProductUiModel,
    private val products: List<ShopHomeProductUiModel>
) : RecyclerView.Adapter<ShopHomeReimagineTerlarisAdapter.ViewHolder>() {

    companion object {
        private const val LEFT_PEEK_MARGIN = 56
    }

    private val leftPeekProductCardWidth by lazy { getScreenWidth() - LEFT_PEEK_MARGIN.toPx() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopHomeReimagineTerlarisProductRankBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }
    override fun getItemCount() = products.size

    inner class ViewHolder(
        private val binding: ItemShopHomeReimagineTerlarisProductRankBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ShopHomeProductUiModel) {
            sendImpressionTracker(products)
            renderProductCard(product)
            adjustProductCardWidthAppearance(products.size)
        }

        private fun sendImpressionTracker(productList: List<ShopHomeProductUiModel>) {
            if (productList.size >= PRODUCT_THREE) {
                setupImpressionListener(products)
            }
        }

        private fun renderProductCard(product: ShopHomeProductUiModel) {
            val withoutBadgeList = listOf<ProductCardModel.ShopBadge>()

            val productCardModel = ShopPageHomeMapper.mapToProductCardModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = product,
                isWideContent = false,
                productRating = product.averageRating,
                isOverrideTheme = true,
                patternColorType = listener.getPatternColorType(),
                backgroundColor = listener.getBackgroundColor(),
                isFestivity = element.isFestivity,
                makeProductCardTransparent = true,
                atcVariantButtonText = binding.productCard.context?.getString(R.string.shop_atc).orEmpty()
            ).copy(shopBadgeList = withoutBadgeList)

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
            if (productCount > PRODUCT_THREE) {
                binding.productContainer.width(leftPeekProductCardWidth)
            }
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
