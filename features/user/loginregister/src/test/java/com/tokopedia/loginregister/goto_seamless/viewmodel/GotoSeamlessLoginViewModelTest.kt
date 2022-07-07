package com.tokopedia.loginregister.goto_seamless.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessHelper
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessLoginViewModel
import com.tokopedia.loginregister.goto_seamless.model.GojekProfileData
import com.tokopedia.loginregister.goto_seamless.usecase.LoginSeamlessUseCase
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GotoSeamlessLoginViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GotoSeamlessLoginViewModel

    private val loginSeamlessUseCase = mockk<LoginSeamlessUseCase>(relaxed = true)
    private val gotoSeamlessHelper = mockk<GotoSeamlessHelper>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private var loginTokenObserver = mockk<Observer<Result<LoginToken>>>(relaxed = true)
    private var gojekProfileObserver = mockk<Observer<Result<GojekProfileData>>>(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(EncoderDecoder())

        viewModel = GotoSeamlessLoginViewModel(loginSeamlessUseCase, gotoSeamlessHelper, userSession, CoroutineTestDispatchersProvider)
        viewModel.loginResponse.observeForever(loginTokenObserver)
        viewModel.gojekProfileData.observeForever(gojekProfileObserver)
    }

    @Test
    fun `Get Gojek Data Success`() {
        val mockGojekProfile = GojekProfileData(name = "yoris")
        coEvery { gotoSeamlessHelper.getGojekProfile() } returns mockGojekProfile

        viewModel.getGojekData()

        verify {
            gojekProfileObserver.onChanged(Success(mockGojekProfile))
        }
    }

    @Test
    fun `Get Gojek Data Failed`() {
        val throwable = Throwable("error")
        coEvery { gotoSeamlessHelper.getGojekProfile() } throws throwable

        viewModel.getGojekData()

        verify {
            gojekProfileObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `Login Token Success`() {
        val data = LoginToken(accessToken = "abc123")
        val response = LoginTokenPojoV2(data)

        coEvery { loginSeamlessUseCase(any()) } returns response

        viewModel.doSeamlessLogin("abc")

        verify {
            viewModel.onSuccessSeamlessLogin(data)
        }
    }

    @Test
    fun `Login Token Failed`() {
        val throwable = Throwable("error")
        coEvery { loginSeamlessUseCase(any()) } throws throwable

        viewModel.doSeamlessLogin("abc")

        verify {
            loginTokenObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `On Success Seamless Login`() {
        val token = "abc123"
        val data = LoginToken(accessToken = token)

        viewModel.onSuccessSeamlessLogin(data)

        verify {
            viewModel.saveAccessToken(data)
        }
    }

    @Test
    fun `On Success Seamless Login - token empty`() {
        val token = ""
        val data = LoginToken(accessToken = token)

        viewModel.onSuccessSeamlessLogin(data)

        verify {
            loginTokenObserver.onChanged(Fail(any()))
        }
    }

    @Test
    fun `Save access token`() {
        val token = "abc123"
        val tokenType = "Bearer"
        val refreshToken = "123abc"

        val data = LoginToken(accessToken = token, tokenType = tokenType, refreshToken = refreshToken)

        mockkStatic(EncoderDecoder::class)
        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"

        viewModel.saveAccessToken(data)

        verify {
            userSession.setToken(
                token,
                tokenType,
                "ok"
            )
        }
    }

}