package com.tokopedia.inbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.inbox.common.TestInboxCoroutineDispatcher
import com.tokopedia.inbox.domain.cache.InboxCacheManager
import com.tokopedia.inbox.domain.data.notification.InboxCounter
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.inbox.domain.data.notification.Notifications
import com.tokopedia.inbox.domain.usecase.InboxNotificationUseCase
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    companion object {
        val inboxCounter: InboxNotificationResponse = FileUtil.parse(
                "/success_get_inbox_data.json",
                InboxNotificationResponse::class.java
        )
    }

}