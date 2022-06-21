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
import com.tokopedia.topads.sdk.domain.model.CpmData
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert

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

internal fun BaseSuggestionDataView.assertShopAdsSuggestionData(
    item: SuggestionItem,
    cpmData: CpmData,
    expectedPosition: Int,
    dimension90: String,
    expectedKeyword: String,
) {
    this.template shouldBe item.template
    this.type shouldBe TYPE_SHOP
    this.applink shouldBe cpmData.applinks
    this.title shouldBe cpmData.cpm.name
    this.subtitle shouldBe cpmData.cpm.cpmShop.location
    this.iconTitle shouldBe cpmData.cpm.badges.first().imageUrl
    this.iconSubtitle shouldBe item.iconSubtitle
    this.imageUrl shouldBe cpmData.cpm.cpmImage.fullEcs
    this.dimension90 shouldBe dimension90
    this.componentId shouldBe item.componentId
    this.trackingOption shouldBe item.trackingOption
    this.position shouldBe expectedPosition
    this.searchTerm shouldBe expectedKeyword

    MatcherAssert.assertThat(this.shopAdsDataView, CoreMatchers.notNullValue())

    this.shopAdsDataView!!.clickUrl shouldBe cpmData.adClickUrl
    this.shopAdsDataView!!.impressionUrl shouldBe cpmData.cpm.cpmImage.fullUrl
    this.shopAdsDataView!!.imageUrl shouldBe cpmData.cpm.cpmImage.fullEcs
}

internal fun BaseSuggestionDataView.assertBoldAllText(expectedValue: Boolean) {
    isBoldAllText() shouldBe expectedValue
}

internal fun BaseSuggestionDataView.assertPartialBoldText(expectedValue: Boolean) {
    isBoldPartialText() shouldBe expectedValue
}

internal fun BaseSuggestionDataView.assertCircleImage(expectedValue: Boolean) {
    isCircleImage() shouldBe expectedValue
}

internal fun Visitable<*>.shouldBeSuggestionDoubleLineDataView(isPartialBold: Boolean = false, isBoldAllText: Boolean = false, isCircle: Boolean = false) {
    shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
    (this as SuggestionDoubleLineDataDataView).data.assertPartialBoldText(isPartialBold)
    (this as SuggestionDoubleLineDataDataView).data.assertBoldAllText(isBoldAllText)
    (this as SuggestionDoubleLineDataDataView).data.assertCircleImage(isCircle)
}

internal fun Visitable<*>.shouldBeSuggestionSingleLineDataDataView(isPartialBold: Boolean = false, isBoldAllText: Boolean = false, isCircle: Boolean = false) {
    shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
    (this as SuggestionSingleLineDataDataView).data.assertPartialBoldText(isPartialBold)
    (this as SuggestionSingleLineDataDataView).data.assertBoldAllText(isBoldAllText)
    (this as SuggestionSingleLineDataDataView).data.assertCircleImage(isCircle)
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