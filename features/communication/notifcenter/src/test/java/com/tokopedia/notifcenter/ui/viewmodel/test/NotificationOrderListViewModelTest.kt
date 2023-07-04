package com.tokopedia.notifcenter.ui.viewmodel.test

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.ui.viewmodel.base.NotificationViewModelTestFixture
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Test

class NotificationOrderListViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `loadNotifOrderList verify no interaction if role is null`() {
        // when
        viewModel.loadNotifOrderList(null)

        // then
        verify(exactly = 0) {
            notifOrderListUseCase.getOrderList(any())
        }
    }

    @Test
    fun `loadNotifOrderList propagate success data`() {
        // given
        val role = RoleType.BUYER
        val expectedValue = Resource.success(notifOrderListResponse)
        val flow = flow { emit(expectedValue) }

        every { notifOrderListUseCase.getOrderList(role) } returns flow

        // when
        viewModel.loadNotifOrderList(role)

        // then
        verify {
            notifOrderListUseCase.getOrderList(role)
        }
        Assert.assertEquals(
            expectedValue,
            viewModel.orderList.value
        )
    }

    @Test
    fun `loadNotifOrderList propagate error data`() {
        // given
        val role = RoleType.BUYER
        val throwable: Throwable = IllegalStateException()
        every { notifOrderListUseCase.getOrderList(role) } throws throwable

        // when
        viewModel.loadNotifOrderList(role)

        // then
        verify {
            notifOrderListUseCase.getOrderList(role)
        }
        Assert.assertEquals(
            throwable,
            viewModel.orderList.value?.throwable
        )
        MatcherAssert.assertThat(viewModel.orderList.value?.throwable, CoreMatchers.`is`(throwable))
    }
}
