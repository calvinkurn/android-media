package com.tokopedia.otp.verification.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.usecase.*
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
    lateinit var getVerificationMethodUseCase2FA: GetVerificationMethodUseCase2FA

    @RelaxedMockK
    lateinit var otpValidateUseCase: OtpValidateUseCase

    @RelaxedMockK
    lateinit var otpValidateUseCase2FA: OtpValidateUseCase2FA

    @RelaxedMockK
    lateinit var sendOtpUseCase2FA: SendOtp2FAUseCase

    @RelaxedMockK
    lateinit var sendOtpUseCase: SendOtpUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig

    @RelaxedMockK
    lateinit var getVerificationMethodResultObserver: Observer<Result<OtpModeListData>>

    @RelaxedMockK
    lateinit var sendOtpResultObserver: Observer<Result<OtpRequestData>>

    @RelaxedMockK
    lateinit var otpValidateResultObserver: Observer<Result<OtpValidateData>>

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private lateinit var viewmodel: VerificationViewModel

    private val otpModeListDataMock = OtpModeListData(success = true, errorMessage = "")
    private val successGetVerificationMethodResponse = OtpModeListPojo(otpModeListDataMock)

    private val otpValidateData = OtpValidateData(success = true, validateToken = "abc123")
    private val successOtpValidationResponse = OtpValidatePojo(otpValidateData)

    private val otpRequestData = OtpRequestData(success = true)
    private val successSendOtpResponse = OtpRequestPojo(otpRequestData)

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewmodel = VerificationViewModel(
                getVerificationMethodUseCase,
                getVerificationMethodUseCase2FA,
                otpValidateUseCase,
                otpValidateUseCase2FA,
                sendOtpUseCase,
                sendOtpUseCase2FA,
                userSessionInterface,
                remoteConfig,
                dispatcherProviderTest
        )
    }

    @Test
    fun `Success get verification method`() {
        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase.getData(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethod("", "", "", "", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Success<OtpModeListData>>()) }
        assert(viewmodel.getVerificationMethodResult.value is Success)

        val result = viewmodel.getVerificationMethodResult.value as Success<OtpModeListData>
        assert(result.data == successGetVerificationMethodResponse.data)
    }

    @Test
    fun `Success get verification method message not empty`() {
        successGetVerificationMethodResponse.data.errorMessage = "error"
        successGetVerificationMethodResponse.data.success = false

        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase.getData(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethod("", "", "", "", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getVerificationMethodResult.value is Fail)
    }

    @Test
    fun `Success get verification method message not empty and success == false`() {
        successGetVerificationMethodResponse.data.errorMessage = ""
        successGetVerificationMethodResponse.data.success = false

        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase.getData(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethod("", "", "", "", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getVerificationMethodResult.value is Fail)
    }

    @Test
    fun `Failed get verification method`() {
        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.getVerificationMethod("", "", "", "", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getVerificationMethodResult.value is Fail)

        val result = viewmodel.getVerificationMethodResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success get verification method 2fa`() {
        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase2FA.getData(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethod2FA("", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Success<OtpModeListData>>()) }
        assert(viewmodel.getVerificationMethodResult.value is Success)

        val result = viewmodel.getVerificationMethodResult.value as Success<OtpModeListData>
        assert(result.data == successGetVerificationMethodResponse.data)
    }

    @Test
    fun `Failed get verification method 2fa error message not empty`() {
        successGetVerificationMethodResponse.data.errorMessage = "error"
        successGetVerificationMethodResponse.data.success = false

        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase2FA.getData(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethod2FA("", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getVerificationMethodResult.value is Fail)
    }

    @Test
    fun `Failed get verification method 2fa error message empty and success == false`() {
        successGetVerificationMethodResponse.data.errorMessage = ""
        successGetVerificationMethodResponse.data.success = false

        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase2FA.getData(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethod2FA("", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getVerificationMethodResult.value is Fail)
    }

    @Test
    fun `Failed get verification method 2fa`() {
        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodUseCase2FA.getData(any()) } coAnswers { throw throwable }

        viewmodel.getVerificationMethod2FA("", "", "")

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
    fun `Success send otp method 2fa`() {
        viewmodel.sendOtpResult.observeForever(sendOtpResultObserver)
        coEvery { sendOtpUseCase2FA.getData(any()) } returns successSendOtpResponse

        viewmodel.sendOtp2FA("", "", "", "", 0, "", "")

        verify { sendOtpResultObserver.onChanged(any<Success<OtpRequestData>>()) }
        assert(viewmodel.sendOtpResult.value is Success)

        val result = viewmodel.sendOtpResult.value as Success<OtpRequestData>
        assert(result.data == successSendOtpResponse.data)
    }

    @Test
    fun `Failed send otp method 2fa`() {
        viewmodel.sendOtpResult.observeForever(sendOtpResultObserver)
        coEvery { sendOtpUseCase2FA.getData(any()) } coAnswers { throw throwable }

        viewmodel.sendOtp2FA("", "", "", "", 0, "", "")

        verify { sendOtpResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.sendOtpResult.value is Fail)

        val result = viewmodel.sendOtpResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success validate otp method`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate("", "", "", "", "", "", "", "", "", 0)

        verify { otpValidateResultObserver.onChanged(any<Success<OtpValidateData>>()) }
        assert(viewmodel.otpValidateResult.value is Success)

        val result = viewmodel.otpValidateResult.value as Success<OtpValidateData>
        assert(result.data == successOtpValidationResponse.data)
    }

    @Test
    fun `Failed validate otp method error message not empty`() {
        successOtpValidationResponse.data.success = false
        successOtpValidationResponse.data.errorMessage = "error"

        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate("", "", "", "", "", "", "", "", "", 0)

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)
    }

    @Test
    fun `Failed validate otp method error message is empty and success == false`() {
        successOtpValidationResponse.data.success = false

        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate("", "", "", "", "", "", "", "", "", 0)

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)
    }

    @Test
    fun `Failed validate otp method`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.otpValidate("", "", "", "", "", "", "", "", "", 0)

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)

        val result = viewmodel.otpValidateResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success validate otp method 2fa`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate2FA("", "", "", "", "")

        verify { otpValidateResultObserver.onChanged(any<Success<OtpValidateData>>()) }
        assert(viewmodel.otpValidateResult.value is Success)

        val result = viewmodel.otpValidateResult.value as Success<OtpValidateData>
        assert(result.data == successOtpValidationResponse.data)
    }

    @Test
    fun `Success validate otp method 2fa error message isNotEmpty`() {
        successOtpValidationResponse.data.errorMessage = "error"
        successOtpValidationResponse.data.success = false

        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate2FA("", "", "", "", "")

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)
    }

    @Test
    fun `Success validate otp method 2fa error message isEmpty & success == false`() {
        successOtpValidationResponse.data.errorMessage = ""
        successOtpValidationResponse.data.success = false

        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate2FA("", "", "", "", "")

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)
    }

    @Test
    fun `Failed validate otp method 2fa`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } coAnswers { throw throwable }

        viewmodel.otpValidate2FA("", "", "", "", "")

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)

        val result = viewmodel.otpValidateResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `on viewmodel clear`() {
        viewmodel.done = false
        viewmodel.isLoginRegisterFlow = true
        val clearValue = true
        coEvery { remoteConfig.getBoolean(RemoteConfigKey.PRE_OTP_LOGIN_CLEAR, true) } returns clearValue

        viewmodel.onCleared()

        assert(userSessionInterface.accessToken.isNullOrEmpty())
    }

    companion object {
        private val throwable = Throwable()
    }
}