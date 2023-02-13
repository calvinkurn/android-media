package com.tokopedia.otp.verification.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.usecase.*
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
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
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class VerificationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getVerificationMethodUseCase: GetVerificationMethodUseCase

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
        coEvery { getVerificationMethodPhoneRegisterMandatoryUseCase(any()) }returns
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

        coVerify(exactly = 0) { generatePublicKeyUseCase.executeOnBackground() }
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

        coVerify(exactly = 0) { generatePublicKeyUseCase.executeOnBackground() }
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
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns GenerateKeyPojo(KeyData("abc", "bca", hash))

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
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns GenerateKeyPojo(KeyData("abc", "bca", hash))

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
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyResponse

        runBlocking {
            assert(viewmodel.getPublicKey() == mocKeyData)
        }
        verify {
            generatePublicKeyUseCase.setParams("pinv2")
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
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns GenerateKeyPojo(KeyData("abc", "bca", hash))

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
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns GenerateKeyPojo(KeyData("abc", "bca", hash))

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

    companion object {
        private val throwable = Throwable()
    }
}
