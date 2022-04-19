package com.tokopedia.salam.umrah.pdp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.pdp.presentation.usecase.UmrahPdpGetAvailabilityUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery


@RunWith(JUnit4::class)
class UmrahPdpDetailViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var mUmrahPdpUseCase: UmrahPdpGetAvailabilityUseCase
    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var umrahPdpViewModel: UmrahPdpDetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        umrahPdpViewModel = UmrahPdpDetailViewModel(mUmrahPdpUseCase, dispatcher)
    }

    @Test
    fun getPdpAvailability_shouldReturn5() {
        // given
        val productModel = UmrahProductModel.UmrahProduct()
        productModel.availableSeat = 5
        coEvery {
            mUmrahPdpUseCase.executeUsecase(any(), any())
        } returns Success(productModel)

        // when
        umrahPdpViewModel.getPdpAvailability("","")

        // then
        val actual = umrahPdpViewModel.pdpAvailability.value
        assert(actual is Success)
        assert((actual as Success).data == productModel.availableSeat)
    }

    @Test
    fun getPdpAvailability_shouldReturnFail() {
        // given
        coEvery {
            mUmrahPdpUseCase.executeUsecase(any(), any())
        } returns Fail(Throwable())

        // when
        umrahPdpViewModel.getPdpAvailability("","")

        // then
        val actual = umrahPdpViewModel.pdpAvailability.value
        assert(actual is Fail)
    }
}