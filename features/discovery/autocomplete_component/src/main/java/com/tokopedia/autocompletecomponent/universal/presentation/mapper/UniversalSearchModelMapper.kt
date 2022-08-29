package com.tokopedia.autocompletecomponent.universal.presentation.mapper

import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_TEMPLATE_CAROUSEL
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_TEMPLATE_DOUBLE_LINE
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_TEMPLATE_LIST_GRID
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemDataView
import com.tokopedia.autocompletecomponent.universal.presentation.model.UniversalDataView
import com.tokopedia.discovery.common.Mapper

internal class UniversalSearchModelMapper(
    private val dimension90: String,
    private val keyword: String,
): Mapper<UniversalSearchModel, UniversalDataView> {
    override fun convert(model: UniversalSearchModel): UniversalDataView {
        return UniversalDataView(
            carouselDataView = model.getCarouselDataView(),
            listDataView = model.getListDataView(),
            doubleLineDataView = model.getDoubleLineDataView(),
        )
    }

    private fun UniversalSearchModel.getCarouselDataView(): List<CarouselDataView> {
        return universalSearch.universalSearchData.items
            .filter { it.template == UNIVERSAL_SEARCH_TEMPLATE_CAROUSEL }
            .map {
                CarouselDataView(
                    id = it.id,
                    applink = it.applink,
                    title = it.title,
                    subtitle = it.subtitle,
                    componentId = it.componentId,
                    trackingOption = it.trackingOption,
                    product = it.product.toCarouselProductDataView(),
                    dimension90 = dimension90,
                    keyword = keyword
                )
            }
    }

    private fun List<UniversalSearchModel.Product>.toCarouselProductDataView(): List<CarouselDataView.Product> {
        return map {
            CarouselDataView.Product(
                id = it.id,
                applink = it.applink,
                imageUrl = it.imageUrl,
                title = it.title,
                componentId = it.componentId,
                trackingOption = it.trackingOption,
                price = it.price,
                originalPrice = it.originalPrice,
                discountPercentage = it.discountPercentage,
                ratingAverage = it.ratingAverage,
                countSold = it.countSold,
                shop = CarouselDataView.Product.Shop(
                    name = it.shop.name,
                    city = it.shop.city,
                ),
                badge = it.badge.toBadgeDataView(),
                keyword = keyword,
                dimension90 = dimension90,
            )
        }
    }

    private fun UniversalSearchModel.getListDataView(): List<ListGridDataView> {
        return universalSearch.universalSearchData.items
            .filter { it.template == UNIVERSAL_SEARCH_TEMPLATE_LIST_GRID }
            .map {
                ListGridDataView(
                    id = it.id,
                    applink = it.applink,
                    title = it.title,
                    subtitle = it.subtitle,
                    componentId = it.componentId,
                    trackingOption = it.trackingOption,
                    related = it.curated.toRelatedItemDataView(),
                    keyword = keyword,
                    dimension90 = dimension90,
                )
            }
    }

    private fun UniversalSearchModel.getDoubleLineDataView(): List<DoubleLineDataView> {
        return universalSearch.universalSearchData.items
            .filter { it.template == UNIVERSAL_SEARCH_TEMPLATE_DOUBLE_LINE }
            .map {
                DoubleLineDataView(
                    id = it.id,
                    applink = it.applink,
                    title = it.title,
                    subtitle = it.subtitle,
                    componentId = it.componentId,
                    trackingOption = it.trackingOption,
                    related = it.curated.toRelatedItemDataView(),
                    keyword = keyword,
                    dimension90 = dimension90,
                )
            }
    }

    private fun List<UniversalSearchModel.Curated>.toRelatedItemDataView(): List<RelatedItemDataView> {
        return map {
            RelatedItemDataView(
                id = it.id,
                applink = it.applink,
                imageUrl = it.imageUrl,
                title = it.title,
                componentId = it.componentId,
                trackingOption = it.trackingOption,
                campaignCode = it.campaignCode,
                keyword = keyword,
                dimension90 = dimension90,
            )
        }
    }

    private fun List<UniversalSearchModel.Badge>.toBadgeDataView(): List<CarouselDataView.Product.Badge> {
        return map {
            CarouselDataView.Product.Badge(
                title = it.title,
                imageUrl = it.imageUrl,
                show = it.show,
            )
        }
    }
}