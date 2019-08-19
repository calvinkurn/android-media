package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import rx.Subscriber

@RunWith(JUnitPlatform::class)
class ProductListPresenterSpekTest: Spek({

    abstract class MockSearchProductUseCase : UseCase<SearchProductModel>()
    abstract class MockSearchProductSubscriber : Subscriber<SearchProductModel>()

    val searchProductFirstPageUseCase: UseCase<SearchProductModel> = mock(MockSearchProductUseCase::class.java)
    val searchProductLoadMoreUseCase: UseCase<SearchProductModel> = mock(MockSearchProductUseCase::class.java)
    lateinit var productListPresenter: ProductListPresenter

    beforeEachTest {
        productListPresenter = ProductListPresenter()

        productListPresenter.attachView(mock(ProductListSectionContract.View::class.java))
        productListPresenter.searchProductFirstPageUseCase = searchProductFirstPageUseCase
        productListPresenter.searchProductLoadMoreUseCase = searchProductLoadMoreUseCase
    }

    given("null parameter when load more data") {
        on("load more data") {
            productListPresenter.loadMoreData(null, null)

            it("should not execute use case") {
                verify(searchProductLoadMoreUseCase, never()).execute(any(RequestParams::class.java), any(MockSearchProductSubscriber::class.java))
            }
        }
    }
})