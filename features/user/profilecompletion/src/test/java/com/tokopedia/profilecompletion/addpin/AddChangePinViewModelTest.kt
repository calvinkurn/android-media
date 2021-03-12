package com.tokopedia.profilecompletion.addpin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 28/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class AddChangePinViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val addPinUseCase = mockk<GraphqlUseCase<AddPinPojo>>(relaxed = true)
    val checkPinUseCase = mockk<GraphqlUseCase<CheckPinPojo>>(relaxed = true)
    val getStatusPinUseCase = mockk<GraphqlUseCase<StatusPinPojo>>(relaxed = true)
    val validatePinUseCase = mockk<GraphqlUseCase<ValidatePinPojo>>(relaxed = true)
    val skipOtpPinUseCase = mockk<GraphqlUseCase<SkipOtpPinPojo>>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: AddChangePinViewModel

    private val rawQueries = mapOf(
            ProfileCompletionQueryConstant.MUTATION_CREATE_PIN to ProfileCompletionQueryConstant.MUTATION_CREATE_PIN,
            ProfileCompletionQueryConstant.QUERY_CHECK_PIN to ProfileCompletionQueryConstant.QUERY_CHECK_PIN,
            ProfileCompletionQueryConstant.QUERY_GET_STATUS_PIN to ProfileCompletionQueryConstant.QUERY_GET_STATUS_PIN,
            ProfileCompletionQueryConstant.QUERY_VALIDATE_PIN to ProfileCompletionQueryConstant.QUERY_VALIDATE_PIN,
            ProfileCompletionQueryConstant.QUERY_SKIP_OTP_PIN to ProfileCompletionQueryConstant.QUERY_SKIP_OTP_PIN
    )

    private var addPinObserver = mockk<Observer<Result<AddChangePinData>>>(relaxed = true)
    private var checkPinObserver = mockk<Observer<Result<CheckPinData>>>(relaxed = true)
    private var getStatusPinObserver = mockk<Observer<Result<StatusPinData>>>(relaxed = true)
    private var validatePinObserver = mockk<Observer<Result<ValidatePinData>>>(relaxed = true)
    private var skipOtpPinObserver = mockk<Observer<Result<SkipOtpPinData>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = AddChangePinViewModel(
            addPinUseCase,
            checkPinUseCase,
            getStatusPinUseCase,
            validatePinUseCase,
            skipOtpPinUseCase,
            rawQueries,
            testDispatcher
        )
        viewModel.addPinResponse.observeForever(addPinObserver)
        viewModel.checkPinResponse.observeForever(checkPinObserver)
        viewModel.getStatusPinResponse.observeForever(getStatusPinObserver)
        viewModel.validatePinResponse.observeForever(validatePinObserver)
        viewModel.skipOtpPinResponse.observeForever(skipOtpPinObserver)
    }

    val token = "abcd1234"
    val addPinPojo = AddPinPojo()

    val mockThrowable = mockk<Throwable>(relaxed = true)

    val pin = "123456"
    val OTP_TYPE_SKIP_VALIDATION = 124

    val checkPinPojo = CheckPinPojo()
    val getStatusPinPojo = StatusPinPojo()
    val validatePinPojo = ValidatePinPojo()
    val skipOtpPinPojo = SkipOtpPinPojo()

    @Test
    fun `on addPin executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_TOKEN to token)

        viewModel.addPin(token)

        /* Then */
        verify {
            addPinUseCase.setTypeClass(any())
            addPinUseCase.setRequestParams(mockParam)
            addPinUseCase.setGraphqlQuery(any())
            addPinUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Add Pin`() {
        /* When */
        addPinPojo.data.success = true

        every { addPinUseCase.execute(any(), any()) } answers {
            firstArg<(AddPinPojo) -> Unit>().invoke(addPinPojo)
        }

        viewModel.addPin(token)

        /* Then */
        verify { addPinObserver.onChanged(Success(addPinPojo.data)) }
    }

    @Test
    fun `on Error Add Pin`() {
        /* When */

        every { addPinUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.addPin(token)

        /* Then */
        verify { addPinObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Add Pin message not empty`() {
        /* When */
        addPinPojo.data.errorAddChangePinData = listOf(ErrorAddChangePinData(message = "Error"))

        every { addPinUseCase.execute(any(), any()) } answers {
            firstArg<(AddPinPojo) -> Unit>().invoke(addPinPojo)
        }

        viewModel.addPin(token)

        /* Then */
        Assert.assertThat(viewModel.addPinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.addPinResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
        Assert.assertEquals(addPinPojo.data.errorAddChangePinData[0].message, (viewModel.addPinResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ addPinObserver.onChanged(any()) }
    }

    @Test
    fun `on another Error Add Pin`() {
        /* When */
        addPinPojo.data.success = false

        every { addPinUseCase.execute(any(), any()) } answers {
            firstArg<(AddPinPojo) -> Unit>().invoke(addPinPojo)
        }

        viewModel.addPin(token)

        /* Then */
        Assert.assertThat(viewModel.addPinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.addPinResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ addPinObserver.onChanged(any()) }
    }



    @Test
    fun `on checkPin executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_PIN to pin)

        viewModel.checkPin(pin)

        /* Then */
        verify {
            checkPinUseCase.setTypeClass(any())
            checkPinUseCase.setRequestParams(mockParam)
            checkPinUseCase.setGraphqlQuery(any())
            checkPinUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Check Pin`() {
        /* When */
        checkPinPojo.data.valid = true

        every { checkPinUseCase.execute(any(), any()) } answers {
            firstArg<(CheckPinPojo) -> Unit>().invoke(checkPinPojo)
        }

        viewModel.checkPin(pin)

        /* Then */
        verify { checkPinObserver.onChanged(Success(checkPinPojo.data)) }
    }

    @Test
    fun `on Error Check Pin`() {
        /* When */

        every { checkPinUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.checkPin(pin)

        /* Then */
        verify { checkPinObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Success Check Pin message not empty`() {
        /* When */
        checkPinPojo.data.errorMessage = "Error"

        every { checkPinUseCase.execute(any(), any()) } answers {
            firstArg<(CheckPinPojo) -> Unit>().invoke(checkPinPojo)
        }

        viewModel.checkPin(pin)

        /* Then */
        verify { checkPinObserver.onChanged(Success(checkPinPojo.data)) }
    }

    @Test
    fun `on another Error Check Pin`() {
        /* When */
        checkPinPojo.data.valid = false

        every { checkPinUseCase.execute(any(), any()) } answers {
            firstArg<(CheckPinPojo) -> Unit>().invoke(checkPinPojo)
        }

        viewModel.checkPin(pin)

        /* Then */
        Assert.assertThat(viewModel.checkPinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.checkPinResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ checkPinObserver.onChanged(any()) }
    }

    @Test
    fun `on getStatusPin executed`() {

        viewModel.getStatusPin()

        /* Then */
        verify {
            viewModel.loadingState.postValue(true)
            getStatusPinUseCase.setTypeClass(any())
            getStatusPinUseCase.setGraphqlQuery(any())
            getStatusPinUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Get Status Pin`() {
        /* When */

        every { getStatusPinUseCase.execute(any(), any()) } answers {
            firstArg<(StatusPinPojo) -> Unit>().invoke(getStatusPinPojo)
        }

        viewModel.getStatusPin()

        /* Then */
        verify { getStatusPinObserver.onChanged(Success(getStatusPinPojo.data)) }
    }

    @Test
    fun `on Error Get Status Pin`() {
        /* When */

        every { getStatusPinUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getStatusPin()

        /* Then */
        verify { getStatusPinObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Get Status Pin message not empty`() {
        /* When */
        getStatusPinPojo.data.errorMessage = "Error"

        every { getStatusPinUseCase.execute(any(), any()) } answers {
            firstArg<(StatusPinPojo) -> Unit>().invoke(getStatusPinPojo)
        }

        viewModel.getStatusPin()

        /* Then */
        Assert.assertThat(viewModel.getStatusPinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.getStatusPinResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
        Assert.assertEquals(getStatusPinPojo.data.errorMessage, (viewModel.getStatusPinResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ getStatusPinObserver.onChanged(any()) }
    }

    @Test
    fun `on validatePin executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_PIN to pin)

        viewModel.validatePin(pin)

        /* Then */
        verify {
            validatePinUseCase.setTypeClass(any())
            validatePinUseCase.setRequestParams(mockParam)
            validatePinUseCase.setGraphqlQuery(any())
            validatePinUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Validate Pin`() {
        /* When */
        validatePinPojo.data.valid = true

        every { validatePinUseCase.execute(any(), any()) } answers {
            firstArg<(ValidatePinPojo) -> Unit>().invoke(validatePinPojo)
        }

        viewModel.validatePin(pin)

        /* Then */
        verify { validatePinObserver.onChanged(Success(validatePinPojo.data)) }
    }

    @Test
    fun `on Error Validate Pin`() {
        /* When */

        every { validatePinUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.validatePin(pin)

        /* Then */
        verify { validatePinObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Validate Pin message not empty`() {
        /* When */
        validatePinPojo.data.errorMessage = "Error"

        every { validatePinUseCase.execute(any(), any()) } answers {
            firstArg<(ValidatePinPojo) -> Unit>().invoke(validatePinPojo)
        }

        viewModel.validatePin(pin)

        /* Then */
        verify { validatePinObserver.onChanged(Success(validatePinPojo.data)) }
    }

    @Test
    fun `on Another Error Validate Pin`() {
        /* When */
        validatePinPojo.data.valid = false

        every { validatePinUseCase.execute(any(), any()) } answers {
            firstArg<(ValidatePinPojo) -> Unit>().invoke(validatePinPojo)
        }

        viewModel.validatePin(pin)

        /* Then */
        Assert.assertThat(viewModel.validatePinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.validatePinResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ validatePinObserver.onChanged(any()) }
    }

    @Test
    fun `on checkSkipOtpPin executed`() {
        val mockParam = mapOf(
                ProfileCompletionQueryConstant.PARAM_OTP_TYPE to OTP_TYPE_SKIP_VALIDATION,
                ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN_SKIP_OTP to "validateToken"
        )

        viewModel.checkSkipOtpPin("validateToken")

        /* Then */
        verify {
            skipOtpPinUseCase.setRequestParams(mockParam)
            skipOtpPinUseCase.setTypeClass(any())
            skipOtpPinUseCase.setGraphqlQuery(any())
            skipOtpPinUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Check Skip Pin`() {
        /* When */

        every { skipOtpPinUseCase.execute(any(), any()) } answers {
            firstArg<(SkipOtpPinPojo) -> Unit>().invoke(skipOtpPinPojo)
        }

        viewModel.checkSkipOtpPin("validateToken")

        /* Then */
        verify { skipOtpPinObserver.onChanged(Success(skipOtpPinPojo.data)) }
    }

    @Test
    fun `on Error Check Skip Pin`() {
        /* When */

        every { skipOtpPinUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.checkSkipOtpPin("validateToken")

        /* Then */
        verify { skipOtpPinObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Check Skip Pin message not empty`() {
        /* When */
        skipOtpPinPojo.data.errorMessage = "Error"

        every { skipOtpPinUseCase.execute(any(), any()) } answers {
            firstArg<(SkipOtpPinPojo) -> Unit>().invoke(skipOtpPinPojo)
        }

        viewModel.checkSkipOtpPin("validateToken")

        /* Then */
        Assert.assertThat(viewModel.skipOtpPinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.skipOtpPinResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
        Assert.assertEquals(skipOtpPinPojo.data.errorMessage, (viewModel.skipOtpPinResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ skipOtpPinObserver.onChanged(any()) }
    }

}