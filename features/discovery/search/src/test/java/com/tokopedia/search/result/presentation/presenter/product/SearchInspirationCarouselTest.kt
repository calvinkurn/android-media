package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.model.QuickFilterViewModel
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelCommon
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelInspirationCarouselFirstPage
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelInspirationCarouselOnlyPosition9
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.usecase.UseCase
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class SearchInspirationCarouselTest: Spek({

    Feature("Show Inspiration Carousel") {
        defaultTimeout = 1000000

        createTestInstance()

        Scenario("Show inspiration carousel") {
            val visitableListSlot = slot<List<Visitable<*>>>()

            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel with Inspiration Carousel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelInspirationCarouselFirstPage)
                }

                val searchProductLoadMoreUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
                }
            }

            Given("Mechanism to save and get product position from cache") {
                val lastProductPositionSlot = slot<Int>()

                every { productListView.lastProductItemPositionFromCache }.answers {
                    if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
                }

                every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
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

            Then("verify view set product list") {
                verifyOrder {
                    productListView.setProductList(capture(visitableListSlot))
                }
            }

            Then("verify visitable list has correct inspiration carousel and product sequence on first page") {
                val visitableList = visitableListSlot.captured

                // 0 -> quick filter
                // 1 -> product
                // 2 -> product
                // 3 -> product
                // 4 -> product
                // 5 -> inspiration carousel
                // 6 -> product
                // 7 -> product
                // 8 -> product
                // 9 -> product
                // 10 -> inspiration carousel
                visitableList.forEachIndexed { index, visitable ->
                    if (index == 0) {
                        visitable.shouldBeInstanceOf<QuickFilterViewModel>(
                                "visitable list at index $index should be QuickFilterViewModel"
                        )
                    }
                    else if (index == 5 || index == 10) {
                        visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                                "visitable list at index $index should be InspirationCarouselViewModel"
                        )
                    }
                    else {
                        visitable.shouldBeInstanceOf<ProductItemViewModel>(
                                "visitable list at index $index should be ProductItemViewModel"
                        )
                    }
                }
            }

            When("Load More") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                    it[SearchApiConst.START] = "0"
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadMoreData(searchParameter)
            }

            Then("verify view add product list") {
                verifyOrder {
                    productListView.addProductList(capture(visitableListSlot))
                }
            }

            Then("verify visitable list has correct inspiration carousel and product sequence after load more") {
                val visitableList = visitableListSlot.captured

                // 0 -> product
                // 1 -> product
                // 2 -> product
                // 3 -> product
                // 4 -> inspiration carousel
                // 5 -> product
                // 6 -> product
                // 7 -> product
                // 8 -> product
                // 9 -> inspiration carousel
                visitableList.forEachIndexed { index, visitable ->
                    if (index == 4 || index == 9) {
                        visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                                "visitable list at index $index should be InspirationCarouselViewModel"
                        )
                    }
                    else {
                        visitable.shouldBeInstanceOf<ProductItemViewModel>(
                                "visitable list at index $index should be ProductItemViewModel"
                        )
                    }
                }
            }
        }

        Scenario("Show inspiration carousel only at position 9 (edge cases)") {
            val visitableListSlot = slot<List<Visitable<*>>>()

            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter

            Given("Search Product API will return SearchProductModel with Inspiration Carousel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelInspirationCarouselOnlyPosition9)
                }

                val searchProductLoadMoreUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelCommon)
                }
            }

            Given("Mechanism to save and get product position from cache") {
                val lastProductPositionSlot = slot<Int>()

                every { productListView.lastProductItemPositionFromCache }.answers {
                    if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
                }

                every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
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

            Then("verify view set product list") {
                verifyOrder {
                    productListView.setProductList(capture(visitableListSlot))
                }
            }

            Then("verify visitable list has correct inspiration carousel and product sequence on first page") {
                val visitableList = visitableListSlot.captured

                // 0 -> quick filter
                // 1 -> product
                // 2 -> product
                // 3 -> product
                // 4 -> product
                // 5 -> product
                // 6 -> product
                // 7 -> product
                // 8 -> product
                visitableList.forEachIndexed { index, visitable ->
                    if (index == 0) {
                        visitable.shouldBeInstanceOf<QuickFilterViewModel>(
                                "visitable list at index $index should be QuickFilterViewModel"
                        )
                    }
                    else {
                        visitable.shouldBeInstanceOf<ProductItemViewModel>(
                                "visitable list at index $index should be ProductItemViewModel"
                        )
                    }
                }
            }

            When("Load More") {
                val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                    it[SearchApiConst.Q] = "samsung"
                    it[SearchApiConst.START] = "0"
                    it[SearchApiConst.UNIQUE_ID] = "unique_id"
                    it[SearchApiConst.USER_ID] = productListPresenter.userId
                }

                productListPresenter.loadMoreData(searchParameter)
            }

            Then("verify view add product list") {
                verifyOrder {
                    productListView.addProductList(capture(visitableListSlot))
                }
            }

            Then("verify visitable list has correct inspiration carousel and product sequence after load more") {
                val visitableList = visitableListSlot.captured

                // 0 -> product
                // 1 -> inspiration carousel
                // 2 -> product
                // 3 -> product
                // 4 -> product
                // 5 -> product
                // 6 -> product
                // 7 -> product
                // 8 -> product
                visitableList.forEachIndexed { index, visitable ->
                    if (index == 1) {
                        visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                                "visitable list at index $index should be InspirationCarouselViewModel"
                        )
                    }
                    else {
                        visitable.shouldBeInstanceOf<ProductItemViewModel>(
                                "visitable list at index $index should be ProductItemViewModel"
                        )
                    }
                }
            }
        }
    }
})