package com.tokopedia.loginregister.sellerseamless.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginregister.login.view.viewmodel.SellerSeamlessViewModel
import com.tokopedia.seamless_login_common.utils.AESUtils
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by Yoris Prayogo on 23/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@ExperimentalCoroutinesApi
class SellerSeamlessViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)

    lateinit var viewModel: SellerSeamlessViewModel
    val observerLoginToken = mockk<Observer<Result<LoginToken>>>(relaxed = true)
    val observerSecurityQuestion = mockk<Observer<Boolean>>(relaxed = true)

    val code = "code"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(AESUtils)

        viewModel = SellerSeamlessViewModel(userSession, loginTokenUseCase, CoroutineTestDispatchersProvider)
        viewModel.loginTokenResponse.observeForever(observerLoginToken)
        viewModel.goToSecurityQuestion.observeForever(observerSecurityQuestion)
    }

    @After
    fun tearDown() {
        viewModel.loginTokenResponse.removeObserver(observerLoginToken)
        viewModel.goToSecurityQuestion.removeObserver(observerSecurityQuestion)
    }

    @Test
    fun `login seamless - has logged in`() {
        val mockEmail = "email"
        coEvery { userSession.email } returns mockEmail

        viewModel.hasLogin()

        assert(userSession.email.isNotEmpty())
    }

    @Test
    fun `login seamless - has not logged in`() {
        val mockEmail = ""
        coEvery { userSession.email } returns mockEmail

        viewModel.hasLogin()

        assert(userSession.email.isEmpty())
    }

    @Test
    fun `login seamless - success`() {
        val mockResponseAes = "encryptedString"
        val mockLoginToken = LoginTokenPojo(LoginToken(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                tokenType = "tokenType"
        ))

        coEvery { AESUtils.encryptSeamless(code.toByteArray()) } returns mockResponseAes
        coEvery { loginTokenUseCase.executeLoginTokenSeamless(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken.invoke(mockLoginToken)
        }

        viewModel.loginSeamless(code)

        verify { observerLoginToken.onChanged(Success(mockLoginToken.loginToken)) }
        val result = viewModel.loginTokenResponse.value as Success<LoginToken>
        assert(result.data == mockLoginToken.loginToken)
    }

    @Test
    fun `login seamless - fail token empty`() {
        val mockResponseAes = ""

        coEvery { AESUtils.encryptSeamless(code.toByteArray()) } returns mockResponseAes

        viewModel.loginSeamless(code)

        /* Then */
        MatcherAssert.assertThat(viewModel.loginTokenResponse.value, CoreMatchers.instanceOf(Fail::class.java))
    }

    @Test
    fun `login seamless - fail`() {
        val mockResponseAes = "encryptedString"
        val mockThrowable = Throwable("Opps!")

        coEvery { AESUtils.encryptSeamless(code.toByteArray()) } returns mockResponseAes
        coEvery { loginTokenUseCase.executeLoginTokenSeamless(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onErrorLoginToken.invoke(mockThrowable)
        }

        viewModel.loginSeamless(code)

        verify { observerLoginToken.onChanged(Fail(mockThrowable)) }
        val result = viewModel.loginTokenResponse.value as Fail
        assert(result.throwable == mockThrowable)
    }

    @Test
    fun `login seamless - goto SQ`() {
        val mockResponseAes = "encryptedString"
        coEvery { AESUtils.encryptSeamless(code.toByteArray()) } returns mockResponseAes

        coEvery { loginTokenUseCase.executeLoginTokenSeamless(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onGoToSecurityQuestion.invoke()
        }

        viewModel.loginSeamless(code)

        verify { observerSecurityQuestion.onChanged(any()) }
    }
}