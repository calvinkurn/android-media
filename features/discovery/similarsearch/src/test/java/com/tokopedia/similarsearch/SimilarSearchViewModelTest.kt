package com.tokopedia.similarsearch

import com.tokopedia.discovery.common.State.Error
import com.tokopedia.discovery.common.State.Success
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
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
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
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
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
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
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
            }

            When("handle view is created") {
                similarSearchViewModel.onViewCreated()
            }

            Then("assert similar search state is success and contains similar search data") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val expectedSimilarProductList = similarProductModel.getProductList().subList(0, SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE)

                similarSearchLiveData.shouldBeInstanceOf<Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithLoadingMore(expectedSimilarProductList)
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
        }

        Scenario("Get Similar Products only returns one page of data") {
            val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will success and return similar product model with product list less than $SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelOnePage)
            }

            When("handle view is created") {
                similarSearchViewModel.onViewCreated()
            }

            Then("assert similar search state is success and contains similar search data") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val expectedSimilarProductList = similarProductModelOnePage.getProductList().subList(0, similarProductModelOnePage.getProductList().size)

                similarSearchLiveData.shouldBeInstanceOf<Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithLoadingMore(expectedSimilarProductList)
            }
        }
    }
})