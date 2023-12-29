package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class NotificationDeleteRemainderViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `deleteReminder should return correctly`() {
        runBlocking {
            // given
            val expectedValue = Resource.success(deleteReminderResponse)
            val flow = flow { emit(expectedValue) }

            coEvery {
                deleteReminderUseCase(any())
            } returns flow

            // when
            viewModel.deleteReminder(ProductData(), NotificationUiModel())

            // then
            Assert.assertEquals(
                expectedValue,
                viewModel.deleteReminder.value
            )
        }
    }

    @Test
    fun `deleteReminder should throw the Fail state`() {
        runBlocking {
            // given
            val expectedValue = Resource.error(Throwable(), null)
            val flow = flow { emit(expectedValue) }

            coEvery {
                deleteReminderUseCase(any())
            } returns flow

            // when
            viewModel.deleteReminder(ProductData(), NotificationUiModel())

            // then
            Assert.assertEquals(
                expectedValue,
                viewModel.deleteReminder.value
            )
        }
    }

    @Test
    fun `deleteReminder should throw throwable when error`() {
        runBlocking {
            // given
            val expectedThrowable = Throwable("Oops!")

            coEvery {
                deleteReminderUseCase(any())
            } throws expectedThrowable

            // when
            viewModel.deleteReminder(ProductData(), NotificationUiModel())

            // then
            Assert.assertEquals(
                expectedThrowable.message,
                (viewModel.deleteReminder.value as Resource).throwable?.message
            )
        }
    }
}
