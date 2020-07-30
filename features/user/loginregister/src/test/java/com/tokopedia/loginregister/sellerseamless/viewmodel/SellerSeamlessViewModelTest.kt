package com.tokopedia.loginregister.sellerseamless.viewmodel

import com.tokopedia.loginregister.login.view.viewmodel.SellerSeamlessViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Test

/**
 * Created by Yoris Prayogo on 23/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SellerSeamlessViewModelTest {

    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)

    val dispatcher = Dispatchers.Unconfined

    val seamlessLoginViewModel = SellerSeamlessViewModel(
            userSession,
            loginTokenUseCase,
            dispatcher
    )

    @Test
    fun `login seamless success`() {
        val code = "code"
        /* When */
        seamlessLoginViewModel.loginSeamless(code)

        /* Then */
//        verify { generateKeyUseCase.executeCoroutines(any(), any()) }
    }
}