package com.tokopedia.notifcenter.ui.viewmodel.test

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.ui.viewmodel.base.NotificationViewModelTestFixture
import io.mockk.every
import io.mockk.verify
import org.junit.Test

class NotificationMarkAsReadViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `markNotificationAsRead verify haven't interaction`() {
        // given
        val element = NotificationUiModel()

        // when
        viewModel.markNotificationAsRead(null, element)

        // then
        verify(exactly = 0) { markAsReadUseCase.markAsRead(any(), any()) }
    }

    @Test
    fun `markNotificationAsRead should called markAsRead() properly`() {
        // given
        val role = RoleType.BUYER
        val element = NotificationUiModel()

        // when
        viewModel.markNotificationAsRead(role, element)

        // then
        verify(exactly = 1) { markAsReadUseCase.markAsRead(role, "") }
    }

    @Test
    fun `markNotificationAsRead should do nothing when error`() {
        // given
        val expectedThrowable = Throwable("Oops!")
        val role = RoleType.BUYER
        val element = NotificationUiModel()
        every { markAsReadUseCase.markAsRead(any(), any()) } throws expectedThrowable

        // when
        viewModel.markNotificationAsRead(role, element)

        // then
        verify(exactly = 1) { markAsReadUseCase.markAsRead(role, element.notifId) }
    }
}
