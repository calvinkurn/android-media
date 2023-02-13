package com.tokopedia.autocompletecomponent.universal.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_TEMPLATE_CAROUSEL
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_TEMPLATE_DOUBLE_LINE
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_TEMPLATE_LIST_GRID
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel.UniversalSearchItem
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemDataView
import com.tokopedia.discovery.common.Mapper

internal class UniversalSearchModelMapper(
    private val dimension90: String,
    private val keyword: String,
): Mapper<UniversalSearchModel, List<Visitable<*>>> {
    override fun convert(model: UniversalSearchModel): List<Visitable<*>> {
        return model.getItems().map {
            when (it.template) {
                UNIVERSAL_SEARCH_TEMPLATE_CAROUSEL -> it.convertToCarouselDataView()
                UNIVERSAL_SEARCH_TEMPLATE_LIST_GRID -> it.convertToListGridDataView()
                UNIVERSAL_SEARCH_TEMPLATE_DOUBLE_LINE -> it.convertToDoubleLineDataView()
                else -> it.convertToCarouselDataView()
            }
        }
    }

    private fun UniversalSearchItem.convertToBaseUniversalDataView(): BaseUniversalDataView {
        return BaseUniversalDataView(
            id = id,
            applink = applink,
            title = title,
            subtitle = subtitle,
            componentId = componentId,
            trackingOption = trackingOption,
            dimension90 = dimension90,
            keyword = keyword,
        )
    }

    private fun UniversalSearchItem.convertToCarouselDataView(): CarouselDataView {
        return CarouselDataView(
            data = this.convertToBaseUniversalDataView(),
            product = product.toCarouselProductDataView(this),
        )
    }

    private fun List<UniversalSearchModel.Product>.toCarouselProductDataView(
        universalSearchItem : UniversalSearchItem
    ): List<CarouselDataView.Product> {
        return mapIndexed { index, product ->
            CarouselDataView.Product(
                id = product.id,
                applink = product.applink,
                imageUrl = product.imageUrl,
                title = product.title,
                componentId = product.componentId,
                trackingOption = product.trackingOption,
                price = product.price,
                priceInt = product.priceInt,
                originalPrice = product.originalPrice,
                discountPercentage = product.discountPercentage,
                ratingAverage = product.ratingAverage,
                countSold = product.countSold,
                shop = CarouselDataView.Product.Shop(
                    name = product.shop.name,
                    city = product.shop.city,
                ),
                badge = product.badge.toBadgeDataView(),
                freeOngkir = product.freeOngkir.toFreeOngkirDataView(),
                keyword = keyword,
                dimension90 = dimension90,
                labelGroups = product.labelGroup.toLabelGroups(),
                carouselTitle = universalSearchItem.title,
                carouselType = universalSearchItem.type,
                position = index + 1,
            )
        }
    }

    private fun UniversalSearchItem.convertToListGridDataView(): ListGridDataView {
        return ListGridDataView(
            data = this.convertToBaseUniversalDataView(),
            related = curated.toRelatedItemDataView(),
        )
    }

    private fun UniversalSearchItem.convertToDoubleLineDataView(): DoubleLineDataView {
        return DoubleLineDataView(
            data = this.convertToBaseUniversalDataView(),
            related = curated.toRelatedItemDataView(),
        )
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

    private fun UniversalSearchModel.FreeOngkir.toFreeOngkirDataView(): CarouselDataView.Product.FreeOngkir {
        return CarouselDataView.Product.FreeOngkir(
            imgUrl = this.imgUrl,
            isActive = this.isActive
        )
    }

    private fun List<UniversalSearchModel.LabelGroup>.toLabelGroups() : List<CarouselDataView.Product.LabelGroup> {
        return map {
            CarouselDataView.Product.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                imageUrl = it.url,
            )
        }
    }
}