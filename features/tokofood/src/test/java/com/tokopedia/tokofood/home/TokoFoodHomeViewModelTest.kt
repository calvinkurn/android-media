package com.tokopedia.tokofood.home

import android.accounts.NetworkErrorException
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.tokofood.data.createAddress
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
import com.tokopedia.tokofood.data.createTickerData
import com.tokopedia.tokofood.data.createUSPModel
import com.tokopedia.tokofood.data.createUSPResponse
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.MERCHANT_TITLE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutItemState
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState.Companion.LOAD_MORE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState.Companion.SHOW
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState.Companion.UPDATE
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment.Companion.SOURCE
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeMerchantTitleUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodHomeViewModelTest: TokoFoodHomeViewModelTestFixture() {

    @Test
    fun `when getting eligibleForAnaRevamp should run and give the success result`() {
        onGetEligibleForAnaRevamp_thenReturn(createKeroAddrIsEligibleForAddressFeature())

        val expectedResponse = createKeroAddrIsEligibleForAddressFeature().data.eligibleForRevampAna
        var actualResponse: Result<EligibleForAddressFeature>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowEligibleForAnaRevamp.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.checkUserEligibilityForAnaRevamp()
            collectorJob.cancel()
        }
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting eligibleForAnaRevamp should throw eligibleForAnaRevamp's exception and get failed result`() {
        onGetEligibleForAnaRevamp_thenReturn(Throwable())

        var actualResponse: Result<EligibleForAddressFeature>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowEligibleForAnaRevamp.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.checkUserEligibilityForAnaRevamp()
            collectorJob.cancel()
        }
        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when getting chooseAddress should run and give the success result`() {
        onGetChooseAddress_thenReturn(createChooseAddress())

        val expectedResponse = createChooseAddress().response
        var actualResponse: Result<GetStateChosenAddressResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowChooseAddress.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.getChooseAddress(SOURCE)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
        verifyIsUpdateAddressManualTrue()
    }

    @Test
    fun `when getting chooseAddress should throw chooseAddress's exception and get failed result`() {
        onGetChooseAddress_thenReturn(Throwable())

        var actualResponse: Result<GetStateChosenAddressResponse>? = null
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowChooseAddress.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.getChooseAddress(SOURCE)
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when manually set isUpdateAddressManual return its value`() {
        viewModel.isAddressManuallyUpdated = false
        verifyIsUpdateAddressManualFalse()
    }

    @Test
    fun `when getting keroEditAddress should run and give the success result true`() {
        onGetKeroEditAddress_thenReturn(createKeroEditAddressResponse())

        val expectedResponse = createKeroEditAddressResponse().keroEditAddress.data.isEditSuccess()
        var actualResponse: Result<Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowUpdatePinPointState.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setUpdatePinPoint("", "", "")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
        Assert.assertTrue((actualResponse as Success).data)
    }

    @Test
    fun `when getting keroEditAddress should run and give the success result false`() {
        onGetKeroEditAddress_thenReturn(createKeroEditAddressResponseFail())

        val expectedResponse = false
        var actualResponse: Result<Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowUpdatePinPointState.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setUpdatePinPoint("", "", "")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
        Assert.assertFalse((actualResponse as Success).data)
    }

    @Test
    fun `when getting keroEditAddress should throw keroEditAddress's exception and get message failed result`() {
        val errorMessage = "Error Change Address"
        onGetKeroEditAddress_thenReturn(Throwable(errorMessage))

        var actualResponse: Result<Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowUpdatePinPointState.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setUpdatePinPoint("", "", "")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when getting error state should run and give the error result`() {
        val throwable = Throwable("Error Timeout")

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setErrorState(throwable)
            collectorJob.cancel()
        }

        val isShownErrorState = (actualResponse as Success).data.items.find { it is TokoFoodErrorStateUiModel }
        Assert.assertNotNull(isShownErrorState)
    }

    @Test
    fun `when getting homeLayout should run and give the success result`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())

        var actualResponse: Result<TokoFoodListUiModel>? = null
        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(),
                createUSPModel(state = TokoFoodLayoutState.LOADING),
                createIconsModel(state = TokoFoodLayoutState.LOADING),
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
            state = SHOW
        )

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            collectorJob.cancel()
        }

        verifyCallHomeLayout()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting homeLayout should run and then get layout component and give the success result`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetTicker_thenReturn(createTicker())

        var actualResponse: Result<TokoFoodListUiModel>? = null
        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when remove ticker should run and give removed the ticker`() {
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())

        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            viewModel.setRemoveTicker(TokoFoodHomeStaticLayoutId.TICKER_WIDGET_ID)
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()
        verifyTickerHasBeenRemoved()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting homeLayout and error layoutComponent data should run and give the success result`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetTicker_thenReturn(NullPointerException())
        onGetUSP_thenReturn(NullPointerException())
        onGetIcons_thenReturn(NullPointerException())

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

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }


    @Test
    fun `when getting homeLayout and there is unsupported layout should run and give the success result`() {
        val unknownLayout = TokoFoodItemUiModel(
            UnknownHomeLayout,
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(unknownLayout)
        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setLayoutComponentData(createAddress())
            collectorJob.cancel()
        }

        val expectedResponse = TokoFoodListUiModel(
                items = emptyList(),
                state = UPDATE
            )

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting homeLayout and there is null layout should run and give the success result`() {
        val unknownLayout = TokoFoodItemUiModel(
            null,
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(unknownLayout)

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setLayoutComponentData(createAddress())
            collectorJob.cancel()
        }

        val expectedResponse = TokoFoodListUiModel(
            items = emptyList(),
            state = UPDATE
        )

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting homeLayout, component layout, merchant list but address empty`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(null, isLoggedIn = true)
            viewModel.setLayoutComponentData(null)
            viewModel.setMerchantList(null)
            collectorJob.cancel()
        }
        Assert.assertNull(actualResponse)
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayoutData_thenReturn(Throwable())

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when scrolledToLastItem and hasNext true when onScrollProductList should set loadMore success`() {
        val containLastItemIndex = 5
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetMerchantList_thenReturn(createMerchantListResponse(), createAddress())

        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }


        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }


    @Test
    fun `when scrolledToLastItem and hasNext true when onScrollProductList but merchant list error should set loadMore failed`() {
        val containLastItemIndex = 5
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetMerchantList_thenReturn(NullPointerException(), createAddress())

        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when check is page showing empty state location should return true`() {
        var actualResponse: Boolean? = null
        viewModel.isAddressManuallyUpdated = true

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = viewModel.isShownEmptyState()
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "0"), true)
            collectorJob.cancel()
        }

        verifyHomeIsShowingEmptyState(actualResponse)
    }

    @Test
    fun `when check is page showing error should return true`() {
        var actualResponse: Boolean? = null
        viewModel.isAddressManuallyUpdated = true

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = viewModel.isShownEmptyState()
                }
            }
            viewModel.setErrorState(NetworkErrorException())
            collectorJob.cancel()
        }

        verifyHomeIsShowingEmptyState(actualResponse)
    }

    @Test
    fun `when check is page showing empty state location should return false`() {

        val actualResponse = viewModel.isShownEmptyState()

        verifyHomeIsNotShowingEmptyState(actualResponse)
    }

    @Test
    fun `when getting no pin poin state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1"), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin state should run and isAddressManuallyUpdated is false and give the loading result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = false
        val expectedResponse = createLoadingState()

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1"), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no address state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoAddressState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no address state should run and isAddressManuallyUpdated is false and give the loading result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = false
        val expectedResponse = createLoadingState()

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when scrolledToLastItem number not last, has next true is empty should not load more`() {
        val containLastItemIndex = 4
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetMerchantList_thenReturn(NullPointerException(), createAddress())

        var actualResponse: Result<TokoFoodListUiModel>? = null
        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            viewModel.setPageKey("")
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when scrolledToLastItem and itemCount number less than 0, has next true is empty should not load more`() {
        val containLastItemIndex = -1
        val itemCount = -2
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetMerchantList_thenReturn(NullPointerException(), createAddress())

        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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
        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            viewModel.setPageKey("")
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when containLastItemIndex number less than 0, has next true is empty should not load more`() {
        val containLastItemIndex = -1
        val itemCount = 0
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetMerchantList_thenReturn(NullPointerException(), createAddress())

        var actualResponse: Result<TokoFoodListUiModel>? = null
        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            viewModel.setPageKey("")
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when scrolledToLastItem and hasNext true when onScrollProductList but next page is not initial should not return more than one merchant main title`() {
        val containLastItemIndex = 5
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetMerchantList_thenReturn(createMerchantListResponse(), createAddress())

        var actualResponse: Result<TokoFoodListUiModel>? = null
        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)

        /** second flow */

        onGetMerchantList_thenReturn(createMerchantListResponse(), createAddress(), pageKey = "1")

        val nextExpectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        var nextActualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    nextActualResponse = it
                }
            }
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        Assert.assertEquals(nextExpectedResponse, (nextActualResponse as Success).data)
    }

    @Test
    fun `when scrolledToLastItem and hasNext true when onScrollProductList, and has next page, but next merchant call is error should not return error`() {
        val containLastItemIndex = 5
        val itemCount = 6
        onGetTicker_thenReturn(createTicker())
        onGetUSP_thenReturn(createUSPResponse())
        onGetIcons_thenReturn(createDynamicIconsResponse())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), createAddress())
        onGetMerchantList_thenReturn(createMerchantListResponse(), createAddress())

        var actualResponse: Result<TokoFoodListUiModel>? = null
        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(createAddress(), true)
            viewModel.setLayoutComponentData(createAddress())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        verifyCallHomeLayout()
        verifyCallTicker()
        verifyCallIcons()
        verifyCallUSP()

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)

        /** second flow */

        onGetMerchantList_thenReturn(NullPointerException(), createAddress(), pageKey = "2")

        val nextExpectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(listOf(createTickerData())),
                createUSPModel(createUSPResponse(), state = TokoFoodLayoutState.SHOW),
                createIconsModel(createDynamicIconsResponse().dynamicIcon.listDynamicIcon, state = TokoFoodLayoutState.SHOW),
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

        var nextActualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    nextActualResponse = it
                }
            }
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        Assert.assertEquals(nextExpectedResponse, (nextActualResponse as Success).data)
    }

    @Test
    fun `when there is no address state and user request load more should not load more`() {
        val noAddressLayout = TokoFoodItemUiModel(
            TokoFoodHomeEmptyStateLocationUiModel(id = TokoFoodHomeStaticLayoutId.EMPTY_STATE_NO_ADDRESS),
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(noAddressLayout)
        val containLastItemIndex = 5
        val itemCount = 6

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        Assert.assertNull(actualResponse)
    }


    @Test
    fun `when there is error state and user request load more should not load more`() {
        val errorStateLayout = TokoFoodItemUiModel(
            TokoFoodErrorStateUiModel(id = TokoFoodHomeStaticLayoutId.ERROR_STATE, Throwable()),
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(errorStateLayout)
        val containLastItemIndex = 5
        val itemCount = 6

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        Assert.assertNull(actualResponse)
    }


    @Test
    fun `when there is load more state and user request load more should not load more`() {
        val errorStateLayout = TokoFoodItemUiModel(
            TokoFoodProgressBarUiModel(id = TokoFoodHomeStaticLayoutId.PROGRESS_BAR),
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(errorStateLayout)
        val containLastItemIndex = 5
        val itemCount = 6

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        Assert.assertNull(actualResponse)
    }

    @Test
    fun `when has page key is empty and user request load more should not load more`() {
        val errorStateLayout = TokoFoodItemUiModel(
            TokoFoodProgressBarUiModel(id = TokoFoodHomeStaticLayoutId.PROGRESS_BAR),
            TokoFoodLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(errorStateLayout)
        val containLastItemIndex = 5
        val itemCount = 6

        var actualResponse: Result<TokoFoodListUiModel>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setPageKey("")
            viewModel.onScrollProductList(containLastItemIndex, itemCount, createAddress())
            collectorJob.cancel()
        }

        Assert.assertNull(actualResponse)
    }

    @Test
    fun `when getting empty address state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoAddressState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = ""), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting not login state should run and give the success result`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), LocalCacheModel(address_id = ""))

        val expectedResponse = TokoFoodListUiModel(
            items = listOf(
                TokoFoodHomeChooseAddressWidgetUiModel(CHOOSE_ADDRESS_WIDGET_ID),
                createHomeTickerDataModel(),
                createUSPModel(state = TokoFoodLayoutState.LOADING),
                createIconsModel(state = TokoFoodLayoutState.LOADING),
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
            state = SHOW
        )

        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = ""), false)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin empty lat and lang state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1", lat = "", long = ""), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin 0 lat and lang state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1", lat = "0.0", long = "0.0"), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin empty lat and 0 lang state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1", lat = "", long = "0.0"), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin 0 lat and empty lang state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1", lat = "0.0", long = ""), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin empty lat and not empty lang state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1", lat = "", long = "-1.9389"), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin not empty lat and empty lang state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1", lat = "-1.9389", long = ""), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin 0 lat and not empty lang state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1", lat = "0.0", long = "-1.9389"), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting no pin poin not empty lat and 0 lang state should run and give the success result`() {
        var actualResponse: Result<TokoFoodListUiModel>? = null
        viewModel.isAddressManuallyUpdated = true

        val expectedResponse = createNoPinPoinState()
        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setHomeLayout(LocalCacheModel(address_id = "1", lat = "-1.9389", long = "0.0"), true)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when search coachmark has been shown, should not show coachmark again`() {
        var actualResult: Boolean? = null
        val hasShown = true
        onGetHasSearchCoachMarkShown_thenReturn(hasShown)

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowShouldShowSearchCoachMark.collectLatest {
                    actualResult = it
                }
            }
            viewModel.checkForSearchCoachMark()
            collectorJob.cancel()
        }

        val expectedResult = !hasShown
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when search coachmark has not been shown, should show coachmark again`() {
        var actualResult: Boolean? = null
        val hasShown = false
        onGetHasSearchCoachMarkShown_thenReturn(hasShown)

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowShouldShowSearchCoachMark.collectLatest {
                    actualResult = it
                }
            }
            viewModel.checkForSearchCoachMark()
            collectorJob.cancel()
        }

        val expectedResult = !hasShown
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when setSearchCoachMarkHasShown should run class method to set true value`() {
        viewModel.setSearchCoachMarkHasShown()

        verify {
            tokofoodHomeSharedPref.setHasSearchCoachmarkShown(true)
        }
    }
}
