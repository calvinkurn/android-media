package com.tokopedia.home_recom.viewModel

import com.tokopedia.home_recom.util.createInstance
import com.tokopedia.home_recom.util.createSimilarRecommendationViewModel
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 02/09/19
 */
@ExperimentalCoroutinesApi
class SimilarProductRecommendationTestViewModel: Spek({

    Feature("Load Recommendation"){
        lateinit var viewModel: SimilarProductRecommendationViewModel
        createInstance()
        val getSingleRecommendationUseCase by memoized<GetSingleRecommendationUseCase>()

        Scenario("Get success data from network"){
            val slot = slot<Subscriber<List<RecommendationItem>>>()
            Given("recommendation view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            Given("set return data success"){
                every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
                every { getSingleRecommendationUseCase.execute(any(), capture(slot)) } answers {
                    slot.captured.onNext(listOf(
                            RecommendationItem()
                    ))
                }
            }

            When("view request network"){
                viewModel.getSimilarProductRecommendation(1, "", "")
            }

            Then("Check data not null"){
                Assert.assertTrue(viewModel.recommendationItem.value != null && viewModel.recommendationItem.value!!.isSuccess())
            }
        }

        Scenario("Get error timeout data from network"){
            val slot = slot<Subscriber<List<RecommendationItem>>>()
            Given("recommendation view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            Given("set return data success"){
                every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
                every { getSingleRecommendationUseCase.execute(any(), capture(slot)) } answers {
                    slot.captured.onError(TimeoutException())
                }
            }

            When("view request network"){
                viewModel.getSimilarProductRecommendation(1, "", "")
            }

            Then("Check data not null"){
                Assert.assertTrue(viewModel.recommendationItem.value != null && viewModel.recommendationItem.value!!.isError())
            }
        }
    }

    Feature("Wishlist"){
        lateinit var viewModel: SimilarProductRecommendationViewModel

        createInstance()

        val addWishListUseCase by memoized<AddWishListUseCase>()
        val removeWishListUseCase by memoized<RemoveWishListUseCase>()
        val topAdsWishlishedUseCase by memoized<TopAdsWishlishedUseCase>()
        val recommendation = RecommendationItem(productId = 1234)

        Scenario("Get success add wishlist from network"){
            val slot = slot<WishListActionListener>()
            var status: Boolean? = null
            Given("recommendation view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            Given("set return data success"){
                every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
                    slot.captured.onSuccessAddWishlist(recommendation.productId.toString())
                }
            }

            When("request network"){
                viewModel.addWishlist(recommendation){ success, _ ->
                    status = success
                }
            }

            Then("Check data is success"){
                Assert.assertTrue(status == true)
            }
        }

        Scenario("Get error add wishlist from network"){
            val slot = slot<WishListActionListener>()
            var status: Boolean? = null
            Given("recommendation view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            Given("set return data success"){
                every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
                    slot.captured.onErrorAddWishList("", recommendation.productId.toString())
                }
            }

            When("request network"){
                viewModel.addWishlist(recommendation){ success, _ ->
                    status = success
                }
            }

            Then("Check data is success"){
                Assert.assertTrue(status == false)
            }
        }

        Scenario("Get success add topads wishlist from network"){
            val slot = slot<Subscriber<WishlistModel>>()
            var status: Boolean? = null
            Given("recommendation view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            Given("set return data success"){
                every { topAdsWishlishedUseCase.execute(any(), capture(slot)) } answers {
                    slot.captured.onError(mockk())
                }
            }

            When("request network"){
                viewModel.addWishlist(recommendation){ success, _ ->
                    status = success
                }
            }

            Then("Check data is success"){
                Assert.assertTrue(status == true)
            }
        }

        Scenario("Get error add topads wishlist from network"){
            val slot = slot<Subscriber<WishlistModel>>()
            var status: Boolean? = null
            Given("recommendation view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            Given("set return data success"){
                every { topAdsWishlishedUseCase.execute(any(), capture(slot)) } answers {
                    slot.captured.onError(mockk())
                }
            }

            When("request network"){
                viewModel.addWishlist(recommendation.copy(isTopAds = true)){ success, _ ->
                    status = success
                }
            }

            Then("Check data is success"){
                Assert.assertTrue(status == false)
            }
        }

        Scenario("Get success remove wishlist from network"){
            val slot = slot<WishListActionListener>()
            var status: Boolean? = null
            Given("recommendation view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            Given("set return data success"){
                every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
                    slot.captured.onSuccessRemoveWishlist(recommendation.productId.toString())
                }
            }

            When("request network"){
                viewModel.removeWishlist(recommendation){ success, _ ->
                    status = success
                }
            }

            Then("Check data is success"){
                Assert.assertTrue(status == true)
            }
        }

        Scenario("Get error remove wishlist from network"){
            val slot = slot<WishListActionListener>()
            var status: Boolean? = null
            Given("recommendation view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            Given("set return data success"){
                every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
                    slot.captured.onErrorRemoveWishlist("", recommendation.productId.toString())
                }
            }

            When("request network"){
                viewModel.removeWishlist(recommendation){ success, _ ->
                    status = success
                }
            }

            Then("Check data is success"){
                Assert.assertTrue(status == false)
            }
        }
    }

    Feature("Test is login"){
        lateinit var viewModel: SimilarProductRecommendationViewModel
        createInstance()
        val userSessionInterface by memoized<UserSessionInterface>()
        Scenario("Is login true"){
            Given("set data true"){
                every { userSessionInterface.isLoggedIn } returns true
            }

            Given("view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            When("Check true"){
                Assert.assertTrue(viewModel.isLoggedIn())
            }
        }

        Scenario("Is login false"){
            Given("set data false"){
                every { userSessionInterface.isLoggedIn } returns false
            }

            Given("view model"){
                viewModel = createSimilarRecommendationViewModel()
            }

            When("Check true"){
                Assert.assertTrue(!viewModel.isLoggedIn())
            }
        }
    }

})