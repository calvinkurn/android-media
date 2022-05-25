package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop_settings.common.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopSettingsOperationalHoursViewModelTest : ShopSettingsOperationalHoursViewModelTestFixture() {

    @Test
    fun `getOperationalHoursInitialData should return Success`() {
        coEvery {
            getShopCloseDetailInfoUseCase.get().executeOnBackground()
        } returns ShopInfo(
                closedInfo = ShopInfo.ClosedInfo(),
                statusInfo = ShopInfo.StatusInfo()
        )

        coEvery {
            gqlGetShopOperationalHoursListUseCase.executeOnBackground()
        } returns ShopOperationalHoursListResponse()

        shopSettingsOperationalHoursViewModel.getShopOperationalHoursInitialData("123")

        coVerify { getShopCloseDetailInfoUseCase.get().executeOnBackground() }
        coVerify(timeout = 1000) { gqlGetShopOperationalHoursListUseCase.executeOnBackground() }

        assertTrue(shopSettingsOperationalHoursViewModel.shopSettingsOperationalHoursListUiModel.value is Success)
    }

    @Test
    fun `getOperationalHoursInitialData should return Fail`() {
        coEvery {
            getShopCloseDetailInfoUseCase.get().executeOnBackground()
        } returns ShopInfo(
                closedInfo = ShopInfo.ClosedInfo(),
                statusInfo = ShopInfo.StatusInfo()
        )

        coEvery {
            gqlGetShopOperationalHoursListUseCase.executeOnBackground()
        } throws Exception()

        shopSettingsOperationalHoursViewModel.getShopOperationalHoursInitialData("123")

        coVerify { getShopCloseDetailInfoUseCase.get().executeOnBackground() }
        coVerify(timeout = 1000) { gqlGetShopOperationalHoursListUseCase.executeOnBackground() }

        assertTrue(shopSettingsOperationalHoursViewModel.shopSettingsOperationalHoursListUiModel.value is Fail)
    }

    @Test
    fun `getOperationalHoursInitialData get shop info should return Fail`() {
        coEvery {
            getShopCloseDetailInfoUseCase.get().executeOnBackground()
        } throws Exception()
        shopSettingsOperationalHoursViewModel.getShopOperationalHoursInitialData("123")
        assertTrue(shopSettingsOperationalHoursViewModel.shopSettingsOperationalHoursListUiModel.value == null)
    }

    @Test
    fun `getOperationalHoursInitialData get operational hours list should return null data`() {
        coEvery {
            getShopCloseDetailInfoUseCase.get().executeOnBackground()
        } returns ShopInfo(
                closedInfo = ShopInfo.ClosedInfo(),
                statusInfo = ShopInfo.StatusInfo()
        )
        coEvery {
            gqlGetShopOperationalHoursListUseCase.executeOnBackground()
        } returns ShopOperationalHoursListResponse(
                getShopOperationalHoursList = null
        )
        shopSettingsOperationalHoursViewModel.getShopOperationalHoursInitialData("123")
    }

    @Test
    fun `setShopCloseSchedule action close should return Success`() {
        val actionClose = ShopScheduleActionDef.CLOSED
        val closeNow = true
        val closeStart = "123" // unix time string
        val closeEnd = "456" // unix time string
        val closeNote = ""

        every {
            updateShopScheduleUseCase.getData(any())
        } returns ""

        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(
                actionClose,
                closeNow,
                closeStart,
                closeEnd,
                closeNote
        )
        assertTrue(shopSettingsOperationalHoursViewModel.shopInfoCloseSchedule.observeAwaitValue() is Success)
    }

    @Test
    fun `setShopCloseSchedule action close and use default param value should return Success`() {
        val actionClose = ShopScheduleActionDef.CLOSED

        every {
            updateShopScheduleUseCase.getData(any())
        } returns ""

        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(actionClose)
        assertTrue(shopSettingsOperationalHoursViewModel.shopInfoCloseSchedule.observeAwaitValue() is Success)
    }

    @Test
    fun `setShopCloseSchedule action close should return Fail`() {
        val actionClose = ShopScheduleActionDef.CLOSED
        val closeNow = true
        val closeStart = "123" // unix time string
        val closeEnd = "456" // unix time string
        val closeNote = ""

        every {
            updateShopScheduleUseCase.getData(any())
        } throws Exception()

        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(
                actionClose,
                closeNow,
                closeStart,
                closeEnd,
                closeNote
        )
        assertTrue(shopSettingsOperationalHoursViewModel.shopInfoCloseSchedule.observeAwaitValue() is Fail)
    }

    @Test
    fun `setShopCloseSchedule action abort should return Success`() {
        val actionAbort = ShopScheduleActionDef.ABORT
        val closeNow = true
        val closeStart = "123" // unix time string
        val closeEnd = "456" // unix time string
        val closeNote = ""

        every {
            updateShopScheduleUseCase.getData(any())
        } returns ""

        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(
                actionAbort,
                closeNow,
                closeStart,
                closeEnd,
                closeNote
        )
        assertTrue(shopSettingsOperationalHoursViewModel.shopInfoAbortSchedule.observeAwaitValue() is Success)
    }

    @Test
    fun `setShopCloseSchedule action abort should return Fail`() {
        val actionClose = ShopScheduleActionDef.ABORT
        val closeNow = true
        val closeStart = "123" // unix time string
        val closeEnd = "456" // unix time string
        val closeNote = ""

        every {
            updateShopScheduleUseCase.getData(any())
        } throws Exception()

        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(
                actionClose,
                closeNow,
                closeStart,
                closeEnd,
                closeNote
        )
        assertTrue(shopSettingsOperationalHoursViewModel.shopInfoAbortSchedule.observeAwaitValue() is Fail)
    }
}