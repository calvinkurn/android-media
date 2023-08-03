package com.tokopedia.otp.verification.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.otp.verification.data.OtpConstant.DEFAULT_OTP_BEHAVIOR_ONE
import com.tokopedia.otp.verification.data.OtpConstant.DEFAULT_OTP_BEHAVIOR_TWO
import com.tokopedia.otp.verification.data.OtpConstant.StaticText.SPAN_USE_OTHER_METHODS
import com.tokopedia.otp.verification.data.OtpConstant.StaticText.SPAN_USE_SMS_METHOD
import com.tokopedia.otp.verification.data.OtpConstant.StaticText.spanFactory
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.usecase.GetOtpModeListUseCase
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodInactivePhoneUseCase
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodPhoneRegisterMandatoryUseCase
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodUseCase
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodUseCase2FA
import com.tokopedia.otp.verification.domain.usecase.OtpValidatePhoneRegisterMandatoryUseCase
import com.tokopedia.otp.verification.domain.usecase.OtpValidateUseCase
import com.tokopedia.otp.verification.domain.usecase.OtpValidateUseCase2FA
import com.tokopedia.otp.verification.domain.usecase.SendOtp2FAUseCase
import com.tokopedia.otp.verification.domain.usecase.SendOtpPhoneRegisterMandatoryUseCase
import com.tokopedia.otp.verification.domain.usecase.SendOtpUseCase
import com.tokopedia.otp.verification.view.uimodel.DefaultOtpUiModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.pin.PinStatusData
import com.tokopedia.sessioncommon.data.pin.PinStatusParam
import com.tokopedia.sessioncommon.data.pin.PinStatusResponse
import com.tokopedia.sessioncommon.domain.usecase.CheckPinHashV2UseCase
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class VerificationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getVerificationMethodUseCase: GetVerificationMethodUseCase

    @RelaxedMockK
    lateinit var getOtpModeList: GetOtpModeListUseCase

    @RelaxedMockK
    lateinit var getVerificationMethodUseCase2FA: GetVerificationMethodUseCase2FA

    @RelaxedMockK
    lateinit var getVerificationMethodInactivePhoneUseCase: GetVerificationMethodInactivePhoneUseCase

    @RelaxedMockK
    lateinit var getVerificationMethodPhoneRegisterMandatoryUseCase: GetVerificationMethodPhoneRegisterMandatoryUseCase

    @RelaxedMockK
    lateinit var checkPinHashV2UseCase: CheckPinHashV2UseCase

    @RelaxedMockK
    lateinit var generatePublicKeyUseCase: GeneratePublicKeyUseCase

    @RelaxedMockK
    lateinit var otpValidateUseCase: OtpValidateUseCase

    @RelaxedMockK
    lateinit var otpValidateUseCase2FA: OtpValidateUseCase2FA

    @RelaxedMockK
    lateinit var otpValidatePhoneRegisterMandatoryUseCase: OtpValidatePhoneRegisterMandatoryUseCase

    @RelaxedMockK
    lateinit var sendOtpUseCase2FA: SendOtp2FAUseCase

    @RelaxedMockK
    lateinit var sendOtpUseCase: SendOtpUseCase

    @RelaxedMockK
    lateinit var sendOtpPhoneRegisterMandatoryUseCase: SendOtpPhoneRegisterMandatoryUseCase

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
            getOtpModeList,
            getVerificationMethodUseCase2FA,
            getVerificationMethodInactivePhoneUseCase,
            getVerificationMethodPhoneRegisterMandatoryUseCase,
            checkPinHashV2UseCase,
            generatePublicKeyUseCase,
            otpValidateUseCase,
            otpValidateUseCase2FA,
            otpValidatePhoneRegisterMandatoryUseCase,
            sendOtpUseCase,
            sendOtpUseCase2FA,
            sendOtpPhoneRegisterMandatoryUseCase,
            userSessionInterface,
            remoteConfig,
            dispatcherProviderTest
        )

        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
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
    fun `Success get verification method inactive phone`() {
        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodInactivePhoneUseCase(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethodInactive("", "", "", "", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Success<OtpModeListData>>()) }
        assert(viewmodel.getVerificationMethodResult.value is Success)

        val result = viewmodel.getVerificationMethodResult.value as Success<OtpModeListData>
        assert(result.data == successGetVerificationMethodResponse.data)
    }

    @Test
    fun `Success get verification method inactive phone - error message not empty`() {
        val errMsg = "error"
        successGetVerificationMethodResponse.data.success = false
        successGetVerificationMethodResponse.data.errorMessage = errMsg

        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodInactivePhoneUseCase(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethodInactive("", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
        assert((viewmodel.getVerificationMethodResult.value as Fail).throwable.message == errMsg)
    }

    @Test
    fun `Success get verification method inactive phone - error message empty & success false`() {
        successGetVerificationMethodResponse.data.success = false
        successGetVerificationMethodResponse.data.errorMessage = ""

        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodInactivePhoneUseCase(any()) } returns successGetVerificationMethodResponse

        viewmodel.getVerificationMethodInactive("", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
    }

    @Test
    fun `Failed get verification method inactive phone`() {
        viewmodel.getVerificationMethodResult.observeForever(getVerificationMethodResultObserver)
        coEvery { getVerificationMethodInactivePhoneUseCase(any()) } coAnswers { throw throwable }

        viewmodel.getVerificationMethodInactive("", "", "")

        verify { getVerificationMethodResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getVerificationMethodResult.value is Fail)

        val result = viewmodel.getVerificationMethodResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success get verification method phone register mandatory`() {
        // Given
        val otpType = "168"
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"

        // When
        coEvery { getVerificationMethodPhoneRegisterMandatoryUseCase(any()) } returns
            successGetVerificationMethodResponse
        viewmodel.getVerificationMethodPhoneRegisterMandatory(otpType, validateToken, email, msisdn)

        // Then
        val result = viewmodel.getVerificationMethodResult.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(successGetVerificationMethodResponse.data, result.data)
    }

    @Test
    fun `Success get verification method phone register mandatory - error message not empty`() {
        // Given
        val otpType = "168"
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"
        val errMsg = "error"
        successGetVerificationMethodResponse.data.success = false
        successGetVerificationMethodResponse.data.errorMessage = errMsg

        // When
        coEvery { getVerificationMethodPhoneRegisterMandatoryUseCase(any()) } returns
            successGetVerificationMethodResponse
        viewmodel.getVerificationMethodPhoneRegisterMandatory(otpType, validateToken, email, msisdn)

        // Then
        val result = viewmodel.getVerificationMethodResult.value
        assertTrue(result is Fail)
        assertEquals(errMsg, result.throwable.message)
    }

    @Test
    fun `Success get verification method phone register mandatory - error message empty & success false`() {
        // Given
        val otpType = "168"
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"
        successGetVerificationMethodResponse.data.success = false
        successGetVerificationMethodResponse.data.errorMessage = ""

        // When
        coEvery { getVerificationMethodPhoneRegisterMandatoryUseCase(any()) } returns
            successGetVerificationMethodResponse
        viewmodel.getVerificationMethodPhoneRegisterMandatory(otpType, validateToken, email, msisdn)

        // Then
        val result = viewmodel.getVerificationMethodResult.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `Failed get verification method phone register mandatory`() {
        // Given
        val otpType = "168"
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"

        // When
        coEvery { getVerificationMethodPhoneRegisterMandatoryUseCase(any()) } coAnswers { throw throwable }
        viewmodel.getVerificationMethodPhoneRegisterMandatory(otpType, validateToken, email, msisdn)

        // Then
        val result = viewmodel.getVerificationMethodResult.getOrAwaitValue()
        assertTrue(result is Fail)
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
    fun `Success send otp method phone register mandatory`() {
        // Given
        val otpType = "168"
        val mode = "SMS"
        val otpDigit = 6
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"

        // When
        coEvery { sendOtpPhoneRegisterMandatoryUseCase(any()) } returns successSendOtpResponse
        viewmodel.sendOtpPhoneRegisterMandatory(otpType, mode, msisdn, email, otpDigit, validateToken)

        // Then
        val result = viewmodel.sendOtpResult.getOrAwaitValue()
        assertTrue(result is Success)
        assert(result.data == successSendOtpResponse.data)
    }

    @Test
    fun `Failed send otp method phone register mandatory`() {
        // Given
        val otpType = "168"
        val mode = "SMS"
        val otpDigit = 6
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"

        // When
        coEvery { sendOtpPhoneRegisterMandatoryUseCase(any()) } coAnswers { throw throwable }
        viewmodel.sendOtpPhoneRegisterMandatory(otpType, mode, msisdn, email, otpDigit, validateToken)

        // Then
        val result = viewmodel.sendOtpResult.getOrAwaitValue()
        assertTrue(result is Fail)
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
    fun `Success validate otp method - pin isNeedHash false`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } returns successOtpValidationResponse

        val data = PinStatusData(isNeedHash = false)
        val mockResponse = PinStatusResponse(data = data)
        coEvery { checkPinHashV2UseCase(any()) } returns mockResponse

        viewmodel.otpValidate("", "", "08123123123", "", "", "", "PIN", "", "", 0)

        coVerify(exactly = 0) { generatePublicKeyUseCase() }
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
    fun `Success validate otp method phone register mandatory`() {
        // Given
        val code = "123456"
        val otpType = "168"
        val mode = "SMS"
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"

        // When
        coEvery { otpValidatePhoneRegisterMandatoryUseCase(any()) } returns successOtpValidationResponse
        viewmodel.otpValidatePhoneRegisterMandatory(code, otpType, mode, msisdn, email, validateToken)

        // Then
        val result = viewmodel.otpValidateResult.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(successOtpValidationResponse.data, result.data)
    }

    @Test
    fun `Success validate otp method phone register mandatory error message isNotEmpty`() {
        // Given
        val code = "123456"
        val otpType = "168"
        val mode = "SMS"
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"
        successOtpValidationResponse.data.errorMessage = "error"
        successOtpValidationResponse.data.success = false

        // When
        coEvery { otpValidatePhoneRegisterMandatoryUseCase(any()) } returns successOtpValidationResponse
        viewmodel.otpValidatePhoneRegisterMandatory(code, otpType, mode, msisdn, email, validateToken)

        // Then
        val result = viewmodel.otpValidateResult.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `Success validate otp method phone register mandatory error message isEmpty & success == false`() {
        // Given
        val code = "123456"
        val otpType = "168"
        val mode = "SMS"
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"
        successOtpValidationResponse.data.errorMessage = ""
        successOtpValidationResponse.data.success = false

        // When
        coEvery { otpValidatePhoneRegisterMandatoryUseCase(any()) } returns successOtpValidationResponse
        viewmodel.otpValidatePhoneRegisterMandatory(code, otpType, mode, msisdn, email, validateToken)

        // Then
        val result = viewmodel.otpValidateResult.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `Failed validate otp method phone register mandatory`() {
        // Given
        val code = "123456"
        val otpType = "168"
        val mode = "SMS"
        val validateToken = "qwerty"
        val email = "habibi@tokopedia.com"
        val msisdn = "08123456789"
        val data = PinStatusData(isNeedHash = false)
        val mockResponse = PinStatusResponse(data = data)

        // When
        coEvery { checkPinHashV2UseCase(any()) } returns mockResponse
        coEvery { otpValidatePhoneRegisterMandatoryUseCase(any()) } throws throwable
        viewmodel.otpValidatePhoneRegisterMandatory(code, otpType, mode, msisdn, email, validateToken)

        // Then
        val result = viewmodel.otpValidateResult.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success validate otp method 2fa`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate2FA("", "", "", "", "", userId = 0)

        verify { otpValidateResultObserver.onChanged(any<Success<OtpValidateData>>()) }
        assert(viewmodel.otpValidateResult.value is Success)

        val result = viewmodel.otpValidateResult.value as Success<OtpValidateData>
        assert(result.data == successOtpValidationResponse.data)
    }

    @Test
    fun `Success validate otp method 2fa isNeedHash false`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        val data = PinStatusData(isNeedHash = false)
        val mockResponse = PinStatusResponse(data = data)
        coEvery { checkPinHashV2UseCase(any()) } returns mockResponse

        viewmodel.otpValidate2FA("", "", "", "PIN", "", userId = 0)

        coVerify(exactly = 0) { generatePublicKeyUseCase() }
    }

    @Test
    fun `Success validate otp method 2fa error message isNotEmpty`() {
        successOtpValidationResponse.data.errorMessage = "error"
        successOtpValidationResponse.data.success = false

        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate2FA("", "", "", "", "", userId = 0)

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)
    }

    @Test
    fun `Success validate otp method 2fa error message isEmpty & success == false`() {
        successOtpValidationResponse.data.errorMessage = ""
        successOtpValidationResponse.data.success = false

        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        viewmodel.otpValidate2FA("", "", "", "", "", userId = 0)

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)
    }

    @Test
    fun `Failed validate otp method 2fa`() {
        val data = PinStatusData(isNeedHash = false)
        val mockResponse = PinStatusResponse(data = data)

        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { checkPinHashV2UseCase(any()) } returns mockResponse
        coEvery { otpValidateUseCase2FA.getData(any()) } throws throwable

        viewmodel.otpValidate2FA("", "", "", "", "", userId = 0)

        verify { otpValidateResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.otpValidateResult.value is Fail)

        val result = viewmodel.otpValidateResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success validate otp method - pin hash`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } returns successOtpValidationResponse
        mockkObject(RsaUtils)
        val hashedPin = "abc1234b"
        val hash = "asd"
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { checkPinHashV2UseCase(any()) } returns PinStatusResponse(PinStatusData(isNeedHash = true))
        coEvery { generatePublicKeyUseCase(SessionConstants.GenerateKeyModule.PIN_V2.value) } returns GenerateKeyPojo(KeyData("abc", "bca", hash))

        viewmodel.otpValidate("", "", "", "", "", "", "PIN", "", "", 0)

        verify { otpValidateResultObserver.onChanged(any<Success<OtpValidateData>>()) }
        assert(viewmodel.otpValidateResult.value is Success)

        val result = viewmodel.otpValidateResult.value as Success<OtpValidateData>
        assert(result.data == successOtpValidationResponse.data)

        verify {
            otpValidateUseCase.getParams(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
        }
    }

    @Test
    fun `CreateCheckPinV2Param - userid`() {
        val testId = "12345"
        assert(viewmodel.createCheckPinV2Param(testId, "", "").id == testId)
    }

    @Test
    fun `CreateCheckPinV2Param - phone`() {
        val phone = "12345"
        assert(viewmodel.createCheckPinV2Param("", phone, "").id == phone)
    }

    @Test
    fun `CreateCheckPinV2Param - email`() {
        val email = "yoris.prayogo@tokopedia.com"
        assert(viewmodel.createCheckPinV2Param("", "", email).id == email)
    }

    @Test
    fun `Validate otp method pin v2 - encryption failed`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase.getData(any()) } returns successOtpValidationResponse
        mockkObject(RsaUtils)
        val hash = "asd"
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns ""
        coEvery { checkPinHashV2UseCase(any()) } returns PinStatusResponse(PinStatusData(isNeedHash = true))
        coEvery { generatePublicKeyUseCase(SessionConstants.GenerateKeyModule.PIN_V2.value) } returns GenerateKeyPojo(KeyData("abc", "bca", hash))

        viewmodel.otpValidate("", "", "", "", "", "", "PIN", "", "", 0)

        verify { otpValidateResultObserver.onChanged(any<Success<OtpValidateData>>()) }
        assert(viewmodel.otpValidateResult.value is Success)

        val result = viewmodel.otpValidateResult.value as Success<OtpValidateData>
        assert(result.data == successOtpValidationResponse.data)
    }

    @Test
    fun `Success Check hash`() {
        val data = PinStatusData(isNeedHash = true)
        val mockResponse = PinStatusResponse(data = data)

        coEvery { checkPinHashV2UseCase(any()) } returns mockResponse

        runBlocking {
            assertTrue(viewmodel.isNeedHash(PinStatusParam("", "")))
        }
    }

    @Test
    fun `Success Check hash - false`() {
        val data = PinStatusData(isNeedHash = false)
        val mockResponse = PinStatusResponse(data = data)

        coEvery { checkPinHashV2UseCase(any()) } returns mockResponse

        runBlocking {
            assertEquals(false, viewmodel.isNeedHash(PinStatusParam("", "")))
        }
    }

    @Test
    fun `Success get pub key`() {
        val mocKeyData = KeyData("abc", "bca", "aaa")
        val generateKeyResponse = GenerateKeyPojo(mocKeyData)
        coEvery { generatePublicKeyUseCase(SessionConstants.GenerateKeyModule.PIN_V2.value) } returns generateKeyResponse

        runBlocking {
            assert(viewmodel.getPublicKey() == mocKeyData)
        }
    }

    @Test
    fun `Success validate otp method 2fa with hashing - not empty hashing`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        mockkObject(RsaUtils)
        val hash = "asd"
        val encrypted = "abd123123"
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns encrypted
        coEvery { checkPinHashV2UseCase(any()) } returns PinStatusResponse(PinStatusData(isNeedHash = true))
        coEvery { generatePublicKeyUseCase(SessionConstants.GenerateKeyModule.PIN_V2.value) } returns GenerateKeyPojo(KeyData("abc", "bca", hash))

        val otpType = "123"
        val validateToken = "abc123"
        val userIdEnc = "advasd"
        val mode = "PIN"
        val code = "1"
        val msisdn = "08123123"
        val uid = 0

        viewmodel.otpValidate2FA(otpType, validateToken, userIdEnc, mode, code, msisdn = msisdn, userId = uid)

        var params = otpValidateUseCase2FA.getParams(
            otpType = otpType,
            validateToken = validateToken,
            userIdEnc = userIdEnc,
            mode = mode,
            code = code,
            msisdn = msisdn
        )

        verify {
            viewmodel.combineWithV2param(params, encrypted, true, hash)
            otpValidateResultObserver.onChanged(any<Success<OtpValidateData>>())
        }
        val result = viewmodel.otpValidateResult.value as Success<OtpValidateData>
        assert(result.data == successOtpValidationResponse.data)
    }

    @Test
    fun `Success validate otp method 2fa with hashing - empty hashing`() {
        viewmodel.otpValidateResult.observeForever(otpValidateResultObserver)
        coEvery { otpValidateUseCase2FA.getData(any()) } returns successOtpValidationResponse

        mockkObject(RsaUtils)
        val hash = "asd"
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns ""
        coEvery { checkPinHashV2UseCase(any()) } returns PinStatusResponse(PinStatusData(isNeedHash = true))
        coEvery { generatePublicKeyUseCase() } returns GenerateKeyPojo(KeyData("abc", "bca", hash))

        viewmodel.otpValidate2FA("", "", "", "", "", userId = 0)

        verify { otpValidateResultObserver.onChanged(any<Success<OtpValidateData>>()) }
        assert(viewmodel.otpValidateResult.value is Success)

        val result = viewmodel.otpValidateResult.value as Success<OtpValidateData>
        assert(result.data == successOtpValidationResponse.data)
    }

    @Test
    fun `combine pin hashing param`() {
        val oldParam = mutableMapOf<String, Any>(
            OtpValidateUseCase.PARAM_CODE to "1234"
        )

        val result = viewmodel.combineWithV2param(oldParam, hashedPin = "abc", true, "1234")
        assert(result[OtpValidateUseCase.PARAM_PIN] == "abc")
        assert(result[OtpValidateUseCase.PARAM_PIN_HASH] == "1234")
        assert(result[OtpValidateUseCase.PARAM_USE_PIN_HASH] == true)
        assert(result[OtpValidateUseCase.PARAM_CODE] == "")
    }

    @Test
    fun `on viewmodel clear`() {
        viewmodel.done = false
        viewmodel.isLoginRegisterFlow = true
        val clearValue = true
        coEvery {
            remoteConfig.getBoolean(
                RemoteConfigKey.PRE_OTP_LOGIN_CLEAR,
                true
            )
        } returns clearValue

        viewmodel.onCleared()

        assert(userSessionInterface.accessToken.isNullOrEmpty())
    }

    @Test
    fun `on viewmodel clear isLoginRegisterFlow = false`() {
        viewmodel.done = false
        viewmodel.isLoginRegisterFlow = false
        val clearValue = true
        coEvery {
            remoteConfig.getBoolean(
                RemoteConfigKey.PRE_OTP_LOGIN_CLEAR,
                true
            )
        } returns clearValue

        viewmodel.onCleared()

        verify(exactly = 0) {
            userSessionInterface.setToken(null, null, null)
        }
    }

    @Test
    fun `on viewmodel clear done = true`() {
        viewmodel.done = true
        viewmodel.isLoginRegisterFlow = false
        val clearValue = true
        coEvery {
            remoteConfig.getBoolean(
                RemoteConfigKey.PRE_OTP_LOGIN_CLEAR,
                true
            )
        } returns clearValue

        viewmodel.onCleared()

        verify(exactly = 0) {
            userSessionInterface.setToken(null, null, null)
        }
    }

    @Test
    fun `on viewmodel clear config = false`() {
        viewmodel.done = true
        viewmodel.isLoginRegisterFlow = false
        coEvery {
            remoteConfig.getBoolean(
                RemoteConfigKey.PRE_OTP_LOGIN_CLEAR,
                true
            )
        } returns false

        viewmodel.onCleared()

        verify(exactly = 0) {
            userSessionInterface.setToken(null, null, null)
        }
    }

    @Test
    fun `isSmshidden return true`() {
        assert(viewmodel.isSmsHidden(DEFAULT_OTP_BEHAVIOR_ONE))
    }

    @Test
    fun `isSmshidden return false`() {
        assert(!viewmodel.isSmsHidden(DEFAULT_OTP_BEHAVIOR_TWO))
    }

    @Test
    fun `renderInitialDefaultOtp more than 1 methods`() {
        val modeCode = 1
        val linkType = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode),
            ModeListData(modeCode = 2),
            ModeListData(modeCode = 3)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = 1, linkType = linkType)
        viewmodel.renderInitialDefaultOtp(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == SPAN_USE_OTHER_METHODS)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == spanFactory(SPAN_USE_OTHER_METHODS, mockData.linkType))
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().displayedModeList.size == 1)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().originalOtpModeList.size == modeList.size)
    }

    @Test
    fun `renderInitialDefaultOtp 1 methods`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = 1)
        viewmodel.renderInitialDefaultOtp(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == "")
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == "")
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().displayedModeList.size == 1)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().originalOtpModeList.size == 1)
    }

    @Test
    fun `render initial default otp when mode code is not exists and sms is hidden`() {
        val modeCode = 11
        val linkType = 1
        val defaultBehaviourMode = 3
        val modeList = arrayListOf(
            ModeListData(modeCode = 1, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA),
            ModeListData(modeCode = 3, modeText = OtpConstant.OtpMode.MISCALL)

        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = defaultBehaviourMode, linkType = linkType)
        viewmodel.renderInitialDefaultOtp(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == SPAN_USE_SMS_METHOD)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == spanFactory(SPAN_USE_SMS_METHOD, mockData.linkType))
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().displayedModeList.find { it.modeText == OtpConstant.OtpMode.SMS } == null)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().originalOtpModeList.size == modeList.size)
    }

    @Test
    fun `when otp mode list has 2 and hide sms use footer sms`() {
        val modeCode = 2
        val linkType = 1
        val defaultBehaviourMode = 3
        val modeList = arrayListOf(
            ModeListData(modeCode = 1, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = defaultBehaviourMode, linkType = linkType)
        viewmodel.renderInitialDefaultOtp(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == SPAN_USE_SMS_METHOD)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == spanFactory(SPAN_USE_SMS_METHOD, mockData.linkType))
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().displayedModeList.find { it.modeText == OtpConstant.OtpMode.SMS } == null)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().originalOtpModeList.size == 2)
    }

    @Test
    fun `renderInitialDefaultOtpOff more than 1 methods and SMS hidden`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = 1)
        viewmodel.renderInitialDefaultOtpOff(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == SPAN_USE_SMS_METHOD)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == spanFactory(SPAN_USE_SMS_METHOD, mockData.linkType))
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().displayedModeList.find { it.modeText == OtpConstant.OtpMode.SMS } == null)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().originalOtpModeList.size == 2)
    }

    @Test
    fun `renderInitialDefaultOtpOff more than 1 methods and has SMS not hidden`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = 2)
        viewmodel.renderInitialDefaultOtpOff(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == "")
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == "")
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().originalOtpModeList.size == 2)
    }

    @Test
    fun `renderInitialDefaultOtpOff has 1 method only`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = 3)
        viewmodel.renderInitialDefaultOtpOff(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == "")
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == "")
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().originalOtpModeList.size == 1)
    }

    @Test
    fun `getOtpModeListForDefaultOtp behavior 3`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA),
            ModeListData(modeCode = 3, modeText = OtpConstant.OtpMode.MISCALL)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = 3)
        coEvery { getOtpModeList(any()) } returns mockData

        viewmodel.getOtpModeListForDefaultOtp("", "", "", "", "", "")

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == SPAN_USE_OTHER_METHODS)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == spanFactory(SPAN_USE_OTHER_METHODS, mockData.linkType))
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().originalOtpModeList.size == modeList.size)
    }

    @Test
    fun `getOtpModeListForDefaultOtp behavior 1`() {
        val modeCode = 0
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = 1)
        coEvery { getOtpModeList(any()) } returns mockData

        viewmodel.getOtpModeListForDefaultOtp("", "", "", "", "", "")

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == SPAN_USE_SMS_METHOD)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == spanFactory(SPAN_USE_SMS_METHOD, mockData.linkType))
    }

    @Test
    fun `getOtpModeListForDefaultOtp behavior 0`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA)
        )

        val mockData = OtpModeListData(success = true, defaultMode = modeCode, modeList = modeList, defaultBehaviorMode = 0)
        coEvery { getOtpModeList(any()) } returns mockData

        viewmodel.getOtpModeListForDefaultOtp("", "", "", "", "", "")

        assert(viewmodel.getVerificationMethodResult.getOrAwaitValue() is Success)
    }

    @Test
    fun `getOtpModeListForDefaultOtp throw exception`() {
        coEvery { getOtpModeList(any()) } throws throwable

        viewmodel.getOtpModeListForDefaultOtp("", "", "", "", "", "")

        assert(viewmodel.getVerificationMethodResult.getOrAwaitValue() is Fail)
    }

    @Test
    fun `showAllMethod hide sms`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA),
            ModeListData(modeCode = 3, modeText = OtpConstant.OtpMode.MISCALL)
        )

        val mockData = DefaultOtpUiModel(
            footerText = "",
            footerClickableSpan = "",
            footerAction = {},
            defaultMode = modeCode,
            defaultBehaviorMode = 3,
            originalOtpModeList = modeList,
            displayedModeList = modeList
        )

        viewmodel.showAllMethod(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == SPAN_USE_SMS_METHOD)
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == spanFactory(SPAN_USE_SMS_METHOD, mockData.linkType))
    }

    @Test
    fun `showAllMethod show sms`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA)
        )

        val mockData = DefaultOtpUiModel(
            footerText = "",
            footerClickableSpan = "",
            footerAction = {},
            defaultMode = modeCode,
            defaultBehaviorMode = 2,
            originalOtpModeList = modeList,
            displayedModeList = modeList
        )

        viewmodel.showAllMethod(mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == "")
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == "")
    }

    @Test(expected = TimeoutException::class)
    fun `showAllMethod null`() {
        viewmodel.showAllMethod(null)
        viewmodel.defaultOtpUiModel.getOrAwaitValue()
    }

    @Test
    fun `getOtpModeListForDefaultOtp with cache`() {
        val modeCode = 1
        val modeList = arrayListOf(
            ModeListData(modeCode = modeCode, modeText = OtpConstant.OtpMode.SMS),
            ModeListData(modeCode = 2, modeText = OtpConstant.OtpMode.WA)
        )

        val mockData = DefaultOtpUiModel(
            footerText = "",
            footerClickableSpan = "",
            footerAction = {},
            defaultMode = modeCode,
            defaultBehaviorMode = 2,
            originalOtpModeList = modeList,
            displayedModeList = modeList
        )

        viewmodel.getOtpModeListForDefaultOtp("", "", "", "", "", "", cache = mockData)

        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerClickableSpan == "")
        assert(viewmodel.defaultOtpUiModel.getOrAwaitValue().footerText == "")
    }

    @Test
    fun `goToInputOtp`() {
        val mockData = ModeListData(modeCode = 1)
        viewmodel.goToInputOtp(mockData)
        assert(viewmodel.gotoInputOtp.getOrAwaitValue().modeCode == 1)
    }

    companion object {
        private val throwable = Throwable()
    }
}
