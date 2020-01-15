package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.multibannerresponse.PushSubscriptionResponse
import com.tokopedia.discovery2.usecase.MultiBannerDataUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MultiBannerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val pushSubscriptionResponse: PushSubscriptionResponse = mockk(relaxed = true)
    private val multiBannerDataUseCase:MultiBannerDataUseCase = mockk(relaxed = true)

    private val viewModel: MultiBannerViewModel by lazy {
        spyk(MultiBannerViewModel(application, componentsItem))
    }


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    /****************************************** onBannerClicked() ****************************************/

    @Test
    fun `banner action is APPLINK`() {
        coEvery { componentsItem.data?.get(0)?.action } returns "APPLINK"
        viewModel.onBannerClicked(0)

        coVerify { RouteManager.route(application, componentsItem.data?.get(0)?.applinks) }
    }

//    @Test
//    fun `banner action is CODE`() {
//        coEvery { componentsItem.data?.get(0)?.action } returns "CODE"
//        viewModel.onBannerClicked(0)
//
//        coVerify { RouteManager.route(application, componentsItem.data?.get(0)?.applinks) }
//    }


    @Test
    fun `user not loggedin for pushNotification`() {
        coEvery { componentsItem.data?.get(0)?.action } returns "PUSH_NOTIFIER"
        coEvery { viewModel.isUserLoggedIn() } returns false

        viewModel.onBannerClicked(0)

        assertTrue(viewModel.getshowLoginData().value ?: false)

    }

    @Test
    fun `user loggedin for pushNotification`() {
        coEvery { componentsItem.data?.get(0)?.action } returns "PUSH_NOTIFIER"
        coEvery { viewModel.isUserLoggedIn() } returns true

        //coEvery { pushSubscriptionResponse.notifierSetReminder?.isSuccess } returns 1

        viewModel.onBannerClicked(0)

      //  assertEquals(viewModel.getPushBannerStatusData(), 0)
        coVerify {
            multiBannerDataUseCase
                    .subscribeToPush(any()) }
    }


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}