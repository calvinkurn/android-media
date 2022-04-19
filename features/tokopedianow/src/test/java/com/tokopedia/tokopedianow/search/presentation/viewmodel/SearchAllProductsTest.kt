package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.AllProductTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchAllProductsTest: SearchTestFixtures() {

    @Test
    fun `search all products should has all products title`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        `Given search view model`(mapOf())
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        `Then assert title for all products`()
    }

    private fun `Then assert title for all products`() {
        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        val titleDataView = visitableList.find { it is TitleDataView } as? TitleDataView
                ?: throw AssertionError("Cannot find title data view")

        assertThat(titleDataView.titleType, instanceOf(AllProductTitle::class.java))
        assertThat(titleDataView.hasSeeAllCategoryButton, shouldBe(true))
    }
}