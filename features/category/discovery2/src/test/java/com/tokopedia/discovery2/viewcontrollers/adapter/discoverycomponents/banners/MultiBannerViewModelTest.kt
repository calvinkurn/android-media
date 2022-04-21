package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.usecase.CheckPushStatusUseCase
import com.tokopedia.discovery2.usecase.bannerusecase.BannerUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherListViewModel
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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
    private val multiBannerDataUseCase: BannerUseCase = mockk(relaxed = true)

    private val viewModel: MultiBannerViewModel by lazy {
        spyk(MultiBannerViewModel(application, componentsItem, 99))
    }


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `shouldShowShimmer test`() {
        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns false
        assert(viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

    }

    @Test
    fun `test for onAttachToViewHolder`(){
        runBlocking {
            every { componentsItem.properties?.dynamic } returns true
            coEvery { viewModel.bannerUseCase.loadFirstPageComponents(any(), any()) } returns true

            viewModel.onAttachToViewHolder()

            TestCase.assertEquals(viewModel.getComponentData().value != null, true)
        }
    }

    /****************************************** onBannerClicked() ****************************************/

    @Test
    fun `banner action is APPLINK`() {
//        coEvery { componentsItem.data?.get(0)?.action } returns "APPLINK"
//
//        coVerify { RouteManager.route(application, componentsItem.data?.get(0)?.applinks) }
    }

    @Test
    fun `user not loggedin for pushNotification`() {
//        coEvery { componentsItem.data?.get(0)?.action } returns "PUSH_NOTIFIER"
//        coEvery { viewModel.isUserLoggedIn() } returns false
//        assertTrue(viewModel.getShowLoginData().value ?: false)

    }

    @Test
    fun `user loggedin for pushNotification`() {
//        coEvery { componentsItem.data?.get(0)?.action } returns "PUSH_NOTIFIER"
//        coEvery { viewModel.isUserLoggedIn() } returns true
//        coVerify {
//        }

    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}