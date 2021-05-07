package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.assertBannerDataView
import com.tokopedia.tokomart.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.assertProductCountDataView
import com.tokopedia.tokomart.searchcategory.assertQuickFilterDataView
import com.tokopedia.tokomart.searchcategory.assertTitleDataView
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.verifyProductItemDataViewList
import io.mockk.coEvery
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test

class SearchFirstPageTest: SearchTestFixtures() {

    @Test
    fun `test first page is last page`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = searchViewModel.visitableListLiveData.value!!

        `Then assert visitable list header`(visitableList, searchModel)
        `Then assert visitable list contents`(visitableList, searchModel)
        `Then assert visitable list does not end with loading more model`(visitableList)
    }

    private fun `Given get search first page use case will be successful`(searchModel: SearchModel) {
        coEvery {
            getSearchFirstPageUseCase.execute(any(), any(), any())
        } coAnswers {
            firstArg<(SearchModel) -> Unit>().invoke(searchModel)
        }
    }

    private fun `When view created`() {
        searchViewModel.onViewCreated()
    }

    private fun `Then assert visitable list header`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel,
    ) {
        visitableList[0].assertChooseAddressDataView()
        visitableList[1].assertBannerDataView()
        visitableList[2].assertTitleDataView(title = "", hasSeeAllCategoryButton = false)
        visitableList[3].assertQuickFilterDataView()
        visitableList[4].assertProductCountDataView(searchModel.searchProduct.header.totalData)
    }

    private fun `Then assert visitable list contents`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel,
    ) {
        val expectedProductList = searchModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(expectedProductList, actualProductItemDataViewList)
    }

    private fun `Then assert visitable list does not end with loading more model`(
            visitableList: List<Visitable<*>>
    ) {
        assertThat(visitableList.last(), not(instanceOf(LoadingMoreModel::class.java)))
    }

    @Test
    fun `test first page has next page`() {
        val searchModel = "search/first-page-16-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = searchViewModel.visitableListLiveData.value!!

        `Then assert visitable list header`(visitableList, searchModel)
        `Then assert visitable list contents`(visitableList, searchModel)
        `Then assert visitable list end with loading more model`(visitableList)
    }

    private fun `Then assert visitable list end with loading more model`(visitableList: List<Visitable<*>>) {
        assertThat(visitableList.last(), instanceOf(LoadingMoreModel::class.java))
    }
}