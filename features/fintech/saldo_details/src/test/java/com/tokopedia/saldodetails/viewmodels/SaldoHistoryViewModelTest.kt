package com.tokopedia.saldodetails.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.saldodetails.contract.SaldoHistoryContract
import com.tokopedia.saldodetails.response.model.DepositActivityResponse
import com.tokopedia.saldodetails.usecase.GetAllTransactionUsecase
import com.tokopedia.saldodetails.usecase.GetDepositSummaryUseCase
import com.tokopedia.saldodetails.utils.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import java.util.*
import kotlin.reflect.KClass

class SaldoHistoryViewModelTest {

    val getalltransactionusecase = mockk<GetAllTransactionUsecase>()
    val getDepositSummaryUseCase = mockk<GetDepositSummaryUseCase>()
    val viewContract = mockk<SaldoHistoryContract.View>()

    lateinit var viewModel: SaldoHistoryViewModel
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = SaldoHistoryViewModel(getDepositSummaryUseCase,getalltransactionusecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun setFirstDateParameter() {
        val cal = Calendar.getInstance()
        val dateFormate = "dd MMM yyyy"
        val endDate  = cal.time.toFormattedString(dateFormate)
        cal.add(Calendar.DAY_OF_MONTH,-1)
        val startDate = cal.time.toFormattedString(dateFormate)

        every { viewContract.setStartDate(startDate) } just Runs
        every { viewContract.setEndDate(endDate) } just Runs

        viewModel.setFirstDateParameter(viewContract)

        verify {
            viewContract.setStartDate(startDate)
            viewContract.setEndDate(endDate)
        }

    }

    @Test
    fun `date validation check with wrong start date and end date`() {
 //       val dateFormate = "dd MMM yyyy"
        val endDate  = "29 JAN 2020"
        val startDate = "30 JAN 2020"

        val observer = mockk<Observer<Errors>>{
            every { onChanged(any()) } just Runs
        }

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>>{
            every { onChanged(any()) } just runs
        }

        viewModel.errors.observeForever(observer)
        viewModel.allDepositResponseLiveData.observeForever(observer1)
        viewModel.buyerResponseLiveData.observeForever(observer1)
        viewModel.sellerResponseLiveData.observeForever(observer1)

        viewModel.onSearchClicked(startDate, endDate)

        verify { observer.onChanged(ofType(ErrorType::class)) }
        verify (exactly = 0){ observer1.onChanged(any())  }

        val result = viewModel.errors.value as ErrorType<*>

        assert(result.type == IN_VALID_DATE_ERROR)

    }

    @Test
    fun `date validation check with wrong date`() {
        //       val dateFormate = "dd MMM yyyy"
        val endDate  = "29 2020"
        val startDate = "30 JAN 2020"

        val observer = mockk<Observer<Errors>>{
            every { onChanged(any()) } just Runs
        }

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>>{
            every { onChanged(any()) } just runs
        }

        viewModel.errors.observeForever(observer)
        viewModel.allDepositResponseLiveData.observeForever(observer1)
        viewModel.buyerResponseLiveData.observeForever(observer1)
        viewModel.sellerResponseLiveData.observeForever(observer1)

        viewModel.onSearchClicked(startDate, endDate)

        verify { observer.onChanged(ofType(ErrorType::class)) }
        verify (exactly = 0){ observer1.onChanged(any())  }

        val result = viewModel.errors.value as ErrorType<*>

        assert(result.type == IN_VALID_DATE_ERROR)

    }

    @Test
    fun `date validation check with wrong max days difference`() {
        //       val dateFormate = "dd MMM yyyy"
        val endDate  = "25 DEC 2020"
        val startDate = "30 JAN 2020"

        val observer = mockk<Observer<Errors>>{
            every { onChanged(any()) } just Runs
        }

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>>{
            every { onChanged(any()) } just runs
        }

        viewModel.errors.observeForever(observer)
        viewModel.allDepositResponseLiveData.observeForever(observer1)
        viewModel.buyerResponseLiveData.observeForever(observer1)
        viewModel.sellerResponseLiveData.observeForever(observer1)

        viewModel.onSearchClicked(startDate, endDate)

        verify { observer.onChanged(ofType(ErrorType::class)) }
        verify (exactly = 0){ observer1.onChanged(any())  }

        val result = viewModel.errors.value as ErrorType<*>

        assert(result.type == IN_VALID_DATE_ERROR)

    }

//    @Test
//    fun `valid date successs case`(){
//        val startDate  = "31 DEC 2019"
//        val endDate = "29 JAN 2020"
//
//        val observer = mockk<Observer<Errors>>{
//            every { onChanged(any()) } just Runs
//        }
//
//        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>>{
//            every { onChanged(any()) } just runs
//        }
//
//        every { getDepositSummaryUseCase.setRequesting(true) } Just Runs
//
//        viewModel.errors.observeForever(observer)
//        viewModel.allDepositResponseLiveData.observeForever(observer1)
//        viewModel.buyerResponseLiveData.observeForever(observer1)
//        viewModel.sellerResponseLiveData.observeForever(observer1)
//
//        viewModel.onSearchClicked(startDate, endDate)
//
//        verify (exactly = 0){ observer.onChanged(ofType(ErrorType::class)) }
//        verify (exactly = 3){ observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))  }
//
//    }

    @Test
    fun loadMoreAllTransaction() {
    }

    @Test
    fun loadMoreSellerTransaction() {
    }

    @Test
    fun loadMoreBuyerTransaction() {
    }
}