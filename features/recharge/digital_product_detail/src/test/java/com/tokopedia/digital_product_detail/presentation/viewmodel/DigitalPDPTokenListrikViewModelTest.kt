package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.common.topupbills.favoritepdp.data.mapper.FavoritePersoMapper
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_product_detail.data.mapper.DigitalAtcMapper
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.presentation.data.TokenListrikDataFactory
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class DigitalPDPTokenListrikViewModelTest : DigitalPDPTokenListrikViewModelTestFixture() {

    private val dataFactory = TokenListrikDataFactory()
    private val mapperFactory = DigitalDenomMapper()
    private val mapAtcFactory = DigitalAtcMapper()
    private val persoMapperFactory = FavoritePersoMapper()

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

        viewModel.getMenuDetail(MENU_ID)
        verifyGetMenuDetailRepoGetCalled()
        verifyGetMenuDetailFail()
    }

    @Test
    fun `given recommendationData loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setRecommendationLoading()
        verifyGetRecommendationLoading(loadingResponse)
    }

    @Test
    fun `when getting recommendation should run and give success result`() = runTest {
        val response = dataFactory.getRecommendationData()
        val mappedResponse = mapperFactory.mapDigiPersoToRecommendation(response.digitalPersoData, true)
        onGetRecommendation_thenReturn(mappedResponse)

        viewModel.getRecommendations(listOf(), listOf(), listOf())
        skipRecommendationDelay()
        verifyGetRecommendationsRepoGetCalled()
        verifyGetRecommendationSuccess(mappedResponse)
    }

    @Test
    fun `when getting recommendation should run and give fail result`() = runTest {
        onGetRecommendation_thenReturn(NullPointerException())

        viewModel.getRecommendations(listOf(), listOf(), listOf())
        skipRecommendationDelay()
        verifyGetRecommendationsRepoGetCalled()
        verifyGetRecommendationFail()
    }

    @Test
    fun `given favoriteNumber loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setFavoriteNumberLoading()
        verifyGetFavoriteNumberLoading(loadingResponse)
    }

    @Test
    fun `when getting favoriteNumber should run and give success result`() {
        val response = dataFactory.getFavoriteNumberData(true)
        val mappedResponse = persoMapperFactory.mapDigiPersoFavoriteToModel(response)
        onGetFavoriteNumber_thenReturn(mappedResponse)

        viewModel.getFavoriteNumbers(listOf(), listOf(), listOf())
        verifyGetFavoriteNumberChipsRepoGetCalled()
        verifyGetFavoriteNumberChipsSuccess(mappedResponse.favoriteChips)
        verifyGetFavoriteNumberListSuccess(mappedResponse.autoCompletes)
        verifyGetFavoriteNumberPrefillSuccess(mappedResponse.prefill)
    }

    @Test
    fun `when getting favoriteNumber without prefill (or any type) should run and give success result with empty default`() {
        val response = dataFactory.getFavoriteNumberData(false)
        val mappedResponse = persoMapperFactory.mapDigiPersoFavoriteToModel(response)
        onGetFavoriteNumber_thenReturn(mappedResponse)

        viewModel.getFavoriteNumbers(listOf(), listOf(), listOf())
        verifyGetFavoriteNumberChipsRepoGetCalled()
        verifyGetFavoriteNumberChipsSuccess(mappedResponse.favoriteChips)
        verifyGetFavoriteNumberListSuccess(mappedResponse.autoCompletes)
        verifyGetFavoriteNumberPrefillSuccess(mappedResponse.prefill)
        verifyGetFavoriteNumberPrefillEmpty()
    }

    @Test
    fun `when updateCheckoutPassData with denom data called should update digitalCheckoutPassData`() {
        val denomData = dataFactory.getDenomData()
        viewModel.updateCheckoutPassData(
            denomData,
            TokenListrikDataFactory.IDEM_POTENCY_KEY,
            TokenListrikDataFactory.VALID_CLIENT_NUMBER,
            TokenListrikDataFactory.OPERATOR_ID
        )

        val expectedResult = dataFactory.getCheckoutPassData()
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `when updateCheckoutPassData with recom data called should update digitalCheckoutPassData`() {
        val recomData = dataFactory.getRecomCardWidgetModelData()
        viewModel.updateCheckoutPassData(
            recomData,
            TokenListrikDataFactory.IDEM_POTENCY_KEY
        )

        val expectedResult = dataFactory.getCheckoutPassData()
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `when updateCategoryCheckoutPassData called should update digitalCheckoutPassData`() {
        verifyCheckoutPassDataCategoryIdEmpty()

        viewModel.updateCategoryCheckoutPassData(TokenListrikDataFactory.CATEGORY_ID)
        verifyCheckoutPassDataCategoryIdUpdated(TokenListrikDataFactory.CATEGORY_ID)
    }

    @Test
    fun `when getting catalogInputMultitab should run and give success result and updated data filter`() = runTest {
        val response = dataFactory.getCatalogInputMultiTabData()
        val mappedResponse = mapperFactory.mapTokenListrikDenom(response)
        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        skipMultitabDelay()
        verifyGetProductInputMultiTabRepoGetCalled()
        verifyGetCatalogInputMultitabSuccess(mappedResponse)
    }

    @Test
    fun `given catalogInputMultitab loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setRechargeCatalogInputMultiTabLoading()
        verifyGetCatalogInputMultitabLoading(loadingResponse)
    }

    @Test
    fun `when getting catalogInputMultitab should run and give error result`() = runTest {
        val errorResponse = MessageErrorException("")
        onGetCatalogInputMultitab_thenReturn(errorResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        skipMultitabDelay()
        verifyGetProductInputMultiTabRepoGetCalled()
        verifyGetCatalogInputMultitabError(errorResponse)
    }

    @Test
    fun `given CancellationException to catalogInputMultitab and should return empty result`() {
        val errorResponse = CancellationException()
        onGetCatalogInputMultitab_thenReturn(errorResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        viewModel.cancelCatalogProductJob()
        verifyGetProductInputMultiTabRepoWasNotCalled()
        verifyGetCatalogInputMultitabErrorCancellation()
    }

    @Test
    fun `when cancelCatalogProductJob called the job should be cancelled and live data should not emit value`() {
        val response = dataFactory.getCatalogInputMultiTabData()
        val mappedResponse = mapperFactory.mapTokenListrikDenom(response)
        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        viewModel.cancelCatalogProductJob()
        verifyCatalogProductJobIsCancelled()
        verifyGetProductInputMultiTabRepoWasNotCalled()
        verifyGetCatalogInputMultitabErrorCancellation()
    }

    @Test
    fun `when getting addToCart should run and return success`() {
        val response = mapAtcFactory.mapAtcToResult(dataFactory.getAddToCartData())
        onGetAddToCart_thenReturn(response)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartSuccess(response)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException should return custom message`() {
        val errorMessage = "error"
        val errorResponseException = ResponseErrorException(errorMessage)
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException but empty message should return default message`() {
        val errorMessage = "Terjadi kesalahan, ulangi beberapa saat lagi"
        val errorResponseException = ResponseErrorException()
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get any error other than ResponseErrorException should return that error`() {
        val errorMessageException = MessageErrorException()
        onGetAddToCart_thenReturn(errorMessageException)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartErrorExceptions(errorMessageException)
    }

    @Test
    fun `given addToCart loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setAddToCartLoading()
        verifyAddToCartErrorLoading(loadingResponse)
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
    fun `given selectedGridProduct non-empty with invalid ID when getSelectedPositionId return default index`() {
        onGetSelectedGridProduct_thenReturn(dataFactory.getInvalidIdSelectedProduct())

        val id = viewModel.getSelectedPositionId(dataFactory.getInvalidListDenomData())
        verifyGetSelectedPositionNull(id)
    }

    @Test
    fun `given selectedGridProduct non-empty when onResetSelectedProduct should reset product`() {
        onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

        viewModel.onResetSelectedProduct()
        verifySelectedGridProductEmpty()
    }

    @Test
    fun `given catalogProductJob null when cancelCatalogProductJob called should do nothing`() {
        viewModel.cancelCatalogProductJob()
        verifyCatalogProductJobIsNull()
    }

    @Test
    fun `given catalogSelectGroup loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setOperatorSelectGroupLoading()
        verifyGetOperatorSelectGroupLoading(loadingResponse)
    }

    @Test
    fun `when getting catalogSelectGroup should run and give success result`() {
        val response = dataFactory.getOperatorSelectGroup()
        onGetOperatorSelectGroup_thenReturn(response)

        viewModel.getOperatorSelectGroup(MENU_ID)
        verifyGetOperatorSelectGroupRepoGetCalled()
        verifyGetOperatorSelectGroupSuccess(response)
        verifySetOperatorListSuccess(response.response.operatorGroups?.firstOrNull()?.operators ?: listOf())
    }

    @Test
    fun `when getting catalogSelectGroup should run and give fail result`() {
        onGetOperatorSelectGroup_thenReturn(NullPointerException())

        viewModel.getOperatorSelectGroup(MENU_ID)
        verifyGetOperatorSelectGroupRepoGetCalled()
        verifyGetOperatorSelectGroupFail()
    }

    @Test
    fun `given empty validator when validateClientNumber should set isEligibleToBuy true`() =
        runTest {
            val response = dataFactory.getOperatorSelectGroupEmptyValidation()
            onGetOperatorSelectGroup_thenReturn(response)

            viewModel.getOperatorSelectGroup(MENU_ID)
            viewModel.validateClientNumber(TokenListrikDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetOperatorSelectGroupSuccess(response)
            verifyValidateClientNumberTrue()
        }

    @Test
    fun `given non-empty validator when validateClientNumber with valid number should set isEligibleToBuy true`() =
        runTest {
            val response = dataFactory.getOperatorSelectGroup()
            onGetOperatorSelectGroup_thenReturn(response)

            viewModel.getOperatorSelectGroup(MENU_ID)
            viewModel.validateClientNumber(TokenListrikDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetOperatorSelectGroupSuccess(response)
            verifyValidateClientNumberTrue()
        }

    @Test
    fun `given non-empty validator when validateClientNumber with non-valid number should set isEligibleToBuy false`() =
        runTest {
            val response = dataFactory.getOperatorSelectGroup()
            onGetOperatorSelectGroup_thenReturn(response)

            viewModel.getOperatorSelectGroup(MENU_ID)
            viewModel.validateClientNumber(TokenListrikDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetOperatorSelectGroupSuccess(response)
            verifyValidateClientNumberFalse()
        }

    @Test
    fun `given validateClientNumber running when cancelValidatorJob called, the job should be cancelled`() {
        runTest {
            viewModel.validateClientNumber(TokenListrikDataFactory.VALID_CLIENT_NUMBER)
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
    fun `given layoutType is not match & other condition fulfilled when call isAutoSelectedProduct should return false`() {
        onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

        val result = viewModel.isAutoSelectedProduct(DenomWidgetEnum.MCCM_GRID_TYPE)
        verifyIsAutoSelectedProductFalse(result)
    }

    @Test
    fun `given layoutType is match & other condition fulfilled when call isAutoSelectedProduct should return true`() =
        runTest {
            // use empty validator to make isEligibleToBuy true
            val response = dataFactory.getOperatorSelectGroupEmptyValidation()
            onGetOperatorSelectGroup_thenReturn(response)
            onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.validateClientNumber(TokenListrikDataFactory.INVALID_CLIENT_NUMBER)
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
        runTest {
            // use empty validator & dummy selectedProduct to make isEligibleToBuy true
            val response = dataFactory.getOperatorSelectGroupEmptyValidation()
            onGetOperatorSelectGroup_thenReturn(response)
            onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.validateClientNumber(TokenListrikDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()
            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)

            verifyIsAutoSelectedProductTrue(isAutoSelect)
        }

    @Test
    fun `given isEligibleToBuy false & other condition fulfilled when isAutoSelectedProduct should return false`() =
        runTest {
            // use empty validator & dummy selectedProduct to make isEligibleToBuy true
            val response = dataFactory.getOperatorSelectGroup()
            onGetOperatorSelectGroup_thenReturn(response)
            onGetSelectedGridProduct_thenReturn(dataFactory.getSelectedProduct())

            viewModel.getOperatorSelectGroup(MENU_ID)
            viewModel.validateClientNumber(TokenListrikDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()

            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)
            verifyIsAutoSelectedProductFalse(isAutoSelect)
        }

    @Test
    fun `given selectedGridProduct pos less than 0 & other condition fulfilled when isAutoSelectedProduct should return false`() {
        runTest {
            // use empty validator & empty selectedProduct to make position < 0
            val response = dataFactory.getOperatorSelectGroupEmptyValidation()
            onGetOperatorSelectGroup_thenReturn(response)
            onGetSelectedGridProduct_thenReturn(dataFactory.getInvalidPositionSelectedProduct())

            viewModel.getOperatorSelectGroup(MENU_ID)
            viewModel.validateClientNumber(TokenListrikDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)
            verifyIsAutoSelectedProductFalse(isAutoSelect)
        }
    }

    @Test
    fun `given validatorJob null when implicit setValidatorJob executed should update validatorJob to non-null`() {
        viewModel.validatorJob = Job()
        verifyValidatorJobIsNotNull()
    }

    @Test
    fun `given catalogProductJob null when implicit setCatalogProductJob called should update catalogProductJob to non-null`() {
        viewModel.catalogProductJob = Job()
        verifyCatalogProductJobIsNotNull()
    }

    @Test
    fun `when cancelRecommendationJob called the job should be cancelled and live data should not emit`() {
        val response = dataFactory.getRecommendationData()
        val mappedResponse = mapperFactory.mapDigiPersoToRecommendation(response.digitalPersoData, true)
        onGetRecommendation_thenReturn(mappedResponse)

        viewModel.getRecommendations(listOf(), listOf(), listOf())
        viewModel.cancelRecommendationJob()
        verifyRecommendationJobIsCancelled()
        verifyGetRecommendationsRepoWasNotCalled()
        verifyGetRecommendationErrorCancellation()
    }

    @Test
    fun `given recommendationJob null when cancelRecommendationJob called should do nothing`() {
        viewModel.cancelRecommendationJob()
        verifyRecommendationJobIsNull()
    }

    @Test
    fun `given recommendationJob null when implicit setRecommendationJob called should update recommendationJob to non-null`() {
        viewModel.recommendationJob = Job()
        verifyRecommendationJobIsNotNull()
    }

    @Test
    fun `when calling implicit setRecomCheckoutUrl should update its value`() {
        val expectedResult = dataFactory.getRecomCardWidgetModelData()

        viewModel.recomCheckoutUrl = expectedResult.appUrl
        verifyRecomCheckoutUrlUpdated(expectedResult.appUrl)
    }

    @Test
    fun `when getting listInfo should run and give success result`() {
        val response = dataFactory.getOperatorSelectGroup()
        val expectedlistInfo = response.response.operatorGroups?.first()
            ?.operators?.first()?.attributes?.operatorDescriptions ?: listOf()
        onGetOperatorSelectGroup_thenReturn(response)

        viewModel.getOperatorSelectGroup(MENU_ID)
        verifyGetOperatorSelectGroupRepoGetCalled()
        verifyGetOperatorSelectGroupSuccess(response)

        val actualListInfo = viewModel.getListInfo()
        verifyListInfoSuccess(expectedlistInfo, actualListInfo)
    }

    @Test
    fun `when getting listInfo should run and give empty result`() {
        val actualListInfo = viewModel.getListInfo()
        verifyListInfoEmpty(actualListInfo)
    }

    @Test
    fun `when given list denom and productId should run and successfully get selected denom`() {
        val response = dataFactory.getCatalogInputMultiTabData()
        val mappedResponse = mapperFactory.mapTokenListrikDenom(response)
        val selectedDenom = dataFactory.getSelectedData(mappedResponse.listDenomData.get(0))
        val idDenom = "182"

        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.setAutoSelectedDenom(mappedResponse.listDenomData, idDenom)

        verifySelectedProductSuccess(selectedDenom)
    }

    @Test
    fun `when given list denom and productId should failed and failed get selected denom`() {
        val response = dataFactory.getCatalogInputMultiTabData()
        val mappedResponse = mapperFactory.mapTokenListrikDenom(response)
        val idDenom = "181"

        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.setAutoSelectedDenom(mappedResponse.listDenomData, idDenom)

        verifySelectedProductNull()
    }

    companion object {
        const val MENU_ID = 291
    }
}
