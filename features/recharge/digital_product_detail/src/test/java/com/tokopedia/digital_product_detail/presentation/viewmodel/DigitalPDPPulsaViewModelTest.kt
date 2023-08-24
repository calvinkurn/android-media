package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.common.topupbills.favoritepdp.data.mapper.FavoritePersoMapper
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.common.DigitalAtcErrorException
import com.tokopedia.digital_product_detail.data.mapper.DigitalAtcMapper
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.data.mapper.DigitalPersoMapper
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.presentation.data.PulsaDataFactory
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class DigitalPDPPulsaViewModelTest : DigitalPDPPulsaViewModelTestFixture() {

    private val dataFactory = PulsaDataFactory()
    private val mapperFactory = DigitalDenomMapper()
    private val persoMapperFactory = FavoritePersoMapper()
    private val mapAtcFactory = DigitalAtcMapper()
    private val digitalPersoMapperFactory = DigitalPersoMapper()

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
    fun `when getting recommendation should run and give success result`() =
        runTest {
            val response = dataFactory.getRecommendationData()
            val mappedResponse =
                mapperFactory.mapDigiPersoToRecommendation(response.digitalPersoData, false)
            onGetRecommendation_thenReturn(mappedResponse)

            viewModel.getRecommendations(listOf(), listOf())
            skipRecommendationDelay()
            verifyGetRecommendationsRepoGetCalled()
            verifyGetRecommendationSuccess(mappedResponse)
        }

    @Test
    fun `when getting recommendation should run and give fail result`() =
        runTest {
            onGetRecommendation_thenReturn(NullPointerException())

            viewModel.getRecommendations(listOf(), listOf())
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
        val favoriteNumberTypes = listOf(
            FavoriteNumberType.CHIP,
            FavoriteNumberType.LIST,
            FavoriteNumberType.PREFILL
        )
        onGetFavoriteNumber_thenReturn(mappedResponse)

        viewModel.getFavoriteNumbers(listOf(), favoriteNumberTypes)
        verifyGetFavoriteNumberChipsRepoGetCalled()
        verifyGetFavoriteNumberChipsSuccess(mappedResponse.favoriteChips)
        verifyGetFavoriteNumberListSuccess(mappedResponse.autoCompletes)
        verifyGetFavoriteNumberPrefillSuccess(mappedResponse.prefill)
        verifyGetFavoriteNumberPrefillEmpty()
    }

    @Test
    fun `when getting favoriteNumber without prefill should run and give success result`() {
        val response = dataFactory.getFavoriteNumberData(false)
        val mappedResponse = persoMapperFactory.mapDigiPersoFavoriteToModel(response)
        val favoriteNumberTypes = listOf(FavoriteNumberType.CHIP, FavoriteNumberType.LIST)
        onGetFavoriteNumber_thenReturn(mappedResponse)

        viewModel.getFavoriteNumbers(listOf(), favoriteNumberTypes)
        verifyGetFavoriteNumberChipsRepoGetCalled()
        verifyGetFavoriteNumberChipsSuccess(mappedResponse.favoriteChips)
        verifyGetFavoriteNumberListSuccess(mappedResponse.autoCompletes)
        verifyGetFavoriteNumberPrefillNull()
    }

    @Test
    fun `when getting rechargeCheckBalance should run and give success result`() {
        val response = dataFactory.getRechargeCheckBalanceData()
        val mappedResponse = digitalPersoMapperFactory.mapDigiPersoToCheckBalanceModel(response.digitalPersoData)
        onGetRechargeCheckBalance_thenReturn(mappedResponse)

        viewModel.getRechargeCheckBalance(listOf(), listOf())
        verifyGetRechargeCheckBalanceRepoGetCalled()
        verifyGetRechargeCheckBalanceSuccess(mappedResponse)
    }

    @Test
    fun `when getting rechargeCheckBalance should run and givefail result`() {
        val exception = MessageErrorException("Tokopedia")
        onGetRechargeCheckBalance_thenReturn(exception)

        viewModel.getRechargeCheckBalance(listOf(), listOf())
        verifyGetRechargeCheckBalanceRepoGetCalled()
        verifyGetRechargeCheckBalanceFail()
    }

    @Test
    fun `given rechargeCheckBalance loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setRechargeCheckBalanceLoading()
        verifyGetRechargeCheckBalanceLoading(loadingResponse)
    }

    @Test
    fun `when saveRechargeUserAccessToken called should run and give success result`() {
        val response = dataFactory.saveRechargeUserAccessToken()
        val mappedResponse = digitalPersoMapperFactory.mapSaveAccessTokenToAccessTokenResultModel(response.rechargeSaveTelcoUserBalanceAccessToken)
        onSaveRechargeUserAccessToken(mappedResponse)

        viewModel.saveRechargeUserAccessToken("", "")
        verifySaveRechargeUserAccessTokenSuccess(mappedResponse)
        verifySaveRechargeUserAccessTokenGetCalled()
    }

    @Test
    fun `when saveRechargeUserAccessToken called should run and give fail result`() {
        val exception = MessageErrorException("Tokopedia")
        onSaveRechargeUserAccessToken(exception)

        viewModel.saveRechargeUserAccessToken("", "")
        verifySaveRechargeUserAccessTokenGetCalled()
        verifySaveRechargeUserAccessTokenFail()
    }

    @Test
    fun `given save loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setRechargeUserAccessTokenLoading()
        verifySaveRechargeUserAccessTokenLoading(loadingResponse)
    }

    @Test
    fun `given catalogPrefixSelect loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setPrefixOperatorLoading()
        verifyPrefixOperatorLoading(loadingResponse)
    }

    @Test
    fun `when getting catalogPrefixSelect should run and give success result`() =
        runTest {
            val response = dataFactory.getPrefixOperatorData()
            onGetPrefixOperator_thenReturn(response)

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()

            verifyGetOperatorListRepoGetCalled()
            verifyGetPrefixOperatorSuccess(response)
        }

    @Test
    fun `when getting catalogPrefixSelect should run and give success fail`() =
        runTest {
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
        runTest {
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
        runTest {
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
        runTest {
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
    fun `given validateClientNumber running when cancelValidatorJob called, the job should be cancelled`() {
        runTest {
            viewModel.validateClientNumber(PulsaDataFactory.VALID_CLIENT_NUMBER)
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
    fun `given selectedFullProduct position default when updateSelectedPositionId should update position `() {
        viewModel.selectedGridProduct = SelectedProduct()
        val newPosition = 2

        viewModel.updateSelectedPositionId(newPosition)
        verifyUpdateSelectedPositionIdTrue(newPosition)
    }

    @Test
    fun `given selectedFullProduct position null when updateSelectedPositionId should not update position `() {
        viewModel.selectedGridProduct = SelectedProduct()
        val newPosition = null
        val defaultPosition = -1

        viewModel.updateSelectedPositionId(newPosition)
        verifyUpdateSelectedPositionIdTrue(defaultPosition)
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
        runTest {
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
        runTest {
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
    fun `given selectedGridProduct pos less than 0 & other condition fulfilled when isAutoSelectedProduct should return false`() {
        runTest {
            // use empty validator & empty selectedProduct to make position < 0
            val response = dataFactory.getPrefixOperatorEmptyValData()
            onGetPrefixOperator_thenReturn(response)
            onGetSelectedGridProduct_thenReturn(dataFactory.getInvalidPositionSelectedProduct())

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()
            viewModel.validateClientNumber(PulsaDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            val isAutoSelect = viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)
            verifyIsAutoSelectedProductFalse(isAutoSelect)
        }
    }

    @Test
    fun `when getting catalogInputMultitab should run and give success result`() =
        runTest {
            val response = dataFactory.getCatalogInputMultiTabData()
            val mappedResponse = mapperFactory.mapMultiTabGridDenom(response)
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
    fun `when getting catalogInputMultitab should run and give error result`() =
        runTest {
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
        val mappedResponse = mapperFactory.mapMultiTabGridDenom(response)
        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        viewModel.cancelCatalogProductJob()
        verifyCatalogProductJobIsCancelled()
        verifyGetProductInputMultiTabRepoWasNotCalled()
        verifyGetCatalogInputMultitabErrorCancellation()
    }

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
    fun `when cancelRecommendationJob called the job should be cancelled and live data should not emit`() {
        val response = dataFactory.getRecommendationData()
        val mappedResponse =
            mapperFactory.mapDigiPersoToRecommendation(response.digitalPersoData, true)
        onGetRecommendation_thenReturn(mappedResponse)

        viewModel.getRecommendations(listOf(), listOf())
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
    fun `when getting addToCart should run and return success`() {
        val response = mapAtcFactory.mapAtcToResult(dataFactory.getAddToCartData())
        onGetAddToCart_thenReturn(response)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartSuccess(response)
    }

    @Test
    fun `when getting addToCart should run and return notNull error from gql`() {
        val error = dataFactory.getErrorAtcFromGql()
        onGetAddToCart_thenReturn(error)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartErrorNotEmpty(dataFactory.getErrorAtc())
    }

    @Test
    fun `when getting addToCart should run and return DigitalAtcErrorException when get error atc`() {
        val error = DigitalAtcErrorException(dataFactory.errorAtcResponse)
        onGetAddToCart_thenReturn(error)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartErrorNotEmpty(dataFactory.getErrorAtc())
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
    fun `when calling implicit setRecomCheckoutUrl should update its value`() {
        val expectedResult = dataFactory.getRecomCardWidgetModelData()

        viewModel.recomCheckoutUrl = expectedResult.appUrl
        verifyRecomCheckoutUrlUpdated(expectedResult.appUrl)
    }

    @Test
    fun `when given list denom and productId should run and successfully get selected denom`() {
        val response = dataFactory.getCatalogInputMultiTabData()
        val mappedResponse = mapperFactory.mapMultiTabGridDenom(response)
        val selectedDenom =
            dataFactory.getSelectedData(mappedResponse.denomWidgetModel.listDenomData.get(0))
        val idDenom = "1"

        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.setAutoSelectedDenom(mappedResponse.denomWidgetModel.listDenomData, idDenom)

        verifySelectedProductSuccess(selectedDenom)
    }

    @Test
    fun `when given list denom and productId should failed and failed get selected denom`() {
        val response = dataFactory.getCatalogInputMultiTabData()
        val mappedResponse = mapperFactory.mapMultiTabGridDenom(response)
        val idDenom = "0"

        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.setAutoSelectedDenom(mappedResponse.denomWidgetModel.listDenomData, idDenom)

        verifySelectedProductNull()
    }

    @Test
    fun `given empty clientNumberThrottleJob when calling runThrottleJob should init new job`() =
        runTest {
            viewModel.clientNumberThrottleJob = null
            viewModel.runThrottleJob {
                // Simulate nothing
            }
            verifyClientNumberThrottleJobIsNotNull()
            verifyClientNumberThrottleJobIsActive()
        }

    @Test
    fun `given non-empty clientNumberThrottleJob when wait for DELAY_CLIENT_NUMBER_TRANSITION the job should done`() =
        runTest {
            viewModel.runThrottleJob {
                // Simulate nothing
            }
            skipClientNumberTransitionDelay()
            verifyClientNumberThrottleJobIsNotNull()
            verifyClientNumberThrottleJobIsCompleted()
        }

    @Test
    fun `given clientNumberThrottleJob running when calling another job should not init job`() =
        runTest {
            viewModel.runThrottleJob {
                // Simulate nothing
            }
            val jobA = viewModel.clientNumberThrottleJob

            viewModel.runThrottleJob {
                // Simulate nothing
            }
            val jobB = viewModel.clientNumberThrottleJob

            verifyClientNumberThrottleJobSame(jobA, jobB)
        }

    @Test
    fun `given clientNumberThrottleJob completed when calling another job should init new job`() =
        runTest {
            viewModel.runThrottleJob {
                // Simulate nothing
            }
            val jobA = viewModel.clientNumberThrottleJob
            skipClientNumberTransitionDelay()
            verifyClientNumberThrottleJobIsCompleted()

            viewModel.runThrottleJob {
                // Simulate nothing
            }
            val jobB = viewModel.clientNumberThrottleJob

            verifyClientNumberThrottleJobNotSame(jobA, jobB)
        }

    @Test
    fun `when given list denom and list mccm is not empty, isEmptyDenomMCCM should return true`() {
        val listDenom = listOf(DenomData())
        val listMccm = listOf(DenomData())

        val expectedResult = viewModel.isEmptyDenomMCCM(listDenom, listMccm)

        verifyDenomAndMCCMIsNotEmpty(expectedResult)
    }

    @Test
    fun `when given list denom empty and list mccm is not empty, isEmptyDenomMCCM should return true`() {
        val listDenom = listOf<DenomData>()
        val listMccm = listOf(DenomData())

        val expectedResult = viewModel.isEmptyDenomMCCM(listDenom, listMccm)

        verifyDenomAndMCCMIsNotEmpty(expectedResult)
    }

    @Test
    fun `when given list denom is not empty and list mccm is empty, isEmptyDenomMCCM should return true`() {
        val listDenom = listOf(DenomData())
        val listMccm = listOf<DenomData>()

        val expectedResult = viewModel.isEmptyDenomMCCM(listDenom, listMccm)

        verifyDenomAndMCCMIsNotEmpty(expectedResult)
    }

    @Test
    fun `when given list denom is empty and list mccm is empty, isEmptyDenomMCCM should return false`() {
        val listDenom = listOf<DenomData>()
        val listMccm = listOf<DenomData>()

        val expectedResult = viewModel.isEmptyDenomMCCM(listDenom, listMccm)

        verifyDenomAndMCCMIsEmpty(expectedResult)
    }

    @Test
    fun `given checkBalanceFailCounter is zero, isCheckBalanceFailedMoreThanThreeTimes should return false`() {
        viewModel.checkBalanceFailCounter = 0

        val actualResult = viewModel.isCheckBalanceFailedMoreThanThreeTimes()
        Assert.assertFalse(actualResult)
    }

    @Test
    fun `given checkBalanceFailCounter is three, isCheckBalanceFailedMoreThanThreeTimes should return true`() {
        viewModel.checkBalanceFailCounter = 3

        val actualResult = viewModel.isCheckBalanceFailedMoreThanThreeTimes()
        Assert.assertTrue(actualResult)
    }

    companion object {
        const val MENU_ID = 289
    }
}
