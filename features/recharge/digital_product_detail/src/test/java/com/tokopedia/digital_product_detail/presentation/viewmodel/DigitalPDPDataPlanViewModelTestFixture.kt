package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
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
abstract class DigitalPDPDataPlanViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    protected lateinit var viewModel: DigitalPDPDataPlanViewModel

    @RelaxedMockK
    lateinit var repo: DigitalPDPTelcoRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DigitalPDPDataPlanViewModel(repo, testCoroutineRule.dispatchers)
    }

    protected fun onGetMenuDetail_thenReturn(response: MenuDetailModel) {
        coEvery {
            repo.getMenuDetail(any(), any(), any())
        } returns response
    }

    protected fun onGetMenuDetail_thenReturn(error: Throwable) {
        coEvery {
            repo.getMenuDetail(any(), any(), any())
        } throws error
    }

    protected fun onGetFavoriteNumber_thenReturn(response: TopupBillsPersoFavNumberData) {
        coEvery {
            repo.getFavoriteNumberChips(any())
        } returns response
    }

    protected fun onGetFavoriteNumber_thenReturn(error: Throwable) {
        coEvery {
            repo.getFavoriteNumberChips(any())
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
            repo.addToCart(any(), any(), any(), any())
        } returns response
    }

    protected fun onGetAddToCart_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.addToCart(any(), any(), any(), any())
        } throws errorThrowable
    }

    protected fun onGetSelectedFullProduct_thenReturn(result: SelectedProduct) {
        viewModel.selectedFullProduct = result
    }

    protected fun verifyGetMenuDetailRepoGetCalled() {
        coVerify { repo.getMenuDetail(any(), any(), any()) }
    }

    protected fun verifyGetFavoriteNumberRepoGetCalled() {
        coVerify { repo.getFavoriteNumberChips(any()) }
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
        coVerify { repo.addToCart(any(), any(), any(), any()) }
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

    protected fun verifyPrefixOperatorLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.catalogPrefixSelect.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetPrefixOperatorSuccess(expectedResponse: TelcoCatalogPrefixSelect) {
        val actualResponse = viewModel.catalogPrefixSelect.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetCatalogInputMultitabSuccess(expectedResponse: InputMultiTabDenomModel) {
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetFilterTagComponentSuccess(expectedResponse: List<TelcoFilterTagComponent>) {
        val actualResponse = viewModel.filterData
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetFilterParam(expectedResponse: ArrayList<HashMap<String, Any>>){
        val actualResponse = viewModel.filterDataParams
        Assert.assertEquals(expectedResponse, actualResponse)
    }
    protected fun verifyGetFilterParamEmpty(expectedResponse: ArrayList<HashMap<String, Any>>){
        val actualResponse = viewModel.filterDataParams
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetFilterTagComponentEmpty() {
        val actualResponse = viewModel.filterData
        Assert.assertEquals(emptyList<TelcoFilterTagComponent>(), actualResponse)
    }

    protected fun verifyGetCatalogInputMultitabLoading(expectedResponse: RechargeNetworkResult.Loading){
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

    protected fun verifyGetPrefixOperatorFail() {
        val actualResponse = viewModel.catalogPrefixSelect.value
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
        Assert.assertEquals(viewModel.clientNumberValidatorMsg.value,
            EMPTY
        )
    }

    protected fun verifyValidateClientNumberFalse() {
        Assert.assertFalse(viewModel.isEligibleToBuy)
        Assert.assertNotEquals(viewModel.clientNumberValidatorMsg.value,
            EMPTY
        )
    }

    protected fun verifySelectedFullProductNonEmpty() {
        Assert.assertEquals(viewModel.selectedFullProduct.position,
            POSITION_0
        )
    }

    protected fun verifySelectedFullProductEmpty() {
        Assert.assertEquals(viewModel.selectedFullProduct.position,
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

    protected fun verifyCatalogProductJobIsNull() {
        Assert.assertNull(viewModel.catalogProductJob)
    }

    protected fun verifyCatalogProductJobIsNotNull() {
        Assert.assertNotNull(viewModel.catalogProductJob)
    }

    protected fun verifyCatalogProductJobIsCancelled() {
        Assert.assertTrue(viewModel.catalogProductJob?.isCancelled == true)
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

    protected fun TestCoroutineScope.skipPrefixOperatorDelay() {
        advanceTimeBy(DigitalPDPConstant.DELAY_PREFIX_TIME)
    }

    protected fun TestCoroutineScope.skipValidatorDelay() {
        advanceTimeBy(DigitalPDPConstant.VALIDATOR_DELAY_TIME)
    }

    protected fun TestCoroutineScope.skipMultitabDelay() {
        advanceTimeBy(DigitalPDPConstant.DELAY_MULTI_TAB)
    }

    companion object {
        const val EMPTY = ""
        const val POSITION_0 = 0
        const val POSITION_DEFAULT = -1
    }
}