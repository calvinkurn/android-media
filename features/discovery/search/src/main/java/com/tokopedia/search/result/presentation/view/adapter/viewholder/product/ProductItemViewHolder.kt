package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlin.math.roundToInt

abstract class ProductItemViewHolder(
        itemView: View,
        protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemViewModel>(itemView) {

    protected val context = itemView.context!!

    protected fun ProductItemViewModel.toProductCardModel(productImage: String): ProductCardModel {
        return ProductCardModel(
                productImageUrl = productImage,
                productName = productName,
                discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
                slashedPrice = if (discountPercentage > 0) originalPrice else "",
                formattedPrice = price,
                priceRange = priceRange ?: "",
                shopBadgeList = badgesList.toProductCardModelShopBadges(),
                shopLocation = shopCity,
                ratingCount = rating.toRatingCount(isTopAds),
                reviewCount = countReview,
                freeOngkir = freeOngkirViewModel.toProductCardModelFreeOngkir(),
                isTopAds = isTopAds || isOrganicAds,
                ratingString = ratingString,
                hasThreeDots = true,
                labelGroupList = labelGroupList.toProductCardModelLabelGroup(),
                shopRating = shopRating,
                isShopRatingYellow = isShopRatingYellow
        )
    }

    private fun List<BadgeItemViewModel>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun Int.toRatingCount(isTopAds: Boolean): Int {
        return if (isTopAds)
            (this / 20f).roundToInt()
        else
            this
    }

    private fun FreeOngkirViewModel.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    private fun List<LabelGroupViewModel>?.toProductCardModelLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type)
        } ?: listOf()
    }

    protected fun createImageProductViewHintListener(productItem: ProductItemViewModel): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                productListener.onProductImpressed(productItem)
            }
        }
    }
}