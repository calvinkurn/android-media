package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.TestException
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.error
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelCommon
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class SearchProductLoadMoreTest: Spek({

    Feature("Load More Data") {
        createTestInstance()

        Scenario("Load More Data Success") {
            val requestParamsSlot = slot<RequestParams>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
                }
            }

            Given("Search Product Load More API will return SearchProductModel") {
                val searchProductLoadMoreUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("Product List Presenter already Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                    it[SearchApiConst.START] = "0"
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadData(searchParameter)
            }

            When("Product List Presenter Load More Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                    it[SearchApiConst.START] = productListPresenter.startFrom
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadMoreData(searchParameter)
            }

            Then("verify load more use case request params") {
                val requestParams = requestParamsSlot.captured

                requestParams.getInt(SearchApiConst.START, -1) shouldBe 8
            }

            Then("verify view interaction when load more data success") {
                val productListView by memoized<ProductListSectionContract.View>()

                verifyOrder {
                    productListView.isAnyFilterActive

                    verifyShowLoading(productListView)

                    verifyProcessingData(productListView)

                    productListView.showBottomNavigation()
                    productListView.updateScrollListener()

                    verifyHideLoading(productListView)

                    verifyProcessingNextPage(productListView)
                }

                confirmVerified(productListView)
            }

            Then("verify start from is incremented twice") {
                val startFrom = productListPresenter.startFrom

                startFrom shouldBe (SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt() * 2)
            }
        }

        Scenario("Load More Data Failed with exception") {
            val testException = TestException()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
                }
            }

            Given("Search Product Load More API will throw exception") {
                val searchProductLoadMoreUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().error(testException)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("Product List Presenter already Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                    it[SearchApiConst.START] = "0"
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadData(searchParameter)
            }

            When("Product List Presenter Load More Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                    it[SearchApiConst.START] = productListPresenter.startFrom
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadMoreData(searchParameter)
            }

            Then("verify view interaction for load data failed with exception") {
                val productListView by memoized<ProductListSectionContract.View>()

                verifyOrder {
                    productListView.isAnyFilterActive

                    verifyShowLoading(productListView)

                    verifyProcessingData(productListView)

                    productListView.showBottomNavigation()
                    productListView.updateScrollListener()

                    verifyHideLoading(productListView)

                    verifyShowLoadMoreError(productListView, 8)
                }

                confirmVerified(productListView)
            }
        }
    }
})