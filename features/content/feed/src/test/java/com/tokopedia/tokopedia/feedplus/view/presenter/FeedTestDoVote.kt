package com.tokopedia.tokopedia.feedplus.view.presenter

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopViewModel
import com.tokopedia.feedplus.view.viewmodel.VoteViewModel
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
import com.tokopedia.tokopedia.feedplus.view.*
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel
import com.tokopedia.vote.domain.usecase.SendVoteUseCase
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

/**
 * @author by yoasfs on 2019-12-17
 */
class FeedTestDoVote : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Do vote") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val sendVoteUseCase by memoized<SendVoteUseCase>()

        Scenario("Success do vote") {
            val successResponse = VoteViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = true
            }

            Given("Mock usecase response") {
                sendVoteUseCase.doVoteWithSample(successResponse.voteModel)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do vote", timeout = 100000) {
                feedViewModel.doVote(0,"", "")
            }

            Then("Vote call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.voteResp.value is Success)
            }
        }

//        Scenario("Success call do favorite shop but get failed result") {
//            val successResponse = FeedPromotedShopViewModel()
//            Given("Feed view model") {
//                feedViewModel = createFeedViewModel()
//            }
//
//            Given("Mock sample response data") {
//                successResponse.isSuccess = false
//            }
//
//            Given("Mock usecase response") {
//                doFavoriteShopUseCase.doFavoriteShopWithSample(successResponse.isSuccess)
//            }
//
//            Given("Mock user id") {
//                every { userSession.userId } returns mockUserId
//            }
//
//            When("Do favorite shop", timeout = 100000) {
//                feedViewModel.doFavoriteShop(Data(), 0)
//            }
//
//            Then("Favorite shop call success", timeout = 100000) {
//                Assert.assertEquals(true, feedViewModel.doFavoriteShopResp.value is Success)
//            }
//
//            Then("Failed favorite shop") {
//                Assert.assertEquals(
//                        (feedViewModel.doFavoriteShopResp.value!! as Success).data.isSuccess,
//                        successResponse.isSuccess)
//            }
//        }
    }
})