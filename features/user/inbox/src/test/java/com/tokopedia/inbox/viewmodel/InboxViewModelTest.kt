package com.tokopedia.inbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.inbox.domain.cache.InboxCacheManager
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.inbox.domain.data.notification.Notifications
import com.tokopedia.inbox.domain.usecase.InboxNotificationUseCase
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class InboxViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: InboxNotificationUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val cacheManager: InboxCacheManager = mockk(relaxed = true)

    private val notificationObserver: Observer<Result<Notifications>> = mockk(relaxed = true)

    private val viewModel = InboxViewModel(useCase, userSession, cacheManager)

    @Before
    fun setUp() {
        viewModel.notifications.observeForever(notificationObserver)
    }

    @Test
    fun `getNotifications should return the data correctly`() {
        // given
        val expectedValue = inboxCounter.notifications

        every { useCase.getNotification(captureLambda(), any()) } answers {
            val onSuccess = lambda<(Notifications) -> Unit>()
            onSuccess.invoke(expectedValue)
        }

        // when
        viewModel.getNotifications()

        // then
        verify(exactly = 1) {
            notificationObserver.onChanged(Success(expectedValue))
        }
    }

    @Test
    fun `getNotifications should throw the Fail state`() {
        // given
        val expectedValue = Throwable()

        every { useCase.getNotification(any(), captureLambda()) } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(expectedValue)
        }

        // when
        viewModel.getNotifications()

        // then
        verify(exactly = 1) {
            notificationObserver.onChanged(Fail(expectedValue))
        }
    }

    @Test
    fun `buyerOnly_hasShowOnBoarding test`() {
        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = true,
                cacheOnBoardingBuyer = true
        )
        assertEquals(true, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = true,
                cacheOnBoardingBuyer = false
        )
        assertEquals(true, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = true,
                cacheOnBoardingBuyer = null
        )
        assertEquals(true, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = false,
                cacheOnBoardingBuyer = true
        )
        assertEquals(true, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = false,
                cacheOnBoardingBuyer = false
        )
        assertEquals(false, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = false,
                cacheOnBoardingBuyer = null
        )
        assertEquals(false, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = null,
                cacheOnBoardingBuyer = true
        )
        assertEquals(true, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = null,
                cacheOnBoardingBuyer = false
        )
        assertEquals(false, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = false,
                cacheOnBoardingSeller = null,
                cacheOnBoardingBuyer = null
        )
        assertEquals(false, viewModel.hasShowOnBoarding())
    }

    @Test
    fun `buyerWithShop_hasShowOnBoarding test`() {
        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = true,
                cacheOnBoardingBuyer = true
        )
        assertEquals(true, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = true,
                cacheOnBoardingBuyer = false
        )
        assertEquals(true, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = true,
                cacheOnBoardingBuyer = null
        )
        assertEquals(true, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = false,
                cacheOnBoardingBuyer = true
        )
        assertEquals(false, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = false,
                cacheOnBoardingBuyer = false
        )
        assertEquals(false, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = false,
                cacheOnBoardingBuyer = null
        )
        assertEquals(false, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = null,
                cacheOnBoardingBuyer = true
        )
        assertEquals(false, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = null,
                cacheOnBoardingBuyer = false
        )
        assertEquals(false, viewModel.hasShowOnBoarding())

        hasShownGivenHelper(
                hashShop = true,
                cacheOnBoardingSeller = null,
                cacheOnBoardingBuyer = null
        )
        assertEquals(false, viewModel.hasShowOnBoarding())
    }

    @Test
    fun `markFinishedBuyerOnBoarding should save the onboarding of buyer as true`() {
        // given
        every { cacheManager.saveCacheBoolean("key_onboarding_buyer", true) } just runs

        // when
        viewModel.markFinishedBuyerOnBoarding()

        // then
        verify(exactly = 1) { cacheManager.saveCacheBoolean(any(), any()) }
    }

    @Test
    fun `markFinishedSellerOnBoarding should save the onboarding of seller as true`() {
        // given
        every { cacheManager.saveCacheBoolean("key_onboarding_seller", true) } just runs

        // when
        viewModel.markFinishedSellerOnBoarding()

        // then
        verify(exactly = 1) { cacheManager.saveCacheBoolean(any(), any()) }
    }

    @Test
    fun `hasBeenVisited should save as true the state of page visited`() {
        // given
        every { cacheManager.loadCacheBoolean(any()) } returns true

        // when
        val actualValue = viewModel.hasBeenVisited()

        // then
        assertEquals(true, actualValue)
    }

    @Test
    fun `hasBeenVisited should save as false the state of page visited`() {
        // given
        every { cacheManager.loadCacheBoolean(any()) } returns false

        // when
        val actualValue = viewModel.hasBeenVisited()

        // then
        assertEquals(false, actualValue)
    }

    @Test
    fun `hasBeenVisited return false if null`() {
        // given
        every { cacheManager.loadCacheBoolean(any()) } returns null

        // when
        val actualValue = viewModel.hasBeenVisited()

        // then
        assertEquals(false, actualValue)
    }

    @Test
    fun `markAsVisited should marking the page as visited`() {
        // given
        every { cacheManager.saveCacheBoolean(any(), true) } just runs

        // when
        viewModel.markAsVisited()

        // then
        verify(exactly = 1) { cacheManager.saveCacheBoolean(any(), any()) }
    }

    private fun hasShownGivenHelper(
            hashShop: Boolean,
            cacheOnBoardingSeller: Boolean?,
            cacheOnBoardingBuyer: Boolean?
    ) {
        every { userSession.hasShop() } returns hashShop
        every { cacheManager.loadCacheBoolean(
                InboxViewModel.KEY_ONBOARDING_SELLER)
        } returns cacheOnBoardingSeller
        every {
            cacheManager.loadCacheBoolean(InboxViewModel.KEY_ONBOARDING_BUYER)
        } returns cacheOnBoardingBuyer
    }

    companion object {
        val inboxCounter: InboxNotificationResponse = FileUtil.parse(
                "/success_get_inbox_data.json",
                InboxNotificationResponse::class.java
        )
    }

}