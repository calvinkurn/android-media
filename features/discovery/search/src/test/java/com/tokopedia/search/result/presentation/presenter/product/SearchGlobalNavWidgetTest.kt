package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.CpmViewModel
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.model.QuickFilterViewModel
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelGlobalNavWidgetAndShowTopAdsFalse
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelGlobalNavWidgetAndShowTopAdsTrue
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelWithoutGlobalNavWidget
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.usecase.UseCase
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class SearchGlobalNavWidgetTest: Spek({

    Feature("Search Product Success with Global Nav Widget and CPM") {
        createTestInstance()

        Scenario("Showing both Global Nav Widget and CPM") {
            val visitableListSlot = slot<List<Visitable<*>>>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is true") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelGlobalNavWidgetAndShowTopAdsTrue)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "pulsa xl"
                    it[SearchApiConst.START] = "0"
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadData(searchParameter)
            }

            Then("verify view set product list") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.setProductList(capture(visitableListSlot))
                }
            }

            Then("verify visitable list has global nav widget and CPM") {
                val visitableList = visitableListSlot.captured

                visitableList[0].shouldBeInstanceOf<GlobalNavViewModel>()
                visitableList[1].shouldBeInstanceOf<QuickFilterViewModel>()
                visitableList[2].shouldBeInstanceOf<CpmViewModel>()
            }
        }

        Scenario("Showing only Global Nav Widget") {
            val visitableListSlot = slot<List<Visitable<*>>>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is false") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelGlobalNavWidgetAndShowTopAdsFalse)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "pulsa xl"
                    it[SearchApiConst.START] = "0"
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadData(searchParameter)
            }

            Then("verify view set product list") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.setProductList(capture(visitableListSlot))
                }
            }

            Then("verify visitable list show global nav widget without CPM") {
                val visitableList = visitableListSlot.captured

                visitableList[0].shouldBeInstanceOf<GlobalNavViewModel>()
                visitableList[1].shouldBeInstanceOf<QuickFilterViewModel>()

                for(i in 2 until visitableList.size) {
                    visitableList[i].shouldBeInstanceOf<ProductItemViewModel>()
                }
            }
        }

        Scenario("Showing only CPM, without Global Nav Widget") {
            val visitableListSlot = slot<List<Visitable<*>>>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel without Global Nav Widget") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelWithoutGlobalNavWidget)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Load Data") {
                val searchParameter: Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "pulsa xl"
                    it[SearchApiConst.START] = "0"
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadData(searchParameter)
            }

            Then("verify view set product list") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.setProductList(capture(visitableListSlot))
                }
            }

            Then("verify visitable list not showing global nav widget and still show CPM") {
                val visitableList = visitableListSlot.captured

                visitableList[0].shouldBeInstanceOf<QuickFilterViewModel>()
                visitableList[1].shouldBeInstanceOf<CpmViewModel>()

                for (i in 2 until visitableList.size) {
                    visitableList[i].shouldBeInstanceOf<ProductItemViewModel>()
                }
            }
        }
    }
})