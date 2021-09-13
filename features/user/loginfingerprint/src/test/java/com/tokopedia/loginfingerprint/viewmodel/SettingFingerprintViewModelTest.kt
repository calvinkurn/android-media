package com.tokopedia.loginfingerprint.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginfingerprint.data.model.*
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RemoveFingerprintUsecase
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingFingerprintViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: SettingFingerprintViewModel

    val registerFingerprintUseCase = mockk<RegisterFingerprintUseCase>(relaxed = true)
    val removeFingerprintUseCase = mockk<RemoveFingerprintUsecase>(relaxed = true)
    val checkFingerprintToggleStatusUseCase = mockk<CheckFingerprintToggleStatusUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val cryptographyUtils = mockk<Cryptography>(relaxed = true)

    private var checkFingerprintObserver = mockk<Observer<Result<CheckFingerprintPojo>>>(relaxed = true)
    private var registerFingerprintObserver = mockk<Observer<Result<RegisterFingerprintResult>>>(relaxed = true)
    private var removeFingerprintObserver = mockk<Observer<Result<RemoveFingerprintData>>>(relaxed = true)

    private val throwable = mockk<Throwable>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = SettingFingerprintViewModel(
            CoroutineTestDispatchersProvider,
            userSession,
            registerFingerprintUseCase,
            removeFingerprintUseCase,
            cryptographyUtils,
            checkFingerprintToggleStatusUseCase
        )

        viewModel.checkFingerprintStatus.observeForever(checkFingerprintObserver)
        viewModel.registerFingerprintResult.observeForever(registerFingerprintObserver)
        viewModel.removeFingerprintResult.observeForever(removeFingerprintObserver)
    }

    @Test
    fun `on Success Check Fingerprint`() {
        /* When */
        val data = CheckFingerprintResult(isSuccess = true, errorMessage = "")
        val response = CheckFingerprintPojo(data)

        every { checkFingerprintToggleStatusUseCase.checkFingerprint(any(), any(), any()) } answers {
            secondArg<(CheckFingerprintPojo) -> Unit>().invoke(response)
        }

        viewModel.getFingerprintStatus()

        /* Then */
        verify { checkFingerprintObserver.onChanged(Success(response)) }
    }

    @Test
    fun `on Error Check Fingerprint`() {

        every { checkFingerprintToggleStatusUseCase.checkFingerprint(any(), any(), any()) } answers {
            thirdArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.getFingerprintStatus()

        /* Then */
        verify { checkFingerprintObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Success Register Fingerprint`() {
        /* When */
        val data = RegisterFingerprintResult(success = true)
        val response = RegisterFingerprintPojo(data)

        every { cryptographyUtils.generateFingerprintSignature(any(), any()) } returns SignatureData("abc", "123")
        every { cryptographyUtils.getPublicKey() } returns "abc123"

        every { registerFingerprintUseCase.registerFingerprint(any(), any(), any(), any()) } answers {
            thirdArg<(RegisterFingerprintPojo) -> Unit>().invoke(response)
        }

        viewModel.registerFingerprint()

        /* Then */
        verify {
            cryptographyUtils.generateFingerprintSignature(userSession.userId, userSession.deviceId)
            registerFingerprintObserver.onChanged(Success(response.data))
        }
    }

    @Test
    fun `on Error Register Fingerprint`() {

        every { cryptographyUtils.generateFingerprintSignature(any(), any()) } returns SignatureData("abc", "123")
        every { cryptographyUtils.getPublicKey() } returns "abc123"

        every { registerFingerprintUseCase.registerFingerprint(any(), any(), any(), any()) } answers {
            arg<(Throwable) -> Unit>(3).invoke(throwable)
        }

        viewModel.registerFingerprint()

        /* Then */
        verify { registerFingerprintObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Error Register Fingerprint - public key empty`() {
        every { cryptographyUtils.generateFingerprintSignature(any(), any()) } returns SignatureData("abc", "123")
        every { cryptographyUtils.getPublicKey() } returns ""

        viewModel.registerFingerprint()

        /* Then */
        assert(viewModel.registerFingerprintResult.value is Fail)
    }

    @Test
    fun `on Success Remove Fingerprint`() {
        /* When */
        val data = RemoveFingerprintData(isSuccess = true)
        val response = RemoveFingerprintPojo(data)

        every { removeFingerprintUseCase.removeFingerprint(any(), any()) } answers {
            firstArg<(RemoveFingerprintPojo) -> Unit>().invoke(response)
        }

        viewModel.removeFingerprint()

        /* Then */
        verify { removeFingerprintObserver.onChanged(Success(response.data)) }
    }

    @Test
    fun `on Has Errors Remove Fingerprint`() {
        /* When */
        val data = RemoveFingerprintData(isSuccess = false, error = "error")
        val response = RemoveFingerprintPojo(data)

        every { removeFingerprintUseCase.removeFingerprint(any(), any()) } answers {
            firstArg<(RemoveFingerprintPojo) -> Unit>().invoke(response)
        }

        viewModel.removeFingerprint()

        /* Then */
        assert(viewModel.removeFingerprintResult.value is Fail)
    }

    @Test
    fun `on Error thrown Remove Fingerprint`() {
        every { removeFingerprintUseCase.removeFingerprint(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.removeFingerprint()

        /* Then */
        assert(viewModel.removeFingerprintResult.value is Fail)
    }

}