package com.tokopedia.profilecompletion.addphone

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.addphone.viewmodel.AddPhoneViewModel
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 26/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddPhoneViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val addPhoneGraphQlUseCase = mockk<GraphqlUseCase<AddPhonePojo>>(relaxed = true)
    val userValidateGraphQlUseCase = mockk<GraphqlUseCase<UserValidatePojo>>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()

    private var addPhoneObserver = mockk<Observer<Result<AddPhoneResult>>>(relaxed = true)
    private var userValidateObserver = mockk<Observer<Result<UserValidatePojo>>>(relaxed = true)

    lateinit var viewModel: AddPhoneViewModel


    private val rawQueries = mapOf(
            ProfileCompletionQueryConstant.MUTATION_ADD_PHONE to ProfileCompletionQueryConstant.MUTATION_ADD_PHONE,
            ProfileCompletionQueryConstant.MUTATION_USER_VALIDATE to ProfileCompletionQueryConstant.MUTATION_USER_VALIDATE
    )

    val msisdn = "08123812381"
    val errMsg = "Error"

    val addPhonePojo = AddPhonePojo()
    val userValidatePojo = UserValidatePojo()

    val mockThrowable = Throwable(errMsg)
    val mockValidateToken = "validateToken"

    @Before
    fun setUp() {
        viewModel = AddPhoneViewModel(
                addPhoneGraphQlUseCase,
                userValidateGraphQlUseCase,
                rawQueries,
                CoroutineTestDispatchersProvider
        )
        viewModel.addPhoneResponse.observeForever(addPhoneObserver)
        viewModel.userValidateResponse.observeForever(userValidateObserver)

    }

    @Test
    fun `on mutateAddPhone executed`() {
        val mockParam = mapOf(
            ProfileCompletionQueryConstant.PARAM_PHONE to msisdn,
            ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to mockValidateToken
        )

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        verify {
            addPhoneGraphQlUseCase.setTypeClass(any())
            addPhoneGraphQlUseCase.setRequestParams(mockParam)
            addPhoneGraphQlUseCase.setGraphqlQuery(any<String>())
            addPhoneGraphQlUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Mutate Add Phone`() {
        /* When */
        addPhonePojo.data.isSuccess = 1
        val addPhoneResult = AddPhoneResult(addPhonePojo, msisdn)

        every { addPhoneGraphQlUseCase.execute(any(), any()) } answers {
            firstArg<(AddPhonePojo) -> Unit>().invoke(addPhonePojo)
        }

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        verify { addPhoneObserver.onChanged(Success(addPhoneResult)) }
    }

    @Test
    fun `on Error Mutate Add Phone Error msg not empty`() {
        /* When */
        addPhonePojo.data.isSuccess = 0
        addPhonePojo.data.errors = arrayListOf("Error")

        every { addPhoneGraphQlUseCase.execute(any(), any()) } answers {
            firstArg<(AddPhonePojo) -> Unit>().invoke(addPhonePojo)
        }

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        assertThat(viewModel.addPhoneResponse.value, instanceOf(Fail::class.java))
        assertEquals(addPhonePojo.data.errors[0], (viewModel.addPhoneResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ addPhoneObserver.onChanged(any()) }
    }

    @Test
    fun `on Another Error Mutate Add Phone`() {
        /* When */
        addPhonePojo.data.isSuccess = 0

        every { addPhoneGraphQlUseCase.execute(any(), any()) } answers {
            firstArg<(AddPhonePojo) -> Unit>().invoke(addPhonePojo)
        }

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        assertThat(viewModel.addPhoneResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.addPhoneResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ addPhoneObserver.onChanged(any()) }
    }

    @Test
    fun `on generic Error Mutate Add Phone`() {

        every { addPhoneGraphQlUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        verify { addPhoneObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on userProfileValidate executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_PHONE to msisdn)

        viewModel.userProfileValidate(msisdn)

        /* Then */
        verify {
            userValidateGraphQlUseCase.setTypeClass(any())
            userValidateGraphQlUseCase.setRequestParams(mockParam)
            userValidateGraphQlUseCase.setGraphqlQuery(any<String>())
            userValidateGraphQlUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success user Validate`() {
        /* When */
        userValidatePojo.userProfileValidate.isValid = true

        every { userValidateGraphQlUseCase.execute(any(), any()) } answers {
            firstArg<(UserValidatePojo) -> Unit>().invoke(userValidatePojo)
        }

        viewModel.userProfileValidate(msisdn)

        /* Then */
        verify { userValidateObserver.onChanged(Success(userValidatePojo)) }
    }

    @Test
    fun `on Error user Validate`() {
        /* When */
        userValidatePojo.userProfileValidate.isValid = true

        every { userValidateGraphQlUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.userProfileValidate(msisdn)

        /* Then */
        verify { userValidateObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error user Validate error message not empty`() {
        /* When */
        userValidatePojo.userProfileValidate.isValid = false
        userValidatePojo.userProfileValidate.message = "Test Error"

        every { userValidateGraphQlUseCase.execute(any(), any()) } answers {
            firstArg<(UserValidatePojo) -> Unit>().invoke(userValidatePojo)
        }

        viewModel.userProfileValidate(msisdn)

        /* Then */
        assertThat(viewModel.userValidateResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.userValidateResponse.value as Fail).throwable, instanceOf(MessageErrorException::class.java))
        assertEquals(userValidatePojo.userProfileValidate.message, (viewModel.userValidateResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ userValidateObserver.onChanged(any()) }
    }

    @Test
    fun `on another Error user Validate`() {
        /* When */
        userValidatePojo.userProfileValidate.isValid = false

        every { userValidateGraphQlUseCase.execute(any(), any()) } answers {
            firstArg<(UserValidatePojo) -> Unit>().invoke(userValidatePojo)
        }

        viewModel.userProfileValidate(msisdn)

        /* Then */
        assertThat(viewModel.userValidateResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.userValidateResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ userValidateObserver.onChanged(any()) }
    }

}