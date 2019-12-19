package com.tokopedia.tokopedia.feedplus.view.presenter

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.interest_pick_common.data.DataItem
import com.tokopedia.interest_pick_common.domain.usecase.GetInterestPickUseCase
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.tokopedia.feedplus.InstantTaskExecutorRuleSpek
import com.tokopedia.tokopedia.feedplus.view.createFeedTestInstance
import com.tokopedia.tokopedia.feedplus.view.createFeedViewModel
import com.tokopedia.tokopedia.feedplus.view.getFeedFirstDataWithSample
import com.tokopedia.tokopedia.feedplus.view.getInterestPickDataWithSample
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import org.junit.Assert
import org.mockito.ArgumentCaptor
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by yoasfs on 2019-12-19
 */
class FeedTestOnboarding: Spek({


    InstantTaskExecutorRuleSpek(this)

    Feature("Feed onboarding - interest pick") {

        createFeedTestInstance()

        val mockUserId = "12345"
        val userSession by memoized<UserSessionInterface>()
        val resultObserver: Observer<Result<OnboardingViewModel>> = mock()
        val resultCaptor: ArgumentCaptor<Result<OnboardingViewModel>> = mock()
        val getInterestPickUseCase by memoized<GetInterestPickUseCase>()
        lateinit var feedViewModel: FeedViewModel

        Scenario("Success get feed onboarding") {
            val successResponse = OnboardingViewModel()
            val successPojoResponse: MutableList<DataItem> = mutableListOf()

            Given("Feed view model") {
                feedViewModel = createFeedViewModel()
            }

            Given("Mock sample pojo data") {
                successPojoResponse.addAll(mutableListOf(
                        DataItem(id = 0),
                        DataItem(id = 1),
                        DataItem(id = 2),
                        DataItem(id = 3),
                        DataItem(id = 4),
                        DataItem(id = 5)
                ))
            }

            Given("Mock sample response data") {
                successResponse.dataList.addAll(mutableListOf(
                        InterestPickDataViewModel(id = 0),
                        InterestPickDataViewModel(id = 1),
                        InterestPickDataViewModel(id = 2),
                        InterestPickDataViewModel(id = 3),
                        InterestPickDataViewModel(id = 4),
                        InterestPickDataViewModel(id = 5)
                ))
            }

            Given("Mock usecase response") {
                feedViewModel.onboardingResp.observeForever(resultObserver)
                getInterestPickUseCase.getInterestPickDataWithSample(successPojoResponse, resultObserver)
            }

            Given("Mock user id") {
                every { userSession.userId } returns mockUserId
            }

            When("Get feed onboarding data", timeout = 100000) {
                feedViewModel.getOnboardingData("")
            }

            Then("Get feed onboarding success", timeout = 100000) {
                resultObserver.onChanged(resultCaptor.capture())
                resultCaptor.allValues
//                Assert.assertEquals(true, feedViewModel.onboardingResp.value as Success)
            }

            Then("Get feed onboarding successfully return data") {
                Assert.assertEquals((feedViewModel.onboardingResp.value as Success).data.dataList.size,
                        successResponse.dataList.size)
            }
        }

    }
})