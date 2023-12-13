package com.tokopedia.logisticorder.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticorder.domain.response.GetDriverTipResponse
import com.tokopedia.logisticorder.mapper.DriverTipMapper
import com.tokopedia.logisticorder.uimodel.LogisticDriverModel
import com.tokopedia.logisticorder.usecase.GetDriverTipUseCase
import com.tokopedia.logisticorder.view.tipping.TippingDriverViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TippingDriverViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getDriverTipUseCase: GetDriverTipUseCase = mockk(relaxed = true)

    private val driverTipMapper = DriverTipMapper()

    private lateinit var viewModel: TippingDriverViewModel

    private val driverTipDataObserver: Observer<Result<LogisticDriverModel>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = TippingDriverViewModel(
            getDriverTipUseCase,
            driverTipMapper
        )
        viewModel.driverTipData.observeForever(driverTipDataObserver)
    }

    @Test
    fun `Driver Tips Data Success`() {
        coEvery { getDriverTipUseCase(any()) } returns GetDriverTipResponse()
        viewModel.getDriverTipsData("12234")
        verify { driverTipDataObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Driver Tips Data Fail`() {
        coEvery { getDriverTipUseCase(any()) } throws defaultThrowable
        viewModel.getDriverTipsData("12234")
        verify { driverTipDataObserver.onChanged(match { it is Fail }) }
    }
}
