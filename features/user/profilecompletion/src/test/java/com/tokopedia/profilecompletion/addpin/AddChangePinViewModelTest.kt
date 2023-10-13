package com.tokopedia.profilecompletion.addpin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.AddPinPojo
import com.tokopedia.profilecompletion.addpin.data.CheckPinPojo
import com.tokopedia.profilecompletion.addpin.data.ErrorAddChangePinData
import com.tokopedia.profilecompletion.addpin.data.SkipOtpPinPojo
import com.tokopedia.profilecompletion.addpin.data.StatusPinPojo
import com.tokopedia.profilecompletion.addpin.data.ValidatePinPojo
import com.tokopedia.profilecompletion.addpin.data.usecase.CreatePinV2UseCase
import com.tokopedia.profilecompletion.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.profilecompletion.changepin.data.model.CreatePinV2Response
import com.tokopedia.profilecompletion.changepin.data.model.ErrorPinModel
import com.tokopedia.profilecompletion.changepin.data.model.MutatePinV2Data
import com.tokopedia.profilecompletion.common.PinPreference
import com.tokopedia.profilecompletion.common.model.CheckPinV2Data
import com.tokopedia.profilecompletion.common.model.CheckPinV2Response
import com.tokopedia.profilecompletion.common.usecase.CheckPinV2UseCase
import com.tokopedia.profilecompletion.domain.CheckPinUseCase
import com.tokopedia.profilecompletion.domain.CreatePinUseCase
import com.tokopedia.profilecompletion.domain.SkipOtpPinUseCase
import com.tokopedia.profilecompletion.domain.StatusPinUseCase
import com.tokopedia.profilecompletion.domain.ValidatePinUseCase
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by Yoris Prayogo on 28/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
@ExperimentalCoroutinesApi
class AddChangePinViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val checkPinV2UseCase = mockk<CheckPinV2UseCase>(relaxed = true)
    val createPinUseCase = mockk<CreatePinUseCase>(relaxed = true)
    val checkPinUseCase = mockk<CheckPinUseCase>(relaxed = true)
    val createPinV2UseCase = mockk<CreatePinV2UseCase>(relaxed = true)
    val statusPinUseCase = mockk<StatusPinUseCase>(relaxed = true)
    val validatePinUseCase = mockk<ValidatePinUseCase>(relaxed = true)
    val skipOtpPinUseCase = mockk<SkipOtpPinUseCase>(relaxed = true)
    val pinPreference = mockk<PinPreference>(relaxed = true)
    val generatePublicKeyUseCase = mockk<GeneratePublicKeyUseCase>(relaxed = true)

    lateinit var viewModel: AddChangePinViewModel

    @Before
    fun setUp() {
        viewModel = AddChangePinViewModel(
            createPinUseCase,
            createPinV2UseCase,
            checkPinUseCase,
            checkPinV2UseCase,
            statusPinUseCase,
            validatePinUseCase,
            skipOtpPinUseCase,
            generatePublicKeyUseCase,
            pinPreference,
            CoroutineTestDispatchersProvider
        )
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
    fun `on Success Add Pin`() {
        /* When */
        addPinPojo.data.success = true

        coEvery { createPinUseCase(any()) } returns addPinPojo

        viewModel.addPin(token)

        /* Then */
        val result = viewModel.addPinResponse.getOrAwaitValue()
        assertEquals(Success(addPinPojo.data), result)
    }

    @Test
    fun `on Error Add Pin`() {
        /* When */

        coEvery { createPinUseCase(any()) } throws mockThrowable

        viewModel.addPin(token)

        /* Then */
        val result = viewModel.addPinResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Add Pin message not empty`() {
        /* When */
        addPinPojo.data.errorAddChangePinData = listOf(ErrorAddChangePinData(message = "Error"))

        coEvery { createPinUseCase(any()) } returns addPinPojo

        viewModel.addPin(token)

        /* Then */
        val result = viewModel.addPinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(
            addPinPojo.data.errorAddChangePinData[0].message,
            (result as Fail).throwable.message
        )
        coVerify(atLeast = 1) { createPinUseCase(any()) }
    }

    @Test
    fun `on another Error Add Pin`() {
        /* When */
        addPinPojo.data.success = false

        coEvery { createPinUseCase(any()) } returns addPinPojo

        viewModel.addPin(token)

        /* Then */
        val result = viewModel.addPinResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        coVerify(atLeast = 1) { createPinUseCase(any()) }
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
        Assert.assertThat(
            result,
            CoreMatchers.instanceOf(Fail::class.java)
        )
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        coVerify(atLeast = 1) { checkPinUseCase(any()) }
    }

    @Test
    fun `on Success Get Status Pin`() {
        /* When */

        coEvery { statusPinUseCase(Unit) } returns getStatusPinPojo

        viewModel.getStatusPin()

        /* Then */
        val result = viewModel.getStatusPinResponse.getOrAwaitValue()
        assertEquals(Success(getStatusPinPojo.data), result)
    }

    @Test
    fun `on Error Get Status Pin`() {
        /* When */

        coEvery { statusPinUseCase(Unit) } throws mockThrowable

        viewModel.getStatusPin()

        /* Then */
        val result = viewModel.getStatusPinResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Get Status Pin message not empty`() {
        /* When */
        getStatusPinPojo.data.errorMessage = "Error"

        coEvery { statusPinUseCase(Unit) } returns getStatusPinPojo

        viewModel.getStatusPin()

        /* Then */
        val result = viewModel.getStatusPinResponse.getOrAwaitValue()
        Assert.assertThat(
            result,
            CoreMatchers.instanceOf(Fail::class.java)
        )
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(
            getStatusPinPojo.data.errorMessage,
            (result as Fail).throwable.message
        )
        coVerify(atLeast = 1) { statusPinUseCase(Unit) }
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
        Assert.assertThat(
            result,
            CoreMatchers.instanceOf(Fail::class.java)
        )
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        coVerify(atLeast = 1) { validatePinUseCase(any()) }
    }

    @Test
    fun `on Success Check Skip Pin`() {
        /* When */

        coEvery { skipOtpPinUseCase(any()) } returns skipOtpPinPojo

        viewModel.checkSkipOtpPin("validateToken")

        /* Then */
        val result = viewModel.skipOtpPinResponse.getOrAwaitValue()
        assertEquals(Success(skipOtpPinPojo.data), result)
    }

    @Test
    fun `on Error Check Skip Pin`() {
        /* When */

        coEvery { skipOtpPinUseCase(any()) } throws mockThrowable

        viewModel.checkSkipOtpPin("validateToken")

        /* Then */
        val result = viewModel.skipOtpPinResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Check Skip Pin message not empty`() {
        /* When */
        skipOtpPinPojo.data.errorMessage = "Error"

        coEvery { skipOtpPinUseCase(any()) } returns skipOtpPinPojo

        viewModel.checkSkipOtpPin("validateToken")

        /* Then */
        val result = viewModel.skipOtpPinResponse.getOrAwaitValue()
        Assert.assertThat(
            result,
            CoreMatchers.instanceOf(Fail::class.java)
        )
        Assert.assertThat(
            (result as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(
            skipOtpPinPojo.data.errorMessage,
            (result as Fail).throwable.message
        )
        coVerify(atLeast = 1) { skipOtpPinUseCase(any()) }
    }

    @Test
    fun `on Success Create Pin V2`() {
        val mockData = MutatePinV2Data(success = true)
        val mockResponse = CreatePinV2Response(mockData)

        /* When */
        coEvery { pinPreference.getTempPin() } returns "123456"
        coEvery { createPinV2UseCase(any()) } returns mockResponse

        viewModel.addPinV2("")

        /* Then */
        verify {
            pinPreference.getTempPin()
            pinPreference.clearTempPin()
        }
        val result = viewModel.mutatePin.getOrAwaitValue()
        assertEquals(Success(mockData), result)
    }

    @Test
    fun `on Error Create Pin V2 - has Error message`() {
        val msg = "error"
        val listOfError = listOf(ErrorPinModel("", msg))
        val mockData = MutatePinV2Data(success = false, errors = listOfError)
        val mockResponse = CreatePinV2Response(mockData)

        /* When */
        coEvery { createPinV2UseCase(any()) } returns mockResponse

        viewModel.addPinV2("")

        /* Then */
        val result = viewModel.mutatePin.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertEquals(msg, (result as Fail).throwable.message)
    }

    @Test
    fun `on Error Create Pin V2 - others error`() {
        val listOfError = listOf<ErrorPinModel>()
        val mockData = MutatePinV2Data(success = false, errors = listOfError)
        val mockResponse = CreatePinV2Response(mockData)

        /* When */
        coEvery { createPinV2UseCase(any()) } returns mockResponse

        viewModel.addPinV2("")

        /* Then */
        val result = viewModel.mutatePin.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
    }

    @Test
    fun `check pin v2 - success`() {
        val hashedPin = "abc1234b"
        val pinToken = "abc12"

        val checkPinData = CheckPinV2Data(valid = true, pinToken = pinToken)
        val checkPinV2Response = CheckPinV2Response(checkPinData)

        mockkObject(RsaUtils)
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns checkPinV2Response

        viewModel.checkPinV2("123456")

        /* Then */
        val result = viewModel.checkPinV2Response.getOrAwaitValue()
        assertTrue((result as Success).data.valid)
        verify {
            pinPreference.clearTempPin()
            pinPreference.setTempPin(pinToken)
        }
        assertEquals(checkPinV2Response.data, result.data)
    }

    @Test
    fun `check pin v2 - has errors`() {
        val hashedPin = "abc1234b"

        val errorMsg = "error"
        val checkPinData = CheckPinV2Data(valid = false, errorMessage = errorMsg)
        val checkPinV2Response = CheckPinV2Response(checkPinData)

        mockkObject(RsaUtils)
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns checkPinV2Response

        viewModel.checkPinV2("123456")

        /* Then */
        val result = viewModel.checkPinV2Response.getOrAwaitValue()
        assertEquals((result as Success).data.errorMessage, errorMsg)
        assertEquals(Success(checkPinV2Response.data), result)
    }

    @Test
    fun `check pin v2 - has other errors`() {
        val hashedPin = "abc1234b"

        val checkPinData = CheckPinV2Data(valid = false, errorMessage = "")
        val checkPinV2Response = CheckPinV2Response(checkPinData)

        mockkObject(RsaUtils)
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin

        val keydata = KeyData(hash = "abc123")
        val generateKeyPojo = GenerateKeyPojo(keydata)

        /* When */
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns checkPinV2Response

        viewModel.checkPinV2("123456")

        /* Then */
        val result = viewModel.checkPinV2Response.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `Success get pub key`() {
        val mocKeyData = KeyData("abc", "bca", "aaa")
        val generateKeyResponse = GenerateKeyPojo(mocKeyData)
        coEvery { generatePublicKeyUseCase.invoke(any()) } returns generateKeyResponse

        runBlocking {
            assert(viewModel.getPublicKey() == mocKeyData)
        }
        coVerify {
            generatePublicKeyUseCase("pinv2")
        }
    }

    @Test
    fun `add pin v2 - throw exception`() {
        coEvery { createPinV2UseCase(any()).mutatePinV2data } throws mockThrowable
        viewModel.addPinV2("12345")
        val result = viewModel.mutatePin.getOrAwaitValue()
        assertEquals((result as Fail).throwable, mockThrowable)
    }

    @Test
    fun `check pin v2 - throw exception`() {
        val hashedPin = "abc1234b"

        val checkPinData = CheckPinV2Data(valid = true)
        val checkPinV2Response = CheckPinV2Response(checkPinData)

        mockkObject(RsaUtils)
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin

        /* When */
        coEvery { generatePublicKeyUseCase.invoke(any()) } throws mockThrowable
        coEvery { checkPinV2UseCase(any()) } returns checkPinV2Response

        viewModel.checkPinV2("123456")

        /* Then */
        val result = viewModel.checkPinV2Response.getOrAwaitValue()
        assertEquals((result as Fail).throwable, mockThrowable)
    }
}
