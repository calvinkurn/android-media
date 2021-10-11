package com.tokopedia.tokopedia.feedplus.view.presenter

import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistNewUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
import com.tokopedia.tokopedia.feedplus.view.createFeedTestInstance
import com.tokopedia.tokopedia.feedplus.view.createFeedViewModel
import com.tokopedia.tokopedia.feedplus.view.getMockData
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
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
        val whiteListFeedUseCase by memoized {
            mockk<GetWhitelistNewUseCase>(relaxed = true)
        }

        val getDynamicFeedUseCase by memoized {
            mockk<GetDynamicFeedNewUseCase>(relaxed = true)
        }

        Scenario("Success get feed first data") {
            val successResponse = DynamicFeedFirstPageDomainModel()

            val whiteListResponse = WhitelistQuery()
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
            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            Given("Mock usecase response") {
                whiteListFeedUseCase.getMockData(whiteListResponse)
            }

            Given("Mock feed first usecase response with dummy cursor") {
                getDynamicFeedUseCase.getMockData(
                    successResponse.dynamicFeedDomainModel.postList,
                    ""
                )
            }

//            When("Get feed data", timeout = 100000) {
//                feedViewModel.getFeedFirstPage()
//            }

//            Then("Feed data call success", timeout = 100000) {
//                Assert.assertEquals(true, feedViewModel.getFeedFirstPageResp.value is Success)
//            }
//
//            Then("Feed data successfully return data") {
//                Assert.assertEquals(
//                        (feedViewModel.getFeedFirstPageResp.value!! as Success).data.dynamicFeedDomainModel.postList.size,
//                        successResponse.dynamicFeedDomainModel.postList.size)
//            }
        }

        Scenario("Success get feed data with empty post") {
            val whiteListResponse = WhitelistQuery()
            val successResponse = DynamicFeedFirstPageDomainModel()
            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock usecase response") {
                whiteListFeedUseCase.getMockData(whiteListResponse)
            }

            Given("Mock sample response data") {
                successResponse.dynamicFeedDomainModel.postList = mutableListOf()
            }

            Given("Mock feed first usecase response with dummy cursor") {
                getDynamicFeedUseCase.getMockData(
                    successResponse.dynamicFeedDomainModel.postList,
                    ""
                )
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

        }
    }

    Feature("Get Feed Next Data") {
        lateinit var feedViewModel: FeedViewModel
        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val getDynamicFeedUseCase by memoized {
            mockk<GetDynamicFeedNewUseCase>(relaxed = true)
        }
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
                getDynamicFeedUseCase.getMockData(successResponse.postList, cursor)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
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
                getDynamicFeedUseCase.getMockData(successResponse.postList, cursor)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }


        }
    }
})