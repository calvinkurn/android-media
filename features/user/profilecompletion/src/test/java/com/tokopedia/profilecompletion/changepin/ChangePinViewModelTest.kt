package com.tokopedia.profilecompletion.changepin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.changepin.data.ChangePin2FAData
import com.tokopedia.profilecompletion.changepin.data.ResetPin2FaPojo
import com.tokopedia.profilecompletion.changepin.data.ResetPinResponse
import com.tokopedia.profilecompletion.changepin.view.viewmodel.ChangePinViewModel
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
    val reset2FAPinUseCase = mockk<GraphqlUseCase<ResetPin2FaPojo>>(relaxed = true)
    val checkPin2FAUseCase = mockk<GraphqlUseCase<CheckPinPojo>>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)

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
    private var resetPin2FAObserver = mockk<Observer<Result<ChangePin2FAData>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChangePinViewModel(
                validatePinUseCase,
                checkPinUseCase,
                checkPin2FAUseCase,
                resetPinUseCase,
                reset2FAPinUseCase,
                changePinUseCase,
                userSession,
                rawQueries,
                testDispatcher
        )
        viewModel.resetPinResponse.observeForever(resetPinObserver)
        viewModel.checkPinResponse.observeForever(checkPinObserver)
        viewModel.validatePinResponse.observeForever(validatePinObserver)
        viewModel.changePinResponse.observeForever(changePinObserver)
        viewModel.resetPin2FAResponse.observeForever(resetPin2FAObserver)
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

    val validateToken = "abc123"
    val userId = "1234"

    @Test
    fun `on checkPin 2FA executed`() {
        val mockParam = mapOf(
                ProfileCompletionQueryConstant.PARAM_PIN to pin,
                ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to validateToken,
                ProfileCompletionQueryConstant.PARAM_ACTION to "reset",
                ProfileCompletionQueryConstant.PARAM_USER_ID to userId.toIntOrZero()
        )

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        verify {
            checkPin2FAUseCase.setTypeClass(any())
            checkPin2FAUseCase.setRequestParams(mockParam)
            checkPin2FAUseCase.setGraphqlQuery(any())
            checkPin2FAUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Check Pin 2FA`() {
        /* When */
        checkPinPojo.data.valid = true

        every { checkPin2FAUseCase.execute(any(), any()) } answers {
            firstArg<(CheckPinPojo) -> Unit>().invoke(checkPinPojo)
        }

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        verify { checkPinObserver.onChanged(Success(checkPinPojo.data)) }
    }

    @Test
    fun `on Error Check Pin 2FA`() {
        /* When */

        every { checkPin2FAUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        verify { checkPinObserver.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Success Check Pin 2FA message not empty`() {
        /* When */
        checkPinPojo.data.errorMessage = "Error"

        every { checkPin2FAUseCase.execute(any(), any()) } answers {
            firstArg<(CheckPinPojo) -> Unit>().invoke(checkPinPojo)
        }

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        verify { checkPinObserver.onChanged(Success(checkPinPojo.data)) }
    }

    @Test
    fun `on another Error Check Pin 2FA`() {
        /* When */
        checkPinPojo.data.valid = false

        every { checkPin2FAUseCase.execute(any(), any()) } answers {
            firstArg<(CheckPinPojo) -> Unit>().invoke(checkPinPojo)
        }

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        Assert.assertThat(viewModel.checkPinResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.checkPinResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ checkPinObserver.onChanged(any()) }
    }

    @Test
    fun `on resetPin 2FA executed`() {
        val mockParam = mapOf(
                ProfileCompletionQueryConstant.PARAM_USER_ID to userId.toIntOrZero(),
                ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to validateToken,
                ProfileCompletionQueryConstant.PARAM_GRANT_TYPE to "extension"
        )

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        verify {
            reset2FAPinUseCase.setTypeClass(any())
            reset2FAPinUseCase.setRequestParams(mockParam)
            reset2FAPinUseCase.setGraphqlQuery(any())
            reset2FAPinUseCase.execute(any(), any())
        }
    }

    var resetPinData = ChangePin2FAData()
    var resetPin2FaPojo = ResetPin2FaPojo(data = resetPinData)

    @Test
    fun `on Success Reset Pin 2FA`() {
        /* When */
        resetPin2FaPojo.data.is_success = 1

        every { reset2FAPinUseCase.execute(any(), any()) } answers {
            firstArg<(ResetPin2FaPojo) -> Unit>().invoke(resetPin2FaPojo)
        }

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        verify { resetPin2FAObserver.onChanged(Success(resetPin2FaPojo.data)) }
    }

    @Test
    fun `on Error Reset Pin 2FA`() {
        /* When */

        every { reset2FAPinUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        verify {
            userSession.setToken(any(), any())
            resetPinObserver.onChanged(Fail(mockThrowable))
        }
    }

    @Test
    fun `on Error Reset Pin 2FA message not empty`() {
        /* When */
        resetPin2FaPojo.data.is_success = 0
        resetPin2FaPojo.data.error = "error"

        every { reset2FAPinUseCase.execute(any(), any()) } answers {
            firstArg<(ResetPin2FaPojo) -> Unit>().invoke(resetPin2FaPojo)
        }

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        Assert.assertThat(viewModel.resetPin2FAResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.resetPin2FAResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
        Assert.assertEquals(resetPin2FaPojo.data.error, (viewModel.resetPin2FAResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ resetPin2FAObserver.onChanged(any()) }
    }
}