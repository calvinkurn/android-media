package com.tokopedia.tkpd.flashsale.presentation.chooseproduct

import androidx.lifecycle.LiveData
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.ProductReserveResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductToReserve
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.FILTER_PRODUCT_CRITERIA_PASSED
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.MAX_PER_PAGE
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.mapper.ChooseProductUiMapper
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.util.*

class ChooseProductViewModelUiInteractionTest: ChooseProductViewModelTestFixture() {

    @Test
    fun `When select and deselect product, Expect correct product count`() = runBlocking {
        // given
        coEvery {
            getFlashSaleProductListToReserveUseCase.execute(any())
        } returns generateProductToReserve(10).copy(
            selectedProductIds = listOf(122, 123)
        )

        // when has no product data
        testSelectAndDeselectProduct(
            // then
            { assert(viewModel.selectedProductCount.getOrAwaitValue() == null) },
            { assert(viewModel.selectedProductCount.getOrAwaitValue() == null) }
        )

        // when has product data
        viewModel.getProductList(0,0,"")
        testSelectAndDeselectProduct(
            // then
            { assert(viewModel.selectedProductCount.getOrAwaitValue() == 1) },
            { assert(viewModel.selectedProductCount.getOrAwaitValue() == 0) }
        )
    }

    private fun testSelectAndDeselectProduct(onSelecting: () -> Unit, onDeselecting: () -> Unit) {
        viewModel.setSelectedProduct(ChooseProductItem(productId = "123", isSelected = true))
        viewModel.setSelectedProduct(ChooseProductItem(productId = "124", isSelected = true))
        onSelecting.invoke()
        viewModel.setSelectedProduct(ChooseProductItem(productId = "123", isSelected = false))
        viewModel.setSelectedProduct(ChooseProductItem(productId = "124", isSelected = false))
        onDeselecting.invoke()
    }

    @Test
    fun `When update criteria list, Expect correct criteria count`() {
        // given
        coEvery {
            getFlashSaleProductListToReserveUseCase.execute(any())
        } returns generateProductToReserve(10).copy(
            selectedProductIds = listOf(122, 123)
        )
        coEvery {
            getFlashSaleProductPerCriteriaUseCase.execute(any())
        } returns generateCriteriaSelection(2)

        viewModel.getCriteriaList()
        viewModel.getProductList(0,0,"")
        assert(viewModel.submittedProductIds.isNotEmpty())
        viewModel.updateCriteriaList(ChooseProductItem(productId = "123", criteriaId = 0, isSelected = true))
        viewModel.criteriaList.assertCriteria(0, 0)
        viewModel.updateCriteriaList(ChooseProductItem(productId = "124", criteriaId = 0, isSelected = true))
        viewModel.criteriaList.assertCriteria(0, 1)
    }

    private fun LiveData<List<CriteriaSelection>>.assertCriteria(criteriaId: Long, expectedCount: Int) {
        val count = getOrAwaitValue().firstOrNull {
            it.criteriaId == criteriaId
        }?.selectionCount
        assert(count == expectedCount)
    }

    @Test
    fun `When there is product, criteria or maxproduct datachanges, Expect invoke validation related flow`() = runBlockingTest {
        // given
        coEvery {
            getFlashSaleProductListToReserveUseCase.execute(any())
        } returns generateProductToReserve(10).copy(
            selectedProductIds = listOf(122, 123)
        )
        coEvery {
            getFlashSaleProductPerCriteriaUseCase.execute(any())
        } returns generateCriteriaSelection(2)
        coEvery {
            getFlashSaleDetailForSellerUseCase.execute(any())
        } returns generateMockFlashSaleData()
        var validationResult: Boolean? = null
        var selectionValidationResult: ChooseProductUiMapper.SelectionValidationResult? = null

        // when
        val validationResultJob = launch {
            viewModel.validationResult.collectLatest {
                validationResult = it
            }
        }
        val selectionValidationResultJob = launch {
            viewModel.selectionValidationResult.collectLatest {
                selectionValidationResult = it
            }
        }
        viewModel.getCriteriaList()
        viewModel.getMaxProductSubmission()
        viewModel.getProductList(0,0,"")
        validationResultJob.cancel()
        selectionValidationResultJob.cancel()

        // then
        assert(validationResult != null)
        assert(selectionValidationResult != null)
    }
}
