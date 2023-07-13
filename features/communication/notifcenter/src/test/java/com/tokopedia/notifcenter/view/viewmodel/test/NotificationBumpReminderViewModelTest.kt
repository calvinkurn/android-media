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

class NotificationBumpReminderViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `bumpReminder should return correctly`() {
        runBlocking {
            // given
            val expectedValue = Resource.success(bumpReminderResponse)
            val flow = flow { emit(expectedValue) }

            coEvery {
                bumpReminderUseCase(any())
            } returns flow

            // when
            viewModel.bumpReminder(ProductData(), NotificationUiModel())

            // then
            Assert.assertEquals(
                expectedValue,
                viewModel.bumpReminder.value
            )
        }
    }

    @Test
    fun `bumpReminder should throw the Fail state`() {
        runBlocking {
            // given
            val expectedValue = Resource.error(Throwable(), null)
            val flow = flow { emit(expectedValue) }

            coEvery {
                bumpReminderUseCase(any())
            } returns flow

            // when
            viewModel.bumpReminder(ProductData(), NotificationUiModel())

            // then
            Assert.assertEquals(
                expectedValue,
                viewModel.bumpReminder.value
            )
        }
    }

    @Test
    fun `bumpReminder should throw when error`() {
        runBlocking {
            // given
            val expectedThrowable = Throwable("Oops!")

            coEvery {
                bumpReminderUseCase(any())
            } throws expectedThrowable

            // when
            viewModel.bumpReminder(ProductData(), NotificationUiModel())

            // then
            Assert.assertEquals(
                expectedThrowable.message,
                (viewModel.bumpReminder.value as Resource).throwable?.message
            )
        }
    }
}
