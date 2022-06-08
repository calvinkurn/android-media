package com.tokopedia.tokofood.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureResponse
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeDynamicChannelUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeDynamicIconsUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeTickerUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeUSPUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

abstract class TokoFoodHomeViewModelTestFixture {

    @RelaxedMockK
    lateinit var tokoFoodDynamicChanelUseCase: TokoFoodHomeDynamicChannelUseCase
    @RelaxedMockK
    lateinit var tokoFoodHomeUSPUseCase: TokoFoodHomeUSPUseCase
    @RelaxedMockK
    lateinit var tokoFoodHomeDynamicIconsUseCase: TokoFoodHomeDynamicIconsUseCase
    @RelaxedMockK
    lateinit var tokoFoodHomeTickerUseCase: TokoFoodHomeTickerUseCase
    @RelaxedMockK
    lateinit var tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase
    @RelaxedMockK
    lateinit var keroEditAddressUseCase: KeroEditAddressUseCase
    @RelaxedMockK
    lateinit var getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase
    @RelaxedMockK
    lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TokoFoodHomeViewModel

    private val privateHomeLayoutItemList by lazy {
        viewModel.getPrivateField<MutableList<TokoFoodItemUiModel>>("homeLayoutItemList")
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoFoodHomeViewModel(
            tokoFoodDynamicChanelUseCase,
            tokoFoodHomeUSPUseCase,
            tokoFoodHomeDynamicIconsUseCase,
            tokoFoodHomeTickerUseCase,
            tokoFoodMerchantListUseCase,
            keroEditAddressUseCase,
            getChooseAddressWarehouseLocUseCase,
            eligibleForAddressUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun verifyGetHomeLayoutResponseSuccess(expectedResponse: TokoFoodListUiModel) {
        val actualResponse = viewModel.layoutList.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyGetErrorLayoutShown() {
        val homeLayoutList = viewModel.layoutList.value
        val actualResponse = (homeLayoutList as Success).data.items.find { it is TokoFoodErrorStateUiModel }
        Assert.assertNotNull(actualResponse)
    }

    protected fun verifyGetChooseAddressFail() {
        val actualResponse = viewModel.chooseAddress.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verfifyGetChooseAddressSuccess(expectedResponse: GetStateChosenAddressResponse) {
        val actualResponse = viewModel.chooseAddress.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyHomeIsShowingEmptyState(actualResponse: Boolean) {
        Assert.assertTrue(actualResponse)
    }

    protected fun verifyHomeIsNotShowingEmptyState(actualResponse: Boolean) {
        Assert.assertFalse(actualResponse)
    }

    protected fun verifyEligibleForAnaRevampFail() {
        val actualResponse = viewModel.eligibleForAnaRevamp.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verfifyEligibleForAnaRevampSuccess(expectedResponse: EligibleForAddressFeature) {
        val actualResponse = viewModel.eligibleForAnaRevamp.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun onGetChooseAddress_thenReturn(getStateChosenAddressResponse: GetStateChosenAddressQglResponse) {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            firstArg<(GetStateChosenAddressResponse)-> Unit>().invoke(getStateChosenAddressResponse.response)
        }
    }

    protected fun onGetChooseAddress_thenReturn(errorThrowable: Throwable) {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            secondArg<(Throwable)-> Unit>().invoke(errorThrowable)
        }
    }

    protected fun onGetEligibleForAnaRevamp_thenReturn(keroAddrIsEligibleForAddressFeatureResponse: KeroAddrIsEligibleForAddressFeatureResponse) {
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            firstArg<(KeroAddrIsEligibleForAddressFeatureData)-> Unit>().invoke(keroAddrIsEligibleForAddressFeatureResponse.data)
        }
    }

    protected fun onGetEligibleForAnaRevamp_thenReturn(errorThrowable: Throwable) {
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            secondArg<(Throwable)-> Unit>().invoke(errorThrowable)
        }
    }

    protected fun verifyGetChooseAddress() {
        coVerify { getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any()) }
    }

    inline fun <reified T>Any.getPrivateField(name: String): T {
        return this::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(this) as T
        }
    }

}