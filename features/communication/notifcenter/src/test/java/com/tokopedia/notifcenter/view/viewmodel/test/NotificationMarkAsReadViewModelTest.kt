package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class NotificationMarkAsReadViewModelTest : NotificationViewModelTestFixture() {

    @Test
    fun `markNotificationAsRead should called markAsRead() properly`() {
        // given
        val role = RoleType.BUYER
        val element = NotificationUiModel()

        // when
        viewModel.markNotificationAsRead(role, element)

        // then
        coVerify(exactly = 1) {
            markAsReadUseCase(any())
        }
    }

    @Test
    fun `markNotificationAsRead should do nothing when error`() {
        // given
        val expectedThrowable = Throwable("Oops!")
        val role = RoleType.BUYER
        val element = NotificationUiModel()
        coEvery { markAsReadUseCase(any()) } throws expectedThrowable

        // when
        viewModel.markNotificationAsRead(role, element)

        // then
        coVerify(exactly = 1) {
            markAsReadUseCase(any())
        }
    }
}
