package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlin.math.roundToInt

abstract class ProductItemViewHolder(
        itemView: View,
        protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemDataView>(itemView) {

    protected val context = itemView.context!!

    protected fun ProductItemDataView.toProductCardModel(productImage: String): ProductCardModel {
        return ProductCardModel(
                productImageUrl = productImage,
                productName = productName,
                discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
                slashedPrice = if (discountPercentage > 0) originalPrice else "",
                formattedPrice = price,
                priceRange = priceRange ?: "",
                shopBadgeList = badgesList.toProductCardModelShopBadges(),
                shopLocation = shopCity,
                freeOngkir = freeOngkirDataView.toProductCardModelFreeOngkir(),
                isTopAds = isTopAds || isOrganicAds,
                countSoldRating = ratingString,
                hasThreeDots = true,
                labelGroupList = labelGroupList.toProductCardModelLabelGroup(),
                labelGroupVariantList = labelGroupVariantList.toProductCardModelLabelGroupVariant()
        )
    }

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
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

    private fun FreeOngkirDataView.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    private fun List<LabelGroupDataView>?.toProductCardModelLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type, imageUrl = it.imageUrl)
        } ?: listOf()
    }

    private fun List<LabelGroupVariantDataView>?.toProductCardModelLabelGroupVariant(): List<ProductCardModel.LabelGroupVariant> {
        return this?.map {
            ProductCardModel.LabelGroupVariant(type = it.type, typeVariant = it.typeVariant, title = it.title, hexColor = it.hexColor)
        } ?: listOf()
    }

    protected fun createImageProductViewHintListener(productItemData: ProductItemDataView): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                productListener.onProductImpressed(productItemData, adapterPosition)
            }
        }
    }
}