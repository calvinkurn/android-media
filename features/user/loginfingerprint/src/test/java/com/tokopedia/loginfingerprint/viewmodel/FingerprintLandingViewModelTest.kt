package com.tokopedia.loginfingerprint.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprint
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprintPojo
import com.tokopedia.loginfingerprint.domain.usecase.VerifyFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.crypto.KeyPairManager
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.fingerprintpreference.FingerprintPreference
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris on 14/07/21.
 */

class FingerprintLandingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FingerprintLandingViewModel

    private val verifyFingerprintUseCase = mockk<VerifyFingerprintUseCase>(relaxed = true)
    private var verifyFingerprintObserver = mockk<Observer<Result<VerifyFingerprint>>>(relaxed = true)

    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val keyPairManager= mockk<Lazy<KeyPairManager?>>(relaxed = true)

    val fingerprintPreferenceManager = mockk<FingerprintPreference>(relaxed = true)

    private val throwable = mockk<Throwable>(relaxed = true)

    val signatureModel = SignatureData(signature = "abc123", datetime = "1235123")
    @Before
    fun setUp() {
        viewModel = FingerprintLandingViewModel(
            CoroutineTestDispatchersProvider,
            userSession,
            keyPairManager,
            verifyFingerprintUseCase,
            fingerprintPreferenceManager
        )

        viewModel.verifyFingerprint.observeForever(verifyFingerprintObserver)
        coEvery { keyPairManager.get()?.generateFingerprintSignature(any(), any()) } returns signatureModel
    }

    @Test
    fun `on Success Verify Fingerprint`() {
        /* When */
        val data = VerifyFingerprint(isSuccess = true,  validateToken = "abc123")
        val response = VerifyFingerprintPojo(data)

        coEvery { verifyFingerprintUseCase.invoke(any()) } returns response

        viewModel.verifyFingerprint()

        /* Then */
        coVerify {
            keyPairManager.get()?.generateFingerprintSignature(any(), any())
            verifyFingerprintObserver.onChanged(Success(response.data))
        }
    }

    @Test
    fun `on Error Throwable Verify Fingerprint`() {
        /* When */
        coEvery { verifyFingerprintUseCase.invoke(any()) } throws throwable

        viewModel.verifyFingerprint()

        /* Then */
        verify { verifyFingerprintObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Has Error Verify Fingerprint`() {
        /* When */
        val data = VerifyFingerprint(isSuccess = true,  validateToken = "abc123", errorMessage = "error")
        val response = VerifyFingerprintPojo(data)

        coEvery { verifyFingerprintUseCase.invoke(any()) } returns response

        viewModel.verifyFingerprint()

        /* Then */
        assert((viewModel.verifyFingerprint.value as Fail).throwable is MessageErrorException)
    }

    @Test
    fun `on Other Errors Verify Fingerprint`() {
        /* When */
        val data = VerifyFingerprint(isSuccess = false,  validateToken = "", errorMessage = "")
        val response = VerifyFingerprintPojo(data)

        coEvery { verifyFingerprintUseCase.invoke(any()) } returns response

        viewModel.verifyFingerprint()

        /* Then */
        assert((viewModel.verifyFingerprint.value as Fail).throwable is RuntimeException)
    }

    @Test
    fun `verify fingerprint - signature null `() {
        coEvery { keyPairManager.get()?.generateFingerprintSignature(any(), any()) } returns null

        viewModel.verifyFingerprint()
    }

    @Test
    fun `verify fingerprint - keypair null `() {
        coEvery { keyPairManager.get() } returns null
        viewModel.verifyFingerprint()
    }

    @Test
    fun `on Has Error Verify Fingerprint - all empty`() {
        /* When */
        val data = VerifyFingerprint(isSuccess = true,  validateToken = "", errorMessage = "")
        val response = VerifyFingerprintPojo(data)

        coEvery { verifyFingerprintUseCase.invoke(any()) } returns response

        viewModel.verifyFingerprint()

        /* Then */
        assert((viewModel.verifyFingerprint.value as Fail).throwable is RuntimeException)
    }

}
