package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.create.domain.usecase.SaveSquareVoucherUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.UploadVoucherUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.ChangeVoucherPeriodUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetTokenUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

@ExperimentalCoroutinesApi
class ChangeVoucherPeriodViewModelTest {

    companion object {
        private val DUMMY_CALENDAR =
                Calendar.Builder()
                        .setDate(1,1,1)
                        .build()

        private const val DUMMY_VOUCHER_SQUARE = "square"
        private const val DUMMY_VOUCHER_SQUARE_URL = "square_url"
    }

    @RelaxedMockK
    lateinit var changeVoucherPeriodUseCase: ChangeVoucherPeriodUseCase

    @RelaxedMockK
    lateinit var uploadVoucherUseCase: UploadVoucherUseCase

    @RelaxedMockK
    lateinit var saveSquareVoucherUseCase: SaveSquareVoucherUseCase

    @RelaxedMockK
    lateinit var getTokenUseCase: GetTokenUseCase

    @RelaxedMockK
    lateinit var updateVoucherSuccessObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var voucherUiModel: VoucherUiModel

    @RelaxedMockK
    lateinit var dummyBitmap: Bitmap

    lateinit var mViewModel: ChangeVoucherPeriodViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = ChangeVoucherPeriodViewModel(CoroutineTestDispatchersProvider, Schedulers.immediate(), Schedulers.immediate(), changeVoucherPeriodUseCase, uploadVoucherUseCase, saveSquareVoucherUseCase, getTokenUseCase)

        mViewModel.updateVoucherSuccessLiveData.observeForever(updateVoucherSuccessObserver)
    }

    @After
    fun cleanup() {
        mViewModel.updateVoucherSuccessLiveData.removeObserver(updateVoucherSuccessObserver)
    }

    @Test
    fun `setting start date calendar will change use case param value and the calendar live data`() {
        with(mViewModel) {
            setStartDateCalendar(DUMMY_CALENDAR)

            assert(startDateCalendarLiveData.value == DUMMY_CALENDAR)
        }
    }

    @Test
    fun `setting end date calendar will change use case param value and the calendar live data`() {
        with(mViewModel) {
            setEndDateCalendar(DUMMY_CALENDAR)

            assert(endDateCalendarLiveData.value == DUMMY_CALENDAR)
        }
    }

    @Test
    fun `success validating voucher period`() = runBlocking {
        with(mViewModel) {
            val dummyUpdateVoucherSuccess = true
            val dummySquareUrl: String? = DUMMY_VOUCHER_SQUARE_URL
            val dummyToken = ""

            coEvery {
                getTokenUseCase.executeOnBackground()
            } returns dummyToken
            coEvery {
                saveSquareVoucherUseCase.executeOnBackground()
            } returns DUMMY_VOUCHER_SQUARE
            coEvery {
                uploadVoucherUseCase.createObservable(any())
            } returns Observable.just(mutableListOf(dummySquareUrl))
            coEvery {
                changeVoucherPeriodUseCase.executeOnBackground()
            } returns dummyUpdateVoucherSuccess

            setStartDateCalendar(DUMMY_CALENDAR)
            setEndDateCalendar(DUMMY_CALENDAR)

            validateVoucherPeriod(voucherUiModel, dummyBitmap)

            coVerify {
                saveSquareVoucherUseCase.executeOnBackground()
                uploadVoucherUseCase.createObservable(any())
                changeVoucherPeriodUseCase.executeOnBackground()
            }

            assert(updateVoucherSuccessLiveData.value == Success(dummyUpdateVoucherSuccess))
        }
    }

    @Test
    fun `fail validating voucher period`() = runBlocking {
        with(mViewModel) {
            val dummySquareUrl: String? = DUMMY_VOUCHER_SQUARE_URL
            val dummyThrowable = MessageErrorException("")
            val dummyToken = ""

            coEvery {
                getTokenUseCase.executeOnBackground()
            } returns dummyToken
            coEvery {
                saveSquareVoucherUseCase.executeOnBackground()
            } returns DUMMY_VOUCHER_SQUARE
            coEvery {
                uploadVoucherUseCase.createObservable(any())
            } returns Observable.just(mutableListOf(dummySquareUrl))
            coEvery {
                changeVoucherPeriodUseCase.executeOnBackground()
            } throws dummyThrowable

            setStartDateCalendar(DUMMY_CALENDAR)
            setEndDateCalendar(DUMMY_CALENDAR)

            validateVoucherPeriod(voucherUiModel, dummyBitmap)

            coroutineContext[Job]?.children?.forEach { it.join() }

            coVerify {
                changeVoucherPeriodUseCase.executeOnBackground()
            }

            assert(updateVoucherSuccessLiveData.value is Fail)
        }
    }

    @Test
    fun `start validating voucher period will change pair live data if both start and end date are not null`() {
        with(mViewModel) {
            setStartDateCalendar(DUMMY_CALENDAR)
            setEndDateCalendar(DUMMY_CALENDAR)

            startValidating()

            assert(startEndDatePairLiveData.value != null)
        }
    }

    @Test
    fun `start validating voucher period will throw error if both start and end date are null`() {
        with(mViewModel) {
            startValidating()

            assert(updateVoucherSuccessLiveData.value is Fail)
        }
    }
}