package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Created By @ilhamsuaib on 24/03/20
 */

@ExperimentalCoroutinesApi
class SellerHomeActivityViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GetShopInfoUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val mViewModel by lazy {
        SellerHomeActivityViewModel(userSession, getNotificationUseCase, getShopInfoUseCase, Dispatchers.Unconfined)
    }

    @Test
    fun `get notifications then returns success result`() {

        val notifications = NotificationUiModel(NotificationChatUiModel(), NotificationCenterUnreadUiModel(),
                NotificationSellerOrderStatusUiModel())

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications

        mViewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }

        assertEquals(Success(notifications), mViewModel.notifications.value)
    }

    @Test
    fun `get notifications then returns failed result`() {

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } throws Throwable()

        mViewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }

        assertTrue(mViewModel.notifications.value is Fail)
    }

    @Test
    fun `get shop info then returns success result`() {
        val userId = "123456"
        val shopInfo = ShopInfoUiModel()

        getShopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userId)

        every {
            userSession.userId
        } returns userId

        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } returns shopInfo

        mViewModel.getShopInfo()

        verify {
            userSession.userId
        }

        coVerify {
            getShopInfoUseCase.executeOnBackground()
        }

        assertEquals(Success(shopInfo), mViewModel.shopInfo.value)
    }

    @Test
    fun `get shop info then returns failed result`() {
        val userId = "123456"

        getShopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userId)

        every {
            userSession.userId
        } returns userId

        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } throws Throwable()

        mViewModel.getShopInfo()

        verify {
            userSession.userId
        }

        coVerify {
            getShopInfoUseCase.executeOnBackground()
        }

        assertTrue(mViewModel.shopInfo.value is Fail)
    }
}