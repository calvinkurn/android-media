package com.tokopedia.tkpd.flashsale.presentation.chooseproduct

import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductReserveResult
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ChooseProductViewModelIntegrationTest: ChooseProductViewModelTestFixture() {

    @Test
    fun `When get product list error, Expect trigger error livedata`() = runBlocking {
        testErrorResponse {
            viewModel.getProductList(0, 0, "")
        }
    }

    @Test
    fun `When get product list success, Expect trigger product related livedata`() = runBlocking {
        runBlocking {
            assert(!viewModel.hasNextPage)
            testGetProduct()
            assert(viewModel.hasNextPage)
            testGetProductEmpty()
            assert(!viewModel.hasNextPage)
        }
    }

    @Test
    fun `When get criteria list success, Expect trigger criteria related livedata`() = runBlocking {
        // given
        coEvery {
            getFlashSaleProductPerCriteriaUseCase.execute(any())
        } returns generateCriteriaSelection(3)

        // when
        viewModel.getCriteriaList()

        // then
        assert(viewModel.criteriaList.getOrAwaitValue().size == 3)
        assert(viewModel.categoryAllList.getOrAwaitValue().size == 0)
        assert(viewModel.isCriteriaEmpty.getOrAwaitValue() == false)
    }

    @Test
    fun `When get criteria list error, Expect trigger error livedata`() = runBlocking {
        testErrorResponse {
            viewModel.getCriteriaList()
        }
    }

    @Test
    fun `When reserve product success, Expect trigger criteria related livedata`() = runBlocking {
        // given
        coEvery {
            doFlashSaleProductReserveUseCase.execute(any())
        } returns ProductReserveResult(
            isSuccess = true,
            errorMessage = "",
            reservationId  = "123"
        )

        // when
        viewModel.reserveProduct()

        // then
        assert(viewModel.productReserveResult.getOrAwaitValue().isSuccess)
    }

    @Test
    fun `When reserve product error, Expect trigger error livedata`() = runBlocking {
        testErrorResponse {
            viewModel.reserveProduct()
        }
    }

    @Test
    fun `When check criteria success, Expect trigger checking result livedata`() = runBlocking {
        // given
        coEvery {
            getFlashSaleProductCriteriaCheckingUseCase.execute(any(), any(), 1)
        } returns listOf(CriteriaCheckingResult())

        // when
        viewModel.checkCriteria(ChooseProductItem(criteriaId = 1))

        // then
        assert(viewModel.criteriaCheckingResult.getOrAwaitValue().isNotEmpty())
    }

    @Test
    fun `When check criteria error, Expect trigger error livedata`() = runBlocking {
        testErrorResponse {
            viewModel.checkCriteria(ChooseProductItem())
        }
    }

    @Test
    fun `When get max submission success, Expect trigger max submission result livedata`() = runBlocking {
        // given
        coEvery {
            getFlashSaleDetailForSellerUseCase.execute(any())
        } returns generateMockFlashSaleData()

        // when
        viewModel.getMaxProductSubmission()

        // then
        assert(viewModel.maxSelectedProduct.getOrAwaitValue().isZero())
    }

    @Test
    fun `When get max submission error, Expect trigger error livedata`() = runBlocking {
        testErrorResponse { viewModel.getMaxProductSubmission() }
    }
}
