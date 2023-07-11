package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.notifcenter.data.entity.NotificationResponse
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class NotificationGetNotificationsViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun should_give_notification_response() {
        runBlocking {
            // Given
            val expected = NotificationResponse()
            coEvery {
                getNotificationCounterUseCase(any())
            } returns expected

            // When
            viewModel.getNotifications("testShopId")

            // Then
            Assert.assertEquals(
                expected.notifications,
                (viewModel.notifications.value as Success).data
            )
        }
    }

    @Test
    fun should_give_error_when_fail_get_notifications() {
        runBlocking {
            // Given
            val expected = Throwable("Oops!")
            coEvery {
                getNotificationCounterUseCase(any())
            } throws expected

            // When
            viewModel.getNotifications("testShopId")

            // Then
            Assert.assertEquals(
                expected,
                (viewModel.notifications.value as Fail).throwable
            )
        }
    }
}
