package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.presentation.data.PulsaDataFactory
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.junit.Test


@ExperimentalCoroutinesApi
class DigitalPDPPulsaViewModelTest : DigitalPDPViewModelTestFixture() {

    private val dataFactory = PulsaDataFactory()

    @Test
    fun `when getting menuDetail should run and give success result`() {
        val response = dataFactory.getMenuDetail()
        onGetMenuDetail_thenReturn(response)

        viewModel.getMenuDetail(MENU_ID, false)
        verifyGetMenuDetailSuccess(response)
    }

    @Test
    fun `when getting favoriteNumber should run and give success result`() {
        val response = dataFactory.getFavoriteNumberData()
        onGetFavoriteNumber_thenReturn(response)

        viewModel.getFavoriteNumber(listOf())
        verifyGetFavoriteNumberSuccess(response.persoFavoriteNumber.items)
    }

    @Test
    fun `when getting catalogPrefixSelect should run and give success result`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getPrefixOperatorData()
            onGetPrefixOperator_thenReturn(response)

            viewModel.getPrefixOperator(MENU_ID)
            skipPrefixOperatorDelay()

            verifyGetPrefixOperatorSuccess(response)
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

    companion object {
        const val MENU_ID = 289
    }
}