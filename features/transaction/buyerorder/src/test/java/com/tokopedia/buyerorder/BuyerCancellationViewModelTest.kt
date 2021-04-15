package com.tokopedia.buyerorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.buyerorder.detail.domain.BuyerGetCancellationReasonUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerInstantCancelUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerRequestCancelUseCase
import com.tokopedia.buyerorder.detail.view.viewmodel.BuyerCancellationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 10/11/20.
 */

@RunWith(JUnit4::class)
class BuyerCancellationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var buyerCancellationViewModel: BuyerCancellationViewModel
    private var listReason =
            arrayListOf<BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem>()
    private var listMsg = arrayListOf<String>()

    @RelaxedMockK
    lateinit var getCancellationUseCase: BuyerGetCancellationReasonUseCase

    @RelaxedMockK
    lateinit var buyerInstantCancelUseCase: BuyerInstantCancelUseCase

    @RelaxedMockK
    lateinit var buyerRequestCancelUseCase: BuyerRequestCancelUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        buyerCancellationViewModel = BuyerCancellationViewModel(dispatcher, getCancellationUseCase,
                buyerInstantCancelUseCase, buyerRequestCancelUseCase)

        listReason.add(BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem(title = "test1"))
        listReason.add(BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem(title = "test2"))
        listReason.add(BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem(title = "test3"))
        listMsg.add("test")

    }

    // get cancel reason data
    @Test
    fun getCancelReasonData_shouldReturnSuccess() {
        //given
        coEvery {
            getCancellationUseCase.execute(any(), any())
        } returns Success(BuyerGetCancellationReasonData.Data(BuyerGetCancellationReasonData.Data.GetCancellationReason(reasons = listReason)))

        //when
        buyerCancellationViewModel.getCancelReasons("", "", "")

        //then
        assert(buyerCancellationViewModel.cancelReasonResult.value is Success)
        assert((buyerCancellationViewModel.cancelReasonResult.value as Success<BuyerGetCancellationReasonData.Data>).data.getCancellationReason.reasons == listReason)
    }

    @Test
    fun getCancelReasonData_shouldReturnFail() {
        //given
        coEvery {
            getCancellationUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        buyerCancellationViewModel.getCancelReasons("", "", "")

        //then
        assert(buyerCancellationViewModel.cancelReasonResult.value is Fail)
    }

    @Test
    fun getCancelReasonData_shouldNotReturnEmpty() {
        //given
        coEvery {
            getCancellationUseCase.execute(any(), any())
        } returns Success(BuyerGetCancellationReasonData.Data(BuyerGetCancellationReasonData.Data.GetCancellationReason(reasons = listReason)))

        //when
        buyerCancellationViewModel.getCancelReasons("", "", "")

        //then
        assert(buyerCancellationViewModel.cancelReasonResult.value is Success)
        assert((buyerCancellationViewModel.cancelReasonResult.value as Success<BuyerGetCancellationReasonData.Data>).data.getCancellationReason.reasons.isNotEmpty())
    }

    // instant cancel
    @Test
    fun getInstantCancel_shouldReturnSuccess() {
        //given
        coEvery {
            buyerInstantCancelUseCase.execute(any(), any())
        } returns Success(BuyerInstantCancelData.Data(BuyerInstantCancelData.Data.BuyerInstantCancel(success = 1)))

        //when
        buyerCancellationViewModel.instantCancellation("", "", "", "")

        //then
        assert(buyerCancellationViewModel.buyerInstantCancelResult.value is Success)
        assert((buyerCancellationViewModel.buyerInstantCancelResult.value as Success<BuyerInstantCancelData.Data>).data.buyerInstantCancel.success == 1)
    }

    @Test
    fun getInstantCancel_shouldReturnFail() {
        //given
        coEvery {
            buyerInstantCancelUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        buyerCancellationViewModel.instantCancellation("", "", "", "")

        //then
        assert(buyerCancellationViewModel.buyerInstantCancelResult.value is Fail)
    }

    @Test
    fun getInstantCancel_shouldNotReturnTitleEmpty() {
        //given
        coEvery {
            buyerInstantCancelUseCase.execute(any(), any())
        } returns Success(BuyerInstantCancelData.Data(BuyerInstantCancelData.Data.BuyerInstantCancel(message = "test")))

        //when
        buyerCancellationViewModel.instantCancellation("", "", "", "")

        //then
        assert(buyerCancellationViewModel.buyerInstantCancelResult.value is Success)
        assert((buyerCancellationViewModel.buyerInstantCancelResult.value as Success<BuyerInstantCancelData.Data>).data.buyerInstantCancel.message.isNotEmpty())
    }

    // request cancel
    @Test
    fun requestCancel_shouldReturnSuccess() {
        //given
        coEvery {
            buyerRequestCancelUseCase.execute(any(), any())
        } returns Success(BuyerRequestCancelData.Data(BuyerRequestCancelData.Data.BuyerRequestCancel(success = 1)))

        //when
        buyerCancellationViewModel.requestCancel("", "", "", "", "")

        //then
        assert(buyerCancellationViewModel.requestCancelResult.value is Success)
        assert((buyerCancellationViewModel.requestCancelResult.value as Success<BuyerRequestCancelData.Data>).data.buyerRequestCancel.success == 1)
    }

    @Test
    fun requestCancel_shouldReturnFail() {
        //given
        coEvery {
            buyerRequestCancelUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        buyerCancellationViewModel.requestCancel("", "", "", "", "")

        //then
        assert(buyerCancellationViewModel.requestCancelResult.value is Fail)
    }

    @Test
    fun requestCancel_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            buyerRequestCancelUseCase.execute(any(), any())
        } returns Success(BuyerRequestCancelData.Data(BuyerRequestCancelData.Data.BuyerRequestCancel(message = listMsg)))

        //when
        buyerCancellationViewModel.requestCancel("", "", "", "", "")

        //then
        assert(buyerCancellationViewModel.requestCancelResult.value is Success)
        assert((buyerCancellationViewModel.requestCancelResult.value as Success<BuyerRequestCancelData.Data>).data.buyerRequestCancel.message.isNotEmpty())
    }
}