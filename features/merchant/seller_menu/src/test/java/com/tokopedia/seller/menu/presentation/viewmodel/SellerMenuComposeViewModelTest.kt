package com.tokopedia.seller.menu.presentation.viewmodel

import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.domain.query.ShopScoreLevelResponse
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState
import com.tokopedia.seller.menu.presentation.util.SellerMenuComposeList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class SellerMenuComposeViewModelTest : SellerMenuComposeViewModelTestFixture() {

    @Test
    fun `when getShopAccountInfo success, should emit success event`() {
        val shopInfoPeriodResponse = ShopInfoPeriodUiModel()

        onGetShopInfoPeriodUseCase_thenReturn(shopInfoPeriodResponse)

        viewModel.getShopAccountInfo()

        val actualResult = viewModel.uiEvent.value

        assert(actualResult is SellerMenuUIEvent.OnSuccessGetShopInfo)
    }

    @Test
    fun `when getShopAccountInfo failed, should emit failed state`() {
        onGetGetShopInfoPeriodUseCase_thenReturnError(MessageErrorException())

        viewModel.getShopAccountInfo()

        val actualResult = viewModel.uiState.value

        assert(actualResult is SellerMenuUIState.OnFailedGetMenuList)
    }

    @Test
    fun `when onEvent GetInitialMenu, should emit success initial state`() {
        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)

        val actualResult = (viewModel.uiState.value as? SellerMenuUIState.OnSuccessGetMenuList)

        assert(actualResult?.isInitialValue == true)
        assert(actualResult?.visitableList == SellerMenuComposeList.createInitialItems())
    }

    @Test
    fun `when getAllSettingShopInfo success, should emit success loaded state`() = runBlocking {
        val shopScoreResponse = ShopScoreLevelResponse.ShopScoreLevel.Result(shopScore = 70)
        val shopSettingsResponse = createShopSettingsResponse()

        val shopId = "123"

        onGetShopId_thenReturn(shopId)
        onGetAllShopInfoUseCase_thenReturn(shopSettingsResponse)
        onGetShopScoreLevel_thenReturn(shopId, shopScoreResponse)

        viewModel.getAllSettingShopInfo(false)

        val actualResult = viewModel.uiState.value

        assert(actualResult is SellerMenuUIState.OnSuccessGetMenuList)
    }

    @Test
    fun `when getAllSettingShopInfo failed, should emit failed loaded state`() {
        onGetAllShopInfoUseCase_thenReturn(MessageErrorException())

        viewModel.getAllSettingShopInfo(false)

        val actualResult = viewModel.uiState.value

        assert(actualResult is SellerMenuUIState.OnFailedGetMenuList)
    }
}
