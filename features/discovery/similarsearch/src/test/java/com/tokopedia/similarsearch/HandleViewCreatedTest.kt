package com.tokopedia.similarsearch

import com.tokopedia.discovery.common.State
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.testinstance.getSimilarProductModelEmptyResult
import com.tokopedia.similarsearch.testinstance.getSimilarProductModelOnePage
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewCreatedTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Created") {
        createTestInstance()

        Scenario("View is created") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will be successful") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
            }

            When("handle view is created") {
                similarSearchViewModel.onViewCreated()
            }

            Then("verify get similar product API is called") {
                getSimilarProductsUseCase.isExecuted()
            }
        }

        Scenario("View is created multiple times") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will be successful") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
            }

            When("handle view is created multiple times") {
                similarSearchViewModel.onViewCreated()
                similarSearchViewModel.onViewCreated()
                similarSearchViewModel.onViewCreated()
            }

            Then("verify get similar product API is called only once") {
                getSimilarProductsUseCase.isExecuted(1)
            }
        }
    }

    Feature("Get Similar Products") {
        createTestInstance()

        Scenario("Get Similar Products Successful") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will be successful") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
            }

            When("handle view is created") {
                similarSearchViewModel.onViewCreated()
            }

            Then("assert original product state is success and contains original product data") {
                val originalProductData = similarSearchViewModel.getOriginalProductLiveData().value

                originalProductData.shouldBe(
                        similarProductModelCommon.getOriginalProduct(),
                        "Original Product data should be equal to original product data from API"
                )
            }

            Then("assert similar search state is success and contains similar search data") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val expectedSimilarProductList = similarProductModelCommon.getSimilarProductList().subList(0, SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE)

                similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithLoadingMore(expectedSimilarProductList)
            }

            Then("assert has next page is true") {
                similarSearchViewModel.getHasNextPage().shouldBe(true,
                        "Has next page should be true")
            }

            Then("assert tracking impression similar product event for all products") {
                val trackingImpressionSimilarProductEvent = similarSearchViewModel.getTrackingImpressionSimilarProductEventLiveData().value
                val trackingSimilarProductContent = trackingImpressionSimilarProductEvent?.getContentIfNotHandled()

                trackingSimilarProductContent.shouldBeListOfMapOfProductItemAsObjectDataLayer(similarProductModelCommon.getSimilarProductList())
            }
        }

        Scenario("Get Similar Products Failed with exception") {
            val exception = TestException()
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will fail with exception") {
                getSimilarProductsUseCase.stubExecute().throws(exception)
            }

            When("handle view is created") {
                similarSearchViewModel.onViewCreated()
            }

            Then("assert stack trace is printed") {
                exception.isStackTracePrinted.shouldBe(true, "Exception stack trace should be printed")
            }

            Then("assert original product live data value should be null") {
                val originalProductData = similarSearchViewModel.getOriginalProductLiveData().value

                originalProductData.shouldBe(
                        null,
                        "Original product data should be null"
                )
            }

            Then("assert similar search state is error and does not contain data") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

                similarSearchLiveData.shouldBeInstanceOf<State.Error<*>>()
                similarSearchLiveData.shouldBeNullOrEmpty()
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }

            Then("assert tracking empty result event is true") {
                val trackingEmptyResultEvent = similarSearchViewModel.getTrackingEmptyResultEventLiveData().value
                val trackingEmptyResultContent = trackingEmptyResultEvent?.getContentIfNotHandled()

                trackingEmptyResultContent shouldBe true
            }
        }

        Scenario("Get Similar Products Failed because of null SimilarSearchModel") {
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will fail by returning null object") {
                getSimilarProductsUseCase.stubExecute().returns(null)
            }

            When("handle view is created") {
                similarSearchViewModel.onViewCreated()
            }

            Then("assert original product live data value should be null") {
                val originalProductData = similarSearchViewModel.getOriginalProductLiveData().value

                originalProductData.shouldBe(
                        null,
                        "Original product data should be null"
                )
            }

            Then("assert similar search state is error and does not contain data") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

                similarSearchLiveData.shouldBeInstanceOf<State.Error<*>>()
                similarSearchLiveData.shouldBeNullOrEmpty()
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }

            Then("assert tracking empty result event is true") {
                val trackingEmptyResultEvent = similarSearchViewModel.getTrackingEmptyResultEventLiveData().value
                val trackingEmptyResultContent = trackingEmptyResultEvent?.getContentIfNotHandled()

                trackingEmptyResultContent shouldBe true
            }
        }

        Scenario("Get Similar Products returns empty result") {
            val similarProductModelEmptyResult = getSimilarProductModelEmptyResult()
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will success and return empty list of product") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelEmptyResult)
            }

            When("handle view is created") {
                similarSearchViewModel.onViewCreated()
            }

            Then("assert original product state is success and contains original product data") {
                val originalProductData = similarSearchViewModel.getOriginalProductLiveData().value

                originalProductData.shouldBe(
                        similarProductModelEmptyResult.getOriginalProduct(),
                        "Original Product data should be equal to original product data from API"
                )
            }

            Then("assert similar search state is success and contains empty search model") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

                similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
                similarSearchLiveData.shouldHaveCorrectEmptyResultView()
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }

            Then("assert tracking empty result event is true") {
                val trackingEmptyResultEvent = similarSearchViewModel.getTrackingEmptyResultEventLiveData().value
                val trackingEmptyResultContent = trackingEmptyResultEvent?.getContentIfNotHandled()

                trackingEmptyResultContent shouldBe true
            }
        }

        Scenario("Get Similar Products only returns one page of data") {
            val similarProductModelOnePage = getSimilarProductModelOnePage()
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will success and return similar product model with one page of product list") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelOnePage)
            }

            When("handle view is created") {
                similarSearchViewModel.onViewCreated()
            }

            Then("assert original product state is success and contains original product data") {
                val originalProductData = similarSearchViewModel.getOriginalProductLiveData().value

                originalProductData.shouldBe(
                        similarProductModelOnePage.getOriginalProduct(),
                        "Original Product data should be equal to original product data from API"
                )
            }

            Then("assert similar search state is success and contains one page similar search data without loading more model") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val expectedSimilarProductList = similarProductModelOnePage.getSimilarProductList().subList(0, similarProductModelOnePage.getSimilarProductList().size)

                similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithoutLoadingMore(expectedSimilarProductList)
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }

            Then("assert tracking impression similar product event for all products") {
                val trackingImpressionSimilarProductEvent = similarSearchViewModel.getTrackingImpressionSimilarProductEventLiveData().value
                val trackingSimilarProductContent = trackingImpressionSimilarProductEvent?.getContentIfNotHandled()

                trackingSimilarProductContent.shouldBeListOfMapOfProductItemAsObjectDataLayer(similarProductModelOnePage.getSimilarProductList())
            }
        }
    }
})