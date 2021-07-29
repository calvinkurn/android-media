package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.shouldBe
import com.tokopedia.autocomplete.shouldBeInstanceOf
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionTopShop
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleDataView
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetDataView

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

internal fun BaseSuggestionDataView.assertBaseSuggestionDataView(template: String, item: SuggestionItem, dimension90: String = "") {
    template shouldBe template
    type shouldBe item.type
    applink shouldBe item.applink
    url shouldBe item.url
    title shouldBe item.title
    subtitle shouldBe item.subtitle
    iconTitle shouldBe item.iconTitle
    iconSubtitle shouldBe item.iconSubtitle
    shortcutUrl shouldBe item.shortcutUrl
    shortcutImage shouldBe item.shortcutImage
    imageUrl shouldBe item.imageUrl
    label shouldBe item.label
    labelType shouldBe item.labelType
    urlTracker shouldBe item.urlTracker
    trackingCode shouldBe item.tracking.code
    this.dimension90 shouldBe dimension90
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