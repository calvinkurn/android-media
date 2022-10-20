package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.verifyProductItemDataViewList
import org.junit.Test

class SearchOssTest: BaseSearchPageLoadTest() {

    @Test
    fun `test oos state product card`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        `Then assert first page visitables`(visitableList, searchModel)
    }

    private fun `Then assert first page visitables`(
        visitableList: List<Visitable<*>>,
        searchModel: SearchModel
    ) {
        `Then assert visitable list contents`(visitableList, searchModel)
    }

    private fun `Then assert visitable list contents`(
        visitableList: List<Visitable<*>>,
        searchModel: SearchModel,
    ) {
        val expectedProductList = searchModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(
            expectedProductList = expectedProductList,
            actualProductItemDataViewList = actualProductItemDataViewList,
            startPosition = 1,
            needToVerifyAtc = false
        )
    }

}
