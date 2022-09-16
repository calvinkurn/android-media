package com.tokopedia.autocompletecomponent.universal.presenter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemDataView

internal fun CarouselDataView.assertCarouselDataView(
    item: UniversalSearchModel.UniversalSearchItem,
    expectedKeyword: String,
    expectedDimension90: String,
) {
    this.data.assertBaseUniversalDataView(item, expectedKeyword, expectedDimension90)

    this.product.forEachIndexed { index, product ->
        product.assertProductCarouselDataView(
            item.product[index],
            expectedKeyword,
            expectedDimension90,
        )
    }
}

internal fun DoubleLineDataView.assertDoubleLineDataView(
    item: UniversalSearchModel.UniversalSearchItem,
    expectedKeyword: String,
    expectedDimension90: String,
) {
    this.data.assertBaseUniversalDataView(item, expectedKeyword, expectedDimension90)

    this.related.forEachIndexed { index, relatedItemDataView ->
        relatedItemDataView.assertRelatedItemDataView(
            item.curated[index],
            expectedKeyword,
            expectedDimension90,
        )
    }
}

internal fun ListGridDataView.assertListGridDataView(
    item: UniversalSearchModel.UniversalSearchItem,
    expectedKeyword: String,
    expectedDimension90: String,
) {
    this.data.assertBaseUniversalDataView(item, expectedKeyword, expectedDimension90)

    this.related.forEachIndexed { index, relatedItemDataView ->
        relatedItemDataView.assertRelatedItemDataView(
            item.curated[index],
            expectedKeyword,
            expectedDimension90,
        )
    }
}

internal fun BaseUniversalDataView.assertBaseUniversalDataView(
    item: UniversalSearchModel.UniversalSearchItem,
    expectedKeyword: String,
    expectedDimension90: String,
) {
    this.id shouldBe item.id
    this.applink shouldBe item.applink
    this.title shouldBe item.title
    this.subtitle shouldBe item.subtitle
    this.componentId shouldBe item.componentId
    this.trackingOption shouldBe item.trackingOption
    this.keyword shouldBe expectedKeyword
    this.dimension90 shouldBe expectedDimension90
}

internal fun CarouselDataView.Product.assertProductCarouselDataView(
    item: UniversalSearchModel.Product,
    expectedKeyword: String,
    expectedDimension90: String,
) {
    this.id shouldBe item.id
    this.applink shouldBe item.applink
    this.imageUrl shouldBe item.imageUrl
    this.title shouldBe item.title
    this.componentId shouldBe item.componentId
    this.trackingOption shouldBe item.trackingOption
    this.price shouldBe item.price
    this.originalPrice shouldBe item.price
    this.discountPercentage shouldBe item.discountPercentage
    this.ratingAverage shouldBe item.ratingAverage
    this.countSold shouldBe item.countSold
    this.keyword shouldBe expectedKeyword
    this.dimension90 shouldBe expectedDimension90

    this.shop.assertProductShopCarouselDataView(item.shop)
    this.badge.forEachIndexed { index, badge ->
        badge.assertProductBadgeCarouselDataView(item.badge[index])
    }
    this.freeOngkir.assertProductFreeOngkirDataView(item.freeOngkir)
    this.labelGroups.forEachIndexed { index, labelGroup ->
        labelGroup.assertLabelGroupDataView(item.labelGroup[index])
    }
}

internal fun CarouselDataView.Product.Shop.assertProductShopCarouselDataView(item: UniversalSearchModel.Shop) {
    this.name shouldBe item.name
    this.city shouldBe item.city
}

internal fun CarouselDataView.Product.Badge.assertProductBadgeCarouselDataView(item: UniversalSearchModel.Badge) {
    this.title shouldBe item.title
    this.imageUrl shouldBe item.imageUrl
    this.show shouldBe item.show
}

internal fun CarouselDataView.Product.LabelGroup.assertLabelGroupDataView(item: UniversalSearchModel.LabelGroup) {
    this.position shouldBe item.position
    this.title shouldBe item.title
    this.type shouldBe item.type
    this.imageUrl shouldBe item.url
}


internal fun CarouselDataView.Product.FreeOngkir.assertProductFreeOngkirDataView(item: UniversalSearchModel.FreeOngkir) {
    this.imgUrl shouldBe item.imgUrl
    this.isActive shouldBe item.isActive
}

internal fun RelatedItemDataView.assertRelatedItemDataView(
    item: UniversalSearchModel.Curated,
    expectedKeyword: String,
    expectedDimension90: String,
) {
    this.id shouldBe item.id
    this.applink shouldBe item.applink
    this.imageUrl shouldBe item.imageUrl
    this.title shouldBe item.title
    this.componentId shouldBe item.componentId
    this.trackingOption shouldBe item.trackingOption
    this.campaignCode shouldBe item.campaignCode
    this.keyword shouldBe expectedKeyword
    this.dimension90 shouldBe expectedDimension90
}

internal fun List<Visitable<*>>.assertVisitableListData(
    item: List<UniversalSearchModel.UniversalSearchItem>,
    expectedKeyword: String,
    expectedDimension90: String,
) {
    forEachIndexed { index, visitable ->
        when(visitable) {
            is CarouselDataView -> visitable.assertCarouselDataView(
                item[index],
                expectedKeyword,
                expectedDimension90
            )
            is DoubleLineDataView -> visitable.assertDoubleLineDataView(
                item[index],
                expectedKeyword,
                expectedDimension90
            )
            is ListGridDataView -> visitable.assertListGridDataView(
                item[index],
                expectedKeyword,
                expectedDimension90
            )
        }
    }
}