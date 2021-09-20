package com.tokopedia.loginfingerprint.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprint
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprintPojo
import com.tokopedia.loginfingerprint.domain.usecase.VerifyFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.network.exception.MessageErrorException
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
    private val cryptographyUtils = mockk<Cryptography>(relaxed = true)

    private val throwable = mockk<Throwable>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = FingerprintLandingViewModel(
            CoroutineTestDispatchersProvider,
            userSession,
            verifyFingerprintUseCase,
            cryptographyUtils
        )

        viewModel.verifyFingerprint.observeForever(verifyFingerprintObserver)
        every { cryptographyUtils.getSignature(any(), any()) } returns "abc12345"
    }

    @Test
    fun `on Success Verify Fingerprint`() {
        /* When */
        val data = VerifyFingerprint(isSuccess = true,  validateToken = "abc123")
        val response = VerifyFingerprintPojo(data)

        every { verifyFingerprintUseCase.verifyFingerprint(any(), any(), any()) } answers {
            secondArg<(VerifyFingerprintPojo) -> Unit>().invoke(response)
        }

        viewModel.verifyFingerprint()

        /* Then */
        verify {
            cryptographyUtils.generateFingerprintSignature(any(), any())
            verifyFingerprintObserver.onChanged(Success(response.data))
        }
    }

    @Test
    fun `on Error Throwable Verify Fingerprint`() {
        /* When */
        every { verifyFingerprintUseCase.verifyFingerprint(any(), any(), any()) } answers {
            thirdArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.verifyFingerprint()

        /* Then */
        verify { verifyFingerprintObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Has Error Verify Fingerprint`() {
        /* When */
        val data = VerifyFingerprint(isSuccess = true,  validateToken = "abc123", errorMessage = "error")
        val response = VerifyFingerprintPojo(data)

        every { verifyFingerprintUseCase.verifyFingerprint(any(), any(), any()) } answers {
            secondArg<(VerifyFingerprintPojo) -> Unit>().invoke(response)
        }

        viewModel.verifyFingerprint()

        /* Then */
        assert((viewModel.verifyFingerprint.value as Fail).throwable is MessageErrorException)
    }

}