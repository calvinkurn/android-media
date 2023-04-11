package com.tokopedia.deals.checkout

import com.tokopedia.common_entertainment.data.DealsCheckoutInstantResponse
import com.tokopedia.common_entertainment.data.DealsCheckoutResponse
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert
import org.junit.Test

class DealsCheckoutViewModelTest: DealsCheckoutViewModelTestFixture() {

    @Test
    fun `when getting checkout general with promo should run and give success result`() {
        onGetCheckoutGeneral_thenReturn(createGeneralResponse())
        val expectedResponse = createGeneralResponse()
        var actualResponse: Result<DealsCheckoutResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowCheckoutGeneral.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.checkoutGeneral(createParamGeneral())
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting checkout general with no promo should run and give success result`() {
        onGetCheckoutGeneral_thenReturn(createGeneralResponse())
        val expectedResponse = createGeneralResponse()
        var actualResponse: Result<DealsCheckoutResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowCheckoutGeneral.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.checkoutGeneral(createParamGeneralNoPromo())
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting checkout general with promo should not run and give error result`() {
        onGetCheckoutGeneral_thenReturn(Throwable(errorMessageGeneral))
        var actualResponse: Result<DealsCheckoutResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowCheckoutGeneral.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.checkoutGeneral(createParamGeneral())
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting checkout general instant with promo should run and give success result`() {
        onGetCheckoutGeneralInstant_thenReturn(createGeneralInstantResponse())
        val expectedResponse = createGeneralInstantResponse()
        var actualResponse: Result<DealsCheckoutInstantResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowCheckoutGeneralInstant.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.checkoutGeneralInstant(createParamGeneralInstant())
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting checkout general instant with no promo should run and give success result`() {
        onGetCheckoutGeneralInstant_thenReturn(createGeneralInstantResponse())
        val expectedResponse = createGeneralInstantResponse()
        var actualResponse: Result<DealsCheckoutInstantResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowCheckoutGeneralInstant.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.checkoutGeneralInstant(createParamGeneralInstantNoPromo())
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting checkout general instant with promo should not run and give error result`() {
        onGetCheckoutGeneralInstant_thenReturn(Throwable(errorMessageGeneral))
        var actualResponse: Result<DealsCheckoutInstantResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowCheckoutGeneralInstant.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.checkoutGeneralInstant(createParamGeneralInstant())
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }
}
