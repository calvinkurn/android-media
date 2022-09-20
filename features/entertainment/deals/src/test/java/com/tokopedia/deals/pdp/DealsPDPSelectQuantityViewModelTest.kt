package com.tokopedia.deals.pdp

import com.tokopedia.common_entertainment.data.DealsVerifyResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class DealsPDPSelectQuantityViewModelTest: DealsPDPSelectQuantityViewModelTestFixture() {

    @Test
    fun `when getting verify data should run and give the success result`() {
        onGetVerify_thenReturn(createVerify())
        val expectedResponse = createVerify()
        var actualResponse: Result<DealsVerifyResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowVerify.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setVerifyRequest(createPDPData().eventProductDetail.productDetailData)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting verify data should run and give the error result`() {
        onGetVerify_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<DealsVerifyResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowVerify.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setVerifyRequest(createPDPData().eventProductDetail.productDetailData)
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting currentQuantity return quantity `() {
        val currentQuantity = 5

        viewModel.currentQuantity = currentQuantity

        Assert.assertEquals(currentQuantity, viewModel.currentQuantity)
    }
}
