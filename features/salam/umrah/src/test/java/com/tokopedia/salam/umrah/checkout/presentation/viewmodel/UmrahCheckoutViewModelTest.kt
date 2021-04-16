package com.tokopedia.salam.umrah.checkout.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutMapperEntity
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutResultEntity
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutResultParams
import com.tokopedia.salam.umrah.checkout.presentation.usecase.UmrahCheckoutGetDataUseCase
import com.tokopedia.salam.umrah.checkout.presentation.usecase.UmrahCheckoutResultUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
@RunWith(JUnit4::class)
class UmrahCheckoutViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var umrahCheckoutGetDataUseCase: UmrahCheckoutGetDataUseCase
    @RelaxedMockK
    lateinit var umrahCheckoutResultUseCase: UmrahCheckoutResultUseCase

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var umrahCheckoutViewModel: UmrahCheckoutViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        umrahCheckoutViewModel = UmrahCheckoutViewModel(umrahCheckoutGetDataUseCase,
                umrahCheckoutResultUseCase,
                dispatcher
        )
    }

    @Test
    fun `responseCheckoutMapper_SuccessCheckoutMapper_ShowActualResult`() {
        //given
        coEvery {
            umrahCheckoutGetDataUseCase.execute(any(),any(), any(), any(), any(),any(),any(), any(), any(), any(), any())
        } returns Success(UmrahCheckoutMapperEntity())

        //when
        umrahCheckoutViewModel.getDataCheckout("",
                "",
                "",
                "",
                "",
                "",
                0,
                0,
                "",
                "",0)

        val actual = umrahCheckoutViewModel.checkoutMapped.value
        assert(actual is Success)

    }

    @Test
    fun `responseCheckoutMapper_FailureCheckoutMapper_ShowFailureResult`(){
        //given
        coEvery {
            umrahCheckoutGetDataUseCase.execute(any(),any(), any(), any(), any(),any(),any(), any(), any(), any(), any())
        } returns Fail(Throwable())

        //when
        umrahCheckoutViewModel.getDataCheckout("",
                "",
                "",
                "",
                "",
                "",
                0,
                0,
                "",
                "",0)

        val actual = umrahCheckoutViewModel.checkoutMapped.value
        assert(actual is Fail)
    }


    @Test
    fun `responseCheckoutResult_SuccessCheckoutResult_ShowActualResult`() {
        //given
        coEvery {
            umrahCheckoutResultUseCase.execute(any(),any<UmrahCheckoutResultParams>())
        } returns Success(UmrahCheckoutResultEntity())

        //when
        umrahCheckoutViewModel.executeCheckout("",
                UmrahCheckoutResultParams()
        )

        val actual = umrahCheckoutViewModel.checkoutResult.value
        assert(actual is Success)

    }

    @Test
    fun `responseCheckoutResult_FailureCheckoutResult_ShowFailureResult`(){
        //given
        coEvery {
            umrahCheckoutResultUseCase.execute(any(),any<UmrahCheckoutResultParams>())
        } returns Fail(Throwable())

        //when
        //when
        umrahCheckoutViewModel.executeCheckout("",
                UmrahCheckoutResultParams()
        )

        val actual = umrahCheckoutViewModel.checkoutResult.value
        assert(actual is Fail)
    }

}