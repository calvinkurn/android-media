package com.tokopedia.otp.qrcode.viewmodel

import FileUtil
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
        private val successVerifyQrResponse: VerifyQrPojo = FileUtil.parse(
                "/success_verify_qr.json",
                VerifyQrPojo::class.java
        )
        private val throwable = Throwable()
    }
}