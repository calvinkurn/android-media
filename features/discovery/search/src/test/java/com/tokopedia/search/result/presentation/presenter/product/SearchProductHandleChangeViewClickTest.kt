package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.ViewType.LIST
import com.tokopedia.discovery.common.constants.SearchConstant.DefaultViewType.VIEW_TYPE_NAME_LIST
import com.tokopedia.discovery.common.constants.SearchConstant.DefaultViewType.VIEW_TYPE_NAME_BIG_GRID
import com.tokopedia.discovery.common.constants.SearchConstant.DefaultViewType.VIEW_TYPE_NAME_SMALL_GRID
import com.tokopedia.discovery.common.constants.SearchConstant.ViewType.SMALL_GRID
import com.tokopedia.discovery.common.constants.SearchConstant.ViewType.BIG_GRID
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import io.mockk.*
import org.junit.Test
import rx.Subscriber

internal class SearchProductHandleChangeViewClickTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Click change view from SMALL GRID to LIST`() {
        `Given View already load data`()

        `When changeViewButton click`(0, SMALL_GRID)

        `Then verify view interaction for click changeViewButton to List`(VIEW_TYPE_NAME_LIST, 0)
    }

    private fun `Given View already load data`() {
        val searchProductModel = searchProductCommonResponseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view already load data`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view already load data`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf())
    }

    private fun `When changeViewButton click`(position: Int, currentLayoutType: SearchConstant.ViewType) {
        productListPresenter.handleChangeView(position, currentLayoutType)
    }

    private fun `Then verify view interaction for click changeViewButton to List`(typeName: String, position: Int) {
        verify {
            productListView.switchSearchNavigationLayoutTypeToListView(position)
            productListView.trackEventSearchResultChangeView(typeName)
        }
    }

    @Test
    fun `Click change view from LIST to BIG GRID`() {
        `Given View already load data`()

        `When changeViewButton click`(0, LIST)

        `Then verify view interaction for click changeViewButton to Big Grid`(VIEW_TYPE_NAME_BIG_GRID, 0)
    }

    private fun `Then verify view interaction for click changeViewButton to Big Grid`(typeName: String, position: Int) {
        verify {
            productListView.switchSearchNavigationLayoutTypeToBigGridView(position)
            productListView.trackEventSearchResultChangeView(typeName)
        }
    }

    @Test
    fun `Click change view from BIG GRID to SMALL GRID`() {
        `Given View already load data`()

        `When changeViewButton click`(0, BIG_GRID)

        `Then verify view interaction for click changeViewButton to Small Grid`(VIEW_TYPE_NAME_SMALL_GRID, 0)
    }

    private fun `Then verify view interaction for click changeViewButton to Small Grid`(typeName: String, position: Int) {
        verify {
            productListView.switchSearchNavigationLayoutTypeToSmallGridView(position)
            productListView.trackEventSearchResultChangeView(typeName)
        }
    }
}