package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class NotificationFilterViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `hasFilter should return true if filter is not FILTER_NONE`() {
        // given
        val expectedValue = true
        viewModel.filter = -1

        // when
        val hasFilter = viewModel.hasFilter()

        assert(hasFilter == expectedValue)
    }

    @Test
    fun `loadNotificationFilter should throw the Resource error state`() {
        runBlocking {
            // given
            val expectedValue = Resource.error(Throwable(), null)
            val flow = flow { emit(expectedValue) }
            coEvery {
                notifcenterFilterUseCase(any())
            } returns flow

            // when
            viewModel.loadNotificationFilter(0)

            // then
            Assert.assertEquals(
                expectedValue,
                viewModel.filterList.value
            )
        }
    }

    @Test
    fun `loadNotificationFilter should throw throwable when error`() {
        runBlocking {
            // given
            val expectedThrowable = Throwable("Oops!")
            coEvery {
                notifcenterFilterUseCase(any())
            } throws expectedThrowable

            // when
            viewModel.loadNotificationFilter(0)

            // then
            Assert.assertEquals(
                expectedThrowable.message,
                (viewModel.filterList.value as Resource).throwable?.message
            )
        }
    }
}
