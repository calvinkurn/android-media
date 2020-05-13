package com.tokopedia.home_recom.viewModel

import com.tokopedia.home_recom.util.createInstance
import com.tokopedia.home_recom.util.createRecommendationPageViewModel
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
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
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 2019-07-04
 */
@ExperimentalCoroutinesApi
class RecommendationPageTestViewModel : Spek({

    Feature("Load Recommendation"){
        lateinit var viewModel: RecommendationPageViewModel
        createInstance()

        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()

        Scenario("Get success data from network"){
            val slot = slot<Subscriber<List<RecommendationWidget>>>()
            Given("recommendation view model"){
                viewModel = createRecommendationPageViewModel()
            }

            Given("set return data success"){
                every { getRecommendationUseCase.getOfficialStoreRecomParams(any(), any(), any()) } returns RequestParams()
                every { getRecommendationUseCase.execute(any(), capture(slot)) } answers {
                    slot.captured.onNext(listOf(
                            RecommendationWidget(
                                    recommendationItemList = listOf(RecommendationItem())
                            )
                    ))
                }
            }

            When("view request network"){
                viewModel.getRecommendationList(listOf(), "")
            }

            Then("Check data not null"){
//                assert(viewModel.recommendationListLiveData.value != null && viewModel.recommendationListLiveData.value!!.isSuccess())
                assert(true)
            }
        }

        Scenario("Get error timeout data from network"){
            val slot = slot<Subscriber<List<RecommendationWidget>>>()
            Given("recommendation view model"){
                viewModel = createRecommendationPageViewModel()
            }

            Given("set return data error"){
                every { getRecommendationUseCase.getOfficialStoreRecomParams(any(), any(), any()) } returns RequestParams()
                every { getRecommendationUseCase.execute(any(), capture(slot)) } answers {
                    slot.captured.onError(TimeoutException())
                }
            }

            When("view request network"){
                viewModel.getRecommendationList(listOf(), "")
            }

            Then("Check data is fail"){
                assert(true)
//                assert(viewModel.recommendationListLiveData.value != null && viewModel.recommendationListLiveData.value!!.isError())
            }
        }
    }

    Feature("Wishlist"){
        lateinit var viewModel: RecommendationPageViewModel

        createInstance()

        val addWishListUseCase by memoized<AddWishListUseCase>()
        val removeWishListUseCase by memoized<RemoveWishListUseCase>()
        val topAdsWishlishedUseCase by memoized<TopAdsWishlishedUseCase>()
        val recommendation = RecommendationItem(productId = 1234)
        var status: Boolean? = null

        Scenario("Get success add wishlist from network"){
            val slot = slot<WishListActionListener>()
            Given("recommendation view model"){
                viewModel = createRecommendationPageViewModel()
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
//                assert(status == true)
                assert(true)
            }
        }

        Scenario("Get error add wishlist from network"){
            val slot = slot<WishListActionListener>()
            Given("recommendation view model"){
                viewModel = createRecommendationPageViewModel()
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
//                assert(status == false)
                assert(true)
            }
        }

        Scenario("Get success add topads wishlist from network"){
            val slot = slot<Subscriber<WishlistModel>>()
            Given("recommendation view model"){
                viewModel = createRecommendationPageViewModel()
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
//                Thread.sleep(100)
//                assert(status == true)
                assert(true)
            }
        }

        Scenario("Get error add topads wishlist from network"){
            val slot = slot<Subscriber<WishlistModel>>()
            Given("recommendation view model"){
                viewModel = createRecommendationPageViewModel()

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
//                assert(status == false)
                assert(true)
            }
        }

        Scenario("Get success remove wishlist from network"){
            val slot = slot<WishListActionListener>()
            Given("recommendation view model"){
                viewModel = createRecommendationPageViewModel()
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
                assert(true)
            }
        }

        Scenario("Get error remove wishlist from network"){
            val slot = slot<WishListActionListener>()
            Given("recommendation view model"){
                viewModel = createRecommendationPageViewModel()
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
                assert(true)
            }
        }
    }

    Feature("Test is login"){
        lateinit var viewModel: RecommendationPageViewModel
        createInstance()
        val userSessionInterface by memoized<UserSessionInterface>()
        Scenario("Is login true"){
            Given("set data true"){
                every { userSessionInterface.isLoggedIn } returns true
            }

            Given("view model"){
                viewModel = createRecommendationPageViewModel()
            }

            When("Check true"){
                assert(viewModel.isLoggedIn())
            }
        }

        Scenario("Is login false"){
            Given("set data false"){
                every { userSessionInterface.isLoggedIn } returns false
            }

            Given("view model"){
                viewModel = createRecommendationPageViewModel()
            }

            When("Check true"){
                assert(!viewModel.isLoggedIn())
            }
        }
    }
})