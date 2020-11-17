package com.tokopedia.tradein.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.tradein.model.AddressResult
import com.tokopedia.tradein.model.DeviceDataResponse
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse
import com.tokopedia.tradein.usecase.DiagnosticDataUseCase
import com.tokopedia.tradein.usecase.GetAddressUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FinalPriceViewModelTest {
    val context: Context = mockk()
    val getAddressUseCase: GetAddressUseCase = mockk()
    val diagnosticDataUseCase: DiagnosticDataUseCase = mockk()
    var finalPriceViewModel = spyk(FinalPriceViewModel(getAddressUseCase, diagnosticDataUseCase))
    val resources: Resources = mockk()

    @get:Rule
    var rule = InstantTaskExecutorRule()

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

    /**************************** getDiagnosticData() *******************************************/
    @Test
    fun getDiagnosticData() {
        val deviceDataResponse: DeviceDataResponse? = null
        coEvery { diagnosticDataUseCase.getDiagnosticData(any(), any()) } returns deviceDataResponse

        finalPriceViewModel.getDiagnosticData()

        assertEquals(finalPriceViewModel.deviceDiagData.value, deviceDataResponse)

    }

    @Test
    fun getDiagnosticDataException() {
        val exception = "Diagnostic Data Exception"
        coEvery { diagnosticDataUseCase.getDiagnosticData(any(), any()) } throws Exception(exception)
        coEvery { finalPriceViewModel.getResource()?.getString(any()) } returns exception

        finalPriceViewModel.getDiagnosticData()

        assertEquals(finalPriceViewModel.getWarningMessage().value, exception)

    }

    /**************************** getDiagnosticData() *******************************************/


    /**************************** getAddress() *******************************************/
    @Test
    fun getAddress() {
        val addressData: MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data? = null
        val token: Token? = null
        val addressResult = AddressResult(addressData, token)
        coEvery { getAddressUseCase.getAddress() } returns addressResult

        finalPriceViewModel.getAddress()

        assertEquals(finalPriceViewModel.addressLiveData.value, addressResult)
        assertEquals(finalPriceViewModel.getProgBarVisibility().value, false)

    }

    @Test
    fun getAddressException() {
        coEvery { getAddressUseCase.getAddress() } throws Exception("AddressResult Exception")

        finalPriceViewModel.getAddress()

        assertEquals(finalPriceViewModel.getErrorMessage().value, "AddressResult Exception")
        assertEquals(finalPriceViewModel.getProgBarVisibility().value, false)

    }

    /**************************** getDiagnosticData() *******************************************/
}