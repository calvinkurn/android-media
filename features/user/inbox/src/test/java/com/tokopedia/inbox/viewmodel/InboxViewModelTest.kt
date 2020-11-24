package com.tokopedia.inbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.inbox.common.TestInboxCoroutineDispatcher
import com.tokopedia.inbox.domain.data.notification.InboxCounter
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.inbox.domain.usecase.InboxNotificationUseCase
import com.tokopedia.inbox.viewmodel.util.FileUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class InboxViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val useCase: InboxNotificationUseCase = mockk(relaxed = true)
    private val dispatcher = TestInboxCoroutineDispatcher()
    private val viewModel = InboxViewModel(useCase, dispatcher)

    private val notificationObserver: Observer<Result<InboxCounter>> = mockk(relaxed = true)

    @Test
    fun `test get data inbox properly`() {
        // given
        val expectedValue = successGetInboxData?.notifications?.inboxCounter

        viewModel.notifications.observeForever(notificationObserver)

        every { useCase.getNotification(captureLambda(), any()) } answers {
            val onSuccess = lambda<(InboxCounter) -> Unit>()
            expectedValue?.let { onSuccess.invoke(it) }
        }

        // when
        viewModel.getNotifications()

        // then
        verify(exactly = 1) {
            expectedValue?.let {
                notificationObserver.onChanged(Success(it))
            }
        }
    }

    @Test
    fun `test get throwable inbox notification`() {
        // given
        val expectedValue = Throwable()

        viewModel.notifications.observeForever(notificationObserver)

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
        val successGetInboxData = FileUtil.parse<InboxNotificationResponse>(
                "/success_get_inbox_data.json",
                InboxNotificationResponse::class.java
        )
    }

}