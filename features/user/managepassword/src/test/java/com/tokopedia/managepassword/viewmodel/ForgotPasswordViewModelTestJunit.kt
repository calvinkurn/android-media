package com.tokopedia.managepassword.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordResponseModel
import com.tokopedia.managepassword.forgotpassword.domain.usecase.ForgotPasswordUseCase
import com.tokopedia.managepassword.forgotpassword.view.viewmodel.ForgotPasswordViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 27/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@ExperimentalCoroutinesApi
class ForgotPasswordViewModelTestJunit {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val forgotPasswordUseCase = mockk<ForgotPasswordUseCase>(relaxed = true)

    val dispatcher = TestCoroutineDispatcher()
    private var observer = mockk<Observer<Result<ForgotPasswordResponseModel>>>(relaxed = true)

    lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setUp() {
        viewModel = ForgotPasswordViewModel(
                forgotPasswordUseCase,
                dispatcher
        )
        viewModel.response.observeForever(observer)
    }

    var email = "yoris.prayogo@tokopedia.com"

    @Test
    fun `resetPassword Success`() {
        val forgotPassword = ForgotPasswordResponseModel.ForgotPasswordModel(isSuccess = true)
        val forgotPasswordResponseModel = ForgotPasswordResponseModel(forgotPassword)

        every { forgotPasswordUseCase.sendRequest(any(), any()) } answers {
            firstArg<(ForgotPasswordResponseModel) -> Unit>().invoke(forgotPasswordResponseModel)
        }
        viewModel.resetPassword(email)

        /* Then */
        verify { observer.onChanged(Success(forgotPasswordResponseModel)) }
    }

    @Test
    fun `resetPassword isSuccess = false`() {
        val forgotPassword = ForgotPasswordResponseModel.ForgotPasswordModel(isSuccess = false, message = "error")
        val forgotPasswordResponseModel = ForgotPasswordResponseModel(forgotPassword)

        every { forgotPasswordUseCase.sendRequest(any(), any()) } answers {
            firstArg<(ForgotPasswordResponseModel) -> Unit>().invoke(forgotPasswordResponseModel)
        }
        viewModel.resetPassword(email)

        /* Then */
        assertEquals((viewModel.response.value as Fail).throwable.message, forgotPassword.message)
    }

    @Test
    fun `resetPassword Error`() {
        val throwable = Throwable(message = "Error")

        every { forgotPasswordUseCase.sendRequest(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.resetPassword(email)

        /* Then */
        verify { observer.onChanged(Fail(throwable)) }
    }

}