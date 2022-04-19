package com.tokopedia.profilecompletion.addname

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.profilecompletion.addname.listener.AddNameListener
import com.tokopedia.profilecompletion.addname.presenter.AddNamePresenter
import com.tokopedia.sessioncommon.data.Error
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

/**
 * Created by Yoris Prayogo on 28/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddNamePresenterTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val registerUseCase = mockk<RegisterUseCase>(relaxed = true)
    val view = mockk<AddNameListener.View>(relaxed = true)

    lateinit var presenter: AddNamePresenter

    private var name = "Yoris Prayogo"
    private var phoneNo = "081201203123123"

    val throwable = mockk<Throwable>(relaxed = true)

    @Before
    fun setUp() {
        presenter = AddNamePresenter(registerUseCase)
        presenter.attachView(view)
    }

    @Test
    fun `on registerPhoneNumberAndName executed`() {
        presenter.registerPhoneNumberAndName(name, phoneNo)
        val mockParam = RegisterUseCase.generateParamRegisterPhone(name, phoneNo)

        verify {
            RegisterUseCase.generateParamRegisterPhone(name, phoneNo)
            view.showLoading()
            registerUseCase.execute(mockParam, any())
        }
    }

    val registerPojo = RegisterPojo()

    val graphqlResponse = mockk<GraphqlResponse>(relaxed = true)

    @Test
    fun `on registerPhoneNumberAndName Success`() {
        val mockParam = RegisterUseCase.generateParamRegisterPhone(name, phoneNo)

        every { graphqlResponse.getData<RegisterPojo>(any()) } returns registerPojo

        every { registerUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<GraphqlResponse>).onNext(graphqlResponse)
        }

        presenter.registerPhoneNumberAndName(name, phoneNo)

        verify {
            view.showLoading()
            registerUseCase.execute(mockParam, any())
            view.onSuccessRegister(registerPojo.register)
        }
    }

    @Test
    fun `on registerPhoneNumberAndName Success but errors not empty`() {
        val mockParam = RegisterUseCase.generateParamRegisterPhone(name, phoneNo)
        registerPojo.register.errors = arrayListOf(Error(message = "Error"))

        every { graphqlResponse.getData<RegisterPojo>(any()) } returns registerPojo

        every { registerUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<GraphqlResponse>).onNext(graphqlResponse)
        }

        presenter.registerPhoneNumberAndName(name, phoneNo)

        verify {
            view.showLoading()
            registerUseCase.execute(mockParam, any())
            view.onErrorRegister(any())
        }
    }

    @Test
    fun `on registerPhoneNumberAndName Error`() {
        val mockParam = RegisterUseCase.generateParamRegisterPhone(name, phoneNo)

        every { registerUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<GraphqlResponse>).onError(throwable)
        }

        presenter.registerPhoneNumberAndName(name, phoneNo)

        verify {
            view.showLoading()
            registerUseCase.execute(mockParam, any())
            view.onErrorRegister(throwable)
        }
    }

}