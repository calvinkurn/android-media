package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.presenter.product.testinstance.*
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelRelatedSearch_3
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelRelatedSearch_6
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelSuggestedSearch
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.UseCase
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class GeneralSearchTrackingSpekTest: Spek({

    Feature("General Search Tracking on first time load") {
        createTestInstance()

        Scenario("General search tracking with result found") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter
            val searchProductModel = searchProductModelCommon
            val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            Given("Previous keyword from view is empty") {
                every { productListView.previousKeyword }.returns("")
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

            Then("verify view send general search tracking") {
                verify {
                    productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
                }
            }

            Then("verify general search tracking model is correct") {
                val generalSearchTrackingModel = generalSearchTrackingModelSlot.captured

                generalSearchTrackingModel shouldBe GeneralSearchTrackingModel(
                        eventLabel = String.format(
                                SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                                searchProductModel.searchProduct.query,
                                searchProductModel.searchProduct.keywordProcess,
                                searchProductModel.searchProduct.responseCode
                        ),
                        isResultFound = true,
                        categoryIdMapping = "65",
                        categoryNameMapping = "Handphone & Tablet",
                        relatedKeyword = "none - none"
                )
            }
        }

        Scenario("General search tracking with result found and multiple categories") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter
            val searchProductModel = searchProductModelMultipleCategories
            val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            Given("Previous keyword from view is empty") {
                every { productListView.previousKeyword }.returns("")
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

            Then("verify view send general search tracking") {
                verify {
                    productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
                }
            }

            Then("verify general search tracking model is correct") {
                val generalSearchTrackingModel = generalSearchTrackingModelSlot.captured

                generalSearchTrackingModel shouldBe GeneralSearchTrackingModel(
                        eventLabel = String.format(
                                SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                                searchProductModel.searchProduct.query,
                                searchProductModel.searchProduct.keywordProcess,
                                searchProductModel.searchProduct.responseCode
                        ),
                        isResultFound = true,
                        categoryIdMapping = "1759,1758,65",
                        categoryNameMapping = "Fashion Pria,Handphone & Tablet,Fashion Wanita",
                        relatedKeyword = "none - none"
                )
            }
        }

        Scenario("General search tracking with no result found") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter
            val searchProductModel = searchProductModelNoResult
            val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            Given("Previous keyword from view is empty") {
                every { productListView.previousKeyword }.returns("")
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

            Then("verify view send general search tracking") {
                verify {
                    productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
                }
            }

            Then("verify general search tracking model is correct") {
                val generalSearchTrackingModel = generalSearchTrackingModelSlot.captured

                generalSearchTrackingModel shouldBe GeneralSearchTrackingModel(
                        eventLabel = String.format(
                                SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                                searchProductModel.searchProduct.query,
                                searchProductModel.searchProduct.keywordProcess,
                                searchProductModel.searchProduct.responseCode
                        ),
                        isResultFound = false,
                        categoryIdMapping = "",
                        categoryNameMapping = "",
                        relatedKeyword = "none - none"
                )
            }
        }

        Scenario("General search tracking with previous keyword") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter
            val searchProductModel = searchProductModelCommon
            val previousKeyword = "xiaomi"
            val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            Given("Previous keyword from view is empty") {
                every { productListView.previousKeyword }.returns(previousKeyword)
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

            Then("verify view send general search tracking") {
                verify {
                    productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
                }
            }

            Then("verify general search tracking model is correct") {
                val generalSearchTrackingModel = generalSearchTrackingModelSlot.captured

                generalSearchTrackingModel shouldBe GeneralSearchTrackingModel(
                        eventLabel = String.format(
                                SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                                searchProductModel.searchProduct.query,
                                searchProductModel.searchProduct.keywordProcess,
                                searchProductModel.searchProduct.responseCode
                        ),
                        isResultFound = true,
                        categoryIdMapping = "65",
                        categoryNameMapping = "Handphone & Tablet",
                        relatedKeyword = "$previousKeyword - none"
                )
            }
        }

        Scenario("General search tracking with previous keyword and has related search with response code 3") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter
            val searchProductModel = searchProductModelRelatedSearch_3
            val previousKeyword = "xiaomi"
            val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            Given("Previous keyword from view is empty") {
                every { productListView.previousKeyword }.returns(previousKeyword)
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

            Then("verify view send general search tracking") {
                verify {
                    productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
                }
            }

            Then("verify general search tracking model is correct") {
                val generalSearchTrackingModel = generalSearchTrackingModelSlot.captured

                generalSearchTrackingModel shouldBe GeneralSearchTrackingModel(
                        eventLabel = String.format(
                                SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                                searchProductModel.searchProduct.query,
                                searchProductModel.searchProduct.keywordProcess,
                                searchProductModel.searchProduct.responseCode
                        ),
                        isResultFound = true,
                        categoryIdMapping = "1759,1758",
                        categoryNameMapping = "Fashion Pria,Fashion Wanita",
                        relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.related.relatedKeyword}"
                )
            }
        }

        Scenario("General search tracking with previous keyword and has related search with response code 6") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter
            val searchProductModel = searchProductModelRelatedSearch_6
            val previousKeyword = "xiaomi"
            val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            Given("Previous keyword from view is empty") {
                every { productListView.previousKeyword }.returns(previousKeyword)
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

            Then("verify view send general search tracking") {
                verify {
                    productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
                }
            }

            Then("verify general search tracking model is correct") {
                val generalSearchTrackingModel = generalSearchTrackingModelSlot.captured

                generalSearchTrackingModel shouldBe GeneralSearchTrackingModel(
                        eventLabel = String.format(
                                SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                                searchProductModel.searchProduct.query,
                                searchProductModel.searchProduct.keywordProcess,
                                searchProductModel.searchProduct.responseCode
                        ),
                        isResultFound = true,
                        categoryIdMapping = "1759,1758",
                        categoryNameMapping = "Fashion Pria,Fashion Wanita",
                        relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.related.relatedKeyword}"
                )
            }
        }

        Scenario("General search tracking with previous keyword and has suggestion with response code 7") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var productListPresenter: ProductListPresenter
            val searchProductModel = searchProductModelSuggestedSearch
            val previousKeyword = "xiaomi"
            val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

            Given("Search Product API will return SearchProductModel") {
                val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
                every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
                }
            }

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            Given("Previous keyword from view is empty") {
                every { productListView.previousKeyword }.returns(previousKeyword)
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

            Then("verify view send general search tracking") {
                verify {
                    productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
                }
            }

            Then("verify general search tracking model is correct") {
                val generalSearchTrackingModel = generalSearchTrackingModelSlot.captured

                generalSearchTrackingModel shouldBe GeneralSearchTrackingModel(
                        eventLabel = String.format(
                                SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                                searchProductModel.searchProduct.query,
                                searchProductModel.searchProduct.keywordProcess,
                                searchProductModel.searchProduct.responseCode
                        ),
                        isResultFound = true,
                        categoryIdMapping = "1759,1758",
                        categoryNameMapping = "Fashion Pria,Fashion Wanita",
                        relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.suggestion.suggestion}"
                )
            }
        }
    }
})