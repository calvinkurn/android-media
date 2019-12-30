package com.tokopedia.tokopedia.feedplus.view.presenter

import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopViewModel
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
import com.tokopedia.tokopedia.feedplus.view.createFeedTestInstance
import com.tokopedia.tokopedia.feedplus.view.createFeedViewModel
import com.tokopedia.tokopedia.feedplus.view.doFavoriteShopWithSample
import com.tokopedia.tokopedia.feedplus.view.doToggleFavoriteShopWithSample
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by yoasfs on 2019-12-17
 */
class FeedTestDoFavoriteShop : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Do Favorite Promoted Shop") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val doFavoriteShopUseCase by memoized<ToggleFavouriteShopUseCase>()

        Scenario("Success do favorite shop") {
            val successResponse = FeedPromotedShopViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = true
            }

            Given("Mock usecase response") {
                doFavoriteShopUseCase.doFavoriteShopWithSample(successResponse.isSuccess)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do favorite shop", timeout = 100000) {
                feedViewModel.doFavoriteShop(Data(), 0)
            }

            Then("Favorite shop call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.doFavoriteShopResp.value is Success)
            }

            Then("Successfully favorite shop") {
                Assert.assertEquals(
                        (feedViewModel.doFavoriteShopResp.value!! as Success).data.isSuccess,
                        successResponse.isSuccess)
            }
        }

        Scenario("Success call do favorite shop but get failed result") {
            val successResponse = FeedPromotedShopViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = false
            }

            Given("Mock usecase response") {
                doFavoriteShopUseCase.doFavoriteShopWithSample(successResponse.isSuccess)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do favorite shop", timeout = 100000) {
                feedViewModel.doFavoriteShop(Data(), 0)
            }

            Then("Favorite shop call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.doFavoriteShopResp.value is Success)
            }

            Then("Failed favorite shop") {
                Assert.assertEquals(
                        (feedViewModel.doFavoriteShopResp.value!! as Success).data.isSuccess,
                        successResponse.isSuccess)
            }
        }
    }

    Feature("Do Toggle Favorite Shop") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val doFavoriteShopUseCase by memoized<ToggleFavouriteShopUseCase>()

        Scenario("Success toggle favorite shop") {
            val successResponse = FeedPromotedShopViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = true
            }

            Given("Mock usecase response") {
                doFavoriteShopUseCase.doToggleFavoriteShopWithSample(successResponse.isSuccess)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do favorite shop", timeout = 100000) {
                feedViewModel.doToggleFavoriteShop(0, 0, "")
            }

            Then("Favorite shop call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.toggleFavoriteShopResp.value is Success)
            }

            Then("Successfully favorite shop") {
                Assert.assertEquals(
                        (feedViewModel.toggleFavoriteShopResp.value!! as Success).data.isSuccess,
                        successResponse.isSuccess)
            }
        }

        Scenario("Success call toggle favorite shop but get failed result") {
            val successResponse = FeedPromotedShopViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = false
            }

            Given("Mock usecase response") {
                doFavoriteShopUseCase.doToggleFavoriteShopWithSample(successResponse.isSuccess)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do favorite shop", timeout = 100000) {
                feedViewModel.doToggleFavoriteShop(0, 0, "")
            }

            Then("Favorite shop call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.toggleFavoriteShopResp.value is Success)
            }

            Then("Failed favorite shop") {
                Assert.assertEquals(
                        (feedViewModel.toggleFavoriteShopResp.value!! as Success).data.isSuccess,
                        successResponse.isSuccess)
            }
        }
    }
})