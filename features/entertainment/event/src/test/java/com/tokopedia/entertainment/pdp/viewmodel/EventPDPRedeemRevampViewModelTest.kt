package com.tokopedia.entertainment.pdp.viewmodel

import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.Participant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class EventPDPRedeemRevampViewModelTest: EventPDPRedeemRevampViewModelTestFixture() {

    @Test
    fun `when set list listRedemptions and oldQuantity then get newest data `() {
        val listRedemptions = listOf<Participant>(Participant(
            id = "1",
            day = 1
        ))
        val quantity = 0

        viewModel.listRedemptions = listRedemptions
        viewModel.oldFlowQuantity = quantity

        Assert.assertEquals(viewModel.listRedemptions, listRedemptions)
        Assert.assertEquals(viewModel.oldFlowQuantity, quantity)
    }

    //region redeem data
    @Test
    fun `when get redeem data should run and give success result`() {
        onGetRedeemData_thenReturn(createRedeemDataMap())
        val expectedResponse = createRedeemData()
        var actualResponse: Result<EventRedeem>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeemData.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemUrl("")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
        Assert.assertEquals(viewModel.listRedemptions, createRedeemData().data.redemptions)
        Assert.assertEquals(viewModel.oldFlowQuantity, createRedeemData().data.quantity)
    }

    @Test
    fun `when get redeem data should run and give success result with empty redemptions`() {
        onGetRedeemData_thenReturn(createRedeemEmptyRedemptionsDataMap())
        val expectedResponse = createRedeemEmptyRedemptionsData()
        var actualResponse: Result<EventRedeem>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeemData.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemUrl("")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, createRedeemData().data.quantity)
    }

    @Test
    fun `when get redeem data should run and give error result`() {
        onGetRedeemData_thenReturn(createRedeemDataMap(NullPointerException()))
        var actualResponse: Result<EventRedeem>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeemData.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when get redeem data should run and give error result with message`() {
        onGetRedeemData_thenReturn(NullPointerException(errorMessage))
        var actualResponse: Result<EventRedeem>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeemData.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessage)
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when get redeem data should run and give error result because null`() {
        onGetRedeemData_thenReturn(createRedeemNullDataMap())
        var actualResponse: Result<EventRedeem>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeemData.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when get redeem data should run and give error result error body`() {
        onGetRedeemData_thenReturn(createRedeemErrorBodyDataMap(Throwable()))
        var actualResponse: Result<EventRedeem>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeemData.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, "Unauthorized")
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when get redeem data should run and give error result error body null`() {
        onGetRedeemData_thenReturn(createRedeemErrorBodyNullDataMap(Throwable()))
        var actualResponse: Result<EventRedeem>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeemData.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when get redeem data should run and give error result is error true`() {
        onGetRedeemData_thenReturn(createRedeemDataIsErrorTrueMap())
        var actualResponse: Result<EventRedeem>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeemData.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, "Unauthorized")
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }
    //endregion redeem data
}
