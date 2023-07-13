package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.view.NotificationViewModel.Companion.CLEAR_ALL_NOTIF_TYPE
import com.tokopedia.notifcenter.view.NotificationViewModel.Companion.DEFAULT_SHOP_ID
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test

class NotificationClearNotifViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `clearNotifCounter propagate success data to liveData`() {
        // Given
        val role = RoleType.BUYER
        val expectedValue = Resource.success(clearNotifCounterResponse)
        val flow = flow { emit(expectedValue) }
        coEvery {
            clearNotifUseCase(role)
        } returns flow

        // when
        viewModel.clearNotifCounter(role)

        // then
        Assert.assertEquals(
            expectedValue,
            viewModel.clearNotif.value
        )
    }

    @Test
    fun `clearNotifCounter propagate success data to liveData for affiliate`() {
        // Given
        val role = RoleType.AFFILIATE
        val expectedValue = Resource.success(clearNotifCounterResponse)
        val flow = flow { emit(expectedValue) }
        coEvery {
            clearNotifUseCase(role)
        } returns flow

        // when
        viewModel.clearNotifCounter(role)

        // then
        Assert.assertEquals(
            expectedValue,
            viewModel.clearNotif.value
        )
    }

    @Test
    fun clearNotifCounter_reset_type_zero_when_user_does_not_have_shop() {
        // Given
        val role = RoleType.BUYER
        val expectedValue = Resource.success(clearNotifCounterResponse)
        val flow = flow { emit(expectedValue) }
        coEvery {
            clearNotifUseCase(CLEAR_ALL_NOTIF_TYPE)
        } returns flow
        every { userSessionInterface.shopId } returns DEFAULT_SHOP_ID

        // when
        viewModel.clearNotifCounter(role)

        // then
        Assert.assertEquals(
            expectedValue,
            viewModel.clearNotif.value
        )
    }

    @Test
    fun clearNotifCounter_when_user_does_not_have_shop_and_is_affiliate() {
        // Given
        val role = RoleType.AFFILIATE
        val expectedValue = Resource.success(clearNotifCounterResponse)
        val flow = flow { emit(expectedValue) }
        coEvery {
            clearNotifUseCase(role)
        } returns flow
        every { userSessionInterface.shopId } returns DEFAULT_SHOP_ID

        // when
        viewModel.clearNotifCounter(role)

        // then
        Assert.assertEquals(
            expectedValue,
            viewModel.clearNotif.value
        )
    }

    @Test
    fun `clearNotifCounter_should_give_null_when_error`() {
        // Given
        val role = RoleType.BUYER
        val errorResponse = Throwable("Oops!")
        coEvery {
            clearNotifUseCase(role)
        } throws errorResponse

        // when
        viewModel.clearNotifCounter(role)

        // then
        Assert.assertEquals(
            null,
            viewModel.clearNotif.value
        )
    }
}
