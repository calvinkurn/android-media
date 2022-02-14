package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.autocompletecomponent.shouldBeInstanceOf
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipWidgetDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionChildItem
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionTopShop
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.title.SuggestionTitleDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopWidgetDataView

internal fun SuggestionTitleDataView.assertSuggestionTitleDataView(item: SuggestionItem) {
    title shouldBe item.title
}

internal fun SuggestionTopShopWidgetDataView.assertTopShopWidgetDataView(item: SuggestionItem, listExpectedData: List<SuggestionTopShop>) {
    template shouldBe item.template
    title shouldBe item.title

    listSuggestionTopShopCardData.forEachIndexed { index, suggestionTopShopCardDataView ->
        suggestionTopShopCardDataView.assertSuggestionTopShopCardDataView(listExpectedData[index])
    }
}

private fun SuggestionTopShopCardDataView.assertSuggestionTopShopCardDataView(suggestionTopShop: SuggestionTopShop) {
    type shouldBe suggestionTopShop.type
    id shouldBe suggestionTopShop.id
    applink shouldBe suggestionTopShop.applink
    url shouldBe suggestionTopShop.url
    title shouldBe suggestionTopShop.title
    subtitle shouldBe suggestionTopShop.subtitle
    iconTitle shouldBe suggestionTopShop.iconTitle
    iconSubtitle shouldBe suggestionTopShop.iconSubtitle
    urlTracker shouldBe suggestionTopShop.urlTracker
    imageUrl shouldBe suggestionTopShop.imageUrl

    productData.forEachIndexed { index, suggestionTopShopProductDataView ->
        suggestionTopShopProductDataView.imageUrl shouldBe suggestionTopShop.topShopProducts[index].imageUrl
    }
}

internal fun BaseSuggestionDataView.assertBaseSuggestionDataView(
    template: String,
    item: SuggestionItem,
    dimension90: String = "",
    expectedKeyword: String,
) {
    this.template shouldBe template
    this.type shouldBe item.type
    this.applink shouldBe item.applink
    this.url shouldBe item.url
    this.title shouldBe item.title
    this.subtitle shouldBe item.subtitle
    this.iconTitle shouldBe item.iconTitle
    this.iconSubtitle shouldBe item.iconSubtitle
    this.shortcutImage shouldBe item.shortcutImage
    this.imageUrl shouldBe item.imageUrl
    this.label shouldBe item.label
    this.labelType shouldBe item.labelType
    this.urlTracker shouldBe item.urlTracker
    this.trackingCode shouldBe item.tracking.code
    this.trackingOption shouldBe item.trackingOption
    this.componentId shouldBe item.componentId
    this.discountPercentage shouldBe item.discountPercentage
    this.originalPrice shouldBe item.originalPrice
    this.dimension90 shouldBe dimension90
    this.searchTerm shouldBe expectedKeyword
}

internal fun SuggestionDoubleLineDataDataView.assertBoldText(expectedValue: Boolean) {
    isBoldText() shouldBe expectedValue
}

internal fun Visitable<*>.shouldBeSuggestionDoubleLineDataView(isBold: Boolean) {
    shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
    (this as SuggestionDoubleLineDataDataView).assertBoldText(isBold)
}

internal fun Visitable<*>.shouldBeSuggestionSingleLineDataDataView() {
    shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
}

internal fun Visitable<*>.shouldBeSuggestionTitleDataView() {
    shouldBeInstanceOf<SuggestionTitleDataView>()
}

internal fun Visitable<*>.shouldBeSuggestionChipWidgetDataView() {
    shouldBeInstanceOf<SuggestionChipWidgetDataView>()
}

internal fun SuggestionChipWidgetDataView.assertSuggestionChipWidgetDataView(
    expectedList: List<SuggestionChildItem>
) {
    expectedList.forEachIndexed { index, expectedChildItem ->
        data.childItems[index].template shouldBe expectedChildItem.template
        data.childItems[index].type shouldBe expectedChildItem.type
        data.childItems[index].applink shouldBe expectedChildItem.applink
        data.childItems[index].url shouldBe expectedChildItem.url
        data.childItems[index].title shouldBe expectedChildItem.title
    }
}