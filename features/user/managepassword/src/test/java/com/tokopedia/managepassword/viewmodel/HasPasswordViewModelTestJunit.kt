package com.tokopedia.managepassword.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import com.tokopedia.managepassword.haspassword.view.viewmode.HasPasswordViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
class HasPasswordViewModelTestJunit {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val usecase = mockk<GetProfileCompletionUseCase>(relaxed = true)

    val dispatcher = TestCoroutineDispatcher()
    private var observer = mockk<Observer<Result<ProfileDataModel>>>(relaxed = true)

    lateinit var viewModel: HasPasswordViewModel

    @Before
    fun setUp() {
        viewModel = HasPasswordViewModel(
                usecase,
                dispatcher
        )
        viewModel.profileDataModel.observeForever(observer)
    }

    @Test
    fun `checkPassword Success`() {
        val profile = ProfileDataModel.Profile()
        val mockResponse = ProfileDataModel(profile)

        every { usecase.getData(any(), any()) } answers {
            firstArg<(ProfileDataModel) -> Unit>().invoke(mockResponse)
        }
        viewModel.checkPassword()

        /* Then */
        verify { observer.onChanged(Success(mockResponse)) }
    }

    @Test
    fun `checkPassword Error`() {
        val throwable = Throwable(message = "Error")

        every { usecase.getData(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.checkPassword()

        /* Then */
        verify { observer.onChanged(Fail(throwable)) }
    }

}