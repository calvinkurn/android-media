package com.tokopedia.managepassword.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordResponseModel
import com.tokopedia.managepassword.changepassword.domain.usecase.ChangePasswordUseCase
import com.tokopedia.managepassword.changepassword.view.viewmodel.ChangePasswordViewModel
import com.tokopedia.managepassword.changepassword.view.viewmodel.LiveDataValidateResult
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 27/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ChangePasswordViewModelTestJunit {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val changePasswordUsecase = mockk<ChangePasswordUseCase>(relaxed = true)

    val dispatcher = TestCoroutineDispatcher()
    private var observer = mockk<Observer<Result<ChangePasswordResponseModel>>>(relaxed = true)
    private var validatePasswordObserver = mockk<Observer<LiveDataValidateResult>>(relaxed = true)
    val mockThrowable = mockk<Throwable>(relaxed = true)

    lateinit var viewModel: ChangePasswordViewModel

    var encode = ""
    var new = ""
    var confirmation = ""
    var validationToken = ""
    var old = ""

    @Before
    fun setUp() {
        viewModel = ChangePasswordViewModel(
                changePasswordUsecase,
                dispatcher
        )
        viewModel.response.observeForever(observer)
        viewModel.validatePassword.observeForever(validatePasswordObserver)

    }

    @Test
    fun `submitChangePassword Success`() {
        var changePassword = ChangePasswordResponseModel.ChangePassword()
        val changePasswordResponseModel = ChangePasswordResponseModel(changePassword)

        every { changePasswordUsecase.submit(any(), any()) } answers {
            firstArg<(ChangePasswordResponseModel) -> Unit>().invoke(changePasswordResponseModel)
        }
        viewModel.submitChangePassword(encode, new, confirmation, validationToken)

        /* Then */
        verify { observer.onChanged(Success(changePasswordResponseModel)) }
    }

    @Test
    fun `submitChangePassword Error`() {
        /* When */
        every { changePasswordUsecase.submit(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.submitChangePassword(encode, new, confirmation, validationToken)

        /* Then */
        verify { observer.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `Execute validatePassword empty`() {
        old = ""
        new = ""
        confirmation = ""

        viewModel.validatePassword(old, new, confirmation)
        /* Then */
        verify {
            validatePasswordObserver.onChanged(LiveDataValidateResult.EMPTY_PARAMS)
        }
    }

    @Test
    fun `Execute validatePassword new == old`() {
        old = "12345678"
        new = "12345678"
        confirmation = ""

        viewModel.validatePassword(old, new, confirmation)
        /* Then */
        verify {
            validatePasswordObserver.onChanged(LiveDataValidateResult.SAME_WITH_OLD)
        }
    }

    @Test
    fun `Execute validatePassword new != confirmation`() {
        old = "123456781"
        new = "12345678"
        confirmation = "123456789"

        viewModel.validatePassword(old, new, confirmation)
        /* Then */
        verify {
            validatePasswordObserver.onChanged(LiveDataValidateResult.CONFIRMATION_INVALID)
        }
    }

    @Test
    fun `Execute validatePassword valid`() {
        old = "12345678999"
        new = "12345678"
        confirmation = "12345678"

        viewModel.validatePassword(old, new, confirmation)
        /* Then */
        verify {
            validatePasswordObserver.onChanged(LiveDataValidateResult.VALID)
        }
    }
}