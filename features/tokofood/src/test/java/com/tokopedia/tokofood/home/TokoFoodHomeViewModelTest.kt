package com.tokopedia.tokofood.home

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.data.createChooseAddress
import com.tokopedia.tokofood.data.createDynamicIconsResponse
import com.tokopedia.tokofood.data.createDynamicLegoBannerDataModel
import com.tokopedia.tokofood.data.createHomeLayoutList
import com.tokopedia.tokofood.data.createHomeTickerDataModel
import com.tokopedia.tokofood.data.createIconsModel
import com.tokopedia.tokofood.data.createKeroAddrIsEligibleForAddressFeature
import com.tokopedia.tokofood.data.createKeroEditAddressResponse
import com.tokopedia.tokofood.data.createLoadingState
import com.tokopedia.tokofood.data.createNoAddressState
import com.tokopedia.tokofood.data.createNoPinPoinState
import com.tokopedia.tokofood.data.createSliderBannerDataModel
import com.tokopedia.tokofood.data.createTicker
import com.tokopedia.tokofood.data.createUSPModel
import com.tokopedia.tokofood.data.createUSPResponse
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutItemState
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState.Companion.SHOW
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState.Companion.UPDATE
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment.Companion.SOURCE
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test
import java.lang.NullPointerException

class TokoFoodHomeViewModelTest: TokoFoodHomeViewModelTestFixture() {

    @Test
    fun `when getting loading state should run and give the success result`() {
        viewModel.getLoadingState()

        val expectedResponse = createLoadingState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting no pin poin state should run and give the success result`() {
        viewModel.getNoPinPoinState()

        val expectedResponse = createNoPinPoinState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting no address state should run and give the success result`() {
        viewModel.getNoAddressState()

        val expectedResponse = createNoAddressState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting error state should run and give the error result`() {
        val throwable = Throwable("Error Timeout")

        viewModel.getErrorState(throwable)

        verifyGetErrorLayoutShown()
    }

    @Test
    fun `when getting chooseAddress should throw chooseAddress's exception and get failed result`() {
        onGetChooseAddress_thenReturn(Throwable())

        viewModel.getChooseAddress(SOURCE)

        verifyGetChooseAddressFail()
    }

    @Test
    fun `when getting chooseAddress should run and give the success result`() {
        onGetChooseAddress_thenReturn(createChooseAddress())

        viewModel.getChooseAddress(SOURCE)

        verifyGetChooseAddress()

        val expectedResponse = createChooseAddress().response
        verfifyGetChooseAddressSuccess(expectedResponse)
    }

    @Test
    fun `when check is page showing empty state location should return true`() {
        viewModel.getNoAddressState()

        val actualResponse = viewModel.isShownEmptyState()

        verifyHomeIsShowingEmptyState(actualResponse)
    }

    @Test
    fun `when check is page showing empty state location should return false`() {

        val actualResponse = viewModel.isShownEmptyState()

        verifyHomeIsNotShowingEmptyState(actualResponse)
    }

    @Test
    fun `when getting eligibleForAnaRevamp should throw eligibleForAnaRevamp's exception and get failed result`() {
        onGetEligibleForAnaRevamp_thenReturn(Throwable())

        viewModel.checkUserEligibilityForAnaRevamp()

        verifyEligibleForAnaRevampFail()
    }

    @Test
    fun `when getting eligibleForAnaRevamp should run and give the success result`() {
        onGetEligibleForAnaRevamp_thenReturn(createKeroAddrIsEligibleForAddressFeature())

        viewModel.checkUserEligibilityForAnaRevamp()

        val expectedResponse = createKeroAddrIsEligibleForAddressFeature().data.eligibleForRevampAna
        verfifyEligibleForAnaRevampSuccess(expectedResponse)
    }

    @Test
    fun `when getting keroEditAddress should run and give the success result`() {
        onGetKeroEditAddress_thenReturn(createKeroEditAddressResponse())

        viewModel.updatePinPoin("", "", "")

        val expectedResponse = createKeroEditAddressResponse().keroEditAddress.data.isEditSuccess()
        verfifyKeroEditAddressSuccess(expectedResponse)
    }

    @Test
    fun `when getting keroEditAddress should throw keroEditAddress's exception and get message failed result`() {
        val errorMessage = "Error Change Address"
        onGetKeroEditAddress_thenReturn(Throwable(errorMessage))

        viewModel.updatePinPoin("", "", "")

        verifyKeroEditAddressFail(errorMessage)
    }

    @Test
    fun `when getting homeLayout should run and give the success result`() {
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())

        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(),
                createUSPModel(),
                createIconsModel(),
                createSliderBannerDataModel(
                    id = "33333",
                    groupId = "",
                    headerName = "Banner TokoFood"
                ),
                createDynamicLegoBannerDataModel(
                    id = "44444",
                    groupId = "",
                    headerName = "6 Image"
                )
            ),
            state = UPDATE
        )

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayoutData_thenReturn(Throwable())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting homeLayout and error layoutComponent data should run and give the success result`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetTicker_thenReturn(NullPointerException())
        onGetUSP_thenReturn(NullPointerException())
        onGetIcons_thenReturn(NullPointerException())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())

        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createSliderBannerDataModel(
                    id = "33333",
                    groupId = "",
                    headerName = "Banner TokoFood"
                ),
                createDynamicLegoBannerDataModel(
                    id = "44444",
                    groupId = "",
                    headerName = "6 Image"
                )
            ),
            state = UPDATE
        )

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting homeLayout and there is unsupported layout should run and give the success result`() {
        val unknownLayout = TokoFoodItemUiModel(
            UnknownHomeLayout,
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(unknownLayout)

        viewModel.getLayoutComponentData(LocalCacheModel())

        val expectedResponse = TokoFoodListUiModel(
                items = emptyList(),
                state = UPDATE
            )

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting homeLayout and there is null layout should run and give the success result`() {
        val unknownLayout = TokoFoodItemUiModel(
            null,
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(unknownLayout)

        viewModel.getLayoutComponentData(LocalCacheModel())

        val expectedResponse = TokoFoodListUiModel(
            items = emptyList(),
            state = UPDATE
        )

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting homeLayout and there is null visitable should run and is unknown type`() {
        val unknownHomeLayout = TokoFoodItemUiModel(
            UnknownLayout,
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(unknownHomeLayout)

        viewModel.getLayoutComponentData(LocalCacheModel())

        verifyGetUnknownShown()
    }


    @Test
    fun `when remove ticker should run and give removed the ticker`() {
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.removeTickerWidget(TokoFoodHomeStaticLayoutId.TICKER_WIDGET_ID)

        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createUSPModel(),
                createIconsModel(),
                createSliderBannerDataModel(
                    id = "33333",
                    groupId = "",
                    headerName = "Banner TokoFood"
                ),
                createDynamicLegoBannerDataModel(
                    id = "44444",
                    groupId = "",
                    headerName = "6 Image"
                )
            ),
            state = UPDATE
        )

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }
}