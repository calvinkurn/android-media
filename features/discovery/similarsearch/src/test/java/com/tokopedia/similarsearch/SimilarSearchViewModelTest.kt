package com.tokopedia.similarsearch

import com.tokopedia.discovery.common.State.Error
import com.tokopedia.discovery.common.State.Success
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
import com.tokopedia.similarsearch.testinstance.*
import com.tokopedia.similarsearch.testinstance.similarProductModelCommon
import com.tokopedia.similarsearch.testinstance.similarProductModelEmptyResult
import com.tokopedia.similarsearch.testinstance.similarProductModelOnePage
import com.tokopedia.similarsearch.testinstance.similarProductModelTwoPage
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.gherkin.FeatureBody

internal class SimilarSearchViewModelTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    val similarSearchSelectedProduct = SimilarSearchSelectedProduct(
        id = "553354058", imageUrl = "someurl.jpg", name = "Samsung Galaxy A70", price = "Rp 5.499.999", location = "Jakarta", ratingCount = 4, reviewCount = 800
    )

    @Suppress("UNUSED_VARIABLE")
    fun FeatureBody.createTestInstance() {
        val getSimilarProductsUseCase by memoized {
            mockk<UseCase<SimilarProductModel>>(relaxed = true)
        }
    }

    fun TestBody.createSimilarSearchViewModel(): SimilarSearchViewModel {
        val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

        return SimilarSearchViewModel(
                TestDispatcherProvider(),
                similarSearchSelectedProduct,
                getSimilarProductsUseCase
        )
    }

    Feature("Create Similar Search View Model") {
        createTestInstance()

        Scenario("Create Similar Search View Model") {
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            When("create similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Then("similar search view model should have selected product") {
                similarSearchViewModel.similarSearchSelectedProduct.shouldBe(similarSearchSelectedProduct)
            }
        }
    }

    Feature("Handle View Created") {
        createTestInstance()

        Scenario("View is created") {
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
                getSimilarProductsUseCase.isExecuted()
            }
        }
    }

    Feature("Get Similar Products") {
        createTestInstance()

        Scenario("Get Similar Products Successful") {
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

            Then("assert similar search state is success and contains similar search data") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val expectedSimilarProductList = similarProductModelCommon.getProductList().subList(0, SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE)

                similarSearchLiveData.shouldBeInstanceOf<Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithLoadingMore(expectedSimilarProductList)
            }

            Then("assert has next page is true") {
                similarSearchViewModel.getHasNextPage().shouldBe(true,
                        "Has next page should be true")
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

            Then("assert similar search state is error and does not contain data") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

                similarSearchLiveData.shouldBeInstanceOf<Error<*>>()
                similarSearchLiveData.shouldBeNullOrEmpty()
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
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

            Then("assert similar search state is error and does not contain data") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

                similarSearchLiveData.shouldBeInstanceOf<Error<*>>()
                similarSearchLiveData.shouldBeNullOrEmpty()
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }
        }

        Scenario("Get Similar Products returns empty result") {
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

            Then("assert similar search state is success and contains empty search model") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

                similarSearchLiveData.shouldBeInstanceOf<Success<*>>()
                similarSearchLiveData.shouldHaveCorrectEmptyResultView()
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }
        }

        Scenario("Get Similar Products only returns one page of data") {
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

            Then("assert similar search state is success and contains one page similar search data without loading more model") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val expectedSimilarProductList = similarProductModelOnePage.getProductList().subList(0, similarProductModelOnePage.getProductList().size)

                similarSearchLiveData.shouldBeInstanceOf<Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithoutLoadingMore(expectedSimilarProductList)
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }
        }
    }

    Feature("Handle View Load More") {
        createTestInstance()

        Scenario("Handle View Load More when similar product list has 2 page of data") {
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will success and return similar product model with two page product list") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelTwoPage)
            }

            Given("view model already handle view created") {
                similarSearchViewModel.onViewCreated()
            }

            When("handle view load more") {
                similarSearchViewModel.onViewLoadMore()
            }

            Then("assert similar search state is success and contains two page of product list") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val expectedSimilarProductList = similarProductModelTwoPage.getProductList().subList(0, similarProductModelTwoPage.getProductList().size)

                similarSearchLiveData.shouldBeInstanceOf<Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithoutLoadingMore(expectedSimilarProductList)
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }
        }

        Scenario("Handle View Load More when similar product list has 3 page of data") {
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will success and return similar product model with three page product list") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelThreePage)
            }

            Given("view model already handle view created") {
                similarSearchViewModel.onViewCreated()
            }

            When("handle view load more, only to load page two") {
                similarSearchViewModel.onViewLoadMore()
            }

            Then("assert similar search state is success and contains two page of product list") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val expectedSimilarProductList = similarProductModelThreePage.getProductList().subList(0, SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE * 2)

                similarSearchLiveData.shouldBeInstanceOf<Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithLoadingMore(expectedSimilarProductList)
            }

            Then("assert has next page is true") {
                similarSearchViewModel.getHasNextPage().shouldBe(true,
                        "Has next page should be true")
            }
        }
    }
})