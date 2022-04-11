package com.tokopedia.profilecompletion.addemail

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.addemail.data.AddEmailPojo
import com.tokopedia.profilecompletion.addemail.data.AddEmailResult
import com.tokopedia.profilecompletion.addemail.data.CheckEmailPojo
import com.tokopedia.profilecompletion.addemail.viewmodel.AddEmailViewModel
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    val checkEmaillUseCase = mockk<GraphqlUseCase<CheckEmailPojo>>(relaxed = true)
    val addEmaillUseCase = mockk<GraphqlUseCase<AddEmailPojo>>(relaxed = true)

    val context = mockk<Context>(relaxed = true)

    private var checkEmailObserver = mockk<Observer<Result<String>>>(relaxed = true)
    private var addEmailObserver = mockk<Observer<Result<AddEmailResult>>>(relaxed = true)

    lateinit var viewModel: AddEmailViewModel

    val mockEmail = "yoris.prayogo@tokopedia.com"
    val mockOtp = "1234"

    val mockAddEmailPojo = AddEmailPojo()
    val mockCheckEmailPojo = CheckEmailPojo()

    @Before
    fun setUp() {
        viewModel = AddEmailViewModel(
                CoroutineTestDispatchersProvider,
                checkEmaillUseCase,
                addEmaillUseCase
        )
        viewModel.mutateCheckEmailResponse.observeForever(checkEmailObserver)
        viewModel.mutateAddEmailResponse.observeForever(addEmailObserver)
    }

    @Test
    fun `on mutateAddEmail executed`() {
        val mockParam = mapOf(
                ProfileCompletionQueryConstant.PARAM_EMAIL to mockEmail,
                ProfileCompletionQueryConstant.PARAM_OTP_CODE to mockOtp
        )

        viewModel.mutateAddEmail(context, mockEmail, mockOtp)

        /* Then */
        verify {
            addEmaillUseCase.setTypeClass(any())
            addEmaillUseCase.setRequestParams(mockParam)
            addEmaillUseCase.setGraphqlQuery(any<String>())
            addEmaillUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Add Email`() {
        mockAddEmailPojo.data.isSuccess = true
        mockAddEmailPojo.data.errorMessage = ""

        val addEmailResult = AddEmailResult(mockAddEmailPojo, mockEmail)

        every { addEmaillUseCase.execute(any(), any()) } answers {
            firstArg<(AddEmailPojo) -> Unit>().invoke(mockAddEmailPojo)
        }

        viewModel.mutateAddEmail(context, mockEmail, mockOtp)

        /* Then */
        verify { addEmailObserver.onChanged(Success(addEmailResult)) }
    }

    @Test
    fun `on Generic Error Add Email`() {
        val mockThrowable = mockk<Throwable>(relaxed = true)

        every { addEmaillUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.mutateAddEmail(context, mockEmail, mockOtp)

        /* Then */
        verify { addEmailObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Add Email Error Message not empty`() {
        mockAddEmailPojo.data.isSuccess = true
        mockAddEmailPojo.data.errorMessage = "Test Error"

        every { addEmaillUseCase.execute(any(), any()) } answers {
            firstArg<(AddEmailPojo) -> Unit>().invoke(mockAddEmailPojo)
        }

        viewModel.mutateAddEmail(context, mockEmail, mockOtp)

        /* Then */
        assertThat(viewModel.mutateAddEmailResponse.value, instanceOf(Fail::class.java))
        assertEquals(mockAddEmailPojo.data.errorMessage, (viewModel.mutateAddEmailResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ addEmailObserver.onChanged(any()) }
    }

    @Test
    fun `on Another Add Email errors occur`() {
        mockAddEmailPojo.data.isSuccess = false
        mockAddEmailPojo.data.errorMessage = ""

        every { addEmaillUseCase.execute(any(), any()) } answers {
            firstArg<(AddEmailPojo) -> Unit>().invoke(mockAddEmailPojo)
        }

        viewModel.mutateAddEmail(context, mockEmail, mockOtp)

        /* Then */
        assertThat(viewModel.mutateAddEmailResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.mutateAddEmailResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ addEmailObserver.onChanged(any()) }
    }

    @Test
    fun `on checkEmail executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_EMAIL to mockEmail)

        viewModel.checkEmail(context, mockEmail)

        /* Then */
        verify {
            checkEmaillUseCase.setTypeClass(any())
            checkEmaillUseCase.setRequestParams(mockParam)
            checkEmaillUseCase.setGraphqlQuery(any<String>())
            checkEmaillUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Check Email`() {
        mockCheckEmailPojo.data.isValid = true
        mockCheckEmailPojo.data.errorMessage = ""

        every { checkEmaillUseCase.execute(any(), any()) } answers {
            firstArg<(CheckEmailPojo) -> Unit>().invoke(mockCheckEmailPojo)
        }

        viewModel.checkEmail(context, mockEmail)

        /* Then */
        verify { checkEmailObserver.onChanged(Success(mockEmail)) }
    }

    @Test
    fun `on Generic Error Check Email`() {
        val mockThrowable = mockk<Throwable>(relaxed = true)

        every { checkEmaillUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.checkEmail(context, mockEmail)

        /* Then */
        verify { checkEmailObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Check Email Error Message not empty`() {
        mockCheckEmailPojo.data.isValid = false
        mockCheckEmailPojo.data.errorMessage = "Test Error"

        every { checkEmaillUseCase.execute(any(), any()) } answers {
            firstArg<(CheckEmailPojo) -> Unit>().invoke(mockCheckEmailPojo)
        }

        viewModel.checkEmail(context, mockEmail)

        /* Then */
        assertThat(viewModel.mutateCheckEmailResponse.value, instanceOf(Fail::class.java))
        assertEquals(mockCheckEmailPojo.data.errorMessage, (viewModel.mutateCheckEmailResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ checkEmailObserver.onChanged(any()) }
    }

    @Test
    fun `on Another check email error occur`() {
        mockCheckEmailPojo.data.isValid = false
        mockCheckEmailPojo.data.errorMessage = ""

        every { checkEmaillUseCase.execute(any(), any()) } answers {
            firstArg<(CheckEmailPojo) -> Unit>().invoke(mockCheckEmailPojo)
        }

        viewModel.checkEmail(context, mockEmail)

        /* Then */
        assertThat(viewModel.mutateCheckEmailResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.mutateCheckEmailResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ checkEmailObserver.onChanged(any()) }
    }

}