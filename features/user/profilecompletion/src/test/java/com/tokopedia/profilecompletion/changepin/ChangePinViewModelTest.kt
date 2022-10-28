package com.tokopedia.profilecompletion.changepin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.changepin.data.ChangePin2FAData
import com.tokopedia.profilecompletion.changepin.data.ResetPin2FaPojo
import com.tokopedia.profilecompletion.changepin.data.ResetPinResponse
import com.tokopedia.profilecompletion.changepin.data.model.ResetPinV2Response
import com.tokopedia.profilecompletion.changepin.data.model.UpdatePinV2Response
import com.tokopedia.profilecompletion.changepin.data.usecase.ResetPinV2UseCase
import com.tokopedia.profilecompletion.changepin.data.usecase.UpdatePinV2UseCase
import com.tokopedia.profilecompletion.changepin.view.viewmodel.ChangePinViewModel
import com.tokopedia.profilecompletion.common.PinPreference
import com.tokopedia.profilecompletion.common.model.CheckPinV2Data
import com.tokopedia.profilecompletion.common.model.CheckPinV2Response
import com.tokopedia.profilecompletion.common.usecase.CheckPinV2UseCase
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.pin.PinStatusData
import com.tokopedia.sessioncommon.data.pin.PinStatusResponse
import com.tokopedia.sessioncommon.data.pin.ValidatePinV2Data
import com.tokopedia.sessioncommon.data.pin.ValidatePinV2Response
import com.tokopedia.sessioncommon.domain.usecase.CheckPinHashV2UseCase
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.ValidatePinV2UseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import kotlin.test.assertTrue

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@ExperimentalCoroutinesApi
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
    private var validatePinV2Observer = mockk<Observer<Result<ValidatePinV2Data>>>(relaxed = true)
    private var checkPinV2Observer = mockk<Observer<Result<CheckPinV2Data>>>(relaxed = true)

    val resetPinV2UseCase = mockk<ResetPinV2UseCase>(relaxed = true)
    val updatePinV2UseCase = mockk<UpdatePinV2UseCase>(relaxed = true)
    val generatePublicKeyUseCase = mockk<GeneratePublicKeyUseCase>(relaxed = true)
    val checkPinHashV2UseCase = mockk<CheckPinHashV2UseCase>(relaxed = true)
    val validatePinV2UseCase = mockk<ValidatePinV2UseCase>(relaxed = true)
    val checkPinV2UseCase = mockk<CheckPinV2UseCase>(relaxed = true)
    val pinPreference = mockk<PinPreference>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChangePinViewModel(
                validatePinUseCase,
                validatePinV2UseCase,
                checkPinUseCase,
                checkPin2FAUseCase,
                resetPinUseCase,
                resetPinV2UseCase,
                reset2FAPinUseCase,
                changePinUseCase,
                updatePinV2UseCase,
                checkPinV2UseCase,
                userSession,
                rawQueries,
                checkPinHashV2UseCase,
                generatePublicKeyUseCase,
                pinPreference,
                testDispatcher
        )
        viewModel.resetPinResponse.observeForever(resetPinObserver)
        viewModel.checkPinResponse.observeForever(checkPinObserver)
        viewModel.validatePinResponse.observeForever(validatePinObserver)
        viewModel.changePinResponse.observeForever(changePinObserver)
        viewModel.resetPin2FAResponse.observeForever(resetPin2FAObserver)
        viewModel.validatePinV2Response.observeForever(validatePinV2Observer)
        viewModel.checkPinV2Response.observeForever(checkPinV2Observer)
        mockkObject(RsaUtils)
    }

    val token = "abcd1234"
    val resetPinPojo = AddChangePinData()

    val mockThrowable = mockk<Throwable>(relaxed = true)

    val pin = "123456"
    val pinConfirm = "123456"
    val pinOld = "654321"
    val hashedPin = "abc1234b"

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
            resetPinUseCase.setGraphqlQuery(any<String>())
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
            checkPinUseCase.setGraphqlQuery(any<String>())
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
            validatePinUseCase.setGraphqlQuery(any<String>())
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
            changePinUseCase.setGraphqlQuery(any<String>())
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
            checkPin2FAUseCase.setGraphqlQuery(any<String>())
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
            reset2FAPinUseCase.setGraphqlQuery(any<String>())
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
        verify {
            userSession.setToken(any(), any())
            resetPin2FAObserver.onChanged(Success(resetPin2FaPojo.data))
        }
    }

    @Test
    fun `on Success Reset Pin 2FA - other errors`() {
        /* When */
        resetPin2FaPojo.data.is_success = 0
        resetPin2FaPojo.data.error = ""

        every { reset2FAPinUseCase.execute(any(), any()) } answers {
            firstArg<(ResetPin2FaPojo) -> Unit>().invoke(resetPin2FaPojo)
        }

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        verify { resetPin2FAObserver.onChanged(any<Fail>()) }
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

    @Test
    fun `isNeedHash - success` () {
        val pinStatusData = PinStatusData(isNeedHash = true)
        val pinStatusResponse = PinStatusResponse(pinStatusData)

        coEvery { checkPinHashV2UseCase(any()) } returns pinStatusResponse
        runBlocking {
            assertTrue(viewModel.isNeedHash("", ""))
        }
    }

    @Test
    fun `validatePinV2 - true` () {
        val validatePinV2Data = ValidatePinV2Data(valid = true)
        val validatePinV2Response = ValidatePinV2Response(validatePinV2Data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { validatePinV2UseCase(any()) } returns validatePinV2Response

        runBlocking {
            validatePinV2Observer.onChanged(Success(validatePinV2Data))
        }
    }

    @Test
    fun `validatePinV2 - exception` () {

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { validatePinV2UseCase(any()) } throws mockThrowable

        runBlocking {
            validatePinV2Observer.onChanged(any<Fail>())
        }
    }

    @Test
    fun `change pin v2 success`() {
        val data = AddChangePinData(success = true)
        val response = UpdatePinV2Response(data)


        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { updatePinV2UseCase(any()) } returns response

        viewModel.changePinV2("", "")

        coVerify {
            generatePublicKeyUseCase.executeOnBackground()
            changePinObserver.onChanged(Success(data))
        }
    }

    @Test
    fun `change pin v2 has erros msg`() {
        val errMsg = "Error"
        val data = AddChangePinData(success = false)
        val response = UpdatePinV2Response(data)

        response.mutatePinV2data.errorAddChangePinData = listOf(ErrorAddChangePinData(message = errMsg))

        val hashedPin = "abc1234b"

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { updatePinV2UseCase(any()) } returns response

        viewModel.changePinV2("", "")

        Assert.assertEquals(response.mutatePinV2data.errorAddChangePinData[0].message, (viewModel.changePinResponse.value as Fail).throwable.message)
    }

    @Test
    fun `change pin v2 throw exception`() {
        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } throws mockThrowable

        viewModel.changePinV2("", "")

        runBlocking {
            changePinObserver.onChanged(any<Fail>())
        }
    }

    @Test
    fun `change pin v2 other errors`() {
        val data = AddChangePinData(success = false)
        val response = UpdatePinV2Response(data)

        response.mutatePinV2data.errorAddChangePinData = listOf()

        val hashedPin = "abc1234b"

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { updatePinV2UseCase(any()) } returns response

        viewModel.changePinV2("", "")

        runBlocking {
            changePinObserver.onChanged(any<Fail>())
        }
    }

    @Test
    fun `reset pin v2 success`() {
        val data = AddChangePinData(success = true)
        val responseReset = ResetPinV2Response(data)

        coEvery { pinPreference.getTempPin() } returns "abc123"
        coEvery { resetPinV2UseCase(any()) } returns responseReset

        /* When */
        viewModel.resetPinV2("")

        coVerify {
            resetPinObserver.onChanged(Success(data))
        }
    }

    @Test
    fun `reset pin v2 has error msg`() {
        val data = AddChangePinData(success = false)
        val response = ResetPinV2Response(data)

        response.mutatePinV2data.errorAddChangePinData = listOf(ErrorAddChangePinData(message = "error"))

        /* When */
        coEvery { resetPinV2UseCase(any()) } returns response

        viewModel.resetPinV2("")

        coVerify {
            resetPinObserver.onChanged(any<Fail>())
        }
    }

    @Test
    fun `reset pin v2 other errors`() {
        val data = AddChangePinData(success = false)
        val response = ResetPinV2Response(data)

        response.mutatePinV2data.errorAddChangePinData = listOf()

        /* When */
        coEvery { resetPinV2UseCase(any()) } returns response

        viewModel.resetPinV2("")

        assert((viewModel.resetPinResponse.value as Fail).throwable is RuntimeException)
        coVerify {
            resetPinObserver.onChanged(any<Fail>())
        }
    }

    @Test
    fun `reset pin v2 throw exception`() {
        /* When */
        coEvery { resetPinV2UseCase(any()) } throws mockThrowable

        viewModel.resetPinV2("")

        assert((viewModel.resetPinResponse.value as Fail).throwable == mockThrowable)
        coVerify {
            resetPinObserver.onChanged(any<Fail>())
        }
    }

    @Test
    fun `validate pin v2 success` () {

        val data = ValidatePinV2Data()
        val response = ValidatePinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { validatePinV2UseCase(any()) } returns response

        viewModel.validatePinV2("")

        verify {
            validatePinV2Observer.onChanged(Success(data))
        }
    }

    @Test
    fun `validate pin v2 throw exception` () {
        /* When */
        coEvery { generatePublicKeyUseCase.executeOnBackground() } throws mockThrowable

        viewModel.validatePinV2("")

        assert((viewModel.validatePinV2Response.value as Fail).throwable == mockThrowable)
        verify {
            validatePinV2Observer.onChanged(any<Fail>())
        }
    }

    @Test
    fun `check pin v2 success` () {
        val data = CheckPinV2Data(valid = true)
        val response = CheckPinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns response

        viewModel.checkPinV2("")

        verify {
            checkPinV2Observer.onChanged(Success(data))
        }
    }

    @Test
    fun `check pin v2 has error msg` () {
        val data = CheckPinV2Data(valid = false, errorMessage = "error")
        val response = CheckPinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns response

        viewModel.checkPinV2("")

        verify {
            checkPinV2Observer.onChanged(Success(data))
        }
    }

    @Test
    fun `check pin v2 throw exception` () {
        /* When */
        coEvery { generatePublicKeyUseCase.executeOnBackground() } throws mockThrowable

        viewModel.checkPinV2("")

        assert((viewModel.checkPinV2Response.value as Fail).throwable == mockThrowable)
        verify {
            checkPinV2Observer.onChanged(any<Fail>())
        }
    }

    @Test
    fun `check pin v2 other errors` () {
        val data = CheckPinV2Data(valid = false)
        val response = CheckPinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns response

        viewModel.checkPinV2("")

        assert((viewModel.checkPinV2Response.value as Fail).throwable is RuntimeException)
        verify {
            checkPinV2Observer.onChanged(any<Fail>())
        }
    }

    @Test
    fun `validate pin mediator need hash` () {
        val pinData = PinStatusData(isNeedHash = true)
        val pinResp = PinStatusResponse(pinData)

        val data = ValidatePinV2Data()
        val response = ValidatePinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { userSession.userId } returns "12345"
        coEvery { checkPinHashV2UseCase(any()) } returns pinResp

        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { validatePinV2UseCase(any()) } returns response

        viewModel.validatePinMediator(pin)

        verify {
            userSession.userId
            validatePinV2Observer.onChanged(Success(data))
        }
    }

    @Test
    fun `validate pin mediator need no hash` () {
        val data = PinStatusData(isNeedHash = false)
        val resp = PinStatusResponse(data)
        val pin = "123456"
        validatePinPojo.data.valid = true

        /* When */
        every { userSession.userId } returns "12345"
        coEvery { checkPinHashV2UseCase(any()) } returns resp

        /* When */
        every { validatePinUseCase.execute(any(), any()) } answers {
            firstArg<(ValidatePinPojo) -> Unit>().invoke(validatePinPojo)
        }

        viewModel.validatePinMediator(pin)

        verify {
            userSession.userId
            validatePinObserver.onChanged(Success(validatePinPojo.data))
        }
    }

    @Test
    fun `validate pin mediator throw exception` () {
        coEvery { checkPinHashV2UseCase(any()) } throws mockThrowable

        viewModel.validatePinMediator(pin)

        assert((viewModel.validatePinResponse.value as Fail).throwable == mockThrowable)
        verify {
            validatePinObserver.onChanged(any<Fail>())
        }
    }

}