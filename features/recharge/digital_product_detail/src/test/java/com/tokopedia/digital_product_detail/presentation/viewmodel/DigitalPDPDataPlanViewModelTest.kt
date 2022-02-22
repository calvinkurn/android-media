package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.presentation.data.DataPlanDataFactory
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.junit.Test

@ExperimentalCoroutinesApi
class DigitalPDPDataPlanViewModelTest: DigitalPDPDataPlanViewModelTestFixture() {

    private val dataFactory = DataPlanDataFactory()
    private val mapperFactory = DigitalDenomMapper()

    @Test
    fun `given menuDetail loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setMenuDetailLoading()
        verifyGetMenuDetailLoading(loadingResponse)
    }

    @Test
    fun `when getting menuDetail should run and give success result`() {
        val response = dataFactory.getMenuDetail()
        onGetMenuDetail_thenReturn(response)

        viewModel.getMenuDetail(DigitalPDPPulsaPulsaViewModelTest.MENU_ID)
        verifyGetMenuDetailSuccess(response)
    }

    @Test
    fun `when getting menuDetail should run and give fail result`() {
        onGetMenuDetail_thenReturn(NullPointerException())

        viewModel.getMenuDetail(DigitalPDPPulsaPulsaViewModelTest.MENU_ID, false)
        verifyGetMenuDetailRepoGetCalled()
        verifyGetMenuDetailFail()
    }

    @Test
    fun `given favoriteNumber loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setFavoriteNumberLoading()
        verifyGetFavoriteNumberLoading(loadingResponse)
    }

    @Test
    fun `when getting favoriteNumber should run and give success result`() {
        val response = dataFactory.getFavoriteNumberData()
        onGetFavoriteNumber_thenReturn(response)

        viewModel.getFavoriteNumber(listOf())
        verifyGetFavoriteNumberRepoGetCalled()
        verifyGetFavoriteNumberSuccess(response.persoFavoriteNumber.items)
    }

    @Test
    fun `when getting favoriteNumber should run and give success fail`() {
        onGetFavoriteNumber_thenReturn(NullPointerException())

        viewModel.getFavoriteNumber(listOf())
        verifyGetFavoriteNumberRepoGetCalled()
        verifyGetFavoriteNumberFail()
    }

    @Test
    fun `given catalogPrefixSelect loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setPrefixOperatorLoading()
        verifyPrefixOperatorLoading(loadingResponse)
    }

    @Test
    fun `when getting catalogPrefixSelect should run and give success result`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getPrefixOperatorData()
            onGetPrefixOperator_thenReturn(response)

            viewModel.getPrefixOperator(DigitalPDPPulsaPulsaViewModelTest.MENU_ID)
            skipPrefixOperatorDelay()

            verifyGetOperatorListRepoGetCalled()
            verifyGetPrefixOperatorSuccess(response)
        }

    @Test
    fun `when getting catalogPrefixSelect should run and give success fail`() =
        testCoroutineRule.runBlockingTest {
            onGetPrefixOperator_thenReturn(NullPointerException())

            viewModel.getPrefixOperator(DigitalPDPPulsaPulsaViewModelTest.MENU_ID)
            skipPrefixOperatorDelay()

            verifyGetOperatorListRepoGetCalled()
            verifyGetPrefixOperatorFail()
        }

    @Test
    fun `when updateCheckoutPassData with denom data called should update digitalCheckoutPassData`() {
        val denomData = dataFactory.getDenomData()
        viewModel.updateCheckoutPassData(
            denomData,
            DataPlanDataFactory.IDEM_POTENCY_KEY,
            DataPlanDataFactory.VALID_CLIENT_NUMBER,
            DataPlanDataFactory.OPERATOR_ID
        )

        val expectedResult = dataFactory.getCheckoutPassData()
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `when updateCheckoutPassData with recom data called should update digitalCheckoutPassData`() {
        val recomData = dataFactory.getRecomCardWidgetModelData()
        viewModel.updateCheckoutPassData(
            recomData,
            DataPlanDataFactory.IDEM_POTENCY_KEY
        )

        val expectedResult = dataFactory.getCheckoutPassData()
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `when updateCategoryCheckoutPassData called should update digitalCheckoutPassData`() {
        verifyCheckoutPassDataCategoryIdEmpty()

        viewModel.updateCategoryCheckoutPassData(DataPlanDataFactory.CATEGORY_ID)
        verifyCheckoutPassDataCategoryIdUpdated(DataPlanDataFactory.CATEGORY_ID)
    }

    @Test
    fun `given empty validator when validateClientNumber should set isEligibleToBuy true`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getPrefixOperatorEmptyValData()
            onGetPrefixOperator_thenReturn(response)

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()
            viewModel.validateClientNumber(DataPlanDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetPrefixOperatorSuccess(response)
            verifyValidateClientNumberTrue()
        }

    @Test
    fun `given non-empty validator when validateClientNumber with valid number should set isEligibleToBuy true`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getPrefixOperatorData()
            onGetPrefixOperator_thenReturn(response)

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()
            viewModel.validateClientNumber(DataPlanDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetPrefixOperatorSuccess(response)
            verifyValidateClientNumberTrue()
        }

    @Test
    fun `given non-empty validator when validateClientNumber with non-valid number should set isEligibleToBuy false`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getPrefixOperatorData()
            onGetPrefixOperator_thenReturn(response)

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()
            viewModel.validateClientNumber(DataPlanDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetPrefixOperatorSuccess(response)
            verifyValidateClientNumberFalse()
        }

    @Test
    fun `given validateClientNumber running when cancelValidatorJob called, the job should be cancelled`() {
        testCoroutineRule.runBlockingTest {
            viewModel.validateClientNumber(DataPlanDataFactory.VALID_CLIENT_NUMBER)
            viewModel.cancelValidatorJob()
            verifyValidatorJobIsCancelled()
        }
    }

    @Test
    fun `given validatorJob null when cancelValidatorJob called should do nothing`() {
        viewModel.cancelValidatorJob()
        verifyValidatorJobIsNull()
    }

    @Test
    fun `given validatorJob null when implicit setValidatorJob executed should update validatorJob to non-null`() {
        viewModel.validatorJob = Job()
        verifyValidatorJobIsNotNull()
    }

    @Test
    fun `given selectedFullProduct non-empty when getSelectedPositionId return index`() {
        onGetSelectedFullProduct_thenReturn(dataFactory.getSelectedProduct())

        viewModel.getSelectedPositionId(dataFactory.getListDenomData())
        verifySelectedFullProductNonEmpty()
    }

    @Test
    fun `given selectedFullProduct empty when getSelectedPositionId return default index`() {
        onGetSelectedFullProduct_thenReturn(SelectedProduct())

        viewModel.getSelectedPositionId(dataFactory.getListDenomData())
        verifySelectedFullProductEmpty()
    }

    @Test
    fun `given selectedFullProduct non-empty with invalid ID when getSelectedPositionId return default index`() {
        onGetSelectedFullProduct_thenReturn(dataFactory.getInvalidIdSelectedProduct())

        val id = viewModel.getSelectedPositionId(dataFactory.getInvalidListDenomData())
        verifyGetSelectedPositionNull(id)
    }

    @Test
    fun `given selectedFullProduct non-empty when onResetSelectedProduct should reset product`() {
        onGetSelectedFullProduct_thenReturn(dataFactory.getSelectedProduct())

        viewModel.onResetSelectedProduct()
        verifySelectedFullProductEmpty()
    }

    @Test
    fun `given layoutType is not match & other condition fulfilled when call isAutoSelectedProduct should return false`() {
        onGetSelectedFullProduct_thenReturn(dataFactory.getSelectedProduct())

        val result = viewModel.isAutoSelectedProduct(DenomWidgetEnum.MCCM_FULL_TYPE)
        verifyIsAutoSelectedProductFalse(result)
    }

    @Test
    fun `given layoutType is match & other condition fulfilled when call isAutoSelectedProduct should return true`() =
        testCoroutineRule.runBlockingTest {
            // use empty validator to make isEligibleToBuy true
            val response = dataFactory.getPrefixOperatorEmptyValData()
            onGetPrefixOperator_thenReturn(response)
            onGetSelectedFullProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.validateClientNumber(DataPlanDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()
            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.FULL_TYPE)

            verifyIsAutoSelectedProductTrue(isAutoSelect)
        }

    @Test
    fun `given empty selectedFullProduct & other condition fulfilled when isAutoSelectedProduct should return false`() {
        onGetSelectedFullProduct_thenReturn(SelectedProduct())

        val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.FULL_TYPE)
        verifyIsAutoSelectedProductFalse(isAutoSelect)
    }

    @Test
    fun `given isEligibleToBuy true & other condition fulfilled when isAutoSelectedProduct should return true`() =
        testCoroutineRule.runBlockingTest {
            // use empty validator & dummy selectedProduct to make isEligibleToBuy true
            val response = dataFactory.getPrefixOperatorEmptyValData()
            onGetPrefixOperator_thenReturn(response)
            onGetSelectedFullProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.validateClientNumber(DataPlanDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()
            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.FULL_TYPE)

            verifyIsAutoSelectedProductTrue(isAutoSelect)
        }

    @Test
    fun `given isEligibleToBuy false & other condition fulfilled when isAutoSelectedProduct should return false`() =
        testCoroutineRule.runBlockingTest {
            // use empty validator & dummy selectedProduct to make isEligibleToBuy true
            val response = dataFactory.getPrefixOperatorData()
            onGetPrefixOperator_thenReturn(response)
            onGetSelectedFullProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()
            viewModel.validateClientNumber(DataPlanDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()

            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.FULL_TYPE)
            verifyIsAutoSelectedProductFalse(isAutoSelect)
        }

    @Test
    fun `given selectedFullProduct pos less than 0 & other condition fulfilled when isAutoSelectedProduct should return false`() {
        testCoroutineRule.runBlockingTest {
            // use empty validator & empty selectedProduct to make position < 0
            val response = dataFactory.getPrefixOperatorEmptyValData()
            onGetPrefixOperator_thenReturn(response)
            onGetSelectedFullProduct_thenReturn(dataFactory.getInvalidPositionSelectedProduct())

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()
            viewModel.validateClientNumber(DataPlanDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.FULL_TYPE)
            verifyIsAutoSelectedProductFalse(isAutoSelect)
        }
    }

    /*
    * TODO: Unit Test Multi Tab Here
    * */

    @Test
    fun `given catalogProductJob null when cancelCatalogProductJob called should do nothing`() {
        viewModel.cancelCatalogProductJob()
        verifyCatalogProductJobIsNull()
    }

    @Test
    fun `given catalogProductJob null when implicit setCatalogProductJob called should update catalogProductJob to non-null`() {
        viewModel.catalogProductJob = Job()
        verifyCatalogProductJobIsNotNull()
    }

    @Test
    fun `when getting addToCart should run and return success`() {
        val response = dataFactory.getAddToCartData().relationships?.category?.data?.id ?: ""
        onGetAddToCart_thenReturn(response)

        viewModel.addToCart(DigitalCheckoutPassData(), RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartSuccess(response)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException should return custom message`() {
        val errorMessage = "error"
        val errorResponseException = ResponseErrorException(errorMessage)
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(DigitalCheckoutPassData(), RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException but empty message should return default message`() {
        val errorMessage = "Terjadi kesalahan, ulangi beberapa saat lagi"
        val errorResponseException = ResponseErrorException()
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(DigitalCheckoutPassData(), RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get any error other than ResponseErrorException should return that error`() {
        val errorMessageException = MessageErrorException()
        onGetAddToCart_thenReturn(errorMessageException)

        viewModel.addToCart(DigitalCheckoutPassData(), RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartErrorExceptions(errorMessageException)
    }

    @Test
    fun `given addToCart loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setAddToCartLoading()
        verifyAddToCartErrorLoading(loadingResponse)
    }

    companion object {
        const val MENU_ID = 290
    }
}