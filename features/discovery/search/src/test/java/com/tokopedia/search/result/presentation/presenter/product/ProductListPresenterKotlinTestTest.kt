package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import io.kotlintest.TestCase
import io.kotlintest.specs.BehaviorSpec
import org.mockito.Mockito
import rx.Subscriber

class ProductListPresenterKotlinTestTest: BehaviorSpec() {

    private abstract class MockSearchProductUseCase : UseCase<SearchProductModel>()
    private abstract class MockSearchProductSubscriber : Subscriber<SearchProductModel>()

    private val searchProductFirstPageUseCase: UseCase<SearchProductModel> = Mockito.mock(MockSearchProductUseCase::class.java)
    private val searchProductLoadMoreUseCase: UseCase<SearchProductModel> = Mockito.mock(MockSearchProductUseCase::class.java)
    private lateinit var productListPresenter: ProductListPresenter

    override fun beforeTest(testCase: TestCase) {
        productListPresenter = ProductListPresenter()

        productListPresenter.attachView(Mockito.mock(ProductListSectionContract.View::class.java))
        productListPresenter.searchProductFirstPageUseCase = searchProductFirstPageUseCase
        productListPresenter.searchProductLoadMoreUseCase = searchProductLoadMoreUseCase
    }

    init {
        Given("product list presenter") {
            When("load more data with null parameters") {
                productListPresenter.loadMoreData(null, null)

                Then("should not execute use case") {
                    Mockito.verify(searchProductLoadMoreUseCase, Mockito.never()).execute(Mockito.any(RequestParams::class.java), Mockito.any(MockSearchProductSubscriber::class.java))
                }
            }
        }
    }
}