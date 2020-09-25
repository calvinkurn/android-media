package com.tokopedia.profilecompletion.changepin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.changepin.data.ResetPinResponse
import com.tokopedia.profilecompletion.changepin.view.viewmodel.ChangePinViewModel
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
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ChangePinViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val resetPinUseCase = mockk<GraphqlUseCase<ResetPinResponse>>(relaxed = true)
    val checkPinUseCase = mockk<GraphqlUseCase<CheckPinPojo>>(relaxed = true)
    val validatePinUseCase = mockk<GraphqlUseCase<ValidatePinPojo>>(relaxed = true)
    val changePinUseCase = mockk<GraphqlUseCase<ChangePinPojo>>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: ChangePinViewModel

    private val rawQueries = mapOf(
            ProfileCompletionQueryConstant.MUTATION_RESET_PIN to ProfileCompletionQueryConstant.MUTATION_RESET_PIN,
            ProfileCompletionQueryConstant.QUERY_CHECK_PIN to ProfileCompletionQueryConstant.QUERY_CHECK_PIN,
            ProfileCompletionQueryConstant.QUERY_VALIDATE_PIN to ProfileCompletionQueryConstant.QUERY_VALIDATE_PIN,
            ProfileCompletionQueryConstant.MUTATION_UPDATE_PIN to ProfileCompletionQueryConstant.MUTATION_UPDATE_PIN
    )

    private var resetPinObserver = mockk<Observer<Result<AddChangePinData>>>(relaxed = true)
    private var checkPinObserver = mockk<Observer<Result<CheckPinData>>>(relaxed = true)
    private var validatePinObserver = mockk<Observer<Result<ValidatePinData>>>(relaxed = true)
    private var changePinObserver = mockk<Observer<Result<AddChangePinData>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChangePinViewModel(
                validatePinUseCase,
                checkPinUseCase,
                resetPinUseCase,
                changePinUseCase,
                rawQueries,
                testDispatcher
        )
        viewModel.resetPinResponse.observeForever(resetPinObserver)
        viewModel.checkPinResponse.observeForever(checkPinObserver)
        viewModel.validatePinResponse.observeForever(validatePinObserver)
        viewModel.changePinResponse.observeForever(changePinObserver)
    }

    val token = "abcd1234"
    val resetPinPojo = AddChangePinData()

    val mockThrowable = mockk<Throwable>(relaxed = true)

    val pin = "123456"
    val pinConfirm = "123456"
    val pinOld = "654321"
    val OTP_TYPE_SKIP_VALIDATION = 124

    val resetPinResponse = ResetPinResponse(data = resetPinPojo)

    val checkPinPojo = CheckPinPojo()
    val validatePinPojo = ValidatePinPojo()
    val changePinPojo = ChangePinPojo()

    @Test
    fun `on resetPin executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to token)

        viewModel.resetPin(token)

        /* Then */
        verify {
            resetPinUseCase.setTypeClass(any())
            resetPinUseCase.setRequestParams(mockParam)
            resetPinUseCase.setGraphqlQuery(any())
            resetPinUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Reset Pin`() {
        /* When */
        resetPinResponse.data.success = true

        every { resetPinUseCase.execute(any(), any()) } answers {
            firstArg<(ResetPinResponse) -> Unit>().invoke(resetPinResponse)
        }

        viewModel.resetPin(token)

        /* Then */
        verify { resetPinObserver.onChanged(Success(resetPinResponse.data)) }
    }

    @Test
    fun `on Error Reset Pin`() {
        /* When */

        every { resetPinUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.resetPin(token)

        /* Then */
        verify { resetPinObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Reset Pin message not empty`() {
        /* When */
        resetPinResponse.data.errorAddChangePinData = listOf(ErrorAddChangePinData(message = "Error"))

        every { resetPinUseCase.execute(any(), any()) } answers {
            firstArg<(ResetPinResponse) -> Unit>().invoke(resetPinResponse)
        }

        viewModel.resetPin(token)

        /* Then */
        Assert.assertThat(viewModel.resetPinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.resetPinResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
        Assert.assertEquals(resetPinResponse.data.errorAddChangePinData[0].message, (viewModel.resetPinResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ resetPinObserver.onChanged(any()) }
    }

    @Test
    fun `on another Error Add Pin`() {
        /* When */
        resetPinResponse.data.success = false

        every { resetPinUseCase.execute(any(), any()) } answers {
            firstArg<(ResetPinResponse) -> Unit>().invoke(resetPinResponse)
        }

        viewModel.resetPin(token)

        /* Then */
        Assert.assertThat(viewModel.resetPinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.resetPinResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ resetPinObserver.onChanged(any()) }
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
    fun `on changePin executed`() {
        val mockParam = mapOf(
                ProfileCompletionQueryConstant.PARAM_PIN to pin,
                ProfileCompletionQueryConstant.PARAM_PIN_CONFIRM to pinConfirm,
                ProfileCompletionQueryConstant.PARAM_PIN_OLD to pinOld
        )

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        verify {
            changePinUseCase.setTypeClass(any())
            changePinUseCase.setRequestParams(mockParam)
            changePinUseCase.setGraphqlQuery(any())
            changePinUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Change Pin`() {
        /* When */
        changePinPojo.data.success = true

        every { changePinUseCase.execute(any(), any()) } answers {
            firstArg<(ChangePinPojo) -> Unit>().invoke(changePinPojo)
        }

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        verify { changePinObserver.onChanged(Success(changePinPojo.data)) }
    }

    @Test
    fun `on Error Change Pin`() {
        /* When */

        every { changePinUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        verify { changePinObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Change Pin message not empty`() {
        /* When */
        changePinPojo.data.errorAddChangePinData = listOf(ErrorAddChangePinData(message = "Error"))

        every { changePinUseCase.execute(any(), any()) } answers {
            firstArg<(ChangePinPojo) -> Unit>().invoke(changePinPojo)
        }

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        Assert.assertThat(viewModel.changePinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.changePinResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
        Assert.assertEquals(changePinPojo.data.errorAddChangePinData[0].message, (viewModel.changePinResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ changePinObserver.onChanged(any()) }
    }

    @Test
    fun `on another Error Change Pin`() {
        /* When */
        changePinPojo.data.success = false

        every { changePinUseCase.execute(any(), any()) } answers {
            firstArg<(ChangePinPojo) -> Unit>().invoke(changePinPojo)
        }

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        Assert.assertThat(viewModel.changePinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.changePinResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ changePinObserver.onChanged(any()) }
    }
}