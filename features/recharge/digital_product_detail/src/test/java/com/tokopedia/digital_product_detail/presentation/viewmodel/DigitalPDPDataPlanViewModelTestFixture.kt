package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.source.ContactDataSource
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteGroupModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceModel
import com.tokopedia.digital_product_detail.domain.model.DigitalSaveAccessTokenResultModel
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class DigitalPDPDataPlanViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    protected lateinit var viewModel: DigitalPDPDataPlanViewModel

    @RelaxedMockK
    lateinit var repo: DigitalPDPTelcoRepository

    @RelaxedMockK
    lateinit var contactDataSource: ContactDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DigitalPDPDataPlanViewModel(repo, contactDataSource, testCoroutineRule.dispatchers)
    }

    protected fun onGetMenuDetail_thenReturn(response: MenuDetailModel) {
        coEvery {
            repo.getMenuDetail(any())
        } returns response
    }

    protected fun onGetMenuDetail_thenReturn(error: Throwable) {
        coEvery {
            repo.getMenuDetail(any())
        } throws error
    }

    protected fun onGetRecommendation_thenReturn(response: RecommendationWidgetModel) {
        coEvery {
            repo.getRecommendations(any(), any(), any(), any(), true)
        } returns response
    }

    protected fun onGetRecommendation_thenReturn(error: Throwable) {
        coEvery {
            repo.getRecommendations(any(), any(), any(), any(), true)
        } throws error
    }

    protected fun onGetMCCM_thenReturn(response: DenomWidgetModel) {
        coEvery {
            repo.getMCCMProducts(any(), any(), any(), any())
        } returns response
    }

    protected fun onGetMCCM_thenReturn(error: Throwable) {
        coEvery {
            repo.getMCCMProducts(any(), any(), any(), any())
        } throws error
    }

    protected fun onGetFavoriteNumber_thenReturn(response: FavoriteGroupModel) {
        coEvery {
            repo.getFavoriteNumbers(any(), any(), any())
        } returns response
    }

    protected fun onGetFavoriteNumber_thenReturn(error: Throwable) {
        coEvery {
            repo.getFavoriteNumbers(any(), any(), any())
        } throws error
    }

    protected fun onGetPrefixOperator_thenReturn(response: TelcoCatalogPrefixSelect) {
        coEvery {
            repo.getOperatorList(any())
        } returns response
    }

    protected fun onGetPrefixOperator_thenReturn(error: Throwable) {
        coEvery {
            repo.getOperatorList(any())
        } throws error
    }

    protected fun onGetCatalogInputMultitab_thenReturn(response: InputMultiTabDenomModel) {
        coEvery {
            repo.getProductInputMultiTabDenomFull(any(), any(), any(), any())
        } returns response
    }

    protected fun onGetCatalogInputMultitabisFiltered_thenReturn(response: InputMultiTabDenomModel) {
        coEvery {
            repo.getProductInputMultiTabDenomFull(any(), any(), any(), any(), any())
        } returns response
    }

    protected fun onGetCatalogInputMultitab_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.getProductInputMultiTabDenomFull(any(), any(), any(), any())
        } throws errorThrowable
    }

    protected fun onGetAddToCart_thenReturn(response: DigitalAtcResult) {
        coEvery {
            repo.addToCart(any(), any(), any(), "")
        } returns response
    }

    protected fun onGetAddToCart_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.addToCart(any(), any(), any(), "")
        } throws errorThrowable
    }

    protected fun onGetRechargeCheckBalance_thenReturn(response: DigitalCheckBalanceModel) {
        coEvery {
            repo.getRechargeCheckBalance(any(), any(), any(), any())
        } returns response
    }

    protected fun onGetRechargeCheckBalance_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.getRechargeCheckBalance(any(), any(), any(), any())
        } throws errorThrowable
    }

    protected fun onSaveRechargeUserAccessToken(response: DigitalSaveAccessTokenResultModel) {
        coEvery {
            repo.saveRechargeUserBalanceAccessToken(any(), any())
        } returns response
    }

    protected fun onSaveRechargeUserAccessToken(errorThrowable: Throwable) {
        coEvery {
            repo.saveRechargeUserBalanceAccessToken(any(), any())
        } throws errorThrowable
    }

    protected fun onGetAddToCartMultiChekout_thenReturn(response: DigitalAtcResult) {
        coEvery {
            repo.addToCart(any(), any(), any(), "pdp_to_multi_checkout")
        } returns response
    }

    protected fun onGetSelectedFullProduct_thenReturn(result: SelectedProduct) {
        viewModel.selectedFullProduct = result
    }

    protected fun verifyGetMenuDetailRepoGetCalled() {
        coVerify { repo.getMenuDetail(any()) }
    }

    protected fun verifyGetRecommendationsRepoGetCalled() {
        coVerify { repo.getRecommendations(any(), any(), any(), any(), true) }
    }

    protected fun verifyGetRecommendationRepoWasNotCalled() {
        coVerify { repo.getRecommendations(any(), any(), any(), any(), true) wasNot Called }
    }

    protected fun verifyGetMCCMRepoGetCalled() {
        coVerify { repo.getMCCMProducts(any(), any(), any(), any()) }
    }

    protected fun verifyGetMCCMRepoWasNotCalled() {
        coVerify { repo.getMCCMProducts(any(), any(), any(), any()) wasNot Called }
    }

    protected fun verifyGetFavoriteNumberChipsRepoGetCalled() {
        coVerify { repo.getFavoriteNumbers(any(), any(), any()) }
    }

    protected fun verifyGetOperatorListRepoGetCalled() {
        coVerify { repo.getOperatorList(any()) }
    }

    protected fun verifyGetProductInputMultiTabRepoGetCalled() {
        coVerify { repo.getProductInputMultiTabDenomFull(any(), any(), any(), any()) }
    }

    protected fun verifyGetProductInputMultiTabRepoWasNotCalled() {
        coVerify { repo.getProductInputMultiTabDenomFull(any(), any(), any(), any()) wasNot Called }
    }

    protected fun verifyGetProductInputMultiTabRepoIsRefreshedGetCalled() {
        coVerify { repo.getProductInputMultiTabDenomFull(any(), any(), any(), any(), any()) }
    }

    protected fun verifyAddToCartRepoGetCalled() {
        coVerify { repo.addToCart(any(), any(), any(), "") }
    }

    protected fun verifyAddToCartMultiCheckoutRepoGetCalled() {
        coVerify { repo.addToCart(any(), any(), any(), "pdp_to_multi_checkout") }
    }

    protected fun verifyGetRechargeCheckBalanceRepoGetCalled() {
        coVerify { repo.getRechargeCheckBalance(any(), any(), any(), any()) }
    }

    protected fun verifySaveRechargeUserAccessTokenGetCalled() {
        coVerify { repo.saveRechargeUserBalanceAccessToken(any(), any()) }
    }

    protected fun verifyGetFavoriteNumberLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.favoriteChipsData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetFavoriteNumberChipsSuccess(expectedResponse: List<FavoriteChipModel>) {
        val actualResponse = viewModel.favoriteChipsData.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetFavoriteNumberListSuccess(expectedResponse: List<AutoCompleteModel>) {
        val actualResponse = viewModel.autoCompleteData.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetFavoriteNumberPrefillSuccess(expectedResponse: PrefillModel) {
        val actualResponse = viewModel.prefillData.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetFavoriteNumberPrefillEmpty() {
        val actualResponse = viewModel.prefillData.value as RechargeNetworkResult.Success
        Assert.assertTrue(actualResponse.data.clientName.isEmpty())
        Assert.assertTrue(actualResponse.data.clientNumber.isEmpty())
    }

    protected fun verifyGetFavoriteNumberPrefillNull() {
        Assert.assertNull(viewModel.prefillData.value)
    }

    protected fun verifyPrefixOperatorLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.catalogPrefixSelect.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetPrefixOperatorSuccess(expectedResponse: TelcoCatalogPrefixSelect) {
        val actualResponse = viewModel.catalogPrefixSelect.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetCatalogInputMultitabSuccess(expectedResponse: InputMultiTabDenomModel) {
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetFilterTagComponentSuccess(expectedResponse: List<TelcoFilterTagComponent>) {
        val actualResponse = viewModel.filterData
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetFilterParam(expectedResponse: ArrayList<HashMap<String, Any>>) {
        val actualResponse = viewModel.filterDataParams
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetFilterParamEmpty(expectedResponse: ArrayList<HashMap<String, Any>>) {
        val actualResponse = viewModel.filterDataParams
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetFilterTagComponentEmpty() {
        val actualResponse = viewModel.filterData
        Assert.assertEquals(emptyList<TelcoFilterTagComponent>(), actualResponse)
    }

    protected fun verifyGetCatalogInputMultitabLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetCatalogInputMultitabError(expectedResponse: Throwable) {
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Fail).error)
    }

    protected fun verifyGetCatalogInputMultitabErrorCancellation() {
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertNull(actualResponse)
    }

    protected fun verifyAddToCartSuccess(expectedResponse: DigitalAtcResult) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyAddToCartMultiChekoutSuccess(expectedResponse: DigitalAtcResult) {
        val actualResponse = viewModel.addToCartMultiCheckoutResult.value
        Assert.assertEquals(
            expectedResponse,
            actualResponse
        )
    }

    protected fun verifyAddToCartErrorNotEmpty(expectedResponse: ErrorAtc) {
        val actualResponse = viewModel.errorAtc.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyAddToCartError(expectedResponse: Throwable) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(
            expectedResponse.message,
            (actualResponse as RechargeNetworkResult.Fail).error.message
        )
    }

    protected fun verifyAddToCartErrorExceptions(expectedResponse: Throwable) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Fail).error)
    }

    protected fun verifyAddToCartErrorLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetPrefixOperatorFail() {
        val actualResponse = viewModel.catalogPrefixSelect.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyGetMenuDetailLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.menuDetailData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetMenuDetailSuccess(expectedResponse: MenuDetailModel) {
        val actualResponse = viewModel.menuDetailData.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetMenuDetailFail() {
        val actualResponse = viewModel.menuDetailData.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyGetRecommendationLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.recommendationData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetRecommendationSuccess(expectedResponse: RecommendationWidgetModel) {
        val actualResponse = viewModel.recommendationData.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetRecommendationFail() {
        val actualResponse = viewModel.recommendationData.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyGetRecommendationErrorCancellation() {
        val actualResponse = viewModel.recommendationData.value
        Assert.assertNull(actualResponse)
    }

    protected fun verifyGetMCCMSuccess(expectedResponse: DenomWidgetModel) {
        val actualResponse = viewModel.mccmProductsData.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetMCCMFail() {
        val actualResponse = viewModel.mccmProductsData.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyGetRechargeCheckBalanceLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.indosatCheckBalance.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetRechargeCheckBalanceSuccess(expectedResponse: DigitalCheckBalanceModel) {
        val actuaLResponse = viewModel.indosatCheckBalance.value
        Assert.assertEquals(
            expectedResponse,
            (actuaLResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetRechargeCheckBalanceFail() {
        val actualResponse = viewModel.indosatCheckBalance.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyGetRechargeCheckBalanceIsCancelled() {
        Assert.assertTrue(viewModel.checkBalanceJob?.isCancelled == true)
    }

    protected fun verifySaveRechargeUserAccessTokenLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.saveAccessTokenResult.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifySaveRechargeUserAccessTokenSuccess(expectedResponse: DigitalSaveAccessTokenResultModel) {
        val actualResponse = viewModel.saveAccessTokenResult.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifySaveRechargeUserAccessTokenFail() {
        val actualResponse = viewModel.saveAccessTokenResult.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyMCCMProductsbLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.mccmProductsData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetMCCMErrorCancellation() {
        val actualResponse = viewModel.mccmProductsData.value
        Assert.assertNull(actualResponse)
    }

    protected fun verifyCheckoutPassDataUpdated(expectedResult: DigitalCheckoutPassData) {
        val actualResult = viewModel.digitalCheckoutPassData
        assertDigitalCheckoutPassDataEqual(expectedResult, actualResult)
    }

    protected fun verifyCheckoutPassDataCategoryIdUpdated(expectedCategoryId: String) {
        val actualCategoryId = viewModel.digitalCheckoutPassData.categoryId
        Assert.assertEquals(expectedCategoryId, actualCategoryId)
    }

    protected fun verifyCheckoutPassDataCategoryIdEmpty() {
        val actualCategoryId = viewModel.digitalCheckoutPassData.categoryId
        Assert.assertTrue(actualCategoryId == EMPTY || actualCategoryId == null)
    }

    protected fun verifyValidateClientNumberTrue() {
        Assert.assertTrue(viewModel.isEligibleToBuy)
        Assert.assertEquals(
            viewModel.clientNumberValidatorMsg.value,
            EMPTY
        )
    }

    protected fun verifyValidateClientNumberFalse() {
        Assert.assertFalse(viewModel.isEligibleToBuy)
        Assert.assertNotEquals(
            viewModel.clientNumberValidatorMsg.value,
            EMPTY
        )
    }

    protected fun verifySelectedFullProductNonEmpty() {
        Assert.assertEquals(
            viewModel.selectedFullProduct.position,
            POSITION_0
        )
    }

    protected fun verifySelectedFullProductEmpty() {
        Assert.assertEquals(
            viewModel.selectedFullProduct.position,
            POSITION_DEFAULT
        )
    }

    protected fun verifyGetSelectedPositionNull(pos: Int?) {
        Assert.assertEquals(pos, null)
    }

    protected fun verifyIsAutoSelectedProductTrue(isAutoSelect: Boolean) {
        Assert.assertTrue(isAutoSelect)
    }

    protected fun verifyIsAutoSelectedProductFalse(isAutoSelect: Boolean) {
        Assert.assertFalse(isAutoSelect)
    }

    protected fun verifyUpdateSelectedPositionIdTrue(expectedValue: Int) {
        Assert.assertTrue(viewModel.selectedFullProduct.position == expectedValue)
    }

    protected fun verifyCatalogProductJobIsNull() {
        Assert.assertNull(viewModel.catalogProductJob)
    }

    protected fun verifyCatalogProductJobIsNotNull() {
        Assert.assertNotNull(viewModel.catalogProductJob)
    }

    protected fun verifyCatalogProductJobIsCancelled() {
        Assert.assertTrue(viewModel.catalogProductJob?.isCancelled == true)
    }

    protected fun verifySelectedProductSuccess(expectedResponse: SelectedProduct) {
        Assert.assertEquals(expectedResponse, viewModel.selectedFullProduct)
    }

    protected fun verifySelectedProductNull() {
        Assert.assertEquals(viewModel.selectedFullProduct.denomData.id, "")
    }

    protected fun verifyRecommendationJobIsNull() {
        Assert.assertNull(viewModel.recommendationJob)
    }

    protected fun verifyRecommendationJobIsNotNull() {
        Assert.assertNotNull(viewModel.recommendationJob)
    }

    protected fun verifyRecommendationJobIsCancelled() {
        Assert.assertTrue(viewModel.recommendationJob?.isCancelled == true)
    }

    protected fun verifyMCCMJobIsNull() {
        Assert.assertNull(viewModel.mccmProductsJob)
    }

    protected fun verifyMCCMJobIsNotNull() {
        Assert.assertNotNull(viewModel.mccmProductsJob)
    }

    protected fun verifyMCCMJobIsCancelled() {
        Assert.assertTrue(viewModel.mccmProductsJob?.isCancelled == true)
    }

    protected fun verifyValidatorJobIsNull() {
        Assert.assertNull(viewModel.validatorJob)
    }

    protected fun verifyValidatorJobIsNotNull() {
        Assert.assertNotNull(viewModel.validatorJob)
    }

    protected fun verifyValidatorJobIsCancelled() {
        Assert.assertTrue(viewModel.validatorJob?.isCancelled == true)
    }

    protected fun verifyClientNumberThrottleJobIsActive() {
        Assert.assertTrue(viewModel.clientNumberThrottleJob?.isActive == true)
    }

    protected fun verifyClientNumberThrottleJobIsNotNull() {
        Assert.assertNotNull(viewModel.clientNumberThrottleJob)
    }

    protected fun verifyClientNumberThrottleJobIsCompleted() {
        Assert.assertTrue(viewModel.clientNumberThrottleJob?.isCompleted == true)
    }

    protected fun verifyClientNumberThrottleJobSame(expectedInstance: Job?, actualInstance: Job?) {
        Assert.assertNotNull(expectedInstance)
        Assert.assertNotNull(actualInstance)
        Assert.assertEquals(expectedInstance, actualInstance)
    }

    protected fun verifyClientNumberThrottleJobNotSame(
        expectedInstance: Job?,
        actualInstance: Job?
    ) {
        Assert.assertNotNull(expectedInstance)
        Assert.assertNotNull(actualInstance)
        Assert.assertNotEquals(expectedInstance, actualInstance)
    }

    protected fun verifyRecomCheckoutUrlUpdated(expectedResult: String) {
        val actualResult = viewModel.recomCheckoutUrl
        Assert.assertEquals(expectedResult, actualResult)
    }

    protected fun verifyDenomAndMCCMIsEmpty(expectedResult: Boolean) {
        Assert.assertTrue(expectedResult)
    }

    protected fun verifyDenomAndMCCMIsNotEmpty(expectedResult: Boolean) {
        Assert.assertFalse(expectedResult)
    }

    private fun assertDigitalCheckoutPassDataEqual(
        expected: DigitalCheckoutPassData,
        actual: DigitalCheckoutPassData
    ) {
        Assert.assertEquals(expected.clientNumber, actual.clientNumber)
        Assert.assertEquals(expected.categoryId, actual.categoryId)
        Assert.assertEquals(expected.operatorId, actual.operatorId)
        Assert.assertEquals(expected.productId, actual.productId)
        Assert.assertEquals(expected.isPromo, actual.isPromo)
        Assert.assertEquals(expected.isSpecialProduct, actual.isSpecialProduct)
        Assert.assertEquals(expected.idemPotencyKey, actual.idemPotencyKey)
    }

    fun verifyFilterIsChanged(isChanged: Boolean) {
        Assert.assertTrue(isChanged)
    }

    fun verifyFilterIsNotChanged(isChanged: Boolean) {
        Assert.assertFalse(isChanged)
    }

    protected fun TestScope.skipPrefixOperatorDelay() {
        advanceUntilIdle()
    }

    protected fun TestScope.skipValidatorDelay() {
        advanceUntilIdle()
    }

    protected fun TestScope.skipMultitabDelay() {
        advanceUntilIdle()
    }

    protected fun TestScope.skipRecommendationDelay() {
        advanceUntilIdle()
    }

    protected fun TestScope.skipClientNumberTransitionDelay() {
        advanceUntilIdle()
    }

    companion object {
        const val EMPTY = ""
        const val POSITION_0 = 0
        const val POSITION_DEFAULT = -1
    }
}
