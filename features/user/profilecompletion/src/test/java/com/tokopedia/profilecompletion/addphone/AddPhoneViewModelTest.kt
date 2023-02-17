package com.tokopedia.profilecompletion.addphone

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.addphone.domain.UserProfileUpdateUseCase
import com.tokopedia.profilecompletion.addphone.domain.UserProfileValidateUseCase
import com.tokopedia.profilecompletion.addphone.viewmodel.AddPhoneViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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

    lateinit var viewModel: AddPhoneViewModel

    val msisdn = "08123812381"
    val errMsg = "Error"

    val addPhonePojo = AddPhonePojo()
    val userValidatePojo = UserValidatePojo()

    val mockThrowable = Throwable(errMsg)
    val mockValidateToken = "validateToken"

    private val userProfileUpdateUseCase = mockk<UserProfileUpdateUseCase>(relaxed = true)
    private val userProfileValidateUseCase = mockk<UserProfileValidateUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = AddPhoneViewModel(
            userProfileUpdateUseCase,
            userProfileValidateUseCase,
            CoroutineTestDispatchersProvider
        )

    }

    @Test
    fun `on Success Mutate Add Phone`() {
        /* When */
        addPhonePojo.data.isSuccess = 1
        val addPhoneResult = AddPhoneResult(addPhonePojo, msisdn)

        coEvery { userProfileUpdateUseCase(any()) } returns addPhonePojo

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        val result = viewModel.addPhoneResponse.getOrAwaitValue()
        assertEquals(Success(addPhoneResult), result)
    }

    @Test
    fun `on Error Mutate Add Phone Error msg not empty`() {
        /* When */
        addPhonePojo.data.isSuccess = 0
        addPhonePojo.data.errors = arrayListOf("Error")

        coEvery { userProfileUpdateUseCase(any()) } returns addPhonePojo

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        val result = viewModel.addPhoneResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertEquals(addPhonePojo.data.errors[0], (result as Fail).throwable.message)
    }

    @Test
    fun `on Another Error Mutate Add Phone`() {
        /* When */
        addPhonePojo.data.isSuccess = 0

        coEvery { userProfileUpdateUseCase(any()) } returns addPhonePojo

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        val result = viewModel.addPhoneResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertThat((result as Fail).throwable, instanceOf(RuntimeException::class.java))
        coVerify(atLeast = 1){ userProfileUpdateUseCase(any()) }
    }

    @Test
    fun `on generic Error Mutate Add Phone`() {

        coEvery { userProfileUpdateUseCase(any()) } throws mockThrowable

        viewModel.mutateAddPhone(msisdn, mockValidateToken)

        /* Then */
        val result = viewModel.addPhoneResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Success user Validate`() {
        /* When */
        userValidatePojo.userProfileValidate.isValid = true

        coEvery { userProfileValidateUseCase(any()) } returns userValidatePojo

        viewModel.userProfileValidate(msisdn)

        /* Then */
        val result = viewModel.userValidateResponse.getOrAwaitValue()
        assertEquals(Success(userValidatePojo), result)
    }

    @Test
    fun `on Error user Validate`() {
        /* When */
        userValidatePojo.userProfileValidate.isValid = true

        coEvery { userProfileValidateUseCase(any()) } throws mockThrowable

        viewModel.userProfileValidate(msisdn)

        /* Then */
        val result = viewModel.userValidateResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error user Validate error message not empty`() {
        /* When */
        userValidatePojo.userProfileValidate.isValid = false
        userValidatePojo.userProfileValidate.message = "Test Error"

        coEvery { userProfileValidateUseCase(any()) } returns userValidatePojo

        viewModel.userProfileValidate(msisdn)

        /* Then */
        val result = viewModel.userValidateResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertThat((result as Fail).throwable, instanceOf(MessageErrorException::class.java))
        assertEquals(userValidatePojo.userProfileValidate.message, (result as Fail).throwable.message)
        coVerify(atLeast = 1){ userProfileValidateUseCase(any()) }
    }

    @Test
    fun `on another Error user Validate`() {
        /* When */
        userValidatePojo.userProfileValidate.isValid = false

        coEvery { userProfileValidateUseCase(any()) } returns userValidatePojo

        viewModel.userProfileValidate(msisdn)

        /* Then */
        val result = viewModel.userValidateResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertThat((result as Fail).throwable, instanceOf(RuntimeException::class.java))
        coVerify(atLeast = 1){ userProfileValidateUseCase(any()) }
    }

}
