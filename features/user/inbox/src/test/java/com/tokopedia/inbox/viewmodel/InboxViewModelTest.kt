package com.tokopedia.inbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.inbox.common.TestInboxCoroutineDispatcher
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

    @get:Rule val rule = InstantTaskExecutorRule()

    private val useCase: InboxNotificationUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val cacheManager: InboxCacheManager = mockk(relaxed = true)
    private val dispatcher = TestInboxCoroutineDispatcher()

    private val notificationObserver: Observer<Result<Notifications>> = mockk(relaxed = true)

    private val viewModel = InboxViewModel(useCase, userSession, cacheManager, dispatcher)

    @Before fun setUp() {
        viewModel.notifications.observeForever(notificationObserver)
    }

    @Test fun `getNotifications should return the data correctly`() {
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

    @Test fun `getNotifications should throw the Fail state`() {
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

    @Test fun `hasShowOnBoarding should return hasShownBuyer`() {
        // given
        every { userSession.hasShop() } returns false
        every { cacheManager.loadCacheBoolean("key_onboarding_seller") } returns false
        every { cacheManager.loadCacheBoolean("key_onboarding_buyer") } returns true

        // when
        val actualValue = viewModel.hasShowOnBoarding()

        // then
        assertEquals(true, actualValue)
    }

    @Test fun `hasShowOnBoarding should return hasShownSeller`() {
        // given
        every { userSession.hasShop() } returns true
        every { cacheManager.loadCacheBoolean("key_onboarding_seller") } returns true
        every { cacheManager.loadCacheBoolean("key_onboarding_buyer") } returns false

        // when
        val actualValue = viewModel.hasShowOnBoarding()

        // then
        assertEquals(true, actualValue)
    }

    @Test fun `markFinishedBuyerOnBoarding should save the onboarding of buyer as true`() {
        // given
        every { cacheManager.saveCacheBoolean("key_onboarding_buyer", true) } just runs

        // when
        viewModel.markFinishedBuyerOnBoarding()

        // then
        verify(exactly = 1) { cacheManager.saveCacheBoolean(any(), any()) }
    }

    @Test fun `markFinishedSellerOnBoarding should save the onboarding of seller as true`() {
        // given
        every { cacheManager.saveCacheBoolean("key_onboarding_seller", true) } just runs

        // when
        viewModel.markFinishedSellerOnBoarding()

        // then
        verify(exactly = 1) { cacheManager.saveCacheBoolean(any(), any()) }
    }

    @Test fun `hasBeenVisited should save as true the state of page visited`() {
        // given
        every { cacheManager.loadCacheBoolean(any()) } returns true

        // when
        val actualValue = viewModel.hasBeenVisited()

        // then
        assertEquals(true, actualValue)
    }

    @Test fun `hasBeenVisited should save as false the state of page visited`() {
        // given
        every { cacheManager.loadCacheBoolean(any()) } returns false

        // when
        val actualValue = viewModel.hasBeenVisited()

        // then
        assertEquals(false, actualValue)
    }

    @Test fun `markAsVisited should marking the page as visited`() {
        // given
        every { cacheManager.saveCacheBoolean(any(), true) } just runs

        // when
        viewModel.markAsVisited()

        // then
        verify(exactly = 1) { cacheManager.saveCacheBoolean(any(), any()) }
    }

    companion object {
        val inboxCounter: InboxNotificationResponse = FileUtil.parse(
                "/success_get_inbox_data.json",
                InboxNotificationResponse::class.java
        )
    }

}