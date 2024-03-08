package com.tokopedia.seller.menu.presentation.viewmodel

import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.list.data.model.filter.Tab
import com.tokopedia.seller.menu.domain.query.ShopScoreLevelResponse
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuProductUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState
import com.tokopedia.seller.menu.presentation.util.SellerMenuComposeList
import com.tokopedia.seller.menu.presentation.util.SellerMenuComposeUiMapper
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class SellerMenuComposeViewModelTest : SellerMenuComposeViewModelTestFixture() {

    @Test
    fun `when getShopAccountInfo success, should emit success event`() = runTest {
        val shopInfoPeriodResponse = ShopInfoPeriodUiModel()

        onGetShopInfoPeriodUseCase_thenReturn(shopInfoPeriodResponse)

        viewModel.getShopAccountInfo()

        advanceUntilIdle()

        val actualResult = viewModel.uiEvent.value

        assert(actualResult is SellerMenuUIEvent.OnSuccessGetShopInfo)
    }

    @Test
    fun `when getShopAccountInfo failed, should emit failed state`() = runTest {
        onGetGetShopInfoPeriodUseCase_thenReturnError(MessageErrorException())

        viewModel.getShopAccountInfo()

        advanceUntilIdle()

        val actualResult = viewModel.uiState.value

        assert(actualResult is SellerMenuUIState.OnFailedGetMenuList)
    }

    @Test
    fun `when onEvent GetInitialMenu, should emit success initial state`() = runTest {
        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)

        advanceUntilIdle()

        val actualResult = (viewModel.uiState.value as? SellerMenuUIState.OnSuccessGetMenuList)

        assert(actualResult?.isInitialValue == true)
        assert(actualResult?.visitableList == SellerMenuComposeList.createInitialItems())
    }

    @Test
    fun `when onEvent OnRefresh, should update isRefreshing`() = runTest {
        val results = mutableListOf<Boolean>()
        val job = launch {
            viewModel.isRefreshing.toList(results)
        }
        viewModel.onEvent(SellerMenuUIEvent.OnRefresh)

        advanceUntilIdle()

        assertEquals(false, results[0])
        assertEquals(true, results[1])

        job.cancel()
    }

    @Test
    fun `when onEvent OnRefresh, if current menu exist, should also show also set value that is not an initial list`() = runTest {
        getAllSettingShopInfoSuccess()

        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)
        viewModel.getAllSettingShopInfo(false)
        viewModel.onEvent(SellerMenuUIEvent.OnRefresh)

        advanceUntilIdle()

        val actualResult = (viewModel.uiState.value as? SellerMenuUIState.OnSuccessGetMenuList)?.isInitialValue
        assertEquals(false, actualResult)
    }

    @Test
    fun `when onEvent Idle, should not do anything`() = runTest {
        val currentValue = viewModel.uiState.value
        viewModel.onEvent(SellerMenuUIEvent.Idle)

        advanceUntilIdle()

        assertEquals(currentValue, viewModel.uiState.value)
    }

    @Test
    fun `when getAllSettingShopInfo success, should emit success loaded state`() = runTest {
        getAllSettingShopInfoSuccess()

        viewModel.getAllSettingShopInfo(false)

        advanceUntilIdle()

        assert(viewModel.uiState.value is SellerMenuUIState.OnSuccessGetMenuList)
    }

    @Test
    fun `when getAllSettingShopInfo failed, should emit failed loaded state`() = runTest {
        val exception = MessageErrorException()
        onGetAllShopInfoUseCase_thenReturn(exception)
        every {
            userSession.shopId
        } returns "123"
        onGetShopScoreLevel_thenReturn("123", MessageErrorException())

        viewModel.getAllSettingShopInfo(false)

        advanceUntilIdle()

        val actualResult = (viewModel.uiState.value as? SellerMenuUIState.OnFailedGetMenuList)

        assert(actualResult is SellerMenuUIState.OnFailedGetMenuList)
    }

    @Test
    fun `when getAllSettingShopInfo success, but both info type failed, should emit failed loaded state`() = runTest {
        val shopScoreResponse = ShopScoreLevelResponse.ShopScoreLevel.Result(shopScore = 70)
        val shopSettingsResponse = createShopSettingsResponse(
            successPair = false to false
        )

        val shopId = "123"

        onGetShopId_thenReturn(shopId)
        onGetAllShopInfoUseCase_thenReturn(shopSettingsResponse)
        onGetShopScoreLevel_thenReturn(shopId, shopScoreResponse)

        viewModel.getAllSettingShopInfo(false)

        advanceUntilIdle()

        assert(viewModel.uiState.value is SellerMenuUIState.OnFailedGetMenuList)
    }

    @Test
    fun `when getAllSettingShopInfo success, but first info type failed, should still emit success loaded state`() = runTest {
        val shopScoreResponse = ShopScoreLevelResponse.ShopScoreLevel.Result(shopScore = 70)
        val shopSettingsResponse = createShopSettingsResponse(
            successPair = true to false
        )

        val shopId = "123"

        onGetShopId_thenReturn(shopId)
        onGetAllShopInfoUseCase_thenReturn(shopSettingsResponse)
        onGetShopScoreLevel_thenReturn(shopId, shopScoreResponse)

        viewModel.getAllSettingShopInfo(false)

        advanceUntilIdle()

        assert(viewModel.uiState.value is SellerMenuUIState.OnSuccessGetMenuList)
    }

    @Test
    fun `when getAllSettingShopInfo success, but second info type failed, should still emit success loaded state`() = runTest {
        val shopScoreResponse = ShopScoreLevelResponse.ShopScoreLevel.Result(shopScore = 70)
        val shopSettingsResponse = createShopSettingsResponse(
            successPair = false to true
        )

        val shopId = "123"

        onGetShopId_thenReturn(shopId)
        onGetAllShopInfoUseCase_thenReturn(shopSettingsResponse)
        onGetShopScoreLevel_thenReturn(shopId, shopScoreResponse)

        viewModel.getAllSettingShopInfo(false)

        advanceUntilIdle()

        assert(viewModel.uiState.value is SellerMenuUIState.OnSuccessGetMenuList)
    }

    @Test
    fun `when getAllSettingShopInfo from toaster retry, should update delay response trigger`() = runTest {
        val results = mutableListOf<Boolean?>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.isToasterAlreadyShown.toList(results)
        }

        viewModel.getAllSettingShopInfo(true)

        assertEquals(false, results[0])

        advanceTimeBy(4900L)
        assertEquals(true, results[1])

        advanceTimeBy(5000L)
        assertEquals(false, results[2])

        job.cancel()
    }

    @Test
    fun `when getAllSettingShopInfo from toaster retry, but toaster already shown, should not update delay response trigger`() = runTest {
        val results = mutableListOf<Boolean?>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.isToasterAlreadyShown.toList(results)
        }

        viewModel.getAllSettingShopInfo(true)

        advanceTimeBy(4900L)

        viewModel.getAllSettingShopInfo(true)

        advanceTimeBy(5000L)

        assertEquals(3, results.size)

        job.cancel()
    }

    @Test
    fun `when getProductCount success, should update product section`() = runTest {
        val tabs = listOf(
            Tab()
        )
        onGetProductListMeta_thenReturn(tabs)
        getAllSettingShopInfoSuccess()

        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)
        viewModel.onEvent(SellerMenuUIEvent.GetShopInfo)

        advanceUntilIdle()

        val actualResult =
            (viewModel.uiState.value as? SellerMenuUIState.OnSuccessGetMenuList)?.visitableList?.firstOrNull { it is SellerMenuProductUiModel } as? SellerMenuProductUiModel
        assertEquals(SellerMenuComposeUiMapper.getProductCount(tabs), actualResult?.count)
    }

    @Test
    fun `when getProductCount failed, should do nothing`() = runTest {
        onGetProductListMeta_thenReturn(MessageErrorException())
        getAllSettingShopInfoSuccess()

        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)
        viewModel.onEvent(SellerMenuUIEvent.GetShopInfo)

        advanceUntilIdle()

        val actualResult =
            (viewModel.uiState.value as? SellerMenuUIState.OnSuccessGetMenuList)?.visitableList?.firstOrNull { it is SellerMenuProductUiModel } as? SellerMenuProductUiModel
        assertEquals(Int.ZERO, actualResult?.count)
    }

    @Test
    fun `when getNotifications, should update notification section`() = runTest {
        val newOrder = 1
        val readyToShip = 2
        val totalUnread = 3
        val talk = 4
        val inResolution = 5
        onGetNotifications_thenReturn(createNotificationResponse(newOrder, readyToShip, totalUnread, talk, inResolution))
        getAllSettingShopInfoSuccess()

        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)
        viewModel.onEvent(SellerMenuUIEvent.GetShopInfo)

        advanceUntilIdle()

        val actualResult =
            (viewModel.uiState.value as? SellerMenuUIState.OnSuccessGetMenuList)?.visitableList?.firstOrNull { it is SellerMenuOrderUiModel } as? SellerMenuOrderUiModel
        assertEquals(newOrder, actualResult?.newOrderCount)
        assertEquals(readyToShip, actualResult?.readyToShip)
    }

    @Test
    fun `when getNotifications failed, should not do anything`() = runTest {
        onGetNotifications_thenReturn(MessageErrorException())
        getAllSettingShopInfoSuccess()

        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)
        viewModel.onEvent(SellerMenuUIEvent.GetShopInfo)

        advanceUntilIdle()

        val actualResult =
            (viewModel.uiState.value as? SellerMenuUIState.OnSuccessGetMenuList)?.visitableList?.firstOrNull { it is SellerMenuOrderUiModel } as? SellerMenuOrderUiModel
        assertEquals(Int.ZERO, actualResult?.newOrderCount)
        assertEquals(Int.ZERO, actualResult?.readyToShip)
    }

    private fun getAllSettingShopInfoSuccess() {
        val shopScoreResponse = ShopScoreLevelResponse.ShopScoreLevel.Result(shopScore = 70)
        val shopSettingsResponse = createShopSettingsResponse()

        val shopId = "123"

        onGetShopId_thenReturn(shopId)
        onGetAllShopInfoUseCase_thenReturn(shopSettingsResponse)
        onGetShopScoreLevel_thenReturn(shopId, shopScoreResponse)
    }
}
