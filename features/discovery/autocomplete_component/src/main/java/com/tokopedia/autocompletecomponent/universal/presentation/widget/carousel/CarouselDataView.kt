package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocompletecomponent.universal.analytics.UniversalTracking.getCarouselUnificationListName
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactory
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder

class CarouselDataView(
    val data: BaseUniversalDataView = BaseUniversalDataView(),
    val product: List<Product> = listOf(),
): Visitable<UniversalSearchTypeFactory>,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = data.trackingOption,
        keyword = data.keyword,
        valueName = data.title,
        componentId = data.componentId,
        applink = data.applink,
        dimension90 = data.dimension90,
    ) {

    override fun type(typeFactory: UniversalSearchTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    class Product(
        val id: String = "",
        val applink: String = "",
        val imageUrl: String = "",
        val title: String = "",
        val componentId: String = "",
        val trackingOption: Int = 0,
        val price: String = "",
        val priceInt: Int = 0,
        val originalPrice: String = "",
        val discountPercentage: String = "",
        val ratingAverage: String = "",
        val countSold: String = "",
        val shop: Shop = Shop(),
        val badge: List<Badge> = listOf(),
        val freeOngkir: FreeOngkir = FreeOngkir(),
        val keyword: String = "",
        val dimension90: String = "",
        val labelGroups: List<LabelGroup> = listOf(),
        val carouselTitle : String = "",
        val carouselType: String = "",
        val position: Int = 0,
    ): ImpressHolder(),
        SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keyword,
        valueName = title,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    ) {
        class Shop(
            val name: String = "",
            val city: String = "",
        )

        class Badge(
            val title: String = "",
            val imageUrl: String = "",
            val show: Boolean = false,
        )

        class FreeOngkir(
            val imgUrl: String = "",
            val isActive: Boolean = false,
        )

        class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = "",
            val imageUrl: String = ""
        ) {
            fun getPositionTitle(): String = "$position.$title"
        }

        private fun List<LabelGroup>?.getFormattedPositionName(): String =
            this?.joinToString(transform = LabelGroup::getPositionTitle) ?: ""

        fun asUnificationObjectDataLayer(): Any {
            return DataLayer.mapOf(
                "name", title,
                "id", id,
                "price", priceInt,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", getCarouselUnificationListName(carouselType, componentId),
                "position", position,
                "dimension115", labelGroups.getFormattedPositionName(),
                "dimension90", dimension90,
            )
        }
    }
}