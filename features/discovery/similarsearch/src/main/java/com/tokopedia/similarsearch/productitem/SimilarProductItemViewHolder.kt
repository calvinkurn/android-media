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
) : BaseViewHolder<Product>(itemView) {
    private var binding: SimilarSearchProductCardLayoutBinding? by viewBinding()

    override fun bind(item: Product) {
        val binding = binding ?: return
        val productCardModel = ProductCardModel(
            productName = item.name,
            productImageUrl = item.imageUrl,
            discountPercentage = if (getDiscountPercentageVisible(item)) "${item.discountPercentage}%" else "",
            slashedPrice = item.originalPrice,
            formattedPrice = item.price,
            priceRange = item.priceRange,
            shopLocation = item.shop.location.ifEmpty { item.shop.name },
            shopBadgeList = createProductCardModelShopBadgeList(item),
            countSoldRating = item.ratingAverage,
            reviewCount = item.countReview,
            labelGroupList = createProductCardLabelGroupList(item),
            labelGroupVariantList = createProductCardLabelGroupVariantList(item),
            freeOngkir = ProductCardModel.FreeOngkir(
                item.freeOngkir.isActive,
                item.freeOngkir.imgUrl
            ),
            hasThreeDots = true,
            cardInteraction = true,
        )

        binding.productCardView.setProductModel(productCardModel)

        binding.productCardView.setOnClickListener {
            similarProductItemListener.onItemClicked(item, bindingAdapterPosition)
        }

        binding.productCardView.setThreeDotsOnClickListener {
            similarProductItemListener.onThreeDotsClicked(item, bindingAdapterPosition)
        }
    }

    private fun getDiscountPercentageVisible(similarProductItem: Product): Boolean {
        return similarProductItem.discountPercentage > 0
    }

    private fun createProductCardModelShopBadgeList(similarProductItem: Product): List<ProductCardModel.ShopBadge> {
        return similarProductItem.badgeList.map {
            ProductCardModel.ShopBadge(
                isShown = true,
                imageUrl = it.imageUrl,
            )
        }
    }

    private fun createProductCardLabelGroupList(
        similarProductItem: Product
    ): List<ProductCardModel.LabelGroup> {
        return similarProductItem.labelGroups.map {
            ProductCardModel.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                imageUrl = it.url,
            )
        }
    }

    private fun createProductCardLabelGroupVariantList(
        similarProductItem: Product
    ): List<ProductCardModel.LabelGroupVariant> {
        return similarProductItem.labelGroupVariantList.map {
            ProductCardModel.LabelGroupVariant(
                typeVariant = it.typeVariant,
                title = it.title,
                type = it.type,
                hexColor = it.hexColor,
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.similar_search_product_card_layout
    }
}
