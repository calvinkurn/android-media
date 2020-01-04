package com.tokopedia.tokopedia.feedplus.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.AtcViewModel
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
import com.tokopedia.tokopedia.feedplus.view.createFeedTestInstance
import com.tokopedia.tokopedia.feedplus.view.createFeedViewModel
import com.tokopedia.tokopedia.feedplus.view.doAtcWithSample
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by yoasfs on 2019-12-17
 */
class FeedTestDoAtc : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Do add to cart") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val atcUseCase by memoized<AddToCartUseCase>()
        val RESULT_SUCCESS = 0

        Scenario("Success do add to cart") {
            val successResponse = AtcViewModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.isSuccess = true
            }

            Given("Mock usecase response") {
                atcUseCase.doAtcWithSample(RESULT_SUCCESS)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Do Atc", timeout = 100000) {
                feedViewModel.doAtc(PostTagItem())
            }

            Then("Atc call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.atcResp.value is Success)
            }
        }
    }
})