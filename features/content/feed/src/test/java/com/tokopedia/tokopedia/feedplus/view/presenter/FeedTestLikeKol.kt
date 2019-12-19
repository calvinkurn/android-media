package com.tokopedia.tokopedia.feedplus.view.presenter

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopViewModel
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
import com.tokopedia.tokopedia.feedplus.view.*
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

/**
 * @author by yoasfs on 2019-12-17
 */
class FeedTestLikeKol : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Do like Kol") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val likeKolPostUseCase by memoized<LikeKolPostUseCase>()

        Scenario("Success like kol") {
            val successResponse = FeedPromotedShopViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = true
            }

            Given("Mock usecase response") {
                likeKolPostUseCase.doLikeKolWithSample(successResponse.isSuccess)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do like kol", timeout = 100000) {
                feedViewModel.doLikeKol(0, 0)
            }

            Then("Like kol call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.likeKolResp.value is Success)
            }

            Then("Successfully like kol") {
                Assert.assertEquals(
                        (feedViewModel.likeKolResp.value!! as Success).data.isSuccess,
                        successResponse.isSuccess)
            }
        }

        Scenario("Success like kol but failed result") {
            val successResponse = FeedPromotedShopViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = false
            }

            Given("Mock usecase response") {
                likeKolPostUseCase.doLikeKolWithSample(successResponse.isSuccess)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do like kol", timeout = 100000) {
                feedViewModel.doLikeKol(0, 0)
            }

            Then("Like kol call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.likeKolResp.value is Success)
            }

            Then("Successfully like kol but get failed result") {
                Assert.assertEquals(
                        (feedViewModel.likeKolResp.value!! as Success).data.isSuccess,
                        successResponse.isSuccess)
            }
        }
    }

    Feature("Do unlike Kol") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val likeKolPostUseCase by memoized<LikeKolPostUseCase>()

        Scenario("Success unlike kol") {
            val successResponse = FeedPromotedShopViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = true
            }

            Given("Mock usecase response") {
                likeKolPostUseCase.doLikeKolWithSample(successResponse.isSuccess)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do unlike kol", timeout = 100000) {
                feedViewModel.doUnlikeKol(0, 0)
            }

            Then("Unlike kol call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.likeKolResp.value is Success)
            }

            Then("Successfully unlike kol") {
                Assert.assertEquals(
                        (feedViewModel.likeKolResp.value!! as Success).data.isSuccess,
                        successResponse.isSuccess)
            }
        }

        Scenario("Success unlike kol but failed result") {
            val successResponse = FeedPromotedShopViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = false
            }

            Given("Mock usecase response") {
                likeKolPostUseCase.doLikeKolWithSample(successResponse.isSuccess)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do unlike kol", timeout = 100000) {
                feedViewModel.doUnlikeKol(0, 0)
            }

            Then("Unlike kol call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.likeKolResp.value is Success)
            }

            Then("Successfully unlike kol but get failed result") {
                Assert.assertEquals(
                        (feedViewModel.likeKolResp.value!! as Success).data.isSuccess,
                        successResponse.isSuccess)
            }
        }
    }
})