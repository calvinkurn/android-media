package com.tokopedia.seller.menu.presentation.viewmodel

import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.list.data.model.filter.Tab
import com.tokopedia.seller.menu.common.view.uimodel.ShopOrderUiModel
import com.tokopedia.seller.menu.common.view.uimodel.ShopProductUiModel
import com.tokopedia.seller.menu.presentation.uimodel.NotificationUiModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.SocketTimeoutException

@ExperimentalCoroutinesApi
class SellerMenuViewModelTest : SellerMenuViewModelTestFixture() {

    @Test
    fun `when getAllSettingShopInfo success should set live data success`() {
        val shopScoreResponse = ShopScoreResult()
        val shopSettingsResponse = createShopSettingsResponse()

        onGetAllShopInfoUseCase_thenReturn(shopSettingsResponse)
        onGetShopScore_thenReturn(shopScoreResponse)

        viewModel.getAllSettingShopInfo()

        val expectedResult = createShopInfoUiModel()
        val actualResult = (viewModel.settingShopInfoLiveData.value as Success).data

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getProductCount success should set live data success`() {
        val tabs = listOf(
            Tab(ProductStatus.ACTIVE.name, "20"),
            Tab(ProductStatus.INACTIVE.name, "20"),
            Tab(ProductStatus.VIOLATION.name, "20")
        )

        onGetProductListMeta_thenReturn(tabs)

        viewModel.getProductCount()

        val expectedResult = ShopProductUiModel(60)
        val actualResult = (viewModel.shopProductLiveData.value as Success).data

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getProductCount error should set live data fail`() {
        val error = SocketTimeoutException()

        onGetProductListMeta_thenReturn(error)

        viewModel.getProductCount()

        val expectedResult = SocketTimeoutException::class.java
        val actualResult = (viewModel.shopProductLiveData.value as Fail).throwable::class.java

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getNotifications success should set live data success`() {
        val response = createNotificationResponse(
            newOrder = 1,
            readyToShip = 2,
            totalUnread = 4,
            talk = 1,
            inResolution = 1
        )

        onGetNotifications_thenReturn(response)

        viewModel.getNotifications()

        val shopOrderUiModel = ShopOrderUiModel(newOrderCount = 1, readyToShip = 2)
        val expectedResult = NotificationUiModel(inboxTalkUnread = 1, notifCenterTotalUnread = 4, order = shopOrderUiModel, resolutionCount = 1)
        val actualResult = (viewModel.sellerMenuNotification.value as Success).data

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getNotifications error should set live data fail`() {
        val error = SocketTimeoutException()

        onGetNotifications_thenReturn(error)

        viewModel.getNotifications()

        val expectedResult = SocketTimeoutException::class.java
        val actualResult = (viewModel.sellerMenuNotification.value as Fail).throwable::class.java

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given getAllShopInfo error when getAllSettingShopInfo should set live data fail`() {
        val error = NullPointerException()

        onGetAllShopInfoUseCase_thenReturn(error)

        viewModel.getAllSettingShopInfo()

        val expectedResult = NullPointerException::class.java
        val actualResult = (viewModel.settingShopInfoLiveData.value as Fail).throwable::class.java

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given getAllShopInfo returns PartialSettingFail when getAllSettingShopInfo should set live data with MessageErrorException`() {
        val error = MessageErrorException("seller menu shop info and topads failed")

        onGetAllShopInfoUseCase_thenReturn(error)

        viewModel.getAllSettingShopInfo()

        val expectedResult = MessageErrorException::class.java
        val actualResult = (viewModel.settingShopInfoLiveData.value as Fail).throwable::class.java

        val expectedMessage = "seller menu shop info and topads failed"
        val actualMessage = (viewModel.settingShopInfoLiveData.value as Fail).throwable.message

        assertEquals(expectedResult, actualResult)
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun `given getShopScore error when getAllSettingShopInfo should set live data fail`() {
        val error = IllegalStateException()
        val shopSettingsResponse = createShopSettingsResponse()

        onGetShopScore_thenReturn(error)
        onGetAllShopInfoUseCase_thenReturn(shopSettingsResponse)

        viewModel.getAllSettingShopInfo()

        val expectedResult = IllegalStateException::class.java
        val actualResult = (viewModel.settingShopInfoLiveData.value as Fail).throwable::class.java

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given isToasterRetry true when getAllSettingShopInfo should set isToasterAlreadyShown true`() {
        coroutineTestRule.runBlockingTest {
            val isToasterRetry = true

            viewModel.getAllSettingShopInfo(isToasterRetry)
            viewModel.getAllSettingShopInfo(isToasterRetry)

            val expectedIsToasterAlreadyShown = true
            val actualIsToasterAlreadyShown = viewModel.isToasterAlreadyShown.value

            assertEquals(expectedIsToasterAlreadyShown, actualIsToasterAlreadyShown)
        }
    }

    @Test
    fun `given isToasterRetry false when getAllSettingShopInfo should NOT set isToasterAlreadyShown true`() {
        val isToasterRetry = false

        viewModel.getAllSettingShopInfo(isToasterRetry)

        val expectedIsToasterAlreadyShown = false
        val actualIsToasterAlreadyShown = viewModel.isToasterAlreadyShown.value

        assertEquals(expectedIsToasterAlreadyShown, actualIsToasterAlreadyShown)
    }

    @Test
    fun `given isToasterRetry true when getAllSettingShopInfo should set isToasterAlreadyShown false`() {
        coroutineTestRule.runBlockingTest {
            val isToasterRetry = true

            viewModel.getAllSettingShopInfo(isToasterRetry)

            advanceTimeBy(5000L)

            val expectedIsToasterAlreadyShown = false
            val actualIsToasterAlreadyShown = viewModel.isToasterAlreadyShown.value

            assertEquals(expectedIsToasterAlreadyShown, actualIsToasterAlreadyShown)
        }
    }
}