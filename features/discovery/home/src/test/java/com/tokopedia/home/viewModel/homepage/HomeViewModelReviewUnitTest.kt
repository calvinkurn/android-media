package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeReviewSuggestedUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature


@ExperimentalCoroutinesApi
class HomeViewModelReviewUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Review test") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getHomeUseCase by memoized<HomeUseCase>()

        Scenario("Test Review is not visible") {
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

            Given("Populate data viewmodel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf()
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect Review widget will show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is ReviewDataModel } == null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Test Review is visible") {
            val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

            Given("Populate data viewmodel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(review)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect Review widget will show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is ReviewDataModel } != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Test Review with data product") {
            val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

            val getHomeReviewSuggestedUseCase by memoized<GetHomeReviewSuggestedUseCase>()

            Given("Populate data viewmodel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(review)
                        )
                )
            }

            Given("Review keyword data"){
                coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                        suggestedProductReview = SuggestedProductReviewResponse(
                                title = "Suggested Title"
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect review widget will show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is ReviewDataModel } != null
                                && (it.list.find { it is ReviewDataModel } as ReviewDataModel)?.suggestedProductReview?.suggestedProductReview?.title == "Suggested Title"
                    })
                }
                confirmVerified(observerHome)
            }
        }
        Scenario("Test Review with data product and close the widget") {
            val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

            val getHomeReviewSuggestedUseCase by memoized<GetHomeReviewSuggestedUseCase>()

            Given("Populate data viewmodel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(review)
                        )
                )
            }

            Given("Review data"){
                coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                        suggestedProductReview = SuggestedProductReviewResponse(
                                title = "Suggested Title"
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Close widget pressed"){
                homeViewModel.onRemoveSuggestedReview()
            }

            Then("Expect Review widget will show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is ReviewDataModel } != null
                                && (it.list.find { it is ReviewDataModel } as ReviewDataModel)?.suggestedProductReview?.suggestedProductReview?.title == "Suggested Title"
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is ReviewDataModel } == null
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }
})