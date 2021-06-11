package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocomplete.shouldBe
import com.tokopedia.autocomplete.shouldBeInstanceOf

internal fun `Then verify RecentViewDataView`(actualData: List<Visitable<*>>, expectedData: List<InitialStateData>, expectedDimension90: String = "") {
    val actualRecentViewDataViewPosition = actualData.indexOfFirst { it is RecentViewTitleDataView } + 1
    val expectedRecentViewDataPosition = expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_RECENT_VIEW }

    actualData[actualRecentViewDataViewPosition].shouldBeInstanceOf<RecentViewDataView>()

    val recentViewDataView = actualData[actualRecentViewDataViewPosition] as RecentViewDataView
    recentViewDataView.assertRecentViewDataView(expectedData[expectedRecentViewDataPosition], expectedDimension90)
}

private fun RecentViewDataView.assertRecentViewDataView(expectedData: InitialStateData, expectedDimension90: String) {
    expectedData.items.forEachIndexed { index, expectedRecentView ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(expectedRecentView, expectedData.featureId, expectedData.header, position, expectedDimension90)
    }
}

private fun BaseItemInitialStateSearch.assertBaseItemInitialStateSearch(expected: InitialStateItem, featureId: String, header: String, position: Int, expectedDimension90: String) {
    template shouldBe expected.template
    imageUrl shouldBe expected.imageUrl
    applink shouldBe expected.applink
    url shouldBe expected.url
    title shouldBe expected.title
    subtitle shouldBe expected.subtitle
    iconTitle shouldBe expected.iconTitle
    iconSubtitle shouldBe expected.iconSubtitle
    label shouldBe expected.label
    labelType shouldBe expected.labelType
    shortcutImage shouldBe expected.shortcutImage
    productId shouldBe expected.itemId
    type shouldBe expected.type
    featureId shouldBe featureId
    header shouldBe header
    discountPercentage shouldBe expected.discountPercentage
    originalPrice shouldBe expected.originalPrice
    this.position shouldBe position
    dimension90 shouldBe expectedDimension90
}

internal fun `Then verify RecentSearchDataView`(actualData: List<Visitable<*>>, expectedData: List<InitialStateData>, expectedDimension90: String = "") {
    val actualRecentSearchDataViewPosition = actualData.indexOfFirst { it is RecentSearchTitleDataView } + 1
    val expectedRecentSearchDataPosition = expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_RECENT_SEARCH }

    actualData[actualRecentSearchDataViewPosition].shouldBeInstanceOf<RecentSearchDataView>()

    val recentSearchDataView = actualData[actualRecentSearchDataViewPosition] as RecentSearchDataView
    recentSearchDataView.assertRecentSearchDataView(expectedData[expectedRecentSearchDataPosition], expectedDimension90)
}

private fun RecentSearchDataView.assertRecentSearchDataView(expectedData: InitialStateData, expectedDimension90: String) {
    list.forEachIndexed { index, actualRecentSearch ->
        val position = index + 1
        actualRecentSearch.assertBaseItemInitialStateSearch(expectedData.items[index], expectedData.featureId, expectedData.header, position, expectedDimension90)
    }
}