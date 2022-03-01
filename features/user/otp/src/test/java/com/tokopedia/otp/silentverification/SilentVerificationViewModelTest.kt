package com.tokopedia.otp.silentverification

import android.net.Network
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResponse
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResult
import com.tokopedia.otp.silentverification.domain.usecase.GetEvUrlUseCase
import com.tokopedia.otp.silentverification.domain.usecase.RequestSilentVerificationOtpUseCase
import com.tokopedia.otp.silentverification.domain.usecase.ValidateSilentVerificationUseCase
import com.tokopedia.otp.silentverification.view.viewmodel.SilentVerificationViewModel
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris on 18/10/21.
 */

class SilentVerificationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    val requestSilentVerificationOtpUseCase = mockk<RequestSilentVerificationOtpUseCase>(relaxed = true)
    val validateSilentVerificationUseCase = mockk<ValidateSilentVerificationUseCase>(relaxed = true)
    val getEvUrlUseCase = mockk<GetEvUrlUseCase>(relaxed = true)

    lateinit var viewModel: SilentVerificationViewModel

    private var requestObserver = mockk<Observer<Result<RequestSilentVerificationResult>>>(relaxed = true)
    private var validationObserver = mockk<Observer<Result<OtpValidateData>>>(relaxed = true)
    private var bokuObserver = mockk<Observer<Result<String>>>(relaxed = true)

    private val throwable = Throwable("Error")

    @Before
    fun setUp() {
        viewModel = SilentVerificationViewModel(
            requestSilentVerificationOtpUseCase,
            validateSilentVerificationUseCase,
            getEvUrlUseCase,
            CoroutineTestDispatchersProvider
        )

        viewModel.requestSilentVerificationResponse.observeForever(requestObserver)
        viewModel.validationResponse.observeForever(validationObserver)
        viewModel.bokuVerificationResponse.observeForever(bokuObserver)
    }

    @Test
    fun `on Success Request Otp`() {
        /* When */
        val responseData = RequestSilentVerificationResult()
        val response = RequestSilentVerificationResponse(data = responseData)

        coEvery { requestSilentVerificationOtpUseCase.invoke(any()) } returns response

        viewModel.requestSilentVerification("112", "silent_verif", "082241231231", "abc", "123")

        /* Then */
        verify { requestObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Failed Request Otp`() {
        /* When */
        coEvery { requestSilentVerificationOtpUseCase.invoke(any()) } throws throwable

        viewModel.requestSilentVerification("112", "silent_verif", "082241231231", "abc", "123")

        /* Then */
        verify { requestObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Success Validate Otp`() {
        /* When */
        val responseData = OtpValidateData()
        val response = OtpValidatePojo(data = responseData)

        coEvery { validateSilentVerificationUseCase.invoke(any()) } returns response

        viewModel.validate("112", "082241231231", "silent_verif", 0, "abc123", "123123", "abc")

        /* Then */
        verify {
            validationObserver.onChanged(Success(responseData))
        }
    }

    @Test
    fun `on Failed Validate Otp`() {
        /* When */
        coEvery { validateSilentVerificationUseCase.invoke(any()) } throws throwable

        viewModel.validate("112", "082241231231", "silent_verif", 0, "abc123", "abc", "abc")

        /* Then */
        verify { validationObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Success Hit EvUrl`() {
        val testResult = "abc"
        val mockNetwork = mockk<Network>(relaxed = true)

        /* When */
        coEvery { getEvUrlUseCase(Unit) } returns testResult


        viewModel.verifyBoku(mockNetwork, "url")

        /* Then */
        verify { bokuObserver.onChanged(Success(testResult)) }
    }

    @Test
    fun `on Fail Hit EvUrl`() {
        val mockNetwork = mockk<Network>(relaxed = true)

        /* When */
        coEvery { getEvUrlUseCase(Unit) } throws throwable

        viewModel.verifyBoku(mockNetwork, "url")

        /* Then */
        assertThat(viewModel.bokuVerificationResponse.value, instanceOf(Fail::class.java))
    }
}