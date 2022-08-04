package com.tokopedia.tokofood.home

import android.accounts.NetworkErrorException
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
}