package com.tokopedia.similarsearch

import com.tokopedia.discovery.common.State
import com.tokopedia.similarsearch.testinstance.getSimilarProductModelThreePage
import com.tokopedia.similarsearch.testinstance.getSimilarProductModelTwoPage
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewLoadMoreTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Load More") {
        createTestInstance()

        Scenario("Handle View Load More when similar product list has 2 page of data") {
            val similarProductModelTwoPage = getSimilarProductModelTwoPage()
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
                val expectedSimilarProductList = similarProductModelTwoPage.getSimilarProductList().subList(0, similarProductModelTwoPage.getSimilarProductList().size)

                similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithoutLoadingMore(expectedSimilarProductList)
            }

            Then("assert has next page is false") {
                similarSearchViewModel.getHasNextPage().shouldBe(false,
                        "Has next page should be false")
            }
        }

        Scenario("Handle View Load More when similar product list has 3 page of data") {
            val similarProductModelThreePage = getSimilarProductModelThreePage()
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
                val expectedSimilarProductList = similarProductModelThreePage.getSimilarProductList().subList(0, SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE * 2)

                similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
                similarSearchLiveData.shouldHaveCorrectViewModelListWithLoadingMore(expectedSimilarProductList)
            }

            Then("assert has next page is true") {
                similarSearchViewModel.getHasNextPage().shouldBe(true,
                        "Has next page should be true")
            }
        }
    }
})