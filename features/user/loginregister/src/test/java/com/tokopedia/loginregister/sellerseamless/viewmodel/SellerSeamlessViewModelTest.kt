package com.tokopedia.loginregister.sellerseamless.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginregister.login.view.viewmodel.SellerSeamlessViewModel
import com.tokopedia.seamless_login_common.utils.AESUtils
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.objectMockk
import io.mockk.use
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by Yoris Prayogo on 23/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SellerSeamlessViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)

    val dispatcher = Dispatchers.Unconfined


    private var loginTokenObserver = mockk<Observer<Result<LoginToken>>>(relaxed = true)

    lateinit var seamlessLoginViewModel: SellerSeamlessViewModel

    @Before
    fun setUp() {
        seamlessLoginViewModel = SellerSeamlessViewModel(
                userSession,
                loginTokenUseCase,
                dispatcher
        )

        seamlessLoginViewModel.loginTokenResponse.observeForever(loginTokenObserver)
    }

    @Test
    fun `login seamless success`() {
        val code = "code"
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)
        responseToken.loginToken.accessToken = "123"
        responseToken.loginToken.refreshToken = "123"
        responseToken.loginToken.tokenType = "123"

        objectMockk(AESUtils).use {
            every { AESUtils.encrypt(any(), any()) } returns "abc1234"
        }

        every { loginTokenUseCase.executeLoginTokenSeamless(any(), any()) } answers {
            (secondArg() as LoginTokenSubscriber).onSuccessLoginToken(responseToken)
        }

        /* When */
//        seamlessLoginViewModel.loginSeamless(code)

        /* Then */
//        verify {
//            loginTokenObserver.onChanged(Success(responseToken.loginToken))
//        }
    }
}