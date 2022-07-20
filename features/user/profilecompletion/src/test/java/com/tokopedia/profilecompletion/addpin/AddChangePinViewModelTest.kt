package com.tokopedia.profilecompletion.addpin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.addpin.data.usecase.CreatePinV2UseCase
import com.tokopedia.profilecompletion.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.profilecompletion.changepin.data.model.CreatePinV2Response
import com.tokopedia.profilecompletion.changepin.data.model.ErrorPinModel
import com.tokopedia.profilecompletion.changepin.data.model.MutatePinV2Data
import com.tokopedia.profilecompletion.common.PinPreference
import com.tokopedia.profilecompletion.common.model.CheckPinV2Data
import com.tokopedia.profilecompletion.common.model.CheckPinV2Response
import com.tokopedia.profilecompletion.common.usecase.CheckPinV2UseCase
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Created by Yoris Prayogo on 28/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
@ExperimentalCoroutinesApi
class AddChangePinViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val addPinUseCase = mockk<GraphqlUseCase<AddPinPojo>>(relaxed = true)
    val checkPinUseCase = mockk<GraphqlUseCase<CheckPinPojo>>(relaxed = true)
    val getStatusPinUseCase = mockk<GraphqlUseCase<StatusPinPojo>>(relaxed = true)
    val validatePinUseCase = mockk<GraphqlUseCase<ValidatePinPojo>>(relaxed = true)
    val skipOtpPinUseCase = mockk<GraphqlUseCase<SkipOtpPinPojo>>(relaxed = true)
    val checkPinV2UseCase = mockk<CheckPinV2UseCase>(relaxed = true)
    val generatePublicKeyUseCase = mockk<GeneratePublicKeyUseCase>(relaxed = true)
    val createPinV2UseCase = mockk<CreatePinV2UseCase>(relaxed = true)
    val pinPreference = mockk<PinPreference>(relaxed = true)

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
    private var mutatePinV2Observer = mockk<Observer<Result<MutatePinV2Data>>>(relaxed = true)
    private var checkPinV2Observer = mockk<Observer<Result<CheckPinV2Data>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = AddChangePinViewModel(
            addPinUseCase,
            createPinV2UseCase,
            checkPinUseCase,
            checkPinV2UseCase,
            getStatusPinUseCase,
            validatePinUseCase,
            skipOtpPinUseCase,
            rawQueries,
            generatePublicKeyUseCase,
            pinPreference,
            CoroutineTestDispatchersProvider
        )

        viewModel.addPinResponse.observeForever(addPinObserver)
        viewModel.checkPinResponse.observeForever(checkPinObserver)
        viewModel.getStatusPinResponse.observeForever(getStatusPinObserver)
        viewModel.validatePinResponse.observeForever(validatePinObserver)
        viewModel.skipOtpPinResponse.observeForever(skipOtpPinObserver)
        viewModel.mutatePin.observeForever(mutatePinV2Observer)
        viewModel.checkPinV2Response.observeForever(checkPinV2Observer)
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
            addPinUseCase.setGraphqlQuery(any<String>())
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
        Assert.assertThat(
            (viewModel.addPinResponse.value as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(
            addPinPojo.data.errorAddChangePinData[0].message,
            (viewModel.addPinResponse.value as Fail).throwable.message
        )
        verify(atLeast = 1) { addPinObserver.onChanged(any()) }
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
        Assert.assertThat(
            (viewModel.addPinResponse.value as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        verify(atLeast = 1) { addPinObserver.onChanged(any()) }
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
        Assert.assertThat(
            viewModel.checkPinResponse.value,
            CoreMatchers.instanceOf(Fail::class.java)
        )
        Assert.assertThat(
            (viewModel.checkPinResponse.value as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        verify(atLeast = 1) { checkPinObserver.onChanged(any()) }
    }

    @Test
    fun `on getStatusPin executed`() {

        viewModel.getStatusPin()

        /* Then */
        verify {
            viewModel.loadingState.postValue(true)
            getStatusPinUseCase.setTypeClass(any())
            getStatusPinUseCase.setGraphqlQuery(any<String>())
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
        Assert.assertThat(
            viewModel.getStatusPinResponse.value,
            CoreMatchers.instanceOf(Fail::class.java)
        )
        Assert.assertThat(
            (viewModel.getStatusPinResponse.value as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(
            getStatusPinPojo.data.errorMessage,
            (viewModel.getStatusPinResponse.value as Fail).throwable.message
        )
        verify(atLeast = 1) { getStatusPinObserver.onChanged(any()) }
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
        Assert.assertThat(
            viewModel.validatePinResponse.value,
            CoreMatchers.instanceOf(Fail::class.java)
        )
        Assert.assertThat(
            (viewModel.validatePinResponse.value as Fail).throwable,
            CoreMatchers.instanceOf(RuntimeException::class.java)
        )
        verify(atLeast = 1) { validatePinObserver.onChanged(any()) }
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
            skipOtpPinUseCase.setGraphqlQuery(any<String>())
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
        Assert.assertThat(
            viewModel.skipOtpPinResponse.value,
            CoreMatchers.instanceOf(Fail::class.java)
        )
        Assert.assertThat(
            (viewModel.skipOtpPinResponse.value as Fail).throwable,
            CoreMatchers.instanceOf(MessageErrorException::class.java)
        )
        Assert.assertEquals(
            skipOtpPinPojo.data.errorMessage,
            (viewModel.skipOtpPinResponse.value as Fail).throwable.message
        )
        verify(atLeast = 1) { skipOtpPinObserver.onChanged(any()) }
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
			mutatePinV2Observer.onChanged(Success(mockData))
		}
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
        Assert.assertThat(viewModel.mutatePin.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertEquals(msg, (viewModel.mutatePin.value as Fail).throwable.message)
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
        Assert.assertThat(viewModel.mutatePin.value, CoreMatchers.instanceOf(Fail::class.java))
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
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns checkPinV2Response

        viewModel.checkPinV2("123456")

        /* Then */
        assertTrue((viewModel.checkPinV2Response.value as Success).data.valid)
        verify {
			pinPreference.clearTempPin()
			pinPreference.setTempPin(pinToken)
			checkPinV2Observer.onChanged(any<Success<CheckPinV2Data>>())
		}
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
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns checkPinV2Response

        viewModel.checkPinV2("123456")

        /* Then */
        assert((viewModel.checkPinV2Response.value as Success).data.errorMessage == errorMsg)
        verify { checkPinV2Observer.onChanged(any<Success<CheckPinV2Data>>()) }
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
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { checkPinV2UseCase(any()) } returns checkPinV2Response

        viewModel.checkPinV2("123456")

        /* Then */
        verify { checkPinV2Observer.onChanged(any<Fail>()) }
    }

    @Test
    fun `Success get pub key`() {
        val mocKeyData = KeyData("abc", "bca", "aaa")
        val generateKeyResponse = GenerateKeyPojo(mocKeyData)
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyResponse

        runBlocking {
            assert(viewModel.getPublicKey() == mocKeyData)
        }
        coVerify {
            generatePublicKeyUseCase.setParams("pinv2")
            generatePublicKeyUseCase.executeOnBackground()
        }
    }

    @Test
    fun `add pin v2 - throw exception`() {
        coEvery { createPinV2UseCase(any()).mutatePinV2data } throws mockThrowable
        viewModel.addPinV2("12345")
        assert((viewModel.mutatePin.value as Fail).throwable == mockThrowable)
        verify { mutatePinV2Observer.onChanged(any<Fail>()) }
    }

    @Test
    fun `check pin v2 - throw exception`() {
        val hashedPin = "abc1234b"

        val checkPinData = CheckPinV2Data(valid = true)
        val checkPinV2Response = CheckPinV2Response(checkPinData)

        mockkObject(RsaUtils)
        every { RsaUtils.encryptWithSalt(any(), any(), any()) } returns hashedPin

        /* When */
        coEvery { generatePublicKeyUseCase.executeOnBackground() } throws mockThrowable
        coEvery { checkPinV2UseCase(any()) } returns checkPinV2Response

        viewModel.checkPinV2("123456")

        /* Then */
        assert((viewModel.checkPinV2Response.value as Fail).throwable == mockThrowable)
        verify { checkPinV2Observer.onChanged(any<Fail>()) }
    }
}