package com.tokopedia.saldodetails.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.saldodetails.utils.SaldoDatePickerUtil
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
import kotlinx.coroutines.test.runBlockingTest
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

    @Test
    fun `get deposite summary successs case`(){
            val startDate = "31 DEC 2019"
            val endDate = "29 JAN 2020"

            val observer = mockk<Observer<Errors>> {
                every { onChanged(any()) } just Runs
            }

            val observer1 = mockk<Observer<Resources<DepositActivityResponse>>> {
                every { onChanged(any()) } just runs
            }

            val allDeposite = mockk<DepositActivityResponse> {
                every { isHaveError } returns false
            }

            val buyerDeposite = mockk<DepositActivityResponse>()
            val sellerResponse = mockk<DepositActivityResponse>()

            coEvery { getDepositSummaryUseCase.execute(any()) } returns mockk {
                every { allDepositHistory } returns allDeposite
                every { buyerDepositHistory } returns buyerDeposite
                every { sellerDepositHistory } returns sellerResponse
            }

            viewModel.errors.observeForever(observer)
            viewModel.allDepositResponseLiveData.observeForever(observer1)
            viewModel.buyerResponseLiveData.observeForever(observer1)
            viewModel.sellerResponseLiveData.observeForever(observer1)

            viewModel.onSearchClicked(startDate, endDate)

            verify(exactly = 0) { observer.onChanged(ofType(ErrorType::class)) }
            verify(ordering = Ordering.ORDERED) {
                observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
                observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
                observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
                observer1.onChanged(ofType(Success::class as KClass<Success<DepositActivityResponse>>))
                observer1.onChanged(ofType(Success::class as KClass<Success<DepositActivityResponse>>))
                observer1.onChanged(ofType(Success::class as KClass<Success<DepositActivityResponse>>))
            }

            assert((viewModel.allDepositResponseLiveData.value as Success).data == allDeposite)
            assert((viewModel.buyerResponseLiveData.value as Success).data == buyerDeposite)
            assert((viewModel.sellerResponseLiveData.value as Success).data == sellerResponse)
    }

    @Test
    fun `get deposite sumary error case`(){
        val startDate = "31 DEC 2019"
        val endDate = "29 JAN 2020"

        val observer = mockk<Observer<Errors>> {
            every { onChanged(any()) } just Runs
        }

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>> {
            every { onChanged(any()) } just runs
        }

        viewModel.errors.observeForever(observer)
        viewModel.allDepositResponseLiveData.observeForever(observer1)
        viewModel.buyerResponseLiveData.observeForever(observer1)
        viewModel.sellerResponseLiveData.observeForever(observer1)

        viewModel.onSearchClicked(startDate, endDate)

        verify(exactly = 0) { observer.onChanged(ofType(ErrorType::class)) }
        verify(ordering = Ordering.ORDERED) {
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<DepositActivityResponse,Any>>))
            observer1.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<DepositActivityResponse,Any>>))
            observer1.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<DepositActivityResponse,Any>>))
        }
    }

    @Test
    fun `on end date clicked`(){


        val endDate = "29 JAN 2020"

        val saldoDatePicker = mockk<SaldoDatePickerUtil>()
        val saldoHistoryContract = mockk<SaldoHistoryContract.View>()

        every { saldoHistoryContract.getEndDate() } returns endDate
        
        every { saldoDatePicker.DatePickerCalendar (any()) } just Runs
        
        every { saldoDatePicker.setDate(any(),any(),any()) } just Runs
        
        viewModel.onEndDateClicked(saldoDatePicker, saldoHistoryContract)
        
        verify(ordering = Ordering.ORDERED) {
            saldoDatePicker.setDate(any(), any(), any())
            saldoDatePicker.DatePickerCalendar(any())
        }
    }
    @Test
    fun `on start date clicked`(){

        val startDate = "31 DEC 2019"


        val saldoDatePicker = mockk<SaldoDatePickerUtil>()
        val saldoHistoryContract = mockk<SaldoHistoryContract.View>()

        every { saldoHistoryContract.getStartDate() } returns startDate

        every { saldoDatePicker.DatePickerCalendar (any()) } just Runs

        every { saldoDatePicker.setDate(any(),any(),any()) } just Runs

        viewModel.onStartDateClicked(saldoDatePicker, saldoHistoryContract)

        verify(ordering = Ordering.ORDERED) {
            saldoDatePicker.setDate(any(), any(), any())
            saldoDatePicker.DatePickerCalendar(any())
        }
    }

    @Test
    fun `on end date formattor wrong`(){

        val endDate = "29  2020"

        val saldoDatePicker = mockk<SaldoDatePickerUtil>()
        val saldoHistoryContract = mockk<SaldoHistoryContract.View>()

        every { saldoHistoryContract.getEndDate() } returns endDate

        every { saldoDatePicker.DatePickerCalendar (any()) } just Runs

        every { saldoDatePicker.setDate(any(),any(),any()) } just Runs

        viewModel.onEndDateClicked(saldoDatePicker, saldoHistoryContract)

        verify(exactly = 0) {
            saldoDatePicker.setDate(any(), any(), any())
            saldoDatePicker.DatePickerCalendar(any())
        }
    }

    @Test
    fun `on end date formattor success`(){

        val endDate = "29 JAN 2020"

        val saldoDatePicker = mockk<SaldoDatePickerUtil>()
        val saldoHistoryContract = mockk<SaldoHistoryContract.View>()

        every { saldoHistoryContract.getEndDate() } returns endDate

        every { saldoDatePicker.DatePickerCalendar (any()) } just Runs

        every { saldoDatePicker.setDate(any(),any(),any()) } just Runs

        viewModel.onEndDateClicked(saldoDatePicker, saldoHistoryContract)

        verify(ordering = Ordering.ORDERED) {
            saldoDatePicker.setDate(any(), any(), any())
            saldoDatePicker.DatePickerCalendar(any())
        }
    }


    @Test
    fun `load MoreAll Transaction for error case`() {
        val startDate = "31 DEC 2019"
        val endDate = "29 JAN 2020"

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>> {
            every { onChanged(any()) } just runs
        }

        viewModel.allDepositResponseLiveData.observeForever(observer1)
        viewModel.setDates(startDate, endDate)
        viewModel.loadMoreAllTransaction(1)

        verify(ordering = Ordering.ORDERED) {
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<DepositActivityResponse, Any>>))
        }
    }

    @Test
    fun `load MoreAll Transaction for Success case`() {
        val startDate = "31 DEC 2019"
        val endDate = "29 JAN 2020"

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>> {
            every { onChanged(any()) } just runs
        }

        val allDeposite = mockk<DepositActivityResponse> {
            every { isHaveError } returns false
        }

        coEvery {  getalltransactionusecase.execute(any()) } returns mockk{
            every { allDepositHistory } returns allDeposite
        }

        viewModel.allDepositResponseLiveData.observeForever(observer1)
        viewModel.setDates(startDate, endDate)
        viewModel.loadMoreAllTransaction(1)

        verify(ordering = Ordering.ORDERED) {
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(AddElements::class as KClass<AddElements<DepositActivityResponse>>))
        }

        assert((viewModel.allDepositResponseLiveData.value as AddElements).data == allDeposite)
    }

    @Test
    fun `load seller Transaction for error case`() {
        val startDate = "31 DEC 2019"
        val endDate = "29 JAN 2020"

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>> {
            every { onChanged(any()) } just runs
        }

        viewModel.sellerResponseLiveData.observeForever(observer1)
        viewModel.setDates(startDate, endDate)
        viewModel.loadMoreSellerTransaction(1)

        verify(ordering = Ordering.ORDERED) {
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<DepositActivityResponse, Any>>))
        }
    }

    @Test
    fun `load seller Transaction for Success case`() {
        val startDate = "31 DEC 2019"
        val endDate = "29 JAN 2020"

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>> {
            every { onChanged(any()) } just runs
        }

        val allDeposite = mockk<DepositActivityResponse> {
            every { isHaveError } returns false
        }

        coEvery {  getalltransactionusecase.execute(any()) } returns mockk{
            every { allDepositHistory } returns allDeposite
        }

        viewModel.sellerResponseLiveData.observeForever(observer1)
        viewModel.setDates(startDate, endDate)
        viewModel.loadMoreSellerTransaction(1)

        verify(ordering = Ordering.ORDERED) {
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(AddElements::class as KClass<AddElements<DepositActivityResponse>>))
        }

        assert((viewModel.sellerResponseLiveData.value as AddElements).data == allDeposite)
    }

    @Test
    fun `load buyer Transaction for error case`() {
        val startDate = "31 DEC 2019"
        val endDate = "29 JAN 2020"

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>> {
            every { onChanged(any()) } just runs
        }

        viewModel.buyerResponseLiveData.observeForever(observer1)
        viewModel.setDates(startDate, endDate)
        viewModel.loadMoreBuyerTransaction(1)

        verify(ordering = Ordering.ORDERED) {
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<DepositActivityResponse, Any>>))
        }
    }

    @Test
    fun `load buyer Transaction for Success case`() {
        val startDate = "31 DEC 2019"
        val endDate = "29 JAN 2020"

        val observer1 = mockk<Observer<Resources<DepositActivityResponse>>> {
            every { onChanged(any()) } just runs
        }

        val allDeposite = mockk<DepositActivityResponse> {
            every { isHaveError } returns false
        }

        coEvery {  getalltransactionusecase.execute(any()) } returns mockk{
            every { allDepositHistory } returns allDeposite
        }

        viewModel.buyerResponseLiveData.observeForever(observer1)
        viewModel.setDates(startDate, endDate)
        viewModel.loadMoreBuyerTransaction(1)

        verify(ordering = Ordering.ORDERED) {
            observer1.onChanged(ofType(Loading::class as KClass<Loading<DepositActivityResponse>>))
            observer1.onChanged(ofType(AddElements::class as KClass<AddElements<DepositActivityResponse>>))
        }

        assert((viewModel.buyerResponseLiveData.value as AddElements).data == allDeposite)
    }
}