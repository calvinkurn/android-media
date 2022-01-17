//package com.tokopedia.tokopedia.feedplus.view.presenter
//
//import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
//import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.TrackAffiliateViewModel
//import com.tokopedia.feedplus.view.presenter.FeedViewModel
//import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
//import com.tokopedia.tokopedia.feedplus.view.createFeedTestInstance
//import com.tokopedia.tokopedia.feedplus.view.createFeedViewModel
//import com.tokopedia.tokopedia.feedplus.view.doTrackAffiliateWithSample
//import com.tokopedia.usecase.coroutines.Success
//import com.tokopedia.user.session.UserSessionInterface
//import io.mockk.every
//import org.junit.Assert
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
///**
// * @author by yoasfs on 2019-12-17
// */
//class FeedTestTrackAffiliate : Spek({
//
//    InstantTaskExecutorRuleSpek(this)
//
//    Feature("Do Track Affiliate") {
//        lateinit var feedViewModel: FeedViewModel
//        createFeedTestInstance()
//
//        val mockUserId = "12345"
//        val userSession by memoized<UserSessionInterface>()
//        val trackAffiliateClickUseCase by memoized<TrackAffiliateClickUseCase>()
//
//        Scenario("Success track affiliate") {
//            val successResponse = TrackAffiliateViewModel()
//            Given("Feed view model") {
//                feedViewModel = createFeedViewModel()
//            }
//
//            Given("Mock sample response data") {
//                successResponse.isSuccess = true
//            }
//
//            Given("Mock usecase response") {
//                trackAffiliateClickUseCase.doTrackAffiliateWithSample(successResponse.isSuccess)
//            }
//
//            Given("Mock user id") {
//                every { userSession.userId } returns mockUserId
//            }
//
//            When("Do track affiliate", timeout = 100000) {
//                feedViewModel.doTrackAffiliate("")
//            }
//
//            Then("Track affiliate call success", timeout = 100000) {
//                Assert.assertEquals(true, feedViewModel.trackAffiliateResp.value is Success)
//            }
//
//            Then("Successfully track affiliate") {
//                Assert.assertEquals(
//                        (feedViewModel.trackAffiliateResp.value!! as Success).data.isSuccess,
//                        successResponse.isSuccess)
//            }
//        }
//
//        Scenario("Success track affiliate but failed response") {
//            val successResponse = TrackAffiliateViewModel()
//            Given("Feed view model") {
//                feedViewModel = createFeedViewModel()
//            }
//
//            Given("Mock sample response data") {
//                successResponse.isSuccess = false
//            }
//
//            Given("Mock usecase response") {
//                trackAffiliateClickUseCase.doTrackAffiliateWithSample(successResponse.isSuccess)
//            }
//
//            Given("Mock user id") {
//                every { userSession.userId } returns mockUserId
//            }
//
//            When("Do track affiliate", timeout = 100000) {
//                feedViewModel.doTrackAffiliate("")
//            }
//
//            Then("Track affiliate call success", timeout = 100000) {
//                Assert.assertEquals(true, feedViewModel.trackAffiliateResp.value is Success)
//            }
//
//            Then("Track affiliate get failed response") {
//                Assert.assertEquals(
//                        (feedViewModel.trackAffiliateResp.value!! as Success).data.isSuccess,
//                        successResponse.isSuccess)
//            }
//        }
//
//    }
//})