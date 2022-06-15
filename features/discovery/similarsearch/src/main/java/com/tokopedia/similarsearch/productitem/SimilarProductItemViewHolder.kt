package com.tokopedia.similarsearch.productitem

import android.view.View
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.abstraction.BaseViewHolder
import com.tokopedia.similarsearch.databinding.SimilarSearchProductCardLayoutBinding
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.utils.view.binding.viewBinding

internal class SimilarProductItemViewHolder(
        itemView: View,
        private val similarProductItemListener: SimilarProductItemListener
): BaseViewHolder<Product>(itemView) {

    companion object {
        val LAYOUT = R.layout.similar_search_product_card_layout
    }
    private var binding: SimilarSearchProductCardLayoutBinding? by viewBinding()

    override fun bind(item: Product) {
        val binding = binding ?: return
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
                labelGroupList = createProductCardLabelGroupList(item),
                freeOngkir = ProductCardModel.FreeOngkir(item.freeOngkir.isActive, item.freeOngkir.imgUrl),
                hasThreeDots = true
        )

        binding.productCardView.setProductModel(productCardModel)

        binding.productCardView.setOnClickListener {
            similarProductItemListener.onItemClicked(item, adapterPosition)
        }

        binding.productCardView.setThreeDotsOnClickListener {
            similarProductItemListener.onThreeDotsClicked(item, adapterPosition)
        }
    }

    private fun getDiscountPercentageVisible(similarProductItem: Product): Boolean {
        return similarProductItem.discountPercentage > 0
    }

    private fun createProductCardModelShopBadgeList(similarProductItem: Product): List<ProductCardModel.ShopBadge> {
        return similarProductItem.badgeList.map {
            ProductCardModel.ShopBadge(
                    isShown = true,
                    imageUrl = it.imageUrl
            )
        }
    }

    private fun createProductCardLabelGroupList(similarProductItem: Product): List<ProductCardModel.LabelGroup> {
        return similarProductItem.labelGroups.map {
            ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type)
        }
    }
}