package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.notifier.NotifierCheckReminder
import com.tokopedia.discovery2.data.notifier.NotifierSetReminder
import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.data.push.PushUnSubscriptionResponse
import com.tokopedia.discovery2.usecase.CheckPushStatusUseCase
import com.tokopedia.discovery2.usecase.SubScribeToUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CalendarWidgetItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem by lazy {
        mockk(relaxed = true)
    }
    private val application: Application = mockk()
    private val stringTest = "stringTest"
    private var context: Context = mockk()

    private var pushSubscriptionResponse: PushSubscriptionResponse =
        spyk(PushSubscriptionResponse(NotifierSetReminder(isSuccess = 1)))
    private var pushUnSubscriptionResponse: PushUnSubscriptionResponse =
        spyk(PushUnSubscriptionResponse(NotifierSetReminder(isSuccess = 1)))
    private var pushStatusResponse: PushStatusResponse =
        spyk(PushStatusResponse(NotifierCheckReminder(status = 1)))
    private val subScribeToUseCase: SubScribeToUseCase = mockk()
    private val checkPushStatusUseCase: CheckPushStatusUseCase = mockk()

    private val viewModel: CalendarWidgetItemViewModel =
        spyk(CalendarWidgetItemViewModel(application, componentsItem, 99))

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkConstructor(UserSession::class)
    }

    @Test
    fun `test for useCase`() {
        val viewModel: CalendarWidgetItemViewModel =
            spyk(CalendarWidgetItemViewModel(application, componentsItem, 99))

        val checkPushStatusUseCase = mockk<CheckPushStatusUseCase>()
        viewModel.checkPushStatusUseCase = checkPushStatusUseCase

        assert(viewModel.checkPushStatusUseCase === checkPushStatusUseCase)
    }

    @Test
    fun `test for subscribeUserForPushNotification`() {
        runBlocking {
            every { viewModel.isUserLoggedIn() } returns true
            coEvery { application.getString(any()) } returns stringTest
            viewModel.subScribeToUseCase = subScribeToUseCase
            coEvery { viewModel.subScribeToUseCase?.subscribeToPush(any()) } returns pushSubscriptionResponse

            viewModel.subscribeUserForPushNotification("1")

            assertEquals(viewModel.getPushBannerStatusData().value, Pair(true, stringTest))

            every { viewModel.isUserLoggedIn() } returns false

            viewModel.subscribeUserForPushNotification("1")
        }
    }

    @Test
    fun `test for unSubscribeUserForPushNotification`() {
        runBlocking {
            every { viewModel.isUserLoggedIn() } returns true
            viewModel.subScribeToUseCase = subScribeToUseCase
            coEvery { application.getString(any()) } returns stringTest
            coEvery { viewModel.subScribeToUseCase?.unSubscribeToPush(any()) } returns pushUnSubscriptionResponse

            viewModel.unSubscribeUserForPushNotification("1")

            assertEquals(viewModel.getPushBannerStatusData().value, Pair(false, stringTest))

            every { viewModel.isUserLoggedIn() } returns false

            viewModel.unSubscribeUserForPushNotification("1")

            assertEquals(viewModel.getPushBannerStatusData().value, Pair(false, stringTest))

            val pushUnSubscriptionResponse1: PushUnSubscriptionResponse =
                spyk(PushUnSubscriptionResponse(null))
            coEvery { subScribeToUseCase.unSubscribeToPush(any()) } returns pushUnSubscriptionResponse1

            viewModel.unSubscribeUserForPushNotification("1")

            assertEquals(viewModel.getPushBannerStatusData().value != null, true)

            coEvery { subScribeToUseCase.unSubscribeToPush(any()) } throws Exception("error")

            viewModel.unSubscribeUserForPushNotification("1")

            assertEquals(viewModel.getShowErrorToastData().value != null, true)
        }
    }

    @Test
    fun `test for checkUserPushStatus`() {
        every { viewModel.isUserLoggedIn() } returns true
        viewModel.checkPushStatusUseCase = checkPushStatusUseCase
        coEvery { checkPushStatusUseCase.checkPushStatus(any()) } returns pushStatusResponse

        viewModel.checkUserPushStatus("1")

        assertEquals(viewModel.getPushBannerSubscriptionData().value, true)

        coEvery { checkPushStatusUseCase.checkPushStatus(any()) } throws Exception("error")

        viewModel.checkUserPushStatus("1")

        assertEquals(viewModel.getPushBannerSubscriptionData().value != null, true)

        val pushStatusResponse1: PushStatusResponse = spyk(PushStatusResponse(null))
        coEvery { checkPushStatusUseCase.checkPushStatus(any()) } returns pushStatusResponse1

        viewModel.checkUserPushStatus("1")

        assertEquals(viewModel.getPushBannerSubscriptionData().value != null, true)
    }

    @Test
    fun `test for loggedInCallback`() {
        viewModel.loggedInCallback()

        assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for isUserLoggedIn`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true

        assertEquals(viewModel.isUserLoggedIn(), true)
    }

    @Test
    fun `test for getUserId`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns "2"

        assertEquals(viewModel.getUserId() == "2", true)
    }

    @Test
    fun `test for position passed`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for components passed`() {
        assert(viewModel.components === componentsItem)
    }
}
