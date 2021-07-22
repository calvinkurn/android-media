package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.chips.InitialStateChipWidgetDataView
import com.tokopedia.autocomplete.initialstate.chips.InitialStateChipWidgetTitleDataView
import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductListDataView
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

internal fun `Then verify PopularSearchDataView`(actualData: List<Visitable<*>>, expectedData: List<InitialStateData>, expectedDimension90: String = "") {
    val actualPopularSearchDataViewPosition = actualData.indexOfFirst { it is PopularSearchTitleDataView } + 1
    val expectedPopularSearchDataPosition = expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_POPULAR_SEARCH }

    actualData[actualPopularSearchDataViewPosition].shouldBeInstanceOf<PopularSearchDataView>()

    val popularSearchDataView = actualData[actualPopularSearchDataViewPosition] as PopularSearchDataView
    popularSearchDataView.assertPopularSearchDataView(expectedData[expectedPopularSearchDataPosition], expectedDimension90)
}

private fun PopularSearchDataView.assertPopularSearchDataView(expectedData: InitialStateData, expectedDimension90: String) {
    expectedData.items.forEachIndexed { index, expectedPopularSearch ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(expectedPopularSearch, expectedData.featureId, expectedData.header, position, expectedDimension90)
    }
}

internal fun `Then verify DynamicInitialStateSearchDataView`(actualData: List<Visitable<*>>, expectedData: List<InitialStateData>, expectedDimension90: String = "") {
    val actualDynamicInitialStateSearchDataViewPosition = actualData.indexOfFirst { it is DynamicInitialStateTitleDataView } + 1
    val expectedDynamicInitialStateSearchDataPosition = expectedData.indexOfFirst { it.id == "new_section" }

    actualData[actualDynamicInitialStateSearchDataViewPosition].shouldBeInstanceOf<DynamicInitialStateSearchDataView>()

    val dynamicInitialStateSearchDataView = actualData[actualDynamicInitialStateSearchDataViewPosition] as DynamicInitialStateSearchDataView
    dynamicInitialStateSearchDataView.assertDynamicInitialStateSearchDataView(expectedData[expectedDynamicInitialStateSearchDataPosition], expectedDimension90)
}

private fun DynamicInitialStateSearchDataView.assertDynamicInitialStateSearchDataView(expectedData: InitialStateData, expectedDimension90: String) {
    expectedData.items.forEachIndexed { index, expectedDynamicInitialState ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(expectedDynamicInitialState, expectedData.featureId, expectedData.header, position, expectedDimension90)
    }
}

internal fun `Then verify InitialStateProductListDataView`(actualData: List<Visitable<*>>, expectedData: List<InitialStateData>, expectedDimension90: String = "") {
    val actualInitialStateProductListDataViewPosition = actualData.indexOfFirst { it is InitialStateProductLineTitleDataView } + 1
    val expectedInitialStateProductListDataPosition = expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_LIST_PRODUCT_LINE }

    actualData[actualInitialStateProductListDataViewPosition].shouldBeInstanceOf<InitialStateProductListDataView>()

    val initialStateProductListDataView = actualData[actualInitialStateProductListDataViewPosition] as InitialStateProductListDataView
    initialStateProductListDataView.assertInitialStateProductListDataView(expectedData[expectedInitialStateProductListDataPosition], expectedDimension90)
}

private fun InitialStateProductListDataView.assertInitialStateProductListDataView(expectedData: InitialStateData, expectedDimension90: String) {
    expectedData.items.forEachIndexed { index, expectedInitialStateProductList ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(expectedInitialStateProductList, expectedData.featureId, expectedData.header, position, expectedDimension90)
    }
}

internal fun `Then verify CuratedCampaignDataView`(actualData: List<Visitable<*>>, expectedData: List<InitialStateData>) {
    val actualCuratedCampaignDataViewPosition = actualData.indexOfFirst { it is CuratedCampaignDataView }
    val expectedCuratedCampaignDataPosition = expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_CURATED_CAMPAIGN }

    actualData[actualCuratedCampaignDataViewPosition].shouldBeInstanceOf<CuratedCampaignDataView>()

    val curatedCampaignDataView = actualData[actualCuratedCampaignDataViewPosition] as CuratedCampaignDataView
    curatedCampaignDataView.assertCuratedCampaignDataView(expectedData[expectedCuratedCampaignDataPosition])
}

private fun CuratedCampaignDataView.assertCuratedCampaignDataView(expectedData: InitialStateData) {
    val expected = expectedData.items[0]

    template shouldBe expected.template
    imageUrl shouldBe expected.imageUrl
    applink shouldBe expected.applink
    url shouldBe expected.url
    title shouldBe expected.title
    subtitle shouldBe expected.subtitle
    productId shouldBe expected.itemId
    type shouldBe expected.type
    featureId shouldBe expectedData.featureId
}

internal fun `Then verify InitialStateChipWidgetDataView`(actualData: List<Visitable<*>>, expectedData: List<InitialStateData>, expectedDimension90: String = "") {
    val actualInitialStateChipWidgetDataViewPosition = actualData.indexOfFirst { it is InitialStateChipWidgetTitleDataView } + 1
    val expectedInitialStateChipWidgetDataPosition = expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_LIST_CHIPS }

    actualData[actualInitialStateChipWidgetDataViewPosition].shouldBeInstanceOf<InitialStateChipWidgetDataView>()

    val initialStateChipWidgetDataView = actualData[actualInitialStateChipWidgetDataViewPosition] as InitialStateChipWidgetDataView
    initialStateChipWidgetDataView.assertInitialStateChipWidgetDataView(expectedData[expectedInitialStateChipWidgetDataPosition], expectedDimension90)
}

private fun InitialStateChipWidgetDataView.assertInitialStateChipWidgetDataView(expectedData: InitialStateData, expectedDimension90: String) {
    expectedData.items.forEachIndexed { index, expectedInitialStateChipWidget ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(expectedInitialStateChipWidget, expectedData.featureId, expectedData.header, position, expectedDimension90)
    }
}