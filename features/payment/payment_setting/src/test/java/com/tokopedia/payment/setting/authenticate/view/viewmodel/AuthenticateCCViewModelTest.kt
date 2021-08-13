package com.tokopedia.payment.setting.authenticate.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.payment.setting.authenticate.domain.CheckUpdateWhiteListCreditCartUseCase
import com.tokopedia.payment.setting.authenticate.model.AuthException
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthenticateCCViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val useCase = mockk<CheckUpdateWhiteListCreditCartUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: AuthenticateCCViewModel

    @Before
    fun setUp() {
        viewModel = AuthenticateCCViewModel(
                useCase,
                userSession,
                dispatcher)
    }

    @Test
    fun `updateWhiteList fail`() {
        val mockThrowable = mockk<Throwable>("fail")
        coEvery { useCase.whiteListResponse(any(), any(), any(), any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.updateWhiteList(0, false, "")
        assert(viewModel.whiteListStatusResultLiveData.value is Fail)
    }

    @Test
    fun `updateWhiteList otp required`() {
        val mockThrowable = mockk<AuthException.CheckOtpException>()
        coEvery { useCase.whiteListResponse(any(), any(), any(), any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.updateWhiteList(1, true, "")

        assert(viewModel.whiteListStatusResultLiveData.value is Fail)
        assert((viewModel.whiteListStatusResultLiveData.value as Fail).throwable is AuthException.CheckOtpException)
    }

    @Test
    fun `checkWhiteList fail`() {
        val mockThrowable = mockk<Throwable>("fail")
        coEvery { useCase.whiteListResponse(any(), any(), any(), any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.checkWhiteList("title1", "desc1", "title2", "desc2")
        assert(viewModel.whiteListResultLiveData.value is Fail)
    }

    @Test
    fun `updateWhiteList success`() {
        val data = CheckWhiteListStatus("", 0, listOf())
        coEvery { useCase.whiteListResponse(any(), any(), any(), any(), any()) } answers {
            firstArg<(CheckWhiteListStatus) -> Unit>().invoke(data)
        }
        viewModel.updateWhiteList(0, false, "12345")
        assert(viewModel.whiteListStatusResultLiveData.value is Success)
    }

    @Test
    fun `checkWhiteList success`() {
        val data = CheckWhiteListStatus("", 0, listOf())
        coEvery { useCase.whiteListResponse(any(), any(), any(), any(), any()) } answers {
            firstArg<(CheckWhiteListStatus) -> Unit>().invoke(data)
        }
        viewModel.checkWhiteList("title1", "desc1", "title2", "desc2")
        assert(viewModel.whiteListResultLiveData.value is Success)
    }
}