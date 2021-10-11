package com.tokopedia.buyerorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.buyerorder.common.ResourceProvider
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.buyerorder.detail.domain.BuyerGetCancellationReasonUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerInstantCancelUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerRequestCancelUseCase
import com.tokopedia.buyerorder.detail.view.model.BuyerCancelRequestReasonValidationResult
import com.tokopedia.buyerorder.detail.view.viewmodel.BuyerCancellationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
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

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        buyerCancellationViewModel = BuyerCancellationViewModel(dispatcher, resourceProvider, getCancellationUseCase,
                buyerInstantCancelUseCase, buyerRequestCancelUseCase)

        listReason.add(BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem(title = "test1"))
        listReason.add(BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem(title = "test2"))
        listReason.add(BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem(title = "test3"))
        listMsg.add("test")
        coEvery {
            resourceProvider.getBuyerRequestCancelReasonMinCharMessage()
        } returns "Min. 15 karakter"
        coEvery {
            resourceProvider.getBuyerRequestCancelReasonShouldNotContainsSpecialCharsErrorMessage()
        } returns "Hindari penggunaan karakter spesial (@#\$%^*)"
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

    @Test
    fun validateBuyerRequestCancelReason_shouldReturnValidWhenGivenValidInput() {
        val input = "Saya ingin membatalkan pesanan ini karena saya ingin merubah pesanan Saya."
        val expectedResult = BuyerCancelRequestReasonValidationResult("Hindari penggunaan karakter spesial (@#\$%^*)", isError = false, isButtonEnable = true)

        buyerCancellationViewModel.validateBuyerRequestCancelReason(input)

        val result = buyerCancellationViewModel.buyerRequestCancelReasonValidationResult.value
        assert(expectedResult == result)
    }

    @Test
    fun validateBuyerRequestCancelReason_shouldReturnInvalidWhenGivenInvalidInput() {
        val input = "S@y@ !ng!n m3mb@t@lk@n p3s@n@n !n! k@r3n@ s@y@ !ng|n m3rvb@h p3s@n4n S4y4."
        val expectedResult = BuyerCancelRequestReasonValidationResult("Hindari penggunaan karakter spesial (@#\$%^*)", isError = true, isButtonEnable = false)

        buyerCancellationViewModel.validateBuyerRequestCancelReason(input)

        val result = buyerCancellationViewModel.buyerRequestCancelReasonValidationResult.value
        assert(expectedResult == result)
    }

    @Test
    fun validateBuyerRequestCancelReason_shouldReturnInvalidWhenGivenInvalidInputWithCharacterCountBelow15() {
        val input = "B@t@l|n!"
        val expectedResult = BuyerCancelRequestReasonValidationResult("Hindari penggunaan karakter spesial (@#\$%^*)", isError = true, isButtonEnable = false)

        buyerCancellationViewModel.validateBuyerRequestCancelReason(input)

        val result = buyerCancellationViewModel.buyerRequestCancelReasonValidationResult.value
        assert(expectedResult == result)
    }

    @Test
    fun validateBuyerRequestCancelReason_shouldReturnInvalidWhenGivenEmptyInput() {
        val input = ""
        val expectedResult = BuyerCancelRequestReasonValidationResult("Hindari penggunaan karakter spesial (@#\$%^*)", isError = false, isButtonEnable = false)

        buyerCancellationViewModel.validateBuyerRequestCancelReason(input)

        val result = buyerCancellationViewModel.buyerRequestCancelReasonValidationResult.value
        assert(expectedResult == result)
    }

    @Test
    fun validateBuyerRequestCancelReason_shouldReturnInvalidWhenGivenValidInputWithCharacterCountBelow15() {
        val input = "Batalkan ya"
        val expectedResult = BuyerCancelRequestReasonValidationResult("Min. 15 karakter", isError = true, isButtonEnable = false)

        buyerCancellationViewModel.validateBuyerRequestCancelReason(input)

        val result = buyerCancellationViewModel.buyerRequestCancelReasonValidationResult.value
        assert(expectedResult == result)
    }
}