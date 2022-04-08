package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_product_detail.data.mapper.DigitalAtcMapper
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.presentation.data.DataPlanDataFactory
import kotlinx.coroutines.CancellationException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlinx.coroutines.Job

@ExperimentalCoroutinesApi
class DigitalPDPDataPlanViewModelTest: DigitalPDPDataPlanViewModelTestFixture() {

    private val dataFactory = DataPlanDataFactory()
    private val mapperFactory = DigitalDenomMapper()
    private val mapAtcFactory = DigitalAtcMapper()

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
    fun `when getting recommendation should run and give success result`() = testCoroutineRule.runBlockingTest {
        val response = dataFactory.getRecommendationData()
        val mappedResponse = mapperFactory.mapDigiPersoToRecommendation(response.recommendationData, true)
        onGetRecommendation_thenReturn(mappedResponse)

        viewModel.getRecommendations(listOf(), listOf())
        skipRecommendationDelay()
        verifyGetRecommendationsRepoGetCalled()
        verifyGetRecommendationSuccess(mappedResponse)
    }

    @Test
    fun `when getting recommendation should run and give fail result`() = testCoroutineRule.runBlockingTest {
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
        onGetFavoriteNumber_thenReturn(response)

        viewModel.getFavoriteNumbers(listOf(), listOf())
        verifyGetFavoriteNumberChipsRepoGetCalled()
        verifyGetFavoriteNumberChipsSuccess(response.favoriteNumberChips.persoFavoriteNumber.items)
        verifyGetFavoriteNumberListSuccess(response.favoriteNumberChips.persoFavoriteNumber.items)
        verifyGetFavoriteNumberPrefillSuccess(response.favoriteNumberChips.persoFavoriteNumber.items)
    }

    @Test
    fun `when getting favoriteNumber without prefill (or any type) should run and give success result with empty default`() {
        val response = dataFactory.getFavoriteNumberData(false)
        onGetFavoriteNumber_thenReturn(response)

        viewModel.getFavoriteNumbers(listOf(), listOf())
        verifyGetFavoriteNumberChipsRepoGetCalled()
        verifyGetFavoriteNumberChipsSuccess(response.favoriteNumberChips.persoFavoriteNumber.items)
        verifyGetFavoriteNumberListSuccess(response.favoriteNumberChips.persoFavoriteNumber.items)
        verifyGetFavoriteNumberPrefillSuccess(response.favoriteNumberChips.persoFavoriteNumber.items)
        verifyGetFavoriteNumberPrefillEmpty()
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
    fun `when getting catalogInputMultitab should run and give success result and updated data filter`() = testCoroutineRule.runBlockingTest {
        val response = dataFactory.getCatalogInputMultiTabData()
        val isRefreshedFilter = true
        val mappedResponse = mapperFactory.mapMultiTabFullDenom(response, isRefreshedFilter)
        val filterResponse = mappedResponse.filterTagComponents
        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        skipMultitabDelay()
        verifyGetProductInputMultiTabRepoGetCalled()
        verifyGetCatalogInputMultitabSuccess(mappedResponse)
        verifyGetFilterTagComponentSuccess(filterResponse)
    }

    @Test
    fun `when getting catalogInputMultitab should run and give success result and empty data filter`() = testCoroutineRule.runBlockingTest {
        val response = dataFactory.getCatalogInputMultiTabData()
        val isRefreshedFilter = false
        val mappedResponse = mapperFactory.mapMultiTabFullDenom(response, isRefreshedFilter)
        onGetCatalogInputMultitabisFiltered_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "", isRefreshedFilter)
        skipMultitabDelay()
        verifyGetProductInputMultiTabRepoIsRefreshedGetCalled()
        verifyGetCatalogInputMultitabSuccess(mappedResponse)
        verifyGetFilterTagComponentEmpty()
    }

    @Test
    fun `given selectedFullProduct non-empty when onResetSelectedProduct should reset product`() {
        onGetSelectedFullProduct_thenReturn(dataFactory.getSelectedProduct())

        viewModel.onResetSelectedProduct()
        verifySelectedFullProductEmpty()
    }

    @Test
    fun `given selectedFullProduct position default when updateSelectedPositionId should update position `() {
        viewModel.selectedFullProduct = SelectedProduct()
        val newPosition = 2

        viewModel.updateSelectedPositionId(newPosition)
        verifyUpdateSelectedPositionIdTrue(newPosition)
    }

