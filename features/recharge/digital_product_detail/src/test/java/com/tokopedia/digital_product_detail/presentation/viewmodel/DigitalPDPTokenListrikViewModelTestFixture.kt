package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteGroupModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTokenListrikRepository
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.unit.test.rule.StandardTestRule
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class DigitalPDPTokenListrikViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

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
            repo.getRecommendations(any(), any(), any(), any(), false)
        } returns response
    }

    protected fun onGetRecommendation_thenReturn(error: Throwable) {
        coEvery {
            repo.getRecommendations(any(), any(), any(), any(), false)
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

    protected fun onGetAddToCart_thenReturn(response: DigitalAtcResult) {
        coEvery {
            repo.addToCart(any(), any(), any())
        } returns response
    }

    protected fun onGetAddToCart_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.addToCart(any(), any(), any())
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
        coVerify { repo.getRecommendations(any(), any(), any(), any(), false) }
    }

    protected fun verifyGetRecommendationsRepoWasNotCalled() {
        coVerify { repo.getRecommendations(any(), any(), any(), any(), false) wasNot Called }
    }

    protected fun verifyGetProductInputMultiTabRepoWasNotCalled() {
        coVerify { repo.getProductTokenListrikDenomGrid(any(), any(), any()) wasNot Called }
    }

    protected fun verifyGetMenuDetailRepoGetCalled() {
        coVerify { repo.getMenuDetail(any()) }
    }

    protected fun verifyGetFavoriteNumberChipsRepoGetCalled() {
        coVerify { repo.getFavoriteNumbers(any(), any(), any()) }
    }

    protected fun verifyAddToCartRepoGetCalled() {
        coVerify { repo.addToCart(any(), any(), any()) }
    }

    protected fun verifyGetOperatorSelectGroupRepoGetCalled() {
        coVerify { repo.getOperatorSelectGroup(any()) }
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

    protected fun verifySelectedProductSuccess(expectedResponse: SelectedProduct) {
        Assert.assertEquals(expectedResponse, viewModel.selectedGridProduct)
    }

    protected fun verifySelectedProductNull() {
        Assert.assertEquals(viewModel.selectedGridProduct.denomData.id, "")
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
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetCatalogInputMultitabLoading(expectedResponse: RechargeNetworkResult.Loading) {
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

    protected fun verifyAddToCartSuccess(expectedResponse: DigitalAtcResult) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
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

    protected fun verifySelectedGridProductNonEmpty() {
        Assert.assertEquals(
            viewModel.selectedGridProduct.position,
            POSITION_0
        )
    }

    protected fun verifySelectedGridProductEmpty() {
        Assert.assertEquals(
            viewModel.selectedGridProduct.position,
            POSITION_DEFAULT
        )
    }

    protected fun verifyGetSelectedPositionNull(pos: Int?) {
        Assert.assertEquals(pos, null)
    }

    protected fun verifyCatalogProductJobIsNull() {
        Assert.assertNull(viewModel.catalogProductJob)
    }

    protected fun verifyGetOperatorSelectGroupLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.catalogSelectGroup.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetOperatorSelectGroupSuccess(expectedResponse: DigitalCatalogOperatorSelectGroup) {
        val actualResponse = viewModel.catalogSelectGroup.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetOperatorSelectGroupFail() {
        val actualResponse = viewModel.catalogSelectGroup.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
    }

    protected fun verifySetOperatorListSuccess(expectedResult: List<CatalogOperator>) {
        val actualResult = viewModel.operatorList
        Assert.assertEquals(expectedResult, actualResult)
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

    protected fun verifyListInfoSuccess(
        expectedListInfo: List<String>,
        actualListInfo: List<String>
    ) {
        Assert.assertEquals(expectedListInfo, actualListInfo)
    }

    protected fun verifyListInfoEmpty(actualListInfo: List<String>) {
        Assert.assertEquals(actualListInfo, listOf<String>())
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

    protected fun TestScope.skipValidatorDelay() {
        advanceUntilIdle()
    }

    protected fun TestScope.skipMultitabDelay() {
        advanceUntilIdle()
    }

    protected fun TestScope.skipRecommendationDelay() {
        advanceUntilIdle()
    }

    companion object {
        const val EMPTY = ""
        const val POSITION_0 = 0
        const val POSITION_DEFAULT = -1
    }
}
