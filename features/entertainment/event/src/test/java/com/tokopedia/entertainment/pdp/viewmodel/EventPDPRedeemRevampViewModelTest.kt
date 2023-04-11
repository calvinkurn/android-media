package com.tokopedia.entertainment.pdp.viewmodel

import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeemedData
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


    @Test
    fun `when update checked list redemptions and return checked list`() {
        val listRedemption = createRedeemData().data.redemptions ?: emptyList()
        viewModel.listRedemptions = listRedemption
        val checkedPair = listOf(Pair("14940", true))

        viewModel.updateCheckedIds(checkedPair)
        val result = viewModel.getCheckedIdsSize()
        Assert.assertEquals(result, 1)
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

    //region new process redeem
    @Test
    fun `when new process redeem should run and give success result`() {
        onGetNewRedeemProcess_thenReturn(createRedeemedDataMap())
        val expectedResponse = createRedeemedData()
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when new process redeem should run and give error result because null`() {
        onGetNewRedeemProcess_thenReturn(createRedeemedNullDataMap())
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when new process redeem should run and give error result error body`() {
        onGetNewRedeemProcess_thenReturn(createRedeemedErrorBodyDataMap(Throwable()))
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, "Unauthorized")
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when new process redeem should run and give error result error body null`() {
        onGetNewRedeemProcess_thenReturn(createRedeemedErrorBodyNullDataMap(Throwable()))
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when new process redeem should run and give error result is error true`() {
        onGetNewRedeemProcess_thenReturn(createRedeemedDataIsErrorTrueMap())
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, "Unauthorized")
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }
    //endregion new process redeem

    //region old process redeem
    @Test
    fun `when old process redeem should run and give success result`() {
        onGetOldRedeemProcess_thenReturn(createRedeemedDataMap())
        val expectedResponse = createRedeemedData()
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowOldRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputOldRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when old process redeem should run and give error result because null`() {
        onGetOldRedeemProcess_thenReturn(createRedeemedNullDataMap())
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowOldRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputOldRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when old process redeem should run and give error result error body`() {
        onGetOldRedeemProcess_thenReturn(createRedeemedErrorBodyDataMap(Throwable()))
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowOldRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputOldRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, "Unauthorized")
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when old process redeem should run and give error result error body null`() {
        onGetOldRedeemProcess_thenReturn(createRedeemedErrorBodyNullDataMap(Throwable()))
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowOldRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputOldRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }

    @Test
    fun `when old process redeem should run and give error result is error true`() {
        onGetOldRedeemProcess_thenReturn(createRedeemedDataIsErrorTrueMap())
        var actualResponse: Result<EventRedeemedData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowOldRedeem.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setInputOldRedeemedUrl("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, "Unauthorized")
        Assert.assertEquals(viewModel.listRedemptions, emptyList<Participant>())
        Assert.assertEquals(viewModel.oldFlowQuantity, 0)
    }
    //endregion old process redeem
}
