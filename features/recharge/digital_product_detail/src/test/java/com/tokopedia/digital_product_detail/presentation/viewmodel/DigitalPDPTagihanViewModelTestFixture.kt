package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
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
import com.tokopedia.digital_product_detail.data.model.data.RechargeProduct
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTagihanListrikRepository
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
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
abstract class DigitalPDPTagihanViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    protected lateinit var viewModel: DigitalPDPTagihanViewModel

    @RelaxedMockK
    lateinit var repo: DigitalPDPTagihanListrikRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DigitalPDPTagihanViewModel(repo, testCoroutineRule.dispatchers)
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

    protected fun onGetTagihanProduct_thenReturn(response: RechargeProduct?) {
        coEvery {
            repo.getProductTagihanListrik(any(), any(), any())
        } returns response
    }

    protected fun onGetTagihanProduct_thenReturn(error: Throwable) {
        coEvery {
            repo.getProductTagihanListrik(any(), any(), any())
        } throws error
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

    protected fun onGetInquiry_thenReturn(response: TopupBillsEnquiryData) {
        coEvery {
            repo.inquiryProduct(any(), any(), any())
        } returns response
    }

    protected fun onGetInquiry_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.inquiryProduct(any(), any(), any())
        } throws errorThrowable
    }

    protected fun onGetOperatorData_thenReturn(operatorData: CatalogOperator) {
        viewModel.operatorData = operatorData
    }

    protected fun onGetOperatorList_thenReturn(operatorList: List<CatalogOperator>) {
        viewModel.operatorList = operatorList
    }

    protected fun verifyGetMenuDetailRepoGetCalled() {
        coVerify { repo.getMenuDetail(any()) }
    }

    protected fun verifyGetFavoriteNumberChipsRepoGetCalled() {
        coVerify { repo.getFavoriteNumbers(any(), any(), any()) }
    }

    protected fun verifyGetOperatorSelectGroupRepoGetCalled() {
        coVerify { repo.getOperatorSelectGroup(any()) }
    }

    protected fun verifyGetTagihanProductRepoGetCalled() {
        coVerify { repo.getProductTagihanListrik(any(), any(), any()) }
    }

    protected fun verifyAddToCartRepoGetCalled() {
        coVerify { repo.addToCart(any(), any(), any()) }
    }

    protected fun verifyInquiryProductRepoGetCalled() {
        coVerify { repo.inquiryProduct(any(), any(), any()) }
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

    protected fun verifyGetTagihanProductLoading(expectedResponse: RechargeNetworkResult.Loading) {
        val actualResponse = viewModel.tagihanProduct.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifySetOperatorListSuccess(expectedResult: List<CatalogOperator>) {
        val actualResult = viewModel.operatorList
        Assert.assertEquals(expectedResult, actualResult)
    }

    protected fun verifyGetTagihanProductSuccess(expectedResponse: RechargeProduct) {
        val actualResponse = viewModel.tagihanProduct.value
        Assert.assertEquals(
            expectedResponse,
            (actualResponse as RechargeNetworkResult.Success).data
        )
    }

    protected fun verifyGetTagihanProductFail() {
        val actualResponse = viewModel.tagihanProduct.value
        Assert.assertTrue(actualResponse is RechargeNetworkResult.Fail)
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

    protected fun verifyValidateClientNumberTrue() {
        Assert.assertTrue(viewModel.isEligibleToBuy)
        Assert.assertEquals(viewModel.clientNumberValidatorMsg.value?.first, EMPTY)
        Assert.assertEquals(viewModel.clientNumberValidatorMsg.value?.second, false)
    }

    protected fun verifyValidateClientNumberFalse() {
        Assert.assertFalse(viewModel.isEligibleToBuy)
        Assert.assertNotEquals(viewModel.clientNumberValidatorMsg.value, EMPTY)
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

    protected fun verifyCheckoutPassDataUpdated(expectedResult: DigitalCheckoutPassData) {
        val actualResult = viewModel.digitalCheckoutPassData
        assertDigitalCheckoutPassDataEqual(expectedResult, actualResult)
    }

    protected fun verifyCheckoutPassDataNotUpdated(expectedResult: DigitalCheckoutPassData) {
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

    protected fun verifyListInfoSuccess(
        expectedListInfo: List<String>,
        actualListInfo: List<String>
    ) {
        Assert.assertEquals(expectedListInfo, actualListInfo)
    }

    protected fun verifyListInfoEmpty(actualListInfo: List<String>) {
        Assert.assertEquals(actualListInfo, listOf<String>())
    }

    protected fun verifyOperatorDataHasCorrectData(expectedResult: CatalogOperator) {
        val actualResult = viewModel.operatorData
        assertOperatorDataEqual(expectedResult, actualResult)
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

    private fun assertOperatorDataEqual(expected: CatalogOperator, actual: CatalogOperator) {
        Assert.assertEquals(expected.id, actual.id)
        Assert.assertEquals(expected.attributes.name, actual.attributes.name)
        Assert.assertEquals(expected.attributes.image, actual.attributes.image)
        Assert.assertEquals(expected.attributes.imageUrl, actual.attributes.imageUrl)
        Assert.assertEquals(expected.attributes.description, actual.attributes.description)
    }

    protected fun TestCoroutineScope.skipValidatorDelay() {
        advanceTimeBy(DigitalPDPConstant.VALIDATOR_DELAY_TIME)
    }

    companion object {
        private const val EMPTY = ""
    }
}
