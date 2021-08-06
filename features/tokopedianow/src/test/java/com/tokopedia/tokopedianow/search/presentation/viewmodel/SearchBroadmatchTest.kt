package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchItemDataView
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchBroadmatchTest: SearchTestFixtures() {

    @Test
    fun `show broadmatch no result`() {
        val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        `Then assert broadmatch for no result`(searchModel)
    }

    private fun `Then assert broadmatch for no result`(searchModel: SearchModel) {
        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        visitableList[1].assertSuggestionDataView(searchModel.getSuggestion())

        val broadMatchList = searchModel.getRelated().otherRelatedList
        broadMatchList.forEachIndexed { index, otherRelated ->
            visitableList[2 + index].assertBroadMatchViewModel(otherRelated)
        }
    }

    private fun Visitable<*>.assertBroadMatchViewModel(
        otherRelated: AceSearchProductModel.OtherRelated
    ) {
        assertThat(this, instanceOf(BroadMatchDataView::class.java))

        this as BroadMatchDataView

        assertThat(keyword, shouldBe(otherRelated.keyword))
        assertThat(applink, shouldBe(otherRelated.applink))
        assertThat(broadMatchItemDataViewList.size, shouldBe(otherRelated.productList.size))

        otherRelated.productList.forEachIndexed { index, otherRelatedProduct ->
            broadMatchItemDataViewList[index].assertBroadMatchItemViewModel(
                otherRelatedProduct, index + 1, otherRelated.keyword
            )
        }
    }

    private fun BroadMatchItemDataView.assertBroadMatchItemViewModel(
        otherRelatedProduct: AceSearchProductModel.OtherRelatedProduct,
        expectedPosition: Int,
        expectedAlternativeKeyword: String
    ) {
        assertThat(id, shouldBe(otherRelatedProduct.id))
        assertThat(name, shouldBe(otherRelatedProduct.name))
        assertThat(price, shouldBe(otherRelatedProduct.price))
        assertThat(imageUrl, shouldBe(otherRelatedProduct.imageUrl))
        assertThat(applink, shouldBe(otherRelatedProduct.applink))
        assertThat(priceString, shouldBe(otherRelatedProduct.priceString))
        assertThat(position, shouldBe(expectedPosition))
        assertThat(alternativeKeyword, shouldBe(expectedAlternativeKeyword))
        assertThat(ratingAverage, shouldBe(otherRelatedProduct.ratingAverage))

        val broadMatchLabelGroupList = otherRelatedProduct.labelGroupList
        assertThat(labelGroupDataList.size, shouldBe(broadMatchLabelGroupList.size))

        labelGroupDataList.forEachIndexed { index, actualLabelGroup ->
            val expectedLabelGroup = broadMatchLabelGroupList[index]
            assertThat(actualLabelGroup.title, shouldBe(expectedLabelGroup.title))
            assertThat(actualLabelGroup.position, shouldBe(expectedLabelGroup.position))
            assertThat(actualLabelGroup.type, shouldBe(expectedLabelGroup.type))
            assertThat(actualLabelGroup.url, shouldBe(expectedLabelGroup.url))
        }
    }
}