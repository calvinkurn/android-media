package com.tokopedia.loginregister.external_register.base.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.domain.usecase.ExternalRegisterUseCase
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.sessioncommon.data.Error
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 28/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class ExternalRegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val activateOvoUseCase = mockk<ExternalRegisterUseCase>(relaxed = true)
    private val externalRegisterPreference = mockk<ExternalRegisterPreference>(relaxed = true)
    private val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    lateinit var viewModel: ExternalRegisterViewModel

    private var registerResponse = mockk<Observer<Result<RegisterPojo>>>(relaxed = true)
    private var getUserInfoResponse = mockk<Observer<Result<ProfileInfoData>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ExternalRegisterViewModel(
                activateOvoUseCase,
                externalRegisterPreference,
                getProfileUseCase,
                userSession,
                CoroutineTestDispatchersProvider
        )
        viewModel.registerRequestResponse.observeForever(registerResponse)
        viewModel.getUserInfoResponse.observeForever(getUserInfoResponse)
    }

    @Test
    fun `on register success`() {
        val registerInfo = RegisterInfo(accessToken = "accessToken", refreshToken = "refreshToken")
        val registerPojo = RegisterPojo(register = registerInfo)

        coEvery { activateOvoUseCase.executeOnBackground() } returns registerPojo

        viewModel.register("testAuthCode")

        verify {
            userSession.clearToken()
            registerResponse.onChanged(Success(registerPojo))
        }
    }

    @Test
    fun `on register have errors`() {
        val errorList = arrayListOf(
                Error("test error 1", "message error 1"),
                Error("test error 2", "message error 2")
        )
        val registerInfo = RegisterInfo(accessToken = "", refreshToken = "", errors = errorList)
        val registerPojo = RegisterPojo(register = registerInfo)

        coEvery { activateOvoUseCase.executeOnBackground() } returns registerPojo

        viewModel.register("testAuthCode")

        assert((viewModel.registerRequestResponse.value as Fail).throwable.message == errorList[0].message)
    }

    @Test
    fun `on register have errors and tokens empty`() {
        val errorList = arrayListOf<Error>()
        val registerInfo = RegisterInfo(accessToken = "", refreshToken = "", errors = errorList)
        val registerPojo = RegisterPojo(register = registerInfo)

        coEvery { activateOvoUseCase.executeOnBackground() } returns registerPojo

        viewModel.register("testAuthCode")
        assertThat(((viewModel.registerRequestResponse.value) as Fail).throwable, instanceOf(RuntimeException::class.java))
    }

    @Test
    fun `on register api throws error`() {
        val error = Throwable("error")

        coEvery { activateOvoUseCase.executeOnBackground() } throws error

        viewModel.register("testAuthCode")
        verify {
            registerResponse.onChanged(Fail(error))
        }
    }

    @Test
    fun `on get user info success`() {
        val profileInfo = ProfileInfo("123", "Yoris", "Yoris", "yoris.prayogo@tokopedia.com")
        val profilePojo = ProfilePojo(profileInfo = profileInfo)

        every { getProfileUseCase.execute(any()) } answers {
            (firstArg() as GetProfileSubscriber).onSuccessGetProfile(profilePojo)
        }
        viewModel.getUserInfo()

        verify {
            getUserInfoResponse.onChanged(Success(ProfileInfoData(profilePojo.profileInfo)))
        }
    }

    @Test
    fun `on get user info fail`() {
        val throwable = Throwable("error")

        every { getProfileUseCase.execute(any()) } answers {
            (firstArg() as GetProfileSubscriber).onErrorGetProfile(throwable)
        }
        viewModel.getUserInfo()

        verify {
            getUserInfoResponse.onChanged(Fail(throwable))
        }
    }
}