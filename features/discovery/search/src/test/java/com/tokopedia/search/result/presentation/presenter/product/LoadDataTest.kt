package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModel
import com.tokopedia.usecase.UseCase
import io.mockk.every
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

class LoadDataTest: Spek({

    Feature("Load Data") {
        createTestInstance()

        Scenario("Load Data Success") {
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().onNext(searchProductModel)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Load Data") {
                productListPresenter.loadData(mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                })
            }

            Then("verify view interaction for load data") {

            }
        }
    }
})