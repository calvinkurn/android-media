package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetDataView
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateItem
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.mps.MpsDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductListDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.SearchBarEducationDataView
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.autocompletecomponent.shouldBeInstanceOf

internal fun `Then verify RecentViewDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String = "",
    expectedKeyword: String,
) {
    val actualRecentViewDataViewPosition =
        actualData.indexOfFirst { it is RecentViewTitleDataView } + 1
    val expectedRecentViewDataPosition =
        expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_RECENT_VIEW }

    actualData[actualRecentViewDataViewPosition].shouldBeInstanceOf<RecentViewDataView>()

    val recentViewDataView = actualData[actualRecentViewDataViewPosition] as RecentViewDataView
    recentViewDataView.assertRecentViewDataView(
        expectedData[expectedRecentViewDataPosition],
        expectedDimension90,
        expectedKeyword,
    )
}

private fun RecentViewDataView.assertRecentViewDataView(
    expectedData: InitialStateData,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    expectedData.items.forEachIndexed { index, expectedRecentView ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(
            expectedRecentView,
            "",
            "",
            position,
            expectedDimension90,
            expectedData.trackingOption,
            expectedKeyword,
        )
    }
}

private fun BaseItemInitialStateSearch.assertBaseItemInitialStateSearch(
    expected: InitialStateItem,
    expectedFeatureId: String,
    expectedHeader: String,
    expectedPosition: Int,
    expectedDimension90: String,
    expectedTrackingOption: Int,
    expectedKeyword: String,
) {
    itemId shouldBe expected.itemId
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
    featureId shouldBe expectedFeatureId
    header shouldBe expectedHeader
    discountPercentage shouldBe expected.discountPercentage
    originalPrice shouldBe expected.originalPrice
    position shouldBe expectedPosition
    dimension90 shouldBe expectedDimension90
    campaignCode shouldBe expected.campaignCode
    componentId shouldBe expected.componentId
    trackingOption shouldBe expectedTrackingOption
    keyword shouldBe expectedKeyword
}

internal fun `Then verify RecentSearchDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String = "",
    expectedKeyword: String,
) {
    val actualRecentSearchDataViewPosition =
        actualData.indexOfFirst { it is RecentSearchDataView }
    val expectedRecentSearchDataPosition =
        expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_RECENT_SEARCH }

    actualData[actualRecentSearchDataViewPosition].shouldBeInstanceOf<RecentSearchDataView>()

    val recentSearchDataView =
        actualData[actualRecentSearchDataViewPosition] as RecentSearchDataView
    recentSearchDataView.assertRecentSearchDataView(
        expectedData[expectedRecentSearchDataPosition],
        expectedDimension90,
        expectedKeyword,
    )
}

private fun RecentSearchDataView.assertRecentSearchDataView(
    expectedData: InitialStateData,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    list.forEachIndexed { index, actualRecentSearch ->
        val position = index + 1
        actualRecentSearch.assertBaseItemInitialStateSearch(
            expectedData.items[index],
            "",
            "",
            position,
            expectedDimension90,
            expectedData.trackingOption,
            expectedKeyword,
        )
    }
}

internal fun `Then verify PopularSearchDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String = "",
    expectedKeyword: String,
) {
    val actualPopularSearchDataViewPosition =
        actualData.indexOfFirst { it is PopularSearchTitleDataView } + 1
    val expectedPopularSearchDataPosition =
        expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_POPULAR_SEARCH }

    actualData[actualPopularSearchDataViewPosition].shouldBeInstanceOf<PopularSearchDataView>()

    val popularSearchDataView =
        actualData[actualPopularSearchDataViewPosition] as PopularSearchDataView
    popularSearchDataView.assertPopularSearchDataView(
        expectedData[expectedPopularSearchDataPosition],
        expectedDimension90,
        expectedKeyword,
    )
}

private fun PopularSearchDataView.assertPopularSearchDataView(
    expectedData: InitialStateData,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    expectedData.items.forEachIndexed { index, expectedPopularSearch ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(
            expectedPopularSearch,
            expectedData.featureId,
            expectedData.header,
            position,
            expectedDimension90,
            expectedData.trackingOption,
            expectedKeyword,
        )
    }
}

internal fun `Then verify DynamicInitialStateSearchDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String = "",
    expectedKeyword: String,
) {
    val actualPosition = actualData.indexOfFirst { it is DynamicInitialStateTitleDataView } + 1
    val expectedPosition = expectedData.indexOfFirst { it.id == "new_section" }

    actualData[actualPosition].shouldBeInstanceOf<DynamicInitialStateSearchDataView>()

    val dynamicInitialStateSearchDataView =
        actualData[actualPosition] as DynamicInitialStateSearchDataView
    dynamicInitialStateSearchDataView.assertDynamicInitialStateSearchDataView(
        expectedData[expectedPosition],
        expectedDimension90,
        expectedKeyword,
    )
}

private fun DynamicInitialStateSearchDataView.assertDynamicInitialStateSearchDataView(
    expectedData: InitialStateData,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    expectedData.items.forEachIndexed { index, expectedDynamicInitialState ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(
            expectedDynamicInitialState,
            expectedData.featureId,
            expectedData.header,
            position,
            expectedDimension90,
            expectedData.trackingOption,
            expectedKeyword,
        )
    }
}

