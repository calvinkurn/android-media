package com.tokopedia.tokopedia.feedplus.view.presenter

import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.viewmodel.VoteViewModel
import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
import com.tokopedia.tokopedia.feedplus.view.createFeedTestInstance
import com.tokopedia.tokopedia.feedplus.view.createFeedViewModel
import com.tokopedia.tokopedia.feedplus.view.doVoteWithSample
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vote.domain.usecase.SendVoteUseCase
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

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
    }
})