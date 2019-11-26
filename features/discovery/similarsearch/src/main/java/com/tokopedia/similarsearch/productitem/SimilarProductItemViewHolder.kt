package com.tokopedia.similarsearch.productitem

import android.view.View
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.abstraction.BaseViewHolder
import kotlinx.android.synthetic.main.similar_search_product_card_layout.view.*

internal class SimilarProductItemViewHolder(
        itemView: View,
        private val similarProductItemListener: SimilarProductItemListener
): BaseViewHolder<Product>(itemView) {

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

        itemView.productCardView?.setOnLongClickListener {
            true
        }

        itemView.productCardView?.setOnClickListener {
            similarProductItemListener.onItemClicked(item, adapterPosition)
        }

        itemView.productCardView?.setButtonWishlistOnClickListener {
            similarProductItemListener.onItemWishlistClicked(item.id, item.isWishlisted)
        }
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

    override fun bind(payload: List<Any>) {
        if (payload.isNotEmpty() && payload[0] is Boolean) {
            itemView.productCardView?.setButtonWishlistImage(payload[0] as Boolean)
        }
    }
}