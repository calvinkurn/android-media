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
import com.tokopedia.usecase.UseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class LoadDataTest: Spek({

    Feature("Load Data") {
        createTestInstance()

        Scenario("Load Data Success") {
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
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

            Then("verify view interaction when load data success") {
                val productListView by memoized<ProductListSectionContract.View>()

                verifyOrder {
                    productListView.isAnyFilterActive

                    verifyShowLoading(productListView)

                    verifyProcessingData(productListView)

                    verifyHideLoading(productListView)
                }

                confirmVerified(productListView)
            }

            Then("verify get dynamic filter use case is executed") {
                val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

                verify(exactly = 1) { getDynamicFilterUseCase.execute(any(), any()) }
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
        }

        Scenario("Load Data Success Is First Time Load") {
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("Product List is first time load") {
                productListPresenter.setIsFirstTimeLoad(true)
            }

            When("Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                }

                productListPresenter.loadData(searchParameter)
            }

            Then("verify view interaction when load data success") {
                val productListView by memoized<ProductListSectionContract.View>()

                verifyOrder {
                    productListView.isAnyFilterActive

                    verifyShowLoading(productListView)

                    verifyProcessingData(productListView)

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
        }

        Scenario("Load Data Success With Redirection") {
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelRedirection)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("Product List is first time load") {
                productListPresenter.setIsFirstTimeLoad(true)
            }

            When("Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "produk wardyah"
                }

                productListPresenter.loadData(searchParameter)
            }

            Then("verify view interaction") {
                val productListView by memoized<ProductListSectionContract.View>()

                verifyOrder {
                    productListView.isAnyFilterActive

                    verifyShowLoading(productListView)

                    productListView.redirectSearchToAnotherPage(searchProductModelRedirection.searchProduct.redirection.redirectApplink)

                    verifyHideLoading(productListView)
                }

                confirmVerified(productListView)
            }

            Then("verify get dynamic filter use case is not executed") {
                val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

                verify(exactly = 1) { getDynamicFilterUseCase.execute(any(), any()) }
            }
        }
    }
})