internal fun `Then verify InitialStateProductListDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String = "",
    expectedKeyword: String,
) {
    val actualPosition =
        actualData.indexOfFirst { it is InitialStateProductLineTitleDataView } + 1
    val expectedPosition =
        expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_LIST_PRODUCT_LINE }

    actualData[actualPosition]
        .shouldBeInstanceOf<InitialStateProductListDataView>()

    val initialStateProductListDataView =
        actualData[actualPosition] as InitialStateProductListDataView
    initialStateProductListDataView.assertInitialStateProductListDataView(
        expectedData[expectedPosition],
        expectedDimension90,
        expectedKeyword,
    )
}

private fun InitialStateProductListDataView.assertInitialStateProductListDataView(
    expectedData: InitialStateData,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    expectedData.items.forEachIndexed { index, expectedInitialStateProductList ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(
            expectedInitialStateProductList,
            expectedData.featureId,
            expectedData.header,
            position,
            expectedDimension90,
            expectedData.trackingOption,
            expectedKeyword,
        )
    }
}

internal fun `Then verify CuratedCampaignDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    val actualPosition =
        actualData.indexOfFirst { it is CuratedCampaignDataView }
    val expectedPosition =
        expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_CURATED_CAMPAIGN }

    actualData[actualPosition].shouldBeInstanceOf<CuratedCampaignDataView>()

    val curatedCampaignDataView = actualData[actualPosition] as CuratedCampaignDataView
    curatedCampaignDataView.assertCuratedCampaignDataView(
        expectedData[expectedPosition],
        expectedDimension90,
        expectedKeyword,
    )
}

private fun CuratedCampaignDataView.assertCuratedCampaignDataView(
    expectedData: InitialStateData,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    val expectedItem = expectedData.items[0]

    baseItemInitialState.assertBaseItemInitialStateSearch(
        expectedItem,
        expectedData.featureId,
        expectedData.header,
        1,
        expectedDimension90,
        expectedData.trackingOption,
        expectedKeyword,
    )
}

internal fun `Then verify InitialStateChipWidgetDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String = "",
    expectedKeyword: String,
) {
    val actualPosition =
        actualData.indexOfFirst { it is InitialStateChipWidgetTitleDataView } + 1
    val expectedPosition =
        expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_LIST_CHIPS }

    actualData[actualPosition]
        .shouldBeInstanceOf<InitialStateChipWidgetDataView>()

    val initialStateChipWidgetDataView =
        actualData[actualPosition] as InitialStateChipWidgetDataView
    initialStateChipWidgetDataView.assertInitialStateChipWidgetDataView(
        expectedData[expectedPosition],
        expectedDimension90,
        expectedKeyword,
    )
}

private fun InitialStateChipWidgetDataView.assertInitialStateChipWidgetDataView(
    expectedData: InitialStateData,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    expectedData.items.forEachIndexed { index, expectedInitialStateChipWidget ->
        val position = index + 1
        list[index].assertBaseItemInitialStateSearch(
            expectedInitialStateChipWidget,
            expectedData.featureId,
            expectedData.header,
            position,
            expectedDimension90,
            expectedData.trackingOption,
            expectedKeyword,
        )
    }
}

internal fun `Then verify SearchBarEducationDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String = "",
    expectedKeyword: String,
) {
    val actualSearchBarEducationDataViewPosition =
        actualData.indexOfFirst { it is SearchBarEducationDataView }
    val expectedSearchBarEducationDataPosition =
        expectedData.indexOfFirst { it.id == InitialStateData.INITIAL_STATE_SEARCHBAR_EDUCATION }

    actualData[actualSearchBarEducationDataViewPosition].shouldBeInstanceOf<SearchBarEducationDataView>()

    val searchBarEducationDataView =
        actualData[actualSearchBarEducationDataViewPosition] as SearchBarEducationDataView
    val expectedSearchBarEducationData =
        expectedData[expectedSearchBarEducationDataPosition]


    searchBarEducationDataView.header shouldBe expectedSearchBarEducationData.header
    searchBarEducationDataView.labelAction shouldBe expectedSearchBarEducationData.labelAction

    searchBarEducationDataView.item.assertBaseItemInitialStateSearch(
        expectedSearchBarEducationData.items.first(),
        "",
        "",
        1,
        expectedDimension90,
        expectedSearchBarEducationData.trackingOption,
        expectedKeyword,
    )
}

internal fun `Then verify MpsDataView`(
    actualData: List<Visitable<*>>,
    expectedData: List<InitialStateData>,
    expectedDimension90: String = "",
    expectedKeyword: String,
) {
    val actualMpsDataViewPosition = actualData.indexOfFirst { it is MpsDataView }
    val expectedMpsDataPosition = expectedData.indexOfFirst {
        it.id == InitialStateData.INITIAL_STATE_MPS
    }

    actualData[actualMpsDataViewPosition].shouldBeInstanceOf<MpsDataView>()

    val mpsDataView = actualData[actualMpsDataViewPosition] as MpsDataView
    mpsDataView.assertMpsDataView(
        expectedData[expectedMpsDataPosition],
        expectedDimension90,
        expectedKeyword,
    )
}

private fun MpsDataView.assertMpsDataView(
    expectedData: InitialStateData,
    expectedDimension90: String,
    expectedKeyword: String,
) {
    list.forEachIndexed { index, actualRecentSearch ->
        val position = index + 1
        actualRecentSearch.assertBaseItemInitialStateSearch(
            expectedData.items[index],
            "",
            "",
            position,
            expectedDimension90,
            expectedData.trackingOption,
            expectedKeyword,
        )
    }
}
