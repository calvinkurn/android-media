package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ChangeVoucherPeriodViewModelTest {

    companion object {
        private val DUMMY_CALENDAR =
                Calendar.Builder()
                        .setDate(1,1,1)
                        .build()
    }

    @RelaxedMockK
    lateinit var changeVoucherPeriodUseCase: ChangeVoucherPeriodUseCase

    @RelaxedMockK
    lateinit var getTokenUseCase: GetTokenUseCase

    @RelaxedMockK
    lateinit var updateVoucherSuccessObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var voucherUiModel: VoucherUiModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel.updateVoucherSuccessLiveData.observeForever(updateVoucherSuccessObserver)
    }

    @After
    fun cleanup() {
        testDispatcher.cleanupTestCoroutines()

        mViewModel.updateVoucherSuccessLiveData.removeObserver(updateVoucherSuccessObserver)
    }

    private val testDispatcher by lazy {
        TestCoroutineDispatcher()
    }

    private val mViewModel by lazy {
        ChangeVoucherPeriodViewModel(testDispatcher, changeVoucherPeriodUseCase, getTokenUseCase)
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
            val dummyToken = ""

            coEvery {
                getTokenUseCase.executeOnBackground()
            } returns dummyToken
            coEvery {
                changeVoucherPeriodUseCase.executeOnBackground()
            } returns dummyUpdateVoucherSuccess

            setStartDateCalendar(DUMMY_CALENDAR)
            setEndDateCalendar(DUMMY_CALENDAR)

            validateVoucherPeriod(voucherUiModel)

            coroutineContext[Job]?.children?.forEach { it.join() }

            coVerify {
                changeVoucherPeriodUseCase.executeOnBackground()
            }

            assert(updateVoucherSuccessLiveData.value == Success(dummyUpdateVoucherSuccess))
        }
    }

    @Test
    fun `fail validating voucher period`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")
            val dummyToken = ""

            coEvery {
                getTokenUseCase.executeOnBackground()
            } returns dummyToken
            coEvery {
                changeVoucherPeriodUseCase.executeOnBackground()
            } throws dummyThrowable

            setStartDateCalendar(DUMMY_CALENDAR)
            setEndDateCalendar(DUMMY_CALENDAR)

            validateVoucherPeriod(voucherUiModel)

            coroutineContext[Job]?.children?.forEach { it.join() }

            coVerify {
                changeVoucherPeriodUseCase.executeOnBackground()
            }

            assert(updateVoucherSuccessLiveData.value is Fail)
        }
    }
}