    @Test
    fun `given selectedFullProduct position null default when updateSelectedPositionId should not update position `() {
        viewModel.selectedFullProduct = SelectedProduct()
        val newPosition = null
        val defaultPosition = -1

        viewModel.updateSelectedPositionId(newPosition)
        verifyUpdateSelectedPositionIdTrue(defaultPosition)
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
        val response = mapAtcFactory.mapAtcToResult(dataFactory.getAddToCartData())
        onGetAddToCart_thenReturn(response)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartSuccess(response)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException should return custom message`() {
        val errorMessage = "error"
        val errorResponseException = ResponseErrorException(errorMessage)
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException but empty message should return default message`() {
        val errorMessage = "Terjadi kesalahan, ulangi beberapa saat lagi"
        val errorResponseException = ResponseErrorException()
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get any error other than ResponseErrorException should return that error`() {
        val errorMessageException = MessageErrorException()
        onGetAddToCart_thenReturn(errorMessageException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
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
        val isRefreshedFilter = false
        val mappedResponse = mapperFactory.mapMultiTabFullDenom(response, isRefreshedFilter)
        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "", isRefreshedFilter)
        viewModel.cancelCatalogProductJob()
        verifyCatalogProductJobIsCancelled()
        verifyGetProductInputMultiTabRepoWasNotCalled()
        verifyGetCatalogInputMultitabErrorCancellation()
    }

    @Test
    fun `when cancelRecommendationJob called the job should be cancelled and live data should not emit`() {
        val response = dataFactory.getRecommendationData()
        val mappedResponse = mapperFactory.mapDigiPersoToRecommendation(response.recommendationData, true)
        onGetRecommendation_thenReturn(mappedResponse)

        viewModel.getRecommendations(listOf(), listOf())
        viewModel.cancelRecommendationJob()
        verifyRecommendationJobIsCancelled()
        verifyGetRecommendationRepoWasNotCalled()
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
    fun `given filterData with some isSelected and should updated filterDataParams` () {
        val initialFilter = dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents
        viewModel.updateFilterData(initialFilter)
        verifyGetFilterTagComponentSuccess(initialFilter)
        verifyGetFilterParamEmpty(dataFactory.getFilterParamsEmpty())

        val selectedFilter = dataFactory.getFilterTagListSelectedData()
        viewModel.updateFilterData(selectedFilter)
        verifyGetFilterTagComponentSuccess(selectedFilter)
        verifyGetFilterParam(dataFactory.getFilterParams())
    }

    @Test
    fun `given filterData and should return that same filterData`() {
       val initialFilter = dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents
       viewModel.filterData = initialFilter

       verifyGetFilterTagComponentSuccess(initialFilter)
    }

    @Test
    fun `when calling implicit setRecomCheckoutUrl should update its value`() {
        val expectedResult = dataFactory.getRecomCardWidgetModelData()

        viewModel.recomCheckoutUrl = expectedResult.appUrl
        verifyRecomCheckoutUrlUpdated(expectedResult.appUrl)
    }

    @Test
    fun  `when given list denom and productId should run and successfully get selected denom`() {
        val response = dataFactory.getCatalogInputMultiTabData()
        val isRefreshedFilter = true
        val mappedResponse = mapperFactory.mapMultiTabFullDenom(response, isRefreshedFilter)
        val selectedDenom = dataFactory.getSelectedData(mappedResponse.denomFull.listDenomData.get(0))
        val idDenom = "10930"

        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.setAutoSelectedDenom(mappedResponse.denomFull.listDenomData, idDenom)

        verifySelectedProductSuccess(selectedDenom)

    }

    @Test
    fun  `when given list denom and productId should failed and failed get selected denom`() {
        val response = dataFactory.getCatalogInputMultiTabData()
        val isRefreshedFilter = true
        val mappedResponse = mapperFactory.mapMultiTabFullDenom(response, isRefreshedFilter)
        val idDenom = "1"

        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.setAutoSelectedDenom(mappedResponse.denomFull.listDenomData, idDenom)

        verifySelectedProductNull()
    }

    @Test
    fun `given empty clientNumberThrottleJob when calling runThrottleJob should init new job`() =
        testCoroutineRule.runBlockingTest {
            viewModel.clientNumberThrottleJob = null
            viewModel.runThrottleJob {
                // Simulate nothing
            }
            verifyClientNumberThrottleJobIsNotNull()
            verifyClientNumberThrottleJobIsActive()
        }

    @Test
    fun `given non-empty clientNumberThrottleJob when wait for DELAY_CLIENT_NUMBER_TRANSITION the job should done`() =
        testCoroutineRule.runBlockingTest {
            viewModel.runThrottleJob {
                // Simulate nothing
            }
            skipClientNumberTransitionDelay()
            verifyClientNumberThrottleJobIsNotNull()
            verifyClientNumberThrottleJobIsCompleted()
        }

    @Test
    fun `given clientNumberThrottleJob running when calling another job should not create new job instance`() =
        testCoroutineRule.runBlockingTest {
            viewModel.runThrottleJob {  }
            val jobA = viewModel.clientNumberThrottleJob

            viewModel.runThrottleJob {  }
            val jobB = viewModel.clientNumberThrottleJob

            verifyClientNumberThrottleJobSame(jobA, jobB)
        }


    @Test
    fun `given clientNumberThrottleJob completed when calling another job should init new job`() =
        testCoroutineRule.runBlockingTest {
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
    fun `when resetFilter is used, filterData must be reseted`() {
        val initialFilter = dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents
        viewModel.updateFilterData(initialFilter)
        verifyGetFilterTagComponentSuccess(initialFilter)
        verifyGetFilterParamEmpty(dataFactory.getFilterParamsEmpty())

        viewModel.resetFilter()
        verifyGetFilterTagComponentSuccess(initialFilter)
    }

    @Test
    fun `when filter not changed not return changed status` () {
        val initialFilter = dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents
        viewModel.updateFilterData(initialFilter)

        val result = viewModel.isFilterChanged(initialFilter)
        verifyFilterIsNotChanged(result)
    }

    @Test
    fun `when filter changed return changed status` () {
        val initialFilter = dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents
        val changedFilter = dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents.toMutableList()
        changedFilter[0].filterTagDataCollections[0].isSelected = true
        viewModel.updateFilterData(initialFilter)

        val result = viewModel.isFilterChanged(changedFilter)
        verifyFilterIsChanged(result)
    }

    @Test
    fun `when filter changed size return changed status` () {
        val initialFilter = dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents
        val changedFilter = listOf(dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents.toMutableList().removeAt(1))
        viewModel.updateFilterData(initialFilter)

        val result = viewModel.isFilterChanged(changedFilter)
        verifyFilterIsChanged(result)
    }

    companion object {
        const val MENU_ID = 290
    }
}