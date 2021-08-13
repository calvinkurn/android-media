package com.tokopedia.loginregister.seamlesslogin.viewmodel

import com.tokopedia.loginregister.seamlesslogin.SeamlessLoginViewModel
import com.tokopedia.loginregister.seamlesslogin.usecase.GenerateKeyUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

/**
 * Created by Yoris Prayogo on 23/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SeamlessLoginViewModelTest {

    val generateKeyUseCase = mockk<GenerateKeyUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)

    val seamlessLoginViewModel = SeamlessLoginViewModel(
            userSession,
            generateKeyUseCase,
            CoroutineTestDispatchersProvider
    )

    @Test
    fun `Get Key`() {
        /* When */
        seamlessLoginViewModel.getKey({}, {})

        /* Then */
        verify { generateKeyUseCase.executeCoroutines(any(), any()) }
    }
}