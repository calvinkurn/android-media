package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.usecase.UseCase
import io.kotlintest.TestCase
import io.kotlintest.specs.BehaviorSpec
import io.mockk.mockk
import io.mockk.verify
import org.mockito.Mockito

class ProductListPresenterMockKTest: BehaviorSpec() {

    private abstract class MockSearchProductUseCase : UseCase<SearchProductModel>()

    private val searchProductFirstPageUseCase: UseCase<SearchProductModel> = mockk<MockSearchProductUseCase>(relaxed = true)
    private val searchProductLoadMoreUseCase: UseCase<SearchProductModel> = mockk<MockSearchProductUseCase>(relaxed = true)
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
                    verify(exactly = 0) {
                        searchProductLoadMoreUseCase.execute(any(), any())
                    }
                }
            }
        }
    }
}