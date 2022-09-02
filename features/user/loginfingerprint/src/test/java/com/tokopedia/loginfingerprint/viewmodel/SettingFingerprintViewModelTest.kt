package com.tokopedia.loginfingerprint.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginfingerprint.data.model.*
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RemoveFingerprintUsecase
import com.tokopedia.loginfingerprint.utils.crypto.RsaSignatureUtils
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingFingerprintViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: SettingFingerprintViewModel

    val registerFingerprintUseCase = mockk<RegisterFingerprintUseCase>(relaxed = true)
    val removeFingerprintUseCase = mockk<RemoveFingerprintUsecase>(relaxed = true)
    val checkFingerprintToggleStatusUseCase =
        mockk<CheckFingerprintToggleStatusUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val rsaSignatureUtils = mockk<Lazy<RsaSignatureUtils?>>(relaxed = true)

    private var checkFingerprintObserver =
        mockk<Observer<Result<CheckFingerprintResult>>>(relaxed = true)
    private var registerFingerprintObserver =
        mockk<Observer<Result<RegisterFingerprintResult>>>(relaxed = true)
    private var removeFingerprintObserver =
        mockk<Observer<Result<RemoveFingerprintData>>>(relaxed = true)
    val fingerprintPreferenceManager = mockk<FingerprintPreference>(relaxed = true)

    val navigateSuccessRegister = mockk<Observer<Void>>(relaxed = true)
    val errorMessageRegister = mockk<Observer<String>>(relaxed = true)

    private val throwable = mockk<Throwable>(relaxed = true)

    private val errMsg = "error"

    @Before
    fun setUp() {
        viewModel = SettingFingerprintViewModel(
            CoroutineTestDispatchersProvider,
            userSession,
            registerFingerprintUseCase,
            removeFingerprintUseCase,
            rsaSignatureUtils,
            checkFingerprintToggleStatusUseCase,
            fingerprintPreferenceManager
        )

        viewModel.checkFingerprintStatus.observeForever(checkFingerprintObserver)
        viewModel.registerFingerprintResult.observeForever(registerFingerprintObserver)
        viewModel.removeFingerprintResult.observeForever(removeFingerprintObserver)
        viewModel.navigateSuccessRegister.observeForever(navigateSuccessRegister)
        viewModel.errorMessageRegister.observeForever(errorMessageRegister)

        every { throwable.message } returns errMsg
    }

    @Test
    fun `on Success Check Fingerprint`() {
        /* When */
        val data = CheckFingerprintResult(isSuccess = true, errorMessage = "")
        val response = CheckFingerprintPojo(data)

        coEvery { checkFingerprintToggleStatusUseCase.invoke(any()) } returns response

        viewModel.getFingerprintStatus()

        /* Then */
        verify { checkFingerprintObserver.onChanged(Success(response.data)) }
    }

    @Test
    fun `on Success Check Fingerprint has errors`() {
        /* When */
        val data = CheckFingerprintResult(isSuccess = false, errorMessage = "")
        val response = CheckFingerprintPojo(data)

        coEvery { checkFingerprintToggleStatusUseCase.invoke(any()) } returns response

        viewModel.getFingerprintStatus()

        /* Then */
        verify { checkFingerprintObserver.onChanged(any<Fail>()) }
        assert((viewModel.checkFingerprintStatus.value as Fail).throwable.message == "Gagal")
    }

    @Test
    fun `on Error Check Fingerprint`() {

        coEvery { checkFingerprintToggleStatusUseCase.invoke(any()) } throws throwable

        viewModel.getFingerprintStatus()

        /* Then */
        verify { checkFingerprintObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Success Register Fingerprint`() {
        /* When */
        val data = RegisterFingerprintResult(success = true)
        val response = RegisterFingerprintPojo(data)

        coEvery {
            rsaSignatureUtils.get()?.generateFingerprintSignature(any(), any())
        } returns SignatureData("abc", "123")
        coEvery { rsaSignatureUtils.get()?.getPublicKey() } returns "abc123"

        coEvery { registerFingerprintUseCase.invoke(any()) } returns response

        viewModel.registerFingerprint()

        /* Then */
        coVerify {
            rsaSignatureUtils.get()
                ?.generateFingerprintSignature(userSession.userId, userSession.deviceId)
            navigateSuccessRegister.onChanged(any())
        }
    }

    @Test
    fun `on Error Register Fingerprint`() {

        val error = Throwable("error")
        coEvery { rsaSignatureUtils.get()?.generateFingerprintSignature(any(), any()) } throws error

        viewModel.registerFingerprint()

        /* Then */
        coVerify {
            errorMessageRegister.onChanged("error")
        }
    }

    @Test
    fun `on Error Register Fingerprint - public key empty`() {
        coEvery {
            rsaSignatureUtils.get()?.generateFingerprintSignature(any(), any())
        } returns SignatureData("abc", "123")
        coEvery { rsaSignatureUtils.get()?.getPublicKey() } returns ""

        viewModel.registerFingerprint()

        /* Then */
        verify {
            errorMessageRegister.onChanged("Terjadi Kesalahan, Silahkan coba lagi")
        }
    }

    @Test
    fun `on Error Register Fingerprint - errorMessage not empty`() {
        /* When */
        val errMsg = "error"
        val data = RegisterFingerprintResult(success = true, errorMessage = errMsg)
        val response = RegisterFingerprintPojo(data)

        coEvery {
            rsaSignatureUtils.get()?.generateFingerprintSignature(any(), any())
        } returns SignatureData("abc", "123")
        coEvery { rsaSignatureUtils.get()?.getPublicKey() } returns "abc123"

        coEvery { registerFingerprintUseCase.invoke(any()) } returns response

        viewModel.registerFingerprint()

        /* Then */
        verify {
            errorMessageRegister.onChanged(errMsg)
        }
    }

    @Test
    fun `on Error Register Fingerprint - other errors`() {
        /* When */
        val data = RegisterFingerprintResult(success = false, errorMessage = "")
        val response = RegisterFingerprintPojo(data)

        coEvery {
            rsaSignatureUtils.get()?.generateFingerprintSignature(any(), any())
        } returns SignatureData("abc", "123")
        coEvery { rsaSignatureUtils.get()?.getPublicKey() } returns "abc123"

        coEvery { registerFingerprintUseCase.invoke(any()) } returns response

        viewModel.registerFingerprint()

        /* Then */
        verify {
            errorMessageRegister.onChanged(any())
        }
    }

    @Test
    fun `on Success Remove Fingerprint`() {
        /* When */
        val data = RemoveFingerprintData(isSuccess = true)
        val response = RemoveFingerprintPojo(data)

        coEvery { removeFingerprintUseCase.invoke(Unit) } returns response

        viewModel.removeFingerprint()

        /* Then */
        coVerify {
            fingerprintPreferenceManager.removeUniqueId()
            removeFingerprintObserver.onChanged(Success(response.data))
        }
    }

    @Test
    fun `on Has Errors Remove Fingerprint`() {
        /* When */
        val data = RemoveFingerprintData(isSuccess = false, error = "error")
        val response = RemoveFingerprintPojo(data)

        coEvery { removeFingerprintUseCase.invoke(Unit) } returns response

        viewModel.removeFingerprint()

        /* Then */
        assert(viewModel.removeFingerprintResult.value is Fail)
    }

    @Test
    fun `on Error thrown Remove Fingerprint`() {
        coEvery { removeFingerprintUseCase.invoke(Unit) } throws throwable

        viewModel.removeFingerprint()

        /* Then */
        assert(viewModel.removeFingerprintResult.value is Fail)
    }

}