package com.tokopedia.managepassword.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import com.tokopedia.managepassword.haspassword.view.viewmodel.HasPasswordViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    val getProfileCompletionUseCase = mockk<GetProfileCompletionUseCase>(relaxed = true)

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    private var observer = mockk<Observer<Result<ProfileDataModel>>>(relaxed = true)

    lateinit var viewModel: HasPasswordViewModel

    @Before
    fun setUp() {
        viewModel = HasPasswordViewModel(
                getProfileCompletionUseCase,
                dispatcherProviderTest
        )
        viewModel.profileDataModel.observeForever(observer)
    }

    @Test
    fun checkPassword() {
        val profile = ProfileDataModel.Profile()
        val mockResponse = ProfileDataModel(profile)

        coEvery { getProfileCompletionUseCase(Unit) } returns mockResponse
        viewModel.checkPassword()

        /* Then */
        coVerify { observer.onChanged(Success(mockResponse)) }
    }

    @Test
    fun `checkPassword - fail`() {
        val mockThrowable = Throwable("Opps!")

        coEvery { getProfileCompletionUseCase(Unit) }.throws(mockThrowable)
        viewModel.checkPassword()

        coVerify { observer.onChanged(Fail(mockThrowable)) }
    }
}