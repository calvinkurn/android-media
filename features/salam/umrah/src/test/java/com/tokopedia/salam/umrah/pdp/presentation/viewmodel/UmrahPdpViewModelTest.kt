package com.tokopedia.salam.umrah.pdp.presentation.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.pdp.presentation.usecase.UmrahPdpGetDataUseCase
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

@RunWith(JUnit4::class)
class UmrahPdpViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var mUmrahPdpUseCase: UmrahPdpGetDataUseCase
    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var umrahPdpViewModel: UmrahPdpViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        umrahPdpViewModel = UmrahPdpViewModel(mUmrahPdpUseCase, dispatcher)
    }

    @Test
    fun getPdpResult_shouldReturn1Data() {
        // given
        coEvery {
            mUmrahPdpUseCase.executeUsecase(any(), any())
        } returns Success(UmrahProductModel.UmrahProduct())

        // when
        umrahPdpViewModel.requestPdpData("","")

        // then
        val actual = umrahPdpViewModel.pdpData.value
        assert(actual is Success)
    }

    @Test
    fun getPdpResult_shouldReturnThrowable() {
        // given
        coEvery {
            mUmrahPdpUseCase.executeUsecase(any(), any())
        } returns Fail(Throwable())

        // when
        umrahPdpViewModel.requestPdpData("","")

        // then
        val actual = umrahPdpViewModel.pdpData.value
        assert(actual is Fail)
    }
}