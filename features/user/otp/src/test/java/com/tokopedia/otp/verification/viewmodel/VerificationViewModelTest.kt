package com.tokopedia.otp.verification.viewmodel

import FileUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.otp.verification.DispatcherProviderTest
import com.tokopedia.otp.verification.domain.data.*
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodUseCase
import com.tokopedia.otp.verification.domain.usecase.OtpValidateUseCase
import com.tokopedia.otp.verification.domain.usecase.SendOtpUseCase
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
class VerificationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getVerificationMethodUseCase: GetVerificationMethodUseCase
    @RelaxedMockK
    lateinit var otpValidateUseCase: OtpValidateUseCase
    @RelaxedMockK
    lateinit var sendOtpUseCase: SendOtpUseCase
    @RelaxedMockK
    lateinit var getVerificationMethodResultObserver: Observer<Result<OtpModeListData>>
    @RelaxedMockK
    lateinit var sendOtpResultObserver: Observer<Result<OtpRequestData>>
    @RelaxedMockK
    lateinit var otpValidateResultObserver: Observer<Result<OtpValidateData>>

    private val dispatcherProviderTest = DispatcherProviderTest()

    private lateinit var viewmodel: VerificationViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewmodel = VerificationViewModel(getVerificationMethodUseCase, otpValidateUseCase, sendOtpUseCase, dispatcherProviderTest)
    }

    @Test
    fun `Success get verification method`() {
        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase.getData(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethod("", "", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Success<OtpModeListData>>()) }
        assert(viewmodel.getVerificationMethodResult.value is Success)

        val result = viewmodel.getVerificationMethodResult.value as Success<OtpModeListData>
        assert(result.data == successGetVerificationMethodResponse.data)
    }

    @Test
    fun `Failed get verification method`() {
        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.getVerificationMethod("", "", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getVerificationMethodResult.value is Fail)

        val result = viewmodel.getVerificationMethodResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success send otp method`() {
        viewmodel.sendOtpResult.observeForever(sendOtpResultObserver)
        coEvery { sendOtpUseCase.getData(any()) } returns successSendOtpResponse

        viewmodel.sendOtp("", "", "", "", 0)

        verify { sendOtpResultObserver.onChanged(any<Success<OtpRequestData>>()) }
        assert(viewmodel.sendOtpResult.value is Success)

        val result = viewmodel.sendOtpResult.value as Success<OtpRequestData>
        assert(result.data == successSendOtpResponse.data)
    }

    @Test
    fun `Failed send otp method`() {
        viewmodel.sendOtpResult.observeForever(sendOtpResultObserver)
        coEvery { sendOtpUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.sendOtp("", "", "", "", 0)

        verify { sendOtpResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.sendOtpResult.value is Fail)

        val result = viewmodel.sendOtpResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success validate otp method`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate(code = "", otpType = "", userId = 0)

        verify { otpValidateResultObserver.onChanged(any<Success<OtpValidateData>>()) }
        assert(viewmodel.otpValidateResult.value is Success)

        val result = viewmodel.otpValidateResult.value as Success<OtpValidateData>
        assert(result.data == successOtpValidationResponse.data)
    }

    @Test
    fun `Failed validate otp method`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.otpValidate(code = "", otpType = "", userId = 0)

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)

        val result = viewmodel.otpValidateResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    companion object {
        private val successGetVerificationMethodResponse: OtpModeListPojo = FileUtil.parse(
                "/success_get_verification_method.json",
                OtpModeListPojo::class.java
        )
        private val successOtpValidationResponse: OtpValidatePojo = FileUtil.parse(
                "/success_otp_validate.json",
                OtpValidatePojo::class.java
        )
        private val successSendOtpResponse: OtpRequestPojo = FileUtil.parse(
                "/success_send_otp.json",
                OtpRequestPojo::class.java
        )
        private val throwable = Throwable()
    }
}