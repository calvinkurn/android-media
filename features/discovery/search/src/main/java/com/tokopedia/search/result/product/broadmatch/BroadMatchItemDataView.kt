package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelatedProduct
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.StockBarDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option.Product
import com.tokopedia.search.result.product.wishlist.Wishlistable
import com.tokopedia.search.utils.getFormattedPositionName
import com.tokopedia.search.utils.orNone

data class BroadMatchItemDataView(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val url: String = "",
    val applink: String = "",
    val priceString: String = "",
    val shopLocation: String = "",
    val shopName: String = "",
    val badgeItemDataViewList: List<BadgeItemDataView> = listOf(),
    val freeOngkirDataView: FreeOngkirDataView = FreeOngkirDataView(),
    override var isWishlisted: Boolean = false,
    val position: Int = 0,
    val alternativeKeyword: String = "",
    val isOrganicAds: Boolean = false,
    val topAdsViewUrl: String = "",
    val topAdsClickUrl: String = "",
    val topAdsWishlistUrl: String = "",
    val ratingAverage: String = "",
    val labelGroupDataList: List<LabelGroupDataView> = listOf(),
    val carouselProductType: CarouselProductType,
    val dimension90: String = "",
    val componentId: String = "",
    val originalPrice: String = "",
    val discountPercentage: Int = 0,
    val externalReference: String = "",
    val stockBarDataView: StockBarDataView = StockBarDataView(),
    val warehouseID: String = "",
) : ImpressHolder(), Wishlistable {

    override fun setWishlist(productID: String, isWishlisted: Boolean) {
        if (this.id == productID) {
            this.isWishlisted = isWishlisted
        }
    }

    private fun asObjectDataLayer(): MutableMap<String, Any> {
        return DataLayer.mapOf(
            "name", name,
            "id", id,
            "price", price,
            "brand", "none / other",
            "category", "none / other",
            "variant", "none / other",
            "list", carouselProductType.getDataLayerList(isOrganicAds, componentId),
            "position", position,
            "dimension90", dimension90,
            "dimension115", labelGroupDataList.getFormattedPositionName(),
            "dimension131", externalReference.orNone(),
            "dimension56", warehouseID.ifNullOrBlank { "0" },
        )
    }

    fun asImpressionObjectDataLayer(): Any {
        return asObjectDataLayer()
    }

    fun asClickObjectDataLayer(): Any {
        return asObjectDataLayer().also {
            it["attribution"] = "none / other"
        }
    }

    companion object {

        fun create(
            otherRelatedProduct: OtherRelatedProduct,
            position: Int,
            alternativeKeyword: String,
            dimension90: String,
            externalReference: String,
        ) = BroadMatchItemDataView(
            id = otherRelatedProduct.id,
            name = otherRelatedProduct.name,
            price = otherRelatedProduct.price,
            imageUrl = otherRelatedProduct.imageUrl,
            url = otherRelatedProduct.url,
            applink = otherRelatedProduct.applink,
            priceString = otherRelatedProduct.priceString,
            shopLocation = otherRelatedProduct.shop.city,
            badgeItemDataViewList = otherRelatedProduct.badgeList.map { BadgeItemDataView.create(it) },
            freeOngkirDataView = FreeOngkirDataView.create(otherRelatedProduct.freeOngkir),
            isWishlisted = otherRelatedProduct.isWishlisted,
            position = position,
            alternativeKeyword = alternativeKeyword,
            isOrganicAds = otherRelatedProduct.isOrganicAds(),
            topAdsViewUrl = otherRelatedProduct.ads.productViewUrl,
            topAdsClickUrl = otherRelatedProduct.ads.productClickUrl,
            topAdsWishlistUrl = otherRelatedProduct.ads.productWishlistUrl,
            ratingAverage = otherRelatedProduct.ratingAverage,
            labelGroupDataList = otherRelatedProduct.labelGroupList.map { LabelGroupDataView.create(it) },
            carouselProductType = BroadMatchProduct(),
            dimension90 = dimension90,
            componentId = otherRelatedProduct.componentId,
            externalReference = externalReference,
        )

        fun create(
            product: Product,
            type: String,
            option: Option,
            index: Int,
            externalReference: String,
        ) = BroadMatchItemDataView(
            id = product.id,
            name = product.name,
            price = product.price,
            imageUrl = product.imgUrl,
            url = product.url,
            applink = product.applink,
            priceString = product.priceStr,
            ratingAverage = product.ratingAverage,
            labelGroupDataList = product.labelGroupDataList,
            badgeItemDataViewList = product.badgeItemDataViewList,
            shopLocation = product.shopLocation,
            shopName = product.shopName,
            position = index + 1,
            alternativeKeyword = option.title,
            carouselProductType = CarouselProductType.of(type, option, product),
            freeOngkirDataView = product.freeOngkirDataView,
            isOrganicAds = product.isOrganicAds,
            topAdsViewUrl = product.topAdsViewUrl,
            topAdsClickUrl = product.topAdsClickUrl,
            topAdsWishlistUrl = product.topAdsWishlistUrl,
            componentId = product.componentId,
            originalPrice = product.originalPrice,
            discountPercentage = product.discountPercentage,
            externalReference = externalReference,
            stockBarDataView = product.stockBarDataView,
            warehouseID = product.warehouseID,
        )
    }
}
