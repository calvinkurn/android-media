package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.notifier.NotifierCheckReminder
import com.tokopedia.discovery2.data.notifier.NotifierSetReminder
import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.data.push.PushUnSubscriptionResponse
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

    private var pushSubscriptionResponse : PushSubscriptionResponse = spyk(PushSubscriptionResponse(NotifierSetReminder(isSuccess = 1)))
    private var pushUnSubscriptionResponse : PushUnSubscriptionResponse = spyk(PushUnSubscriptionResponse(NotifierSetReminder(isSuccess = 1)))
    private var pushStatusResponse : PushStatusResponse = spyk(PushStatusResponse(NotifierCheckReminder(status = 1)))

    private val viewModel: CalendarWidgetItemViewModel = spyk(CalendarWidgetItemViewModel(application, componentsItem, 99))
    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test for subscribeUserForPushNotification`(){
        runBlocking {

            every { viewModel.isUserLoggedIn() } returns true
            coEvery { application.getString(any()) } returns stringTest
            coEvery { viewModel.subScribeToUseCase.subscribeToPush(any()) } returns pushSubscriptionResponse

            viewModel.subscribeUserForPushNotification("1")

            assertEquals(viewModel.getPushBannerStatusData().value, Pair(true, stringTest))


            every { viewModel.isUserLoggedIn() } returns false

            viewModel.subscribeUserForPushNotification("1")

        }
    }

    @Test
    fun `test for unSubscribeUserForPushNotification`(){
        runBlocking {

            every { viewModel.isUserLoggedIn() } returns true
            coEvery { application.getString(any()) } returns stringTest
            coEvery { viewModel.subScribeToUseCase.unSubscribeToPush(any()) } returns pushUnSubscriptionResponse

            viewModel.unSubscribeUserForPushNotification("1")

            assertEquals(viewModel.getPushBannerStatusData().value, Pair(false, stringTest))


            every { viewModel.isUserLoggedIn() } returns false

            viewModel.unSubscribeUserForPushNotification("1")

        }
    }
    @Test
    fun `test for checkUserPushStatus`(){
        runBlocking {

            every { viewModel.isUserLoggedIn() } returns true
            coEvery { viewModel.checkPushStatusUseCase.checkPushStatus(any()) } returns pushStatusResponse

            viewModel.checkUserPushStatus("1")

            assertEquals(viewModel.getPushBannerSubscriptionData().value, true)


            every { viewModel.isUserLoggedIn() } returns false

            viewModel.checkUserPushStatus("1")

        }
    }

    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for components passed`(){
        assert(viewModel.components === componentsItem)
    }}