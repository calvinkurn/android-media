package com.tokopedia.tokofood.home

import android.accounts.NetworkErrorException
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.data.createChooseAddress
import com.tokopedia.tokofood.data.createDynamicIconsResponse
import com.tokopedia.tokofood.data.createDynamicLegoBannerDataModel
import com.tokopedia.tokofood.data.createHomeLayoutList
import com.tokopedia.tokofood.data.createHomeTickerDataModel
import com.tokopedia.tokofood.data.createIconsModel
import com.tokopedia.tokofood.data.createKeroAddrIsEligibleForAddressFeature
import com.tokopedia.tokofood.data.createKeroEditAddressResponse
import com.tokopedia.tokofood.data.createKeroEditAddressResponseFail
import com.tokopedia.tokofood.data.createLoadMoreState
import com.tokopedia.tokofood.data.createLoadingState
import com.tokopedia.tokofood.data.createMerchantListModel1
import com.tokopedia.tokofood.data.createMerchantListModel2
import com.tokopedia.tokofood.data.createMerchantListResponse
import com.tokopedia.tokofood.data.createNoAddressState
import com.tokopedia.tokofood.data.createNoPinPoinState
import com.tokopedia.tokofood.data.createSliderBannerDataModel
import com.tokopedia.tokofood.data.createTicker
import com.tokopedia.tokofood.data.createUSPModel
import com.tokopedia.tokofood.data.createUSPResponse
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.MERCHANT_TITLE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutItemState
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState.Companion.LOAD_MORE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState.Companion.UPDATE
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment.Companion.SOURCE
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeMerchantTitleUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import org.junit.Test

class TokoFoodHomeViewModelTest: TokoFoodHomeViewModelTestFixture() {

    @Test
    fun `when getting loading state should run and give the success result`() {
        viewModel.showLoadingState()

        val expectedResponse = createLoadingState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting no pin poin state should run and give the success result`() {
        viewModel.showNoPinPointState()

        val expectedResponse = createNoPinPoinState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting no address state should run and give the success result`() {
        viewModel.showNoAddressState()

        val expectedResponse = createNoAddressState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting error state should run and give the error result`() {
        val throwable = Throwable("Error Timeout")

        viewModel.showErrorState(throwable)

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
        verifyIsUpdateAddressManualTrue()
    }

    @Test
    fun `when manuallay set isUpdateAddressManual return its value`() {
        viewModel.isAddressManuallyUpdated = false
        verifyIsUpdateAddressManualFalse()
    }

    @Test
    fun `when check is page showing empty state location should return true`() {
        viewModel.showNoAddressState()

        val actualResponse = viewModel.isShownEmptyState()

        verifyHomeIsShowingEmptyState(actualResponse)
    }

    @Test
    fun `when check is page showing error should return true`() {
        viewModel.showErrorState(NetworkErrorException())

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
    fun `when getting keroEditAddress should run and give the success result true`() {
        onGetKeroEditAddress_thenReturn(createKeroEditAddressResponse())

        viewModel.updatePinPoin("", "", "")

        val expectedResponse = createKeroEditAddressResponse().keroEditAddress.data.isEditSuccess()
        verfifyKeroEditAddressSuccess(expectedResponse)
    }

    @Test
    fun `when getting keroEditAddress should run and give the success result false`() {
        onGetKeroEditAddress_thenReturn(createKeroEditAddressResponseFail())

        viewModel.updatePinPoin("", "", "")

        val expectedResponse = createKeroEditAddressResponseFail().keroEditAddress.data.isEditSuccess()
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

        verifyTickerHasBeenRemoved()
        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when scrolledToLastItem and hasNext true when onScrollProductList should set loadMore success`() {
        val containLastItemIndex = 5
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetMerchantList_thenReturn(createMerchantListResponse())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

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
                ),
                TokoFoodHomeMerchantTitleUiModel(MERCHANT_TITLE),
                createMerchantListModel1(),
                createMerchantListModel2()
            ),
            state = LOAD_MORE
        )

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when scrolledToLastItem and hasNext true when onScrollProductList but merchant list error should set loadMore failed`() {
        val containLastItemIndex = 5
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetMerchantList_thenReturn(NullPointerException())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

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
                ),
            ),
            state = LOAD_MORE
        )

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when scrolledToLastItem and hasNext true when onScrollProductList but next page is not initial should not return more than one merchant main title`() {
        val containLastItemIndex = 5
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetMerchantList_thenReturn(createMerchantListResponse())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

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
                ),
                TokoFoodHomeMerchantTitleUiModel(MERCHANT_TITLE),
                createMerchantListModel1(),
                createMerchantListModel2()
            ),
            state = LOAD_MORE
        )

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "1")
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

        val nextExpectedResponse = TokoFoodListUiModel(
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
                ),
                TokoFoodHomeMerchantTitleUiModel(MERCHANT_TITLE),
                createMerchantListModel1(),
                createMerchantListModel2(),
                createMerchantListModel1(),
                createMerchantListModel2(),
            ),
            state = LOAD_MORE
        )

        verifyGetHomeLayoutResponseSuccess(nextExpectedResponse)
    }

    @Test
    fun `when scrolledToLastItem and hasNext true when onScrollProductList, and has next page, but next merchant call is error should not return error`() {
        val containLastItemIndex = 5
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetMerchantList_thenReturn(createMerchantListResponse())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

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
                ),
                TokoFoodHomeMerchantTitleUiModel(MERCHANT_TITLE),
                createMerchantListModel1(),
                createMerchantListModel2()
            ),
            state = LOAD_MORE
        )

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)

        onGetMerchantList_thenReturn(NullPointerException(), pageKey = "2")
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

        val nextExpectedResponse = TokoFoodListUiModel(
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
                ),
                TokoFoodHomeMerchantTitleUiModel(MERCHANT_TITLE),
                createMerchantListModel1(),
                createMerchantListModel2(),
            ),
            state = LOAD_MORE
        )

        verifyGetHomeLayoutResponseSuccess(nextExpectedResponse)
    }

    @Test
    fun `when scrolledToLastItem number not last, has next true is empty should not load more`() {
        val containLastItemIndex = 4
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetMerchantList_thenReturn(createMerchantListResponse())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.setPageKey("")
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

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
                ),
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
    fun `when scrolledToLastItem and itemCount number less than 0, has next true is empty should not load more`() {
        val containLastItemIndex = -1
        val itemCount = -2
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetMerchantList_thenReturn(createMerchantListResponse())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.setPageKey("")
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

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
                ),
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
    fun `when containLastItemIndex number less than 0, has next true is empty should not load more`() {
        val containLastItemIndex = -1
        val itemCount = 0
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetMerchantList_thenReturn(createMerchantListResponse())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.setPageKey("")
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

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
                ),
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
    fun `when there is no address state and user request load more should not load more`() {
        viewModel.showNoAddressState()
        val expectedResponse = createNoAddressState()
        val containLastItemIndex = 5
        val itemCount = 6

        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())


        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when there is error state and user request load more should not load more`() {
        val throwable = Throwable("Error Timeout")

        viewModel.showErrorState(throwable)

        val containLastItemIndex = 5
        val itemCount = 6
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())

        verifyGetErrorLayoutShown()
    }

    @Test
    fun `when there is load more state and user request load more should not load more`() {
        viewModel.showProgressBar()
        val expectedResponse = createLoadMoreState()
        val containLastItemIndex = 5
        val itemCount = 6

        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())


        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when has page key is empty and user request load more should not load more`() {
        viewModel.showProgressBar()
        val expectedResponse = createLoadMoreState()
        val containLastItemIndex = 5
        val itemCount = 6
        viewModel.setPageKey("")
        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())


        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }
}