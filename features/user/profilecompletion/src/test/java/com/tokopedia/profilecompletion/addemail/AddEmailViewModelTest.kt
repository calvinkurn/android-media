package com.tokopedia.profilecompletion.addemail

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.profilecompletion.addemail.data.AddEmailPojo
import com.tokopedia.profilecompletion.addemail.data.AddEmailResult
import com.tokopedia.profilecompletion.addemail.data.CheckEmailPojo
import com.tokopedia.profilecompletion.addemail.viewmodel.AddEmailViewModel
import com.tokopedia.profilecompletion.domain.AddEmailUseCase
import com.tokopedia.profilecompletion.domain.CheckEmailUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
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
class AddEmailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val context = mockk<Context>(relaxed = true)

    lateinit var viewModel: AddEmailViewModel

    val mockEmail = "yoris.prayogo@tokopedia.com"
    val mockOtp = "1234"
    val mockValidateToken = "validateToken"

    val mockAddEmailPojo = AddEmailPojo()
    val mockCheckEmailPojo = CheckEmailPojo()

    private val checkEmailUseCase = mockk<CheckEmailUseCase>(relaxed = true)
    private val addEmailUseCase = mockk<AddEmailUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = AddEmailViewModel(
                CoroutineTestDispatchersProvider,
                checkEmailUseCase,
                addEmailUseCase
        )
    }

    @Test
    fun `on Success Add Email`() {
        mockAddEmailPojo.data.isSuccess = true
        mockAddEmailPojo.data.errorMessage = ""

        val addEmailResult = AddEmailResult(mockAddEmailPojo, mockEmail)

        coEvery { addEmailUseCase(any()) } returns mockAddEmailPojo

        viewModel.mutateAddEmail(mockEmail, mockOtp, mockValidateToken)

        /* Then */
        val result = viewModel.mutateAddEmailResponse.getOrAwaitValue()
        assertEquals(Success(addEmailResult), result)
    }

    @Test
    fun `on Generic Error Add Email`() {
        val mockThrowable = mockk<Throwable>(relaxed = true)

        coEvery { addEmailUseCase(any()) } throws mockThrowable

        viewModel.mutateAddEmail(mockEmail, mockOtp, mockValidateToken)

        /* Then */
        val result = viewModel.mutateAddEmailResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Add Email Error Message not empty`() {
        mockAddEmailPojo.data.isSuccess = true
        mockAddEmailPojo.data.errorMessage = "Test Error"

        coEvery { addEmailUseCase(any()) } returns mockAddEmailPojo

        viewModel.mutateAddEmail(mockEmail, mockOtp, mockValidateToken)

        /* Then */
        val result = viewModel.mutateAddEmailResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertEquals(mockAddEmailPojo.data.errorMessage, (result as Fail).throwable.message)
    }

    @Test
    fun `on Another Add Email errors occur`() {
        mockAddEmailPojo.data.isSuccess = false
        mockAddEmailPojo.data.errorMessage = ""

        coEvery { addEmailUseCase(any()) } returns mockAddEmailPojo

        viewModel.mutateAddEmail(mockEmail, mockOtp, mockValidateToken)

        /* Then */
        val result = viewModel.mutateAddEmailResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertThat((result as Fail).throwable, instanceOf(RuntimeException::class.java))
    }

    @Test
    fun `on Success Check Email`() {
        mockCheckEmailPojo.data.isValid = true
        mockCheckEmailPojo.data.errorMessage = ""

        coEvery { checkEmailUseCase(any()) } returns mockCheckEmailPojo

        viewModel.checkEmail(mockEmail)

        /* Then */
        val result = viewModel.mutateCheckEmailResponse.getOrAwaitValue()
        assertEquals(Success(mockEmail), result)
    }

    @Test
    fun `on Generic Error Check Email`() {
        val mockThrowable = mockk<Throwable>(relaxed = true)

        coEvery { checkEmailUseCase(any()) } throws mockThrowable

        viewModel.checkEmail(mockEmail)

        /* Then */
        val result = viewModel.mutateCheckEmailResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Check Email Error Message not empty`() {
        mockCheckEmailPojo.data.isValid = false
        mockCheckEmailPojo.data.errorMessage = "Test Error"

        coEvery { checkEmailUseCase(any()) } returns mockCheckEmailPojo

        viewModel.checkEmail(mockEmail)

        /* Then */
        val result = viewModel.mutateCheckEmailResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertEquals(mockCheckEmailPojo.data.errorMessage, (result as Fail).throwable.message)
    }

    @Test
    fun `on Another check email error occur`() {
        mockCheckEmailPojo.data.isValid = false
        mockCheckEmailPojo.data.errorMessage = ""

        coEvery { checkEmailUseCase(any()) } returns mockCheckEmailPojo

        viewModel.checkEmail(mockEmail)

        /* Then */
        val result = viewModel.mutateCheckEmailResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertThat((result as Fail).throwable, instanceOf(RuntimeException::class.java))
    }

}
