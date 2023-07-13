package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Test

class NotificationOrderListViewModelTest : NotificationViewModelTestFixture() {

    @Test
    fun `loadNotifOrderList propagate success data`() {
        // given
        val role = RoleType.BUYER
        val expectedValue = Resource.success(notifOrderListResponse)
        val flow = flow { emit(expectedValue) }

        coEvery {
            notifOrderListUseCase(role)
        } returns flow

        // when
        viewModel.loadNotifOrderList(role)

        // then
        coVerify {
            notifOrderListUseCase(role)
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
        coEvery {
            notifOrderListUseCase(role)
        } throws throwable

        // when
        viewModel.loadNotifOrderList(role)

        // then
        coVerify {
            notifOrderListUseCase(role)
        }
        Assert.assertEquals(
            throwable,
            viewModel.orderList.value?.throwable
        )
        MatcherAssert.assertThat(viewModel.orderList.value?.throwable, CoreMatchers.`is`(throwable))
    }
}
