package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.CpmViewModel
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.model.QuickFilterViewModel
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val globalNavWidgetAndShowTopAdsTrue = "searchproduct/globalnavwidget/show-topads-true.json"
private const val globalNavWidgetAndShowTopAdsFalse = "searchproduct/globalnavwidget/show-topads-false.json"
private const val noGlobalNavWidget = "searchproduct/globalnavwidget/no-global-nav-widget.json"

internal class SearchProductGlobalNavWidgetTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Showing both Global Nav Widget and CPM`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is true`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has global nav widget and CPM`()
    }

    private fun `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is true`() {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(globalNavWidgetAndShowTopAdsTrue.jsonToObject())
        }
    }

    private fun `When Load Data`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "pulsa xl"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify view set product list`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has global nav widget and CPM`() {
        val visitableList = visitableListSlot.captured

        visitableList[0].shouldBeInstanceOf<GlobalNavViewModel>()
        visitableList[1].shouldBeInstanceOf<QuickFilterViewModel>()
        visitableList[2].shouldBeInstanceOf<CpmViewModel>()
    }

    @Test
    fun `Showing only Global Nav Widget`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is false`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has global nav widget and no CPM`()
    }

    private fun `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is false`() {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(globalNavWidgetAndShowTopAdsFalse.jsonToObject())
        }
    }

    private fun `Then verify visitable list has global nav widget and no CPM`() {
        val visitableList = visitableListSlot.captured

        visitableList[0].shouldBeInstanceOf<GlobalNavViewModel>()
        visitableList[1].shouldBeInstanceOf<QuickFilterViewModel>()

        for(i in 2 until visitableList.size) {
            visitableList[i].shouldBeInstanceOf<ProductItemViewModel>()
        }
    }

    @Test
    fun `Showing only CPM, without Global Nav Widget`() {
        `Given Search Product API will return SearchProductModel without Global Nav Widget`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list not showing global nav widget and still show CPM`()
    }

    private fun `Given Search Product API will return SearchProductModel without Global Nav Widget`() {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(noGlobalNavWidget.jsonToObject())
        }
    }

    private fun `Then verify visitable list not showing global nav widget and still show CPM`() {
        val visitableList = visitableListSlot.captured

        visitableList[0].shouldBeInstanceOf<QuickFilterViewModel>()
        visitableList[1].shouldBeInstanceOf<CpmViewModel>()

        for (i in 2 until visitableList.size) {
            visitableList[i].shouldBeInstanceOf<ProductItemViewModel>()
        }
    }

    @Test
    fun `Search Result Page is landing page and should not show Global Nav Widget`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is false`()
        `Given Search Result Page is landing page`()

        `When Load Data`()

        `Then verify view set product list`()

        `Then verify visitable list not showing global nav widget and still show CPM`()
    }

    private fun `Given Search Result Page is landing page`() {
        every { productListView.isLandingPage } returns true
    }
}