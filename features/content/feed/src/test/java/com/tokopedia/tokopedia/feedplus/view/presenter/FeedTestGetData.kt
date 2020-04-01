package com.tokopedia.tokopedia.feedplus.view.presenter

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
import com.tokopedia.tokopedia.feedplus.view.createFeedTestInstance
import com.tokopedia.tokopedia.feedplus.view.createFeedViewModel
import com.tokopedia.tokopedia.feedplus.view.getFeedFirstDataWithSample
import com.tokopedia.tokopedia.feedplus.view.getFeedNextDataWithSample
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by yoasfs on 2019-12-17
 */
class FeedTestGetData : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Get Feed First Data") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val getDynamicFeedFirstPageUseCase by memoized<GetDynamicFeedFirstPageUseCase>()
        val firstPageCursor = "firstCursor"

        Scenario("Success get feed first data") {
            val successResponse = DynamicFeedFirstPageDomainModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.dynamicFeedDomainModel.postList = mutableListOf(
                        DynamicPostViewModel(id = 1),
                        DynamicPostViewModel(id = 2),
                        DynamicPostViewModel(id = 3),
                        DynamicPostViewModel(id = 4),
                        DynamicPostViewModel(id = 5),
                        DynamicPostViewModel(id = 6)
                )
            }

            Given("Mock usecase response") {
                getDynamicFeedFirstPageUseCase.getFeedFirstDataWithSample(successResponse.dynamicFeedDomainModel.postList)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Get feed data", timeout = 100000) {
                feedViewModel.getFeedFirstPage(firstPageCursor)
            }

            Then("Feed data call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.getFeedFirstPageResp.value is Success)
            }

            Then("Feed data successfully return data") {
                Assert.assertEquals(
                        (feedViewModel.getFeedFirstPageResp.value!! as Success).data.dynamicFeedDomainModel.postList.size,
                        successResponse.dynamicFeedDomainModel.postList.size)
            }
        }

        Scenario("Success get feed data with empty post") {
            val successResponse = DynamicFeedFirstPageDomainModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.dynamicFeedDomainModel.postList = mutableListOf()
            }

            Given("Mock usecase response") {
                getDynamicFeedFirstPageUseCase.getFeedFirstDataWithSample(successResponse.dynamicFeedDomainModel.postList)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Get feed data", timeout = 100000) {
                feedViewModel.getFeedFirstPage(firstPageCursor)
            }

            Then("Feed data call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.getFeedFirstPageResp.value is Success)
            }

            Then("Feed data successfully return empty post") {
                Assert.assertEquals(
                        (feedViewModel.getFeedFirstPageResp.value!! as Success).data.dynamicFeedDomainModel.postList.size,
                        successResponse.dynamicFeedDomainModel.postList.size)
            }
        }

//        Scenario("Error get feed") {
//            val successResponse = DynamicFeedFirstPageDomainModel()
//            Given("Feed view model") {
//                feedViewModel = createFeedViewModel()
//            }
//
//            Given("Mock sample response data") {
//                successResponse.dynamicFeedDomainModel.postList = mutableListOf()
//            }
//
//            Given("Mock usecase response") {
//                try {
//                    getDynamicFeedFirstPageUseCase.getErrorFeedData()
//                } catch (e: Exception) {
//                    Fail(e)
//                }
//            }
//
//            Given("Mock user id") {
//                every { userSession.userId } returns mockUserId
//            }
//
//            When("Get feed data", timeout = 100000) {
//                feedViewModel.getFeedFirstPage(firstPageCursor)
//            }
//
//            Then("Feed data call successfully error", timeout = 100000) {
//                Assert.assertEquals(true, feedViewModel.getFeedFirstPageResp.value is Fail)
//            }
//        }

    }

    Feature("Get Feed Next Data") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val getDynamicFeedUseCase by memoized<GetDynamicFeedUseCase>()
        val getDynamicFeedFirstPageUseCase by memoized<GetDynamicFeedFirstPageUseCase>()
        val cursor = "mockCursor"

        Scenario("Success get feed next data") {
            val successFirstResponse = DynamicFeedFirstPageDomainModel()
            val successResponse = DynamicFeedDomainModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample response data") {
                successResponse.postList = mutableListOf(
                        DynamicPostViewModel(id = 1),
                        DynamicPostViewModel(id = 2),
                        DynamicPostViewModel(id = 3),
                        DynamicPostViewModel(id = 4),
                        DynamicPostViewModel(id = 5),
                        DynamicPostViewModel(id = 6)
                )
            }


            Given("Mock feed first usecase response with dummy cursor") {
                getDynamicFeedFirstPageUseCase.getFeedFirstDataWithSample(successFirstResponse.dynamicFeedDomainModel.postList, cursor)
            }

            Given("Mock feed next usecase response") {
                getDynamicFeedUseCase.getFeedNextDataWithSample(successResponse.postList)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Get feed first data", timeout = 100000) {
                feedViewModel.getFeedFirstPage("")
            }

            When("Get feed next data", timeout = 100000) {
                feedViewModel.getFeedNextPage()
            }

            Then("Feed data call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.getFeedNextPageResp.value is Success)
            }

            Then("Feed data successfully return data") {
                Assert.assertEquals(
                        (feedViewModel.getFeedNextPageResp.value!! as Success).data.postList.size,
                        successResponse.postList.size)
            }
        }

        Scenario("Success get feed next data with empty post") {
            val successFirstResponse = DynamicFeedFirstPageDomainModel()
            val successResponse = DynamicFeedDomainModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock empty response data") {
                successResponse.postList = mutableListOf()
            }

            Given("Mock feed first usecase response with dummy cursor") {
                getDynamicFeedFirstPageUseCase.getFeedFirstDataWithSample(successFirstResponse.dynamicFeedDomainModel.postList, cursor)
            }

            Given("Mock feed next usecase response") {
                getDynamicFeedUseCase.getFeedNextDataWithSample(successResponse.postList)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Get feed first data", timeout = 100000) {
                feedViewModel.getFeedFirstPage("")
            }

            When("Get feed next data", timeout = 100000) {
                feedViewModel.getFeedNextPage()
            }

            Then("Feed data call success", timeout = 100000) {
                Assert.assertEquals(true, feedViewModel.getFeedNextPageResp.value is Success)
            }

            Then("Feed data successfully return empty post") {
                Assert.assertEquals(
                        (feedViewModel.getFeedNextPageResp.value!! as Success).data.postList.size,
                        successResponse.postList.size)
            }
        }
    }
})