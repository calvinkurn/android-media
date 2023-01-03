package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_INTEGRITY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.StockBarDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking.getInspirationCarouselUnificationListName
import com.tokopedia.search.utils.getFormattedPositionName
import com.tokopedia.search.utils.orNone

class InspirationCarouselDataView(
    val title: String = "",
    val type: String = "",
    val position: Int = 0,
    val layout: String = "",
    val trackingOption: Int = 0,
    val options: List<Option> = listOf(),
) : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    @Suppress("LongParameterList")
    class Option(
        val title: String = "",
        val subtitle: String = "",
        val iconSubtitle: String = "",
        val url: String = "",
        val applink: String = "",
        val bannerImageUrl: String = "",
        val bannerLinkUrl: String = "",
        val bannerApplinkUrl: String = "",
        val identifier: String = "",
        var product: List<Product> = listOf(),
        val inspirationCarouselType: String = "",
        val layout: String = "",
        val position: Int = 0,
        val carouselTitle: String = "",
        val optionPosition: Int = 0,
        var isChipsActive: Boolean = false,
        val hexColor: String = "",
        val chipImageUrl: String = "",
        val componentId: String = "",
        val trackingOption: Int = 0,
        val dimension90: String = "",
        val cardButton: CardButton = CardButton(),
        val bundle: Bundle = Bundle(),
        val keyword: String = "",
        val externalReference: String = "",
    ): Visitable<InspirationCarouselOptionTypeFactory> {

        override fun type(typeFactory: InspirationCarouselOptionTypeFactory): Int {
            return typeFactory.type(layout)
        }

        fun shouldAddBannerCard(): Boolean {
            return bannerImageUrl.isNotEmpty() || title.isNotEmpty()
        }

        fun getBannerDataLayer(keyword: String): Any {
            return DataLayer.mapOf(
                "creative", carouselTitle,
                "id", "0",
                "name", "/search - $keyword",
                "position", position
            )
        }

        fun hasProducts() = product.isNotEmpty()

        fun isShowChipsIcon() = hexColor.isNotEmpty() || chipImageUrl.isNotEmpty()

        @Suppress("LongParameterList")
        class Product(
            val id: String = "",
            val name: String = "",
            val price: Int = 0,
            val priceStr: String = "",
            val imgUrl: String = "",
            val rating: Int = 0,
            val countReview: Int = 0,
            val url: String = "",
            val applink: String = "",
            val description: List<String> = listOf(),
            val optionPosition: Int = 0,
            val inspirationCarouselType: String = "",
            val ratingAverage: String = "",
            val labelGroupDataList: List<LabelGroupDataView> = listOf(),
            val layout: String = "",
            val originalPrice: String = "",
            val discountPercentage: Int = 0,
            val position: Int = 0,
            val optionTitle: String = "",
            val shopId: String = "",
            val shopLocation: String = "",
            val shopName: String = "",
            val badgeItemDataViewList: List<BadgeItemDataView> = listOf(),
            val freeOngkirDataView: FreeOngkirDataView = FreeOngkirDataView(),
            val isOrganicAds: Boolean = false,
            val topAdsViewUrl: String = "",
            val topAdsClickUrl: String = "",
            val topAdsWishlistUrl: String = "",
            val componentId: String = "",
            val inspirationCarouselTitle: String = "",
            val dimension90: String = "",
            val customVideoURL : String = "",
            val externalReference: String = "",
            val discount : String = "",
            val label: String = "",
            val bundleId: String = "",
            val parentId: String = "",
            val minOrder: String = "",
            val trackingOption: Int = 0,
            val stockBarDataView: StockBarDataView = StockBarDataView(),
        ): ImpressHolder(),
            Visitable<InspirationCarouselOptionTypeFactory> {

            companion object {
                private const val ZERO_PARENT_ID = "0"
            }

            override fun type(typeFactory: InspirationCarouselOptionTypeFactory): Int {
                return typeFactory.type(layout)
            }

            fun willShowSalesAndRating(): Boolean{
                return ratingAverage.isNotEmpty() && getLabelIntegrity() != null
            }

            fun getLabelIntegrity(): LabelGroupDataView? {
                return findLabelGroup(LABEL_INTEGRITY)
            }

            private fun findLabelGroup(position: String): LabelGroupDataView? {
                return labelGroupDataList.find { it.position == position }
            }

            fun willShowRating(): Boolean {
                return ratingAverage.isNotEmpty()
            }

            fun shouldOpenVariantBottomSheet(): Boolean =
                parentId != "" && parentId != ZERO_PARENT_ID

            fun getInspirationCarouselInfoProductAsObjectDataLayer(): Any {
                return DataLayer.mapOf(
                        "id", id,
                        "name", "/search - carousel",
                        "creative", name,
                        "position", optionPosition,
                        "category", "none / other"
                )
            }

            fun asUnificationObjectDataLayer(filterSortParams: String): Any {
                return DataLayer.mapOf(
                    "name", name,
                    "id", id,
                    "price", price,
                    "brand", "none / other",
                    "category", "none / other",
                    "variant", "none / other",
                    "list", getInspirationCarouselUnificationListName(inspirationCarouselType, componentId),
                    "position", optionPosition,
                    "dimension115", labelGroupDataList.getFormattedPositionName(),
                    "dimension61", filterSortParams,
                    "dimension90", dimension90,
                    "dimension131", externalReference.orNone(),
                )
            }

            fun asUnificationAtcObjectDataLayer(
                filterSortParams: String,
                cartId: String,
                quantity: Int,
            ): Any {
                return DataLayer.mapOf(
                    "item_name", name,
                    "item_id", id,
                    "price", price,
                    "item_brand", "none / other",
                    "item_category", "none / other",
                    "list", getInspirationCarouselUnificationListName(inspirationCarouselType, componentId),
                    "position", optionPosition,
                    "dimension115", labelGroupDataList.getFormattedPositionName(),
                    "dimension61", filterSortParams,
                    "dimension90", dimension90,
                    "dimension131", externalReference.orNone(),
                    "dimension45", cartId,
                    "quantity", quantity,
                    "shop_id", shopId,
                    "shop_name", shopName,
                    "shop_type", "none / other",
                    "variant", "none / other",
                )
            }

            fun asSearchComponentTracking(keyword: String): SearchComponentTracking =
                searchComponentTracking(
                    trackingOption = trackingOption,
                    keyword = keyword,
                    valueId = id,
                    valueName = name,
                    componentId = componentId,
                    applink = applink,
                    dimension90 = dimension90
                )
        }
    }

    data class CardButton(val title: String = "", val applink: String = "")

    data class Bundle(
        val shop: Shop = Shop(),
        val countSold: String = "",
        val price: Long = 0,
        val originalPrice: String = "",
        val discount: String = "",
        val discountPercentage: Int = 0,
    ) {
        companion object {
            fun create(option: SearchProductModel.InspirationCarouselOption): Bundle {
                return Bundle(
                    Shop.create(option.bundle.shop),
                    option.bundle.countSold,
                    option.bundle.price,
                    option.bundle.originalPrice,
                    option.bundle.discount,
                    option.bundle.discountPercentage,
                )
            }
        }

        data class Shop(
            val name: String = "",
            val url: String = "",
        ) {
            companion object {
                fun create(shop: SearchProductModel.InspirationCarouselBundle.Shop): Shop {
                    return Shop(shop.name, shop.url)
                }
            }
        }
    }
}


