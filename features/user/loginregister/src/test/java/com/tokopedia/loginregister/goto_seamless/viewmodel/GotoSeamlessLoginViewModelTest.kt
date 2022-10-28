package com.tokopedia.loginregister.goto_seamless.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessHelper
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessLoginViewModel
import com.tokopedia.loginregister.goto_seamless.model.GetNameData
import com.tokopedia.loginregister.goto_seamless.model.GetNameResponse
import com.tokopedia.loginregister.goto_seamless.model.GojekProfileData
import com.tokopedia.loginregister.goto_seamless.usecase.GetNameUseCase
import com.tokopedia.loginregister.goto_seamless.usecase.LoginSeamlessUseCase
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
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
    private val getNameUseCase = mockk<GetNameUseCase>(relaxed = true)
    private val gotoSeamlessHelper = mockk<GotoSeamlessHelper>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val exception = Exception("error")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(EncoderDecoder())
        mockkObject(TokenGenerator())

        mockkStatic(EncoderDecoder::class)
        viewModel = GotoSeamlessLoginViewModel(loginSeamlessUseCase, getNameUseCase, gotoSeamlessHelper, userSession, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `Get Gojek Data Success without name`() {
        val mockGojekProfile = GojekProfileData(name = "yoris")
        coEvery { gotoSeamlessHelper.getGojekProfile() } returns mockGojekProfile

        viewModel.getGojekData()

        assert((viewModel.gojekProfileData.getOrAwaitValue() as Success).data == mockGojekProfile)
    }

    @Test
    fun `Get Gojek Data Success with name`() {
        val tokoName = "tokopedia name"
        val mockGojekProfile = GojekProfileData(name = "yoris", authCode = "abc123")
        val mockGetData = GetNameData(name = tokoName, error = "")
        val mockGetName = GetNameResponse(data = mockGetData)
        coEvery { gotoSeamlessHelper.getGojekProfile() } returns mockGojekProfile
        coEvery { getNameUseCase(any()) } returns mockGetName

        viewModel.getGojekData()

        assert((viewModel.gojekProfileData.getOrAwaitValue() as Success).data.tokopediaName == tokoName)
    }

    @Test
    fun `Get Gojek Data Failed`() {
        coEvery { gotoSeamlessHelper.getGojekProfile() } throws exception

        viewModel.getGojekData()

        assert((viewModel.gojekProfileData.getOrAwaitValue() as Fail).throwable.message == "error")
    }

    @Test
    fun `Login Token Success`() {
        val data = LoginToken(accessToken = "abc123")
        val response = LoginTokenPojoV2(data)

        coEvery { loginSeamlessUseCase(any()) } returns response
        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"

        viewModel.doSeamlessLogin("abc")

        assert((viewModel.loginResponse.getOrAwaitValue() as Success).data == data)
    }

    @Test
    fun `Login Token Failed`() {
        coEvery { loginSeamlessUseCase(any()) } throws exception

        viewModel.doSeamlessLogin("abc")

        assert((viewModel.loginResponse.getOrAwaitValue() as Fail).throwable.message == "error")
    }

    @Test
    fun `On Success Seamless Login`() {
        val token = "abc123"
        val data = LoginToken(accessToken = token)
        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"

        viewModel.onSuccessSeamlessLogin(data)

        assert((viewModel.loginResponse.getOrAwaitValue() as Success).data == data)
    }

    @Test
    fun `On Success Seamless Login - token empty`() {
        val token = ""
        val data = LoginToken(accessToken = token)

        viewModel.onSuccessSeamlessLogin(data)

        assert((viewModel.loginResponse.getOrAwaitValue() as Fail).throwable.message == "Access token is empty")
    }

    @Test
    fun `Save access token`() {
        val token = "abc123"
        val tokenType = "Bearer"
        val refreshToken = "123abc"

        val data = LoginToken(accessToken = token, tokenType = tokenType, refreshToken = refreshToken)

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
