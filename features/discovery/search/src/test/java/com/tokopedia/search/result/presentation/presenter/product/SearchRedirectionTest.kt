package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelRedirection
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class SearchRedirectionTest: Spek({

    Feature("Search success with redirection") {
        createTestInstance()

        Scenario("Load Data Success With Redirection") {
            val requestParamsSlot = slot<RequestParams>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel with redirection") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelRedirection)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Load Data") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "produk wardyah"
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

            Then("verify start from is incremented") {
                val startFrom = productListPresenter.startFrom

                startFrom shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
            }
        }
    }
})