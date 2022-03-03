package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTokenListrikRepository
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class DigitalPDPTokenListrikViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    protected lateinit var viewModel: DigitalPDPTokenListrikViewModel

    @RelaxedMockK
    lateinit var repo: DigitalPDPTokenListrikRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DigitalPDPTokenListrikViewModel(repo, testCoroutineRule.dispatchers)
    }

    protected fun onGetMenuDetail_thenReturn(response: MenuDetailModel) {
        coEvery {
            repo.getMenuDetail(any(), any())
        } returns response
    }

    protected fun onGetMenuDetail_thenReturn(error: Throwable) {
        coEvery {
            repo.getMenuDetail(any(), any())
        } throws error
    }

    protected fun onGetRecommendation_thenReturn(response: RecommendationWidgetModel) {
        coEvery {
            repo.getRecommendations(any(), any(), any())
        } returns response
    }

    protected fun onGetRecommendation_thenReturn(error: Throwable) {
        coEvery {
            repo.getRecommendations(any(), any(), any())
        } throws error
    }

    protected fun onGetFavoriteNumber_thenReturn(response: TopupBillsPersoFavNumberData) {
        coEvery {
            repo.getFavoriteNumber(any())
        } returns response
    }

    protected fun onGetFavoriteNumber_thenReturn(error: Throwable) {
        coEvery {
            repo.getFavoriteNumber(any())
        } throws error
    }

    protected fun onGetCatalogInputMultitab_thenReturn(response: DenomWidgetModel) {
        coEvery {
            repo.getProductTokenListrikDenomGrid(any(), any(), any())
        } returns response
    }

    protected fun onGetCatalogInputMultitab_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.getProductTokenListrikDenomGrid(any(), any(), any())
        } throws errorThrowable
    }

    protected fun onGetAddToCart_thenReturn(response: String) {
        coEvery {
            repo.addToCart(any(), any(), any(), any())
        } returns response
    }

    protected fun onGetAddToCart_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.addToCart(any(), any(), any(), any())
        } throws errorThrowable
    }

    protected fun onGetOperatorSelectGroup_thenReturn(response: DigitalCatalogOperatorSelectGroup) {
        coEvery {
            repo.getOperatorSelectGroup(any())
        } returns response
    }

    protected fun onGetOperatorSelectGroup_thenReturn(error: Throwable) {
        coEvery {
            repo.getOperatorSelectGroup(any())
        } throws error
    }

    protected fun onGetSelectedGridProduct_thenReturn(result: SelectedProduct) {
        viewModel.selectedGridProduct = result
    }

    protected fun verifyGetProductInputMultiTabRepoGetCalled() {
        coVerify { repo.getProductTokenListrikDenomGrid(any(), any(), any()) }
    }

    protected fun verifyGetRecommendationsRepoGetCalled() {
        coVerify { repo.getRecommendations(any(), any()) }
    }

    protected fun verifyGetRecommendationsRepoWasNotCalled() {
        coVerify { repo.getRecommendations(any(), any()) wasNot Called }
    }

    protected fun verifyGetProductInputMultiTabRepoWasNotCalled() {
        coVerify { repo.getProductTokenListrikDenomGrid(any(), any(), any()) wasNot Called }
    }

    protected fun verifyGetMenuDetailRepoGetCalled() {
        coVerify { repo.getMenuDetail(any(), any()) }
    }

    protected fun verifyGetFavoriteNumberRepoGetCalled() {
        coVerify { repo.getFavoriteNumber(any()) }
    }

    protected fun verifyAddToCartRepoGetCalled() {
        coVerify { repo.addToCart(any(), any(), any(), any()) }
    }

    protected fun verifyGetOperatorSelectGroupRepoGetCalled() {
        coVerify { repo.getOperatorSelectGroup(any()) }
    }

    protected fun verifyGetFavoriteNumberLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.favoriteNumberData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetFavoriteNumberSuccess(expectedResponse: List<TopupBillsPersoFavNumberItem>) {
        val actualResponse = viewModel.favoriteNumberData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetFavoriteNumberFail() {
        val actualResponse = viewModel.favoriteNumberData.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyGetMenuDetailLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.menuDetailData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetMenuDetailSuccess(expectedResponse: MenuDetailModel) {
        val actualResponse = viewModel.menuDetailData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetMenuDetailFail() {
        val actualResponse = viewModel.menuDetailData.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyGetRecommendationLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.recommendationData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetRecommendationSuccess(expectedResponse: RecommendationWidgetModel) {
        val actualResponse = viewModel.recommendationData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetRecommendationFail() {
        val actualResponse = viewModel.recommendationData.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyGetRecommendationErrorCancellation() {
        val actualResponse = viewModel.recommendationData.value
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

    protected fun verifyGetCatalogInputMultitabSuccess(expectedResponse: DenomWidgetModel) {
        val actualResponse = viewModel.observableDenomData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetCatalogInputMultitabLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.observableDenomData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetCatalogInputMultitabError(expectedResponse: Throwable) {
        val actualResponse = viewModel.observableDenomData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Fail).error)
    }

    protected fun verifyGetCatalogInputMultitabErrorCancellation() {
        val actualResponse = viewModel.observableDenomData.value
        Assert.assertNull(actualResponse)
    }

    protected fun verifyCatalogProductJobIsCancelled() {
        Assert.assertTrue(viewModel.catalogProductJob?.isCancelled == true)
    }

    protected fun verifyAddToCartSuccess(expectedResponse: String) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyAddToCartError(expectedResponse: Throwable) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse.message, (actualResponse as RechargeNetworkResult.Fail).error.message)
    }

    protected fun verifyAddToCartErrorExceptions(expectedResponse: Throwable) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Fail).error)
    }

    protected fun verifyAddToCartErrorLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifySelectedGridProductNonEmpty() {
        Assert.assertEquals(viewModel.selectedGridProduct.position,
            POSITION_0
        )
    }

    protected fun verifySelectedGridProductEmpty() {
        Assert.assertEquals(viewModel.selectedGridProduct.position,
            POSITION_DEFAULT
        )
    }

    protected fun verifyGetSelectedPositionNull(pos: Int?) {
        Assert.assertEquals(pos, null)
    }

    protected fun verifyCatalogProductJobIsNull() {
        Assert.assertNull(viewModel.catalogProductJob)
    }

    protected fun verifyGetOperatorSelectGroupLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.catalogSelectGroup.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetOperatorSelectGroupSuccess(expectedResponse: DigitalCatalogOperatorSelectGroup) {
        val actualResponse = viewModel.catalogSelectGroup.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetOperatorSelectGroupFail() {
        val actualResponse = viewModel.catalogSelectGroup.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifyValidateClientNumberTrue() {
        Assert.assertTrue(viewModel.isEligibleToBuy)
        Assert.assertEquals(viewModel.clientNumberValidatorMsg.value, EMPTY)
    }

    protected fun verifyValidateClientNumberFalse() {
        Assert.assertFalse(viewModel.isEligibleToBuy)
        Assert.assertNotEquals(viewModel.clientNumberValidatorMsg.value, EMPTY)
    }

    protected fun verifyValidatorJobIsNull() {
        Assert.assertNull(viewModel.validatorJob)
    }

    protected fun verifyValidatorJobIsCancelled() {
        Assert.assertTrue(viewModel.validatorJob?.isCancelled == true)
    }

    protected fun verifyIsAutoSelectedProductTrue(isAutoSelect: Boolean) {
        Assert.assertTrue(isAutoSelect)
    }

    protected fun verifyIsAutoSelectedProductFalse(isAutoSelect: Boolean) {
        Assert.assertFalse(isAutoSelect)
    }

    protected fun verifyValidatorJobIsNotNull() {
        Assert.assertNotNull(viewModel.validatorJob)
    }

    protected fun verifyCatalogProductJobIsNotNull() {
        Assert.assertNotNull(viewModel.catalogProductJob)
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

    protected fun verifyRecomCheckoutUrlUpdated(expectedResult: String) {
        val actualResult = viewModel.recomCheckoutUrl
        Assert.assertEquals(expectedResult, actualResult)
    }

    private fun assertDigitalCheckoutPassDataEqual(expected: DigitalCheckoutPassData, actual: DigitalCheckoutPassData) {
        Assert.assertEquals(expected.clientNumber, actual.clientNumber)
        Assert.assertEquals(expected.categoryId, actual.categoryId)
        Assert.assertEquals(expected.operatorId, actual.operatorId)
        Assert.assertEquals(expected.productId, actual.productId)
        Assert.assertEquals(expected.isPromo, actual.isPromo)
        Assert.assertEquals(expected.isSpecialProduct, actual.isSpecialProduct)
        Assert.assertEquals(expected.idemPotencyKey, actual.idemPotencyKey)
    }

    protected fun TestCoroutineScope.skipValidatorDelay() {
        advanceTimeBy(DigitalPDPConstant.VALIDATOR_DELAY_TIME)
    }

    protected fun TestCoroutineScope.skipMultitabDelay() {
        advanceTimeBy(DigitalPDPConstant.DELAY_MULTI_TAB)
    }

    protected fun TestCoroutineScope.skipRecommendationDelay() {
        advanceTimeBy(DigitalPDPConstant.DELAY_MULTI_TAB)
    }

    companion object {
        const val EMPTY = ""
        const val POSITION_0 = 0
        const val POSITION_DEFAULT = -1
    }
}