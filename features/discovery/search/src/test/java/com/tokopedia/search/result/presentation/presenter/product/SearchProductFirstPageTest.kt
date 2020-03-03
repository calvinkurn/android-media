package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.TestException
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.error
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelCommon
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelRedirection
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class SearchProductFirstPageTest: Spek({

    Feature("Load Data") {
        createTestInstance()

        Scenario("Load Data Success") {
            val requestParamsSlot = slot<RequestParams>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                    it[SearchApiConst.START] = "0"
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadData(searchParameter)
            }

            Then("verify use case request params") {
                val requestParams = requestParamsSlot.captured

                requestParams.getString(SearchApiConst.START, null) shouldBe "0"
            }

            Then("verify view interaction when load data success") {
                val productListView by memoized<ProductListSectionContract.View>()

                verifyOrder {
                    productListView.isAnyFilterActive

                    verifyShowLoading(productListView)

                    verifyProcessingData(productListView)

                    productListView.showBottomNavigation()
                    productListView.updateScrollListener()

                    verifyHideLoading(productListView)
                }

                confirmVerified(productListView)
            }

            Then("verify get dynamic filter use case is executed") {
                val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

                verify(exactly = 1) { getDynamicFilterUseCase.execute(any(), any()) }
            }

            Then("verify start from is incremented") {
                val startFrom = productListPresenter.startFrom

                startFrom shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
            }
        }

        Scenario("Load Data Failed with exception") {
            val testException = TestException()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will throw exception") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().error(testException)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                }

                productListPresenter.loadData(searchParameter)
            }

            Then("verify view interaction for load data failed with exception") {
                val productListView by memoized<ProductListSectionContract.View>()

                verifyOrder {
                    productListView.isAnyFilterActive

                    verifyShowError(productListView)
                }

                confirmVerified(productListView)
            }

            Then("verify get dynamic filter use case not executed") {
                val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

                verify(exactly = 0) { getDynamicFilterUseCase.execute(any(), any()) }
            }

            Then("verify start from is incremented") {
                val startFrom = productListPresenter.startFrom

                startFrom shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
            }
        }

        Scenario("Load Data Success Is First Time Load") {
            val productListView by memoized<ProductListSectionContract.View>()
            val requestParamsSlot = slot<RequestParams>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            Given("View reload data immediately calls load data") {
                every { productListView.reloadData() }.answers {
                    val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                        it[SearchApiConst.Q] = "samsung"
                        it[SearchApiConst.START] = "0"
                        it[SearchApiConst.UNIQUE_ID] = "unique_id"
                        it[SearchApiConst.USER_ID] = productListPresenter.userId
                    }

                    productListPresenter.loadData(searchParameter)
                }
            }

            When("View is created") {
                productListPresenter.onViewCreated()
            }

            Then("verify use case request params") {
                val requestParams = requestParamsSlot.captured

                requestParams.getString(SearchApiConst.START, null) shouldBe "0"
            }

            Then("verify view interaction when load data success") {

                verifyOrder {
                    productListView.isFirstActiveTab
                    productListView.reloadData()

                    productListView.isAnyFilterActive

                    verifyShowLoading(productListView)

                    verifyProcessingData(productListView)

                    productListView.showBottomNavigation()
                    productListView.updateScrollListener()

                    verifySendTrackingOnFirstTimeLoad(productListView, GeneralSearchTrackingModel(
                            searchProductModelCommon.searchProduct.query,
                            searchProductModelCommon.searchProduct.keywordProcess,
                            searchProductModelCommon.searchProduct.responseCode,
                            true,
                            mutableMapOf<String, String>().also {
                                it["65"] = "Handphone & Tablet"
                            }
                    ))

                    verifyHideLoading(productListView)
                }

                confirmVerified(productListView)
            }

            Then("verify get dynamic filter use case is executed") {
                val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

                verify(exactly = 1) { getDynamicFilterUseCase.execute(any(), any()) }
            }

            Then("verify start from is incremented") {
                val startFrom = productListPresenter.startFrom

                startFrom shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
            }
        }
    }
})