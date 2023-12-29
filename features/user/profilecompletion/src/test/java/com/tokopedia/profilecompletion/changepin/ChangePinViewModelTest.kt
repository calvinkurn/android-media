package com.tokopedia.profilecompletion.changepin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData
import com.tokopedia.profilecompletion.addpin.data.ChangePinPojo
import com.tokopedia.profilecompletion.addpin.data.CheckPinPojo
import com.tokopedia.profilecompletion.addpin.data.ErrorAddChangePinData
import com.tokopedia.profilecompletion.addpin.data.ValidatePinPojo
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
import com.tokopedia.profilecompletion.domain.ChangePinUseCase
import com.tokopedia.profilecompletion.domain.CheckPin2FaUseCase
import com.tokopedia.profilecompletion.domain.CheckPinUseCase
import com.tokopedia.profilecompletion.domain.ResetPin2FaUseCase
import com.tokopedia.profilecompletion.domain.ResetPinUseCase
import com.tokopedia.profilecompletion.domain.ValidatePinUseCase
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.pin.PinStatusData
import com.tokopedia.sessioncommon.data.pin.PinStatusResponse
import com.tokopedia.sessioncommon.data.pin.ValidatePinV2Data
import com.tokopedia.sessioncommon.data.pin.ValidatePinV2Response
import com.tokopedia.sessioncommon.domain.usecase.CheckPinHashV2UseCase
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.ValidatePinV2UseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ChangePinViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: ChangePinViewModel

    val validatePinUseCase = mockk<ValidatePinUseCase>(relaxed = true)
    val checkPinUseCase = mockk<CheckPinUseCase>(relaxed = true)
    val checkPin2FaUseCase = mockk<CheckPin2FaUseCase>(relaxed = true)
    val resetPinUseCase = mockk<ResetPinUseCase>(relaxed = true)
    val resetPinV2UseCase = mockk<ResetPinV2UseCase>(relaxed = true)
    val resetPin2FaUseCase = mockk<ResetPin2FaUseCase>(relaxed = true)
    val changePinUseCase = mockk<ChangePinUseCase>(relaxed = true)
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
            checkPin2FaUseCase,
            resetPinUseCase,
            resetPinV2UseCase,
            resetPin2FaUseCase,
            changePinUseCase,
            updatePinV2UseCase,
            checkPinV2UseCase,
            userSession,
            checkPinHashV2UseCase,
            generatePublicKeyUseCase,
            pinPreference,
            CoroutineTestDispatchersProvider
        )
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
    fun `on Success Reset Pin`() {
        /* When */
        resetPinResponse.data.success = true

        coEvery { resetPinUseCase(any()) } returns resetPinResponse

        viewModel.resetPin(token)

        /* Then */
        val result = viewModel.resetPinResponse.getOrAwaitValue()
        assertEquals(Success(resetPinResponse.data), result)
    }

    @Test
    fun `on Error Reset Pin`() {
        /* When */

        coEvery { resetPinUseCase(any()) } throws mockThrowable

        viewModel.resetPin(token)

        /* Then */
        val result = viewModel.resetPinResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Reset Pin message not empty`() {
        /* When */
        resetPinResponse.data.errorAddChangePinData =
            listOf(ErrorAddChangePinData(message = "Error"))

        coEvery { resetPinUseCase(any()) } returns resetPinResponse

        viewModel.resetPin(token)

        /* Then */
        val result = viewModel.resetPinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(
            resetPinResponse.data.errorAddChangePinData[0].message,
            (result as Fail).throwable.message
        )
        coVerify(atLeast = 1) { resetPinUseCase(any()) }
    }

    @Test
    fun `on another Error Add Pin`() {
        /* When */
        resetPinResponse.data.success = false

        coEvery { resetPinUseCase(any()) } returns resetPinResponse

        viewModel.resetPin(token)

        /* Then */
        val result = viewModel.resetPinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        coVerify(atLeast = 1) { resetPinUseCase(any()) }
    }

    @Test
    fun `on Success Check Pin`() {
        /* When */
        checkPinPojo.data.valid = true

        coEvery { checkPinUseCase(any()) } returns checkPinPojo

        viewModel.checkPin(pin)

        /* Then */
        val result = viewModel.checkPinResponse.getOrAwaitValue()
        assertEquals(Success(checkPinPojo.data), result)
    }

    @Test
    fun `on Error Check Pin`() {
        /* When */

        coEvery { checkPinUseCase(any()) } throws mockThrowable

        viewModel.checkPin(pin)

        /* Then */
        val result = viewModel.checkPinResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Success Check Pin message not empty`() {
        /* When */
        checkPinPojo.data.errorMessage = "Error"

        coEvery { checkPinUseCase(any()) } returns checkPinPojo

        viewModel.checkPin(pin)

        /* Then */
        val result = viewModel.checkPinResponse.getOrAwaitValue()
        assertEquals(Success(checkPinPojo.data), result)
    }

    @Test
    fun `on another Error Check Pin`() {
        /* When */
        checkPinPojo.data.valid = false

        coEvery { checkPinUseCase(any()) } returns checkPinPojo

        viewModel.checkPin(pin)

        /* Then */
        val result = viewModel.checkPinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        coVerify(atLeast = 1) { checkPinUseCase(any()) }
    }

    @Test
    fun `on Success Validate Pin`() {
        /* When */
        validatePinPojo.data.valid = true

        coEvery { validatePinUseCase(any()) } returns validatePinPojo

        viewModel.validatePin(pin)

        /* Then */
        val result = viewModel.validatePinResponse.getOrAwaitValue()
        assertEquals(Success(validatePinPojo.data), result)
    }

    @Test
    fun `on Error Validate Pin`() {
        /* When */

        coEvery { validatePinUseCase(any()) } throws mockThrowable

        viewModel.validatePin(pin)

        /* Then */
        val result = viewModel.validatePinResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Validate Pin message not empty`() {
        /* When */
        validatePinPojo.data.errorMessage = "Error"

        coEvery { validatePinUseCase(any()) } returns validatePinPojo

        viewModel.validatePin(pin)

        /* Then */
        val result = viewModel.validatePinResponse.getOrAwaitValue()
        assertEquals(Success(validatePinPojo.data), result)
    }

    @Test
    fun `on Another Error Validate Pin`() {
        /* When */
        validatePinPojo.data.valid = false

        coEvery { validatePinUseCase(any()) } returns validatePinPojo

        viewModel.validatePin(pin)

        /* Then */
        val result = viewModel.validatePinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        coVerify(atLeast = 1) { validatePinUseCase(any()) }
    }

    @Test
    fun `on Success Change Pin`() {
        /* When */
        changePinPojo.data.success = true

        coEvery { changePinUseCase(any()) } returns changePinPojo

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        val result = viewModel.changePinResponse.getOrAwaitValue()
        assertEquals(Success(changePinPojo.data), result)
    }

    @Test
    fun `on Error Change Pin`() {
        /* When */

        coEvery { changePinUseCase(any()) } throws mockThrowable

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        val result = viewModel.changePinResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Change Pin message not empty`() {
        /* When */
        changePinPojo.data.errorAddChangePinData = listOf(ErrorAddChangePinData(message = "Error"))

        coEvery { changePinUseCase(any()) } returns changePinPojo

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        val result = viewModel.changePinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(
            changePinPojo.data.errorAddChangePinData[0].message,
            (result as Fail).throwable.message
        )
        coVerify(atLeast = 1) { changePinUseCase(any()) }
    }

    @Test
    fun `on another Error Change Pin`() {
        /* When */
        changePinPojo.data.success = false

        coEvery { changePinUseCase(any()) } returns changePinPojo

        viewModel.changePin(pin, pinConfirm, pinOld)

        /* Then */
        val result = viewModel.changePinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        coVerify(atLeast = 1) { changePinUseCase(any()) }
    }

    val validateToken = "abc123"
    val userId = "1234"

    @Test
    fun `on Success Check Pin 2FA`() {
        /* When */
        checkPinPojo.data.valid = true

        coEvery { checkPin2FaUseCase(any()) } returns checkPinPojo

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        val result = viewModel.checkPinResponse.getOrAwaitValue()
        assertEquals(Success(checkPinPojo.data), result)
    }

    @Test
    fun `on Error Check Pin 2FA`() {
        /* When */

        coEvery { checkPin2FaUseCase(any()) } throws mockThrowable

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        val result = viewModel.checkPinResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Success Check Pin 2FA message not empty`() {
        /* When */
        checkPinPojo.data.errorMessage = "Error"

        coEvery { checkPin2FaUseCase(any()) } returns checkPinPojo

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        val result = viewModel.checkPinResponse.getOrAwaitValue()
        assertEquals(Success(checkPinPojo.data), result)
    }

    @Test
    fun `on another Error Check Pin 2FA`() {
        /* When */
        checkPinPojo.data.valid = false

        coEvery { checkPin2FaUseCase(any()) } returns checkPinPojo

        viewModel.checkPin2FA(pin, validateToken, userId)

        /* Then */
        val result = viewModel.checkPinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        coVerify(atLeast = 1) { checkPin2FaUseCase(any()) }
    }

    var resetPinData = ChangePin2FAData()
    var resetPin2FaPojo = ResetPin2FaPojo(data = resetPinData)

    @Test
    fun `on Success Reset Pin 2FA`() {
        /* When */
        resetPin2FaPojo.data.is_success = 1

        coEvery { resetPin2FaUseCase(any()) } returns resetPin2FaPojo

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        verify {
            userSession.setToken(any(), any())
        }
        val result = viewModel.resetPin2FAResponse.getOrAwaitValue()
        assertEquals(Success(resetPin2FaPojo.data), result)
    }

    @Test
    fun `on Success Reset Pin 2FA - other errors`() {
        /* When */
        resetPin2FaPojo.data.is_success = 0
        resetPin2FaPojo.data.error = ""

        coEvery { resetPin2FaUseCase(any()) } returns resetPin2FaPojo

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        val result = viewModel.resetPin2FAResponse.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `on Error Reset Pin 2FA`() {
        /* When */

        coEvery { resetPin2FaUseCase(any()) } throws mockThrowable

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        verify {
            userSession.setToken(any(), any())
        }
        val result = viewModel.resetPin2FAResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Reset Pin 2FA message not empty`() {
        /* When */
        resetPin2FaPojo.data.is_success = 0
        resetPin2FaPojo.data.error = "error"

        coEvery { resetPin2FaUseCase(any()) } returns resetPin2FaPojo

        viewModel.resetPin2FA(userId, validateToken)

        /* Then */
        val result = viewModel.resetPin2FAResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(resetPin2FaPojo.data.error, (result as Fail).throwable.message)
        coVerify(atLeast = 1) { resetPin2FaUseCase(any()) }
    }

    @Test
    fun `isNeedHash - success`() {
        val pinStatusData = PinStatusData(isNeedHash = true)
        val pinStatusResponse = PinStatusResponse(pinStatusData)

        coEvery { checkPinHashV2UseCase(any()) } returns pinStatusResponse
        runBlocking {
            assertTrue(viewModel.isNeedHash("", ""))
        }
    }

    @Test
    fun `validatePinV2 - true`() {
        val validatePinV2Data = ValidatePinV2Data(valid = true)
        val validatePinV2Response = ValidatePinV2Response(validatePinV2Data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { validatePinV2UseCase(any()) } returns validatePinV2Response

        viewModel.validatePinV2("123456")

        val result = viewModel.validatePinV2Response.getOrAwaitValue()
        assertEquals(Success(validatePinV2Data), result)
    }

    @Test
    fun `validatePinV2 - exception`() {
        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.invoke() } returns generateKeyPojo
        coEvery { validatePinV2UseCase(any()) } throws mockThrowable

        viewModel.validatePinV2("123456")

        val result = viewModel.validatePinV2Response.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `change pin v2 success`() {
        val data = AddChangePinData(success = true)
        val response = UpdatePinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { updatePinV2UseCase(any()) } returns response

        viewModel.changePinV2("", "")
        val result = viewModel.changePinResponse.getOrAwaitValue()
        assertEquals(Success(data), result)
    }

    @Test
    fun `change pin v2 has erros msg`() {
        val errMsg = "Error"
        val data = AddChangePinData(success = false)
        val response = UpdatePinV2Response(data)

        response.mutatePinV2data.errorAddChangePinData =
            listOf(ErrorAddChangePinData(message = errMsg))

        val hashedPin = "abc1234b"

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { updatePinV2UseCase(any()) } returns response

        viewModel.changePinV2("", "")

        val result = viewModel.changePinResponse.getOrAwaitValue()
        Assert.assertEquals(
            response.mutatePinV2data.errorAddChangePinData[0].message,
            (result as Fail).throwable.message
        )
    }

    @Test
    fun `change pin v2 throw exception`() {
        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } throws mockThrowable

        viewModel.changePinV2("", "")

        val result = viewModel.changePinResponse.getOrAwaitValue()
        Assert.assertTrue(result is Fail)
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
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { updatePinV2UseCase(any()) } returns response

        viewModel.changePinV2("", "")

        val result = viewModel.changePinResponse.getOrAwaitValue()
        Assert.assertTrue(result is Fail)
    }

    @Test
    fun `reset pin v2 success`() {
        val data = AddChangePinData(success = true)
        val responseReset = ResetPinV2Response(data)

        coEvery { pinPreference.getTempPin() } returns "abc123"
        coEvery { resetPinV2UseCase(any()) } returns responseReset

        /* When */
        viewModel.resetPinV2("")

        val result = viewModel.resetPinResponse.getOrAwaitValue()
        assertEquals(Success(data), result)
    }

    @Test
    fun `reset pin v2 has error msg`() {
        val data = AddChangePinData(success = false)
        val response = ResetPinV2Response(data)

        response.mutatePinV2data.errorAddChangePinData =
            listOf(ErrorAddChangePinData(message = "error"))

        /* When */
        coEvery { resetPinV2UseCase(any()) } returns response

        viewModel.resetPinV2("")

        val result = viewModel.resetPinResponse.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `reset pin v2 other errors`() {
        val data = AddChangePinData(success = false)
        val response = ResetPinV2Response(data)

        response.mutatePinV2data.errorAddChangePinData = listOf()

        /* When */
        coEvery { resetPinV2UseCase(any()) } returns response

        viewModel.resetPinV2("")

        val result = viewModel.resetPinResponse.getOrAwaitValue()
        assertTrue((result as Fail).throwable is RuntimeException)
    }

    @Test
    fun `reset pin v2 throw exception`() {
        /* When */
        coEvery { resetPinV2UseCase(any()) } throws mockThrowable

        viewModel.resetPinV2("")

        val result = viewModel.resetPinResponse.getOrAwaitValue()
        assertEquals((result as Fail).throwable, mockThrowable)
    }

    @Test
    fun `validate pin v2 success`() {
        val data = ValidatePinV2Data()
        val response = ValidatePinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { validatePinV2UseCase(any()) } returns response

        viewModel.validatePinV2("")

        val result = viewModel.validatePinV2Response.getOrAwaitValue()
        assertEquals(Success(data), result)
    }

    @Test
    fun `validate pin v2 throw exception`() {
        /* When */
        coEvery { generatePublicKeyUseCase.invoke(any()) } throws mockThrowable

        viewModel.validatePinV2("")

        val result = viewModel.validatePinV2Response.getOrAwaitValue()
        assertEquals((result as Fail).throwable, mockThrowable)
    }

    @Test
    fun `check pin v2 success`() {
        val data = CheckPinV2Data(valid = true)
        val response = CheckPinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns response

        viewModel.checkPinV2("")

        val result = viewModel.checkPinV2Response.getOrAwaitValue()
        assertEquals(Success(data), result)
    }

    @Test
    fun `check pin v2 has error msg`() {
        val data = CheckPinV2Data(valid = false, errorMessage = "error")
        val response = CheckPinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns response

        viewModel.checkPinV2("")

        val result = viewModel.checkPinV2Response.getOrAwaitValue()
        assertEquals(Success(data), result)
    }

    @Test
    fun `check pin v2 throw exception`() {
        /* When */
        coEvery { generatePublicKeyUseCase.invoke(any()) } throws mockThrowable

        viewModel.checkPinV2("")

        val result = viewModel.checkPinV2Response.getOrAwaitValue()
        assertEquals((result as Fail).throwable, mockThrowable)
    }

    @Test
    fun `check pin v2 other errors`() {
        val data = CheckPinV2Data(valid = false)
        val response = CheckPinV2Response(data)

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns response

        viewModel.checkPinV2("")

        val result = viewModel.checkPinV2Response.getOrAwaitValue()
        assertTrue((result as Fail).throwable is RuntimeException)
    }

    @Test
    fun `validate pin mediator need hash`() {
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
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { validatePinV2UseCase(any()) } returns response

        viewModel.validatePinMediator(pin)

        verify {
            userSession.userId
        }
        val result = viewModel.validatePinV2Response.getOrAwaitValue()
        assertEquals(Success(data), result)
    }

    @Test
    fun `validate pin mediator need no hash`() {
        val data = PinStatusData(isNeedHash = false)
        val resp = PinStatusResponse(data)
        val pin = "123456"
        validatePinPojo.data.valid = true

        /* When */
        every { userSession.userId } returns "12345"
        coEvery { checkPinHashV2UseCase(any()) } returns resp

        /* When */
        coEvery { validatePinUseCase(any()) } returns validatePinPojo

        viewModel.validatePinMediator(pin)

        verify {
            userSession.userId
        }
        val result = viewModel.validatePinResponse.getOrAwaitValue()
        assertEquals(Success(validatePinPojo.data), result)
    }

    @Test
    fun `validate pin mediator throw exception`() {
        coEvery { checkPinHashV2UseCase(any()) } throws mockThrowable

        viewModel.validatePinMediator(pin)

        val result = viewModel.validatePinResponse.getOrAwaitValue()
        assertEquals((result as Fail).throwable, mockThrowable)
    }
}
