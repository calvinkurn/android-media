package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.presentation.data.PulsaDataFactory
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test


@ExperimentalCoroutinesApi
class DigitalPDPPulsaViewModelTest : DigitalPDPViewModelTestFixture() {

    private val dataFactory = PulsaDataFactory()
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

        viewModel.getMenuDetail(MENU_ID)
        verifyGetMenuDetailSuccess(response)
    }

    @Test
    fun `when getting menuDetail should run and give fail result`() {
        onGetMenuDetail_thenReturn(NullPointerException())

        viewModel.getMenuDetail(MENU_ID, false)
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

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()

            verifyGetOperatorListRepoGetCalled()
            verifyGetPrefixOperatorSuccess(response)
        }

    @Test
    fun `when getting catalogPrefixSelect should run and give success fail`() =
        testCoroutineRule.runBlockingTest {
            onGetPrefixOperator_thenReturn(NullPointerException())

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()

            verifyGetOperatorListRepoGetCalled()
            verifyGetPrefixOperatorFail()
        }

    @Test
    fun `when updateCheckoutPassData with denom data called should update digitalCheckoutPassData`() {
        val denomData = dataFactory.getDenomData()
        viewModel.updateCheckoutPassData(
            denomData,
            PulsaDataFactory.IDEM_POTENCY_KEY,
            PulsaDataFactory.VALID_CLIENT_NUMBER,
            PulsaDataFactory.OPERATOR_ID
        )

        val expectedResult = dataFactory.getCheckoutPassData()
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `when updateCheckoutPassData with recom data called should update digitalCheckoutPassData`() {
        val recomData = dataFactory.getRecomCardWidgetModelData()
        viewModel.updateCheckoutPassData(
            recomData,
            PulsaDataFactory.IDEM_POTENCY_KEY
        )

        val expectedResult = dataFactory.getCheckoutPassData()
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `when updateCategoryCheckoutPassData called should update digitalCheckoutPassData`() {
        verifyCheckoutPassDataCategoryIdEmpty()

        viewModel.updateCategoryCheckoutPassData(PulsaDataFactory.CATEGORY_ID)
        verifyCheckoutPassDataCategoryIdUpdated(PulsaDataFactory.CATEGORY_ID)
    }

    @Test
    fun `given empty validator when validateClientNumber should set isEligibleToBuy true`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getPrefixOperatorEmptyValData()
            onGetPrefixOperator_thenReturn(response)

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()
            viewModel.validateClientNumber(PulsaDataFactory.VALID_CLIENT_NUMBER)
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
            viewModel.validateClientNumber(PulsaDataFactory.VALID_CLIENT_NUMBER)
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
            viewModel.validateClientNumber(PulsaDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetPrefixOperatorSuccess(response)
            verifyValidateClientNumberFalse()
        }

    @Test
    fun `given selectedGridProduct non-empty when getSelectedPositionId return index`() {
        onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

        viewModel.getSelectedPositionId(dataFactory.getListDenomData())
        verifySelectedGridProductNonEmpty()
    }

    @Test
    fun `given selectedGridProduct empty when getSelectedPositionId return default index`() {
        onGetSelectedGridProduct_thenReturn(SelectedProduct())

        viewModel.getSelectedPositionId(dataFactory.getListDenomData())
        verifySelectedGridProductEmpty()
    }

    @Test
    fun `given selectedGridProduct non-empty when onResetSelectedProduct should reset product`() {
        onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

        viewModel.onResetSelectedProduct()
        verifySelectedGridProductEmpty()
    }

    @Test
    fun `given layoutType is not match & other condition fulfilled when call isAutoSelectedProduct should return false`() {
        onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

        val result = viewModel.isAutoSelectedProduct(DenomWidgetEnum.MCCM_GRID_TYPE)
        verifyIsAutoSelectedProductFalse(result)
    }

    @Test
    fun `given layoutType is match & other condition fulfilled when call isAutoSelectedProduct should return true`() =
        testCoroutineRule.runBlockingTest {
            // use empty validator to make isEligibleToBuy true
            val response = dataFactory.getPrefixOperatorEmptyValData()
            onGetPrefixOperator_thenReturn(response)
            onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.validateClientNumber(PulsaDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()
            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)

            verifyIsAutoSelectedProductTrue(isAutoSelect)
        }

    @Test
    fun `given empty selectedGridProduct & other condition fulfilled when isAutoSelectedProduct should return false`() {
        onGetSelectedGridProduct_thenReturn(SelectedProduct())

        val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)
        verifyIsAutoSelectedProductFalse(isAutoSelect)
    }

    @Test
    fun `given isEligibleToBuy true & other condition fulfilled when isAutoSelectedProduct should return true`() =
        testCoroutineRule.runBlockingTest {
            // use empty validator & dummy selectedProduct to make isEligibleToBuy true
            val response = dataFactory.getPrefixOperatorEmptyValData()
            onGetPrefixOperator_thenReturn(response)
            onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.validateClientNumber(PulsaDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()
            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)

            verifyIsAutoSelectedProductTrue(isAutoSelect)
        }

    @Test
    fun `given isEligibleToBuy false & other condition fulfilled when isAutoSelectedProduct should return false`() =
        testCoroutineRule.runBlockingTest {
            // use empty validator & dummy selectedProduct to make isEligibleToBuy true
            val response = dataFactory.getPrefixOperatorData()
            onGetPrefixOperator_thenReturn(response)
            onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()
            viewModel.validateClientNumber(PulsaDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()

            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)
            verifyIsAutoSelectedProductFalse(isAutoSelect)

        }

    @Test
    fun `when getting catalogInputMultitab should run and give success result`() = testCoroutineRule.runBlockingTest {
        val response = dataFactory.getCatalogInputMultiTabData()
        val mappedResponse = mapperFactory.mapMultiTabGridDenom(response)
        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        skipMultitabDelay()
        verifyGetCatalogInputMultitabSuccess(mappedResponse)
    }

    @Test
    fun `given catalogInputMultitab loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setRechargeCatalogInputMultiTabLoading()
        verifyGetCatalogInputMultitabLoading(loadingResponse)
    }

    @Test
    fun `when getting catalogInputMultitab should run and give error result`() = testCoroutineRule.runBlockingTest {
        val errorResponse = MessageErrorException("")
        onGetCatalogInputMultitab_thenReturn(errorResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        skipMultitabDelay()
        verifyGetCatalogInputMultitabError(errorResponse)
    }

    @Test
    fun `given CancellationException to catalogInputMultitab and should return empty result`() = testCoroutineRule.runBlockingTest {
        val errorResponse = CancellationException()
        onGetCatalogInputMultitab_thenReturn(errorResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        skipMultitabDelay()
        verifyGetCatalogInputMultitabErrorCancellation()
    }

    @Test
    fun `when getting addToCart should run and return success`() {
        val response = dataFactory.getAddToCartData().relationships?.category?.data?.id ?: ""
        onGetAddToCart_thenReturn(response)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartSuccess(response)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException should return custom message`() {
        val errorMessage = "error"
        val errorResponseException = ResponseErrorException(errorMessage)
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException but empty message should return default message`() {
        val errorMessage = "Terjadi kesalahan, ulangi beberapa saat lagi"
        val errorResponseException = ResponseErrorException()
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get any error other than ResponseErrorException should return that error`() {
        val errorMessageException = MessageErrorException()
        onGetAddToCart_thenReturn(errorMessageException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartErrorExceptions(errorMessageException)
    }

    @Test
    fun `given addToCart loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setAddToCartLoading()
        verifyAddToCartErrorLoading(loadingResponse)
    }

    companion object {
        const val MENU_ID = 289
    }
}