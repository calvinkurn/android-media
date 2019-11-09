package com.tokopedia.similarsearch

import android.view.View
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.similar_search_product_card_layout.view.*

internal class SimilarProductItemViewHolder(itemView: View): BaseViewHolder<Product>(itemView) {

    companion object {
        val LAYOUT = R.layout.similar_search_product_card_layout
    }

    override fun bind(item: Product) {
        val productCardModel = ProductCardModel(
                productName = item.name,
                productImageUrl = item.imageUrl,
                discountPercentage = if (getDiscountPercentageVisible(item)) "${item.discountPercentage}%" else "",
                slashedPrice = item.originalPrice,
                formattedPrice = item.price,
                shopLocation = item.shop.location,
                shopBadgeList = createProductCardModelShopBadgeList(item),
                ratingCount = item.rating,
                reviewCount = item.countReview,
                isWishlisted = item.isWishlisted,
                isWishlistVisible = true
        )

        itemView.productCardView?.setProductModel(productCardModel)
    }

    private fun getDiscountPercentageVisible(similarProductItem: Product): Boolean {
        return similarProductItem.discountPercentage > 0
    }

    private fun createProductCardModelShopBadgeList(similarProductItem: Product): List<ProductCardModel.ShopBadge> {
        val productCardModelShopBadgeList = mutableListOf<ProductCardModel.ShopBadge>()

        similarProductItem.badgeList.forEach {
            productCardModelShopBadgeList.add(ProductCardModel.ShopBadge(
                    isShown = true,
                    imageUrl = it.imageUrl
            ))
        }

        return productCardModelShopBadgeList
    }
}