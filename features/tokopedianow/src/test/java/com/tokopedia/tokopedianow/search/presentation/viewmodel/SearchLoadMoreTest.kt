package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.verifyProductItemDataViewList
import io.mockk.every
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class SearchLoadMoreTest: BaseSearchPageLoadTest() {

    @Test
    fun `test load more page as last page`() {
        val searchModelPage1 = "search/first-page-16-products.json".jsonToObject<SearchModel>()
        val searchModelPage2 = "search/load-more-products.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModelPage1)
        `Given get search load more page use case will be successful`(searchModelPage2)
        `Given view already created`()

        `When view load more`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(2))
        `Then assert load more page data`(searchModelPage1, searchModelPage2, visitableList)
        `Then assert visitable list does not end with loading more model`(visitableList)
        `Then assert has next page value`(false)
    }

    @Test
    fun `test load more page as last page but getting failed`() {
        val searchModelPage1 = "search/first-page-16-products.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModelPage1)
        `Given get search load more page use case will be failed`()
        `Given view already created`()

        `When view load more`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert load more page data`(searchModelPage1, null, visitableList)
    }

    private fun `Given get search load more page use case will be successful`(searchModel: SearchModel) {
        every {
            getSearchLoadMorePageUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
            firstArg<(SearchModel) -> Unit>().invoke(searchModel)
        }
    }

    private fun `Given get search load more page use case will be failed`() {
        every {
            getSearchLoadMorePageUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
           secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
    }

    private fun `When view load more`() {
        tokoNowSearchViewModel.onLoadMore()
    }

    private fun `Then assert load more page data`(
            searchModelPage1: SearchModel,
            searchModelPage2: SearchModel?,
            visitableList: List<Visitable<*>>
    ) {
        val firstProductIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val page1ProductSize = searchModelPage1.searchProduct.data.productList.size
        val nextPageProductIndex = firstProductIndex + page1ProductSize
        val page2ProductList = searchModelPage2?.searchProduct?.data?.productList.orEmpty()
        val nextPageVisitableList = visitableList.subList(
                nextPageProductIndex, nextPageProductIndex + page2ProductList.size
        )

        verifyProductItemDataViewList(
                page2ProductList,
                nextPageVisitableList.filterIsInstance<ProductItemDataView>(),
                1
        )
    }

    @Test
    fun `test load more page has next page`() {
        val searchModelPage1 = "search/first-page-24-products.json".jsonToObject<SearchModel>()
        val searchModelPage2 = "search/load-more-products.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModelPage1)
        `Given get search load more page use case will be successful`(searchModelPage2)
        `Given view already created`()

        `When view load more`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(2))
        `Then assert load more page data`(searchModelPage1, searchModelPage2, visitableList)
        `Then assert visitable list end with loading more model`(visitableList)
        `Then assert has next page value`(true)
        `Then assert load more success trigger`(Unit)
    }

    @Test
    fun `test do not load more page when already reached last page`() {
        val searchModelPage1 = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModelPage1)
        `Given view already created`()

        `When view load more`()

        `Then verify get search load more use case is not called`()
        `Then assert load more success trigger`(null)
    }

    private fun `Then verify get search load more use case is not called`() {
        verify(exactly = 0) {
            getSearchLoadMorePageUseCase.execute(any(), any(), any())
        }
    }

    private fun `Then assert load more success trigger`(unit: Unit?) {
        Assert.assertEquals(tokoNowSearchViewModel.loadMoreSuccessTriggerLiveData.value, unit)
    }
}
