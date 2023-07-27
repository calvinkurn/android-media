package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductListener

abstract class InspirationProductItemViewHolder(
    itemView: View,
    protected val inspirationProductListener: InspirationProductListener,
    private val productListener: ProductListener
): AbstractViewHolder<InspirationProductItemDataView>(itemView) {

    abstract val productCardView: IProductCardView?

    fun InspirationProductItemDataView.toProductCardModel(): ProductCardModel =
        ProductCardModel(
            productName = this.name,
            formattedPrice = this.priceString,
            productImageUrl = this.imageUrl,
            countSoldRating = this.ratingAverage,
            labelGroupList = this.labelGroupDataList.toProductCardLabelGroup(),
            shopLocation = if (this.shopLocation.isNotEmpty()) this.shopLocation else this.shopName,
            shopBadgeList = this.badgeItemDataViewList.toProductCardModelShopBadges(),
            freeOngkir = this.freeOngkirDataView.toProductCardModelFreeOngkir(),
            isTopAds = this.isOrganicAds,
            hasThreeDots = this.seamlessInspirationProductType.hasThreeDots,
            cardInteraction = true,
            discountPercentage = if (this.discountPercentage > 0) "${this.discountPercentage}%"
            else "",
            slashedPrice = if (this.discountPercentage > 0) this.originalPrice else "",
            stockBarPercentage = this.stockBarDataView.percentageValue,
            stockBarLabel = this.stockBarDataView.value,
            stockBarLabelColor = this.stockBarDataView.color,
            isWideContent = false
        )

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun List<LabelGroupDataView>?.toProductCardLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(
                title = it.title,
                position = it.position,
                type = it.type,
                imageUrl = it.imageUrl
            )
        } ?: listOf()
    }

    private fun FreeOngkirDataView.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    fun registerLifecycleObserver(productCardModel: ProductCardModel) {
        val productCardView = productCardView ?: return

        productListener.productCardLifecycleObserver?.register(productCardView, productCardModel)
    }

    override fun onViewRecycled() {
        val productCardView = this.productCardView ?: return

        productCardView.recycle()
        productListener.productCardLifecycleObserver?.unregister(productCardView)
    }
}
