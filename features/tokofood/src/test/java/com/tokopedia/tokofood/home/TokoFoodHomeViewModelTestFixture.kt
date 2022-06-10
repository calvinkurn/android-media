package com.tokopedia.tokofood.home

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureResponse
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.feature.home.domain.data.HomeLayoutResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeDynamicIconsResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeLayoutResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeTickerResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeUSPResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeDynamicChannelUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeDynamicIconsUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeTickerUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeUSPUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeTypeFactory
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLayoutUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.KeroEditAddressResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
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

    private val privateHasTickerBeenRemoved by lazy {
        viewModel.getPrivateField<Boolean>("hasTickerBeenRemoved")
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

    protected fun verifyGetHomeLayoutResponseFail() {
        val actualResponse = viewModel.layoutList.value
        Assert.assertTrue(actualResponse is Fail)
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

    protected fun verifyKeroEditAddressFail(expectedResponse: String) {
        val actualResponse = viewModel.errorMessage.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verfifyKeroEditAddressSuccess(expectedResponse: Boolean) {
        val actualResponse = viewModel.updatePinPointState.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyTickerHasBeenRemoved(){
        Assert.assertTrue(privateHasTickerBeenRemoved)
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

    protected fun onGetKeroEditAddress_thenReturn(keroEditAddressResponse: KeroEditAddressResponse) {
        coEvery {
            keroEditAddressUseCase.execute("", "", "")
        } returns keroEditAddressResponse.keroEditAddress.data.isEditSuccess()
    }

    protected fun onGetKeroEditAddress_thenReturn(errorThrowable: Throwable) {
        coEvery {
            keroEditAddressUseCase.execute("", "", "")
        } throws errorThrowable
    }

    protected fun onGetTicker_thenReturn(tickerResponse: TokoFoodHomeTickerResponse) {
        coEvery { tokoFoodHomeTickerUseCase.execute(localCacheModel = any()) } returns tickerResponse
    }

    protected fun onGetTicker_thenReturn(errorThrowable: Throwable) {
        coEvery {
            tokoFoodHomeTickerUseCase.execute(localCacheModel = any())
        } throws errorThrowable
    }

    protected fun verifyCallTicker() {
        coVerify { tokoFoodHomeTickerUseCase.execute(any()) }
    }

    protected fun onGetUSP_thenReturn(uspResponse: TokoFoodHomeUSPResponse) {
        coEvery { tokoFoodHomeUSPUseCase.execute() } returns uspResponse
    }

    protected fun onGetUSP_thenReturn(errorThrowable: Throwable) {
        coEvery {
            tokoFoodHomeUSPUseCase.execute()
        } throws errorThrowable
    }

    protected fun verifyCallUSP() {
        coVerify { tokoFoodHomeUSPUseCase.execute() }
    }

    protected fun onGetIcons_thenReturn(iconResponse: TokoFoodHomeDynamicIconsResponse) {
        coEvery { tokoFoodHomeDynamicIconsUseCase.execute(any()) } returns iconResponse
    }

    protected fun onGetIcons_thenReturn(errorThrowable: Throwable) {
        coEvery {
            tokoFoodHomeDynamicIconsUseCase.execute(any())
        } throws errorThrowable
    }

    protected fun verifyCallIcons() {
        coVerify { tokoFoodHomeDynamicIconsUseCase.execute(any()) }
    }

    protected fun onGetHomeLayoutData_thenReturn(
        layoutResponse: TokoFoodHomeLayoutResponse,
        localCacheModel: LocalCacheModel = LocalCacheModel()
    ) {
        coEvery { tokoFoodDynamicChanelUseCase.execute(localCacheModel) } returns layoutResponse
    }

    protected fun onGetHomeLayoutData_thenReturn(
        error: Throwable,
        localCacheModel: LocalCacheModel = LocalCacheModel()
    ) {
        coEvery { tokoFoodDynamicChanelUseCase.execute(localCacheModel) } throws error
    }

    protected fun verifyCallHomeLayout() {
        coVerify { tokoFoodDynamicChanelUseCase.execute(any()) }
    }

    protected fun onGetMerchantList_thenReturn(
        layoutResponse: TokoFoodMerchantListResponse,
        localCacheModel: LocalCacheModel = LocalCacheModel(),
        pageKey: String = "1"
    ) {
        coEvery { tokoFoodMerchantListUseCase.execute(localCacheModel = localCacheModel, pageKey = pageKey) } returns layoutResponse
    }

    protected fun onGetMerchantList_thenReturn(
        error: Throwable,
        localCacheModel: LocalCacheModel = LocalCacheModel(),
        pageKey: String = "1"
    ) {
        coEvery { tokoFoodMerchantListUseCase.execute(localCacheModel = localCacheModel, pageKey = pageKey) } throws error
    }


    inline fun <reified T>Any.getPrivateField(name: String): T {
        return this::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(this) as T
        }
    }

    fun<T: Any> LiveData<Result<T>>.verifySuccessEquals(expected: Success<Any>) {
        val expectedResult = expected.data
        val actualResult = (value as? Success<T>)?.data
        Assert.assertEquals(expectedResult, actualResult)
    }

    protected fun addHomeLayoutItem(item: TokoFoodItemUiModel) {
        privateHomeLayoutItemList.add(item)
    }

    object UnknownHomeLayout: TokoFoodHomeLayoutUiModel("1") {
        override fun type(typeFactory: TokoFoodHomeTypeFactory?) = 0
    }
}