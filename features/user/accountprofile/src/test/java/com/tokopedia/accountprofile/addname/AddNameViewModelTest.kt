package com.tokopedia.accountprofile.addname

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.accountprofile.settingprofile.addname.viewmodel.AddNameViewModel
import com.tokopedia.sessioncommon.data.Error
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 28/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddNameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val registerUseCase = mockk<RegisterUseCase>(relaxed = true)

    val addNameObserver = mockk<Observer<Result<RegisterInfo>>>(relaxed = true)

    val registerPojo = RegisterPojo()

    lateinit var viewModel: AddNameViewModel

    private var name = "Yoris Prayogo"
    private var phoneNo = "081201203123123"
    val dummyToken = "1231231323"

    @Before
    fun setUp() {
        viewModel = AddNameViewModel(registerUseCase, CoroutineTestDispatchersProvider)
        viewModel.registerLiveData.observeForever(addNameObserver)
    }

    @Test
    fun `on registerPhoneNumberAndName executed`() {
        viewModel.registerPhoneNumberAndName(name, phoneNo, dummyToken)
        val mockParam = RegisterUseCase.generateParamRegisterPhone(name, phoneNo, dummyToken)

        coVerify {
            RegisterUseCase.generateParamRegisterPhone(name, phoneNo, dummyToken)
            registerUseCase(any())
        }
    }

    @Test
    fun `on registerPhoneNumberAndName Success`() {
        val mockParam = RegisterUseCase.generateParamRegisterPhone(name, phoneNo, dummyToken)

        coEvery { registerUseCase(any()) } returns registerPojo

        viewModel.registerPhoneNumberAndName(name, phoneNo, dummyToken)

        coVerify {
            registerUseCase(any())
            addNameObserver.onChanged(Success(registerPojo.register))
        }
    }

    @Test
    fun `on registerPhoneNumberAndName Success but errors not empty`() {
        val mockParam = RegisterUseCase.generateParamRegisterPhone(name, phoneNo, dummyToken)
        val dummyError = arrayListOf(Error(message = "Error"))
        registerPojo.register.errors = dummyError

        coEvery { registerUseCase(any()) } returns registerPojo

        viewModel.registerPhoneNumberAndName(name, phoneNo, dummyToken)

        coVerify {
            registerUseCase(any())
            addNameObserver.onChanged(viewModel.registerLiveData.value as Fail)
        }
    }

    @Test
    fun `on registerPhoneNumberAndName Error`() {
        val dummyError = Exception("")
        val mockParam = RegisterUseCase.generateParamRegisterPhone(name, phoneNo, dummyToken)

        coEvery { registerUseCase(any()) } throws dummyError

        viewModel.registerPhoneNumberAndName(name, phoneNo, dummyToken)

        coVerify {
            registerUseCase(any())
            addNameObserver.onChanged(Fail(dummyError))
        }
    }
}
