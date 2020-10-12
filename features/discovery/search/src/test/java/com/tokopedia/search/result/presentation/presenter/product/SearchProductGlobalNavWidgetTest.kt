package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.CpmViewModel
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.model.SingleGlobalNavViewModel
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val globalNavWidgetAndShowTopAdsTrue = "searchproduct/globalnavwidget/show-topads-true.json"
private const val globalNavWidgetAndShowTopAdsFalse = "searchproduct/globalnavwidget/show-topads-false.json"
private const val noGlobalNavWidget = "searchproduct/globalnavwidget/no-global-nav-widget.json"
private const val singleGlobalNavPill = "searchproduct/globalnavwidget/single-global-nav-pill.json"
private const val singleGlobalNavCard = "searchproduct/globalnavwidget/single-global-nav-card.json"

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
        visitableList[1].shouldBeInstanceOf<CpmViewModel>()
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

        for(i in 1 until visitableList.size) {
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

        visitableList[0].shouldBeInstanceOf<CpmViewModel>()

        for (i in 1 until visitableList.size) {
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

    @Test
    fun `Showing Single Global Nav Pill`() {
        val searchProductModel = singleGlobalNavPill.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Single Global Nav`(searchProductModel)

        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "qurban"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }
        `When Load Data`(searchParameter)

        `Then verify view set product list`()
        `Then verify visitable list has single global nav widget`(searchProductModel)
    }

    private fun `Given Search Product API will return SearchProductModel with Single Global Nav`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify visitable list has single global nav widget`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured

        visitableList[0].shouldBeInstanceOf<SingleGlobalNavViewModel>()
        (visitableList[0] as SingleGlobalNavViewModel).assertViewModel(searchProductModel.globalSearchNavigation.data)

        for(i in 1 until visitableList.size) {
            visitableList[i].shouldBeInstanceOf<ProductItemViewModel>()
        }
    }

    private fun SingleGlobalNavViewModel.assertViewModel(expectedData: SearchProductModel.GlobalNavData) {
        source shouldBe expectedData.source
        keyword shouldBe expectedData.keyword
        title shouldBe expectedData.title
        navTemplate shouldBe expectedData.navTemplate
        background shouldBe expectedData.background

        val expectedItem = expectedData.globalNavItems.first()
        item.categoryName shouldBe expectedItem.categoryName
        item.name shouldBe expectedItem.name
        item.info shouldBe expectedItem.info
        item.imageUrl shouldBe expectedItem.imageUrl
        item.clickItemApplink shouldBe expectedItem.applink
        item.clickItemUrl shouldBe expectedItem.url
        item.subtitle shouldBe expectedItem.subtitle
        item.logoUrl shouldBe expectedItem.logoUrl
    }

    @Test
    fun `Showing Single Global Nav Card`() {
        val searchProductModel = singleGlobalNavCard.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Single Global Nav`(searchProductModel)

        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "ps 3"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }
        `When Load Data`(searchParameter)

        `Then verify view set product list`()
        `Then verify visitable list has single global nav widget`(searchProductModel)
    }
}