package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class DigitalPDPViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    protected lateinit var viewModel: DigitalPDPPulsaViewModel

    @RelaxedMockK
    lateinit var repo: DigitalPDPTelcoRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DigitalPDPPulsaViewModel(repo, testCoroutineRule.dispatchers)
    }

    protected fun onGetMenuDetail_thenReturn(response: MenuDetailModel) {
        coEvery {
            repo.getMenuDetail(any(), any(), any())
        } returns response
    }

    protected fun onGetFavoriteNumber_thenReturn(response: TopupBillsPersoFavNumberData) {
        coEvery {
            repo.getFavoriteNumber(any())
        } returns response
    }

    protected fun onGetPrefixOperator_thenReturn(response: TelcoCatalogPrefixSelect) {
        coEvery {
            repo.getOperatorList(any())
        } returns response
    }

    protected fun onGetSelectedGridProduct_thenReturn(result: SelectedProduct) {
        viewModel.selectedGridProduct = result
    }

    protected fun verifyGetFavoriteNumberSuccess(expectedResponse: List<TopupBillsPersoFavNumberItem>) {
        val actualResponse = viewModel.favoriteNumberData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetPrefixOperatorSuccess(expectedResponse: TelcoCatalogPrefixSelect) {
        val actualResponse = viewModel.catalogPrefixSelect.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetMenuDetailSuccess(expectedResponse: MenuDetailModel) {
        val actualResponse = viewModel.menuDetailData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
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
        Assert.assertEquals(viewModel.clientNumberValidatorMsg.value, EMPTY)
    }

    protected fun verifyValidateClientNumberFalse() {
        Assert.assertFalse(viewModel.isEligibleToBuy)
        Assert.assertNotEquals(viewModel.clientNumberValidatorMsg.value, EMPTY)
    }

    protected fun verifySelectedGridProductNonEmpty() {
        Assert.assertEquals(viewModel.selectedGridProduct.position, POSITION_0)
    }
    protected fun verifySelectedGridProductEmpty() {
        Assert.assertEquals(viewModel.selectedGridProduct.position, POSITION_DEFAULT)
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

    companion object {
        const val EMPTY = ""
        const val POSITION_0 = 0
        const val POSITION_DEFAULT = -1
    }
}