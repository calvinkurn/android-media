package com.tokopedia.moneyin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.moneyin.model.MoneyInCheckoutMutationResponse
import com.tokopedia.moneyin.model.MoneyInCourierResponse
import com.tokopedia.moneyin.viewmodel.MoneyInCheckoutViewModel
import com.tokopedia.moneyin.model.MoneyInScheduleOptionResponse
import com.tokopedia.moneyin.usecase.MoneyInCheckoutUseCase
import com.tokopedia.moneyin.usecase.MoneyInCourierRatesUseCase
import com.tokopedia.moneyin.usecase.MoneyInPickupScheduleUseCase
import com.tokopedia.moneyin.viewmodel.liveState.CourierPriceError
import com.tokopedia.moneyin.viewmodel.liveState.MutationCheckoutError
import com.tokopedia.moneyin.viewmodel.liveState.ScheduleTimeError
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class MoneyInCheckoutViewModelTest {
    private val moneyInPickupScheduleUseCase: MoneyInPickupScheduleUseCase = mockk()
    private val moneyInCourierRatesUseCase: MoneyInCourierRatesUseCase = mockk()
    private val moneyInCheckoutUseCase: MoneyInCheckoutUseCase = mockk()
    private var moneyInCheckoutViewModel = spyk(MoneyInCheckoutViewModel(moneyInPickupScheduleUseCase,
            moneyInCourierRatesUseCase,
            moneyInCheckoutUseCase))

    companion object{
        private const val SUCCESS = 1
        private const val FAILURE = 0
    }

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var scheduleOptionsResult: MoneyInScheduleOptionResponse.ResponseData

    @RelaxedMockK
    lateinit var courierResult: MoneyInCourierResponse.ResponseData

    @RelaxedMockK
    lateinit var checkoutResult: MoneyInCheckoutMutationResponse.ResponseData

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** getPickupScheduleOption() *******************************************/

    @Test
    fun getPickupScheduleOption(){
        coEvery { moneyInPickupScheduleUseCase.getPickupScheduleOption(any()) } returns scheduleOptionsResult

        moneyInCheckoutViewModel.getPickupScheduleOption()

        assertEquals(moneyInCheckoutViewModel.getPickupScheduleOptionLiveData().value, Success(scheduleOptionsResult.getPickupScheduleOption!!))
    }


    @Test
    fun getPickupScheduleOptionException(){
        coEvery { moneyInPickupScheduleUseCase.getPickupScheduleOption(any()) } throws Exception("Schedule Options Exception")

        moneyInCheckoutViewModel.getPickupScheduleOption()

        assertEquals(moneyInCheckoutViewModel.getErrorLiveData().value, ScheduleTimeError("Schedule Options Exception"))
    }

    /**************************** getPickupScheduleOption() *******************************************/

    /**************************** getCourierRates() *******************************************/

    @Test
    fun getCourierRates(){
        coEvery { moneyInCourierRatesUseCase.getCourierRates(any(), any()) } returns courierResult

        moneyInCheckoutViewModel.getCourierRates(anyString())

        assertEquals(moneyInCheckoutViewModel.getCourierRatesLiveData().value, Success(courierResult.ratesV4.data))
    }


    @Test
    fun getCourierRatesException(){
        coEvery { moneyInCourierRatesUseCase.getCourierRates(any(), any()) } throws Exception("Couriers Exception")

        moneyInCheckoutViewModel.getCourierRates(anyString())

        assertEquals(moneyInCheckoutViewModel.getErrorLiveData().value, CourierPriceError("Couriers Exception"))
    }

    /**************************** getCourierRates() *******************************************/

    /**************************** makeCheckoutMutation() *******************************************/

    @Test
    fun makeCheckoutMutation(){
        coEvery { moneyInCheckoutUseCase.createRequestParams(any(),any(),any(),any(),any()) } returns HashMap()
        coEvery { moneyInCheckoutUseCase.makeCheckoutMutation(any(), any()) } returns checkoutResult
        coEvery { checkoutResult.checkoutGeneral.data.success } returns SUCCESS

        moneyInCheckoutViewModel.makeCheckoutMutation("",0,0,0,0)

        assertEquals(moneyInCheckoutViewModel.getCheckoutDataLiveData().value, Success(checkoutResult.checkoutGeneral.data.data))

        /**Failure Case**/
        coEvery { checkoutResult.checkoutGeneral.data.success } returns FAILURE

        moneyInCheckoutViewModel.makeCheckoutMutation("",0,0,0,0)

        assertEquals(moneyInCheckoutViewModel.getCheckoutDataLiveData().value, Success(checkoutResult.checkoutGeneral.data.data))
    }


    @Test
    fun makeCheckoutMutationException(){
        coEvery { moneyInCheckoutUseCase.createRequestParams(any(),any(),any(),any(),any()) } returns HashMap()
        coEvery { moneyInCheckoutUseCase.makeCheckoutMutation(any(), any()) } throws Exception("CheckoutResult Exception")

        moneyInCheckoutViewModel.makeCheckoutMutation("",0,0,0,0)

        assertEquals(moneyInCheckoutViewModel.getErrorLiveData().value, MutationCheckoutError("CheckoutResult Exception"))
    }

    /**************************** makeCheckoutMutation() *******************************************/

}