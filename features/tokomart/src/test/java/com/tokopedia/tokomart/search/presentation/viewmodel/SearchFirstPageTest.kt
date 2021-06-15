package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.assertBannerDataView
import com.tokopedia.tokomart.searchcategory.assertCategoryFilterDataView
import com.tokopedia.tokomart.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.assertProductCountDataView
import com.tokopedia.tokomart.searchcategory.assertQuickFilterDataView
import com.tokopedia.tokomart.searchcategory.assertTitleDataView
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.verifyProductItemDataViewList
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchFirstPageTest: BaseSearchPageLoadTest() {

    @Test
    fun `test first page is last page`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)

        `When view created`()

        val visitableList = searchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list does not end with loading more model`(visitableList)
        `Then assert has next page value`(false)
        `Then assert get first page success interactions`(searchModel)
    }

    private fun `Then assert first page visitables`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel
    ) {
        `Then assert visitable list header`(visitableList, searchModel)
        `Then assert visitable list contents`(visitableList, searchModel)
    }

    private fun `Then assert visitable list header`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel,
    ) {
        visitableList[0].assertChooseAddressDataView()
        visitableList[1].assertBannerDataView()
        visitableList[2].assertTitleDataView(title = "", hasSeeAllCategoryButton = false)
        visitableList[3].assertCategoryFilterDataView(searchModel.categoryFilter)
        visitableList[4].assertQuickFilterDataView(searchModel.quickFilter)
        visitableList[5].assertProductCountDataView(searchModel.searchProduct.header.totalDataText)
    }

    private fun `Then assert visitable list contents`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel,
    ) {
        val expectedProductList = searchModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(expectedProductList, actualProductItemDataViewList)
    }

    private fun `Then assert get first page success interactions`(searchModel: SearchModel) {
        `Then assert auto complete applink from API`(searchModel)
        `Then assert header background is shown`()
        `Then assert content is not loading`()
    }

    private fun `Then assert auto complete applink from API`(searchModel: SearchModel) {
        val expectedApplink = searchModel.searchProduct.data.autocompleteApplink

        assertThat(searchViewModel.autoCompleteApplink, shouldBe(expectedApplink))
    }

    private fun `Then assert header background is shown`() {
        assertThat(searchViewModel.isHeaderBackgroundVisibleLiveData.value, shouldBe(true))
    }

    private fun `Then assert content is not loading`() {
        assertThat(searchViewModel.isContentLoadingLiveData.value, shouldBe(false))
    }

    @Test
    fun `test first page has next page`() {
        val searchModel = "search/first-page-16-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)

        `When view created`()

        val visitableList = searchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list end with loading more model`(visitableList)
        `Then assert has next page value`(true)
        `Then assert get first page success interactions`(searchModel)
    }

    @Test
    fun `test first page should get warehouse id if shop id is empty`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        `Given choose address data`(ChooseAddressConstant.emptyAddress)
        `Given search view model`()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        
    }
}