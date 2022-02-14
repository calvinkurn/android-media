package com.tokopedia.otp.qrcode.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.otp.qrcode.domain.pojo.VerifyQrData
import com.tokopedia.otp.qrcode.domain.pojo.VerifyQrPojo
import com.tokopedia.otp.qrcode.domain.usecase.VerifyQrUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LoginByQrViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var verifyQrUseCase: VerifyQrUseCase

    @RelaxedMockK
    lateinit var verifyQrResultObserver: Observer<Result<VerifyQrData>>

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private lateinit var viewmodel: LoginByQrViewModel

    private val verifyQrData = VerifyQrData(success = true, imglink = "http://test.com", messageTitle = "title", messageBody = "body", buttonType = "button")
    private val successVerifyQrResponse = VerifyQrPojo(verifyQrData)

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewmodel = LoginByQrViewModel(
                verifyQrUseCase,
                dispatcherProviderTest
        )
    }

    @Test
    fun `Success verify qr`() {
        viewmodel.verifyQrResult.observeForever(verifyQrResultObserver)
        coEvery { verifyQrUseCase.getData(any()) } returns successVerifyQrResponse

        viewmodel.verifyQrCode("", "", "")

        verify { verifyQrResultObserver.onChanged(any<Success<VerifyQrData>>()) }
        assert(viewmodel.verifyQrResult.value is Success)

        val result = viewmodel.verifyQrResult.value as Success<VerifyQrData>
        assert(result.data == successVerifyQrResponse.data)
    }

    @Test
    fun `Failed verify qr errorMessage not empty`() {
        val errMsg = "error"
        successVerifyQrResponse.data.imglink = ""
        successVerifyQrResponse.data.errorMessage = errMsg

        viewmodel.verifyQrResult.observeForever(verifyQrResultObserver)
        coEvery { verifyQrUseCase.getData(any()) } returns successVerifyQrResponse

        viewmodel.verifyQrCode("", "", "")

        verify { verifyQrResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyQrResult.value is Fail)

        val result = viewmodel.verifyQrResult.value as Fail
        assert(result.throwable.message == errMsg)
    }

    @Test
    fun `Failed verify qr message not empty`() {
        val msg = "message"
        successVerifyQrResponse.data.imglink = ""
        successVerifyQrResponse.data.errorMessage = ""
        successVerifyQrResponse.data.message = msg

        viewmodel.verifyQrResult.observeForever(verifyQrResultObserver)
        coEvery { verifyQrUseCase.getData(any()) } returns successVerifyQrResponse

        viewmodel.verifyQrCode("", "", "")

        verify { verifyQrResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyQrResult.value is Fail)

        val result = viewmodel.verifyQrResult.value as Fail
        assert(result.throwable.message == msg)
    }

    @Test
    fun `Failed verify qr other error`() {
        successVerifyQrResponse.data.imglink = ""
        successVerifyQrResponse.data.errorMessage = ""
        successVerifyQrResponse.data.message = ""

        viewmodel.verifyQrResult.observeForever(verifyQrResultObserver)
        coEvery { verifyQrUseCase.getData(any()) } returns successVerifyQrResponse

        viewmodel.verifyQrCode("", "", "")

        verify { verifyQrResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyQrResult.value is Fail)
    }

    @Test
    fun `Failed verify qr`() {
        viewmodel.verifyQrResult.observeForever(verifyQrResultObserver)
        coEvery { verifyQrUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.verifyQrCode("", "", "")

        verify { verifyQrResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyQrResult.value is Fail)

        val result = viewmodel.verifyQrResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    companion object {
        private val throwable = Throwable()
    }
}