package com.tokopedia.privacycenter.dsar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.privacycenter.data.AddEmailResponse
import com.tokopedia.privacycenter.data.DsarCheckEmailResponse
import com.tokopedia.privacycenter.data.UserProfileCompletionUpdateEmail
import com.tokopedia.privacycenter.data.UserProfileCompletionValidate
import com.tokopedia.privacycenter.domain.DsarAddEmailUseCase
import com.tokopedia.privacycenter.domain.DsarCheckEmailUseCase
import com.tokopedia.privacycenter.ui.dsar.addemail.DsarAddEmailViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DsarAddEmailViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    lateinit var viewModel: DsarAddEmailViewModel

    val dsarCheckEmailUseCase = mockk<DsarCheckEmailUseCase>(relaxed = true)
    val dsarAddEmailUseCase = mockk<DsarAddEmailUseCase>(relaxed = true)

    val routeToSuccess = mockk<Observer<Void>>(relaxed = true)

    val testEmail = "yoris.prayogo@tokopedia.com"

    val testException = Exception("error")

    @Before
    fun setup() {
        viewModel = DsarAddEmailViewModel(dsarCheckEmailUseCase, dsarAddEmailUseCase, dispatcherProviderTest)
        viewModel.routeToSuccessPage.observeForever(routeToSuccess)
    }

    @Test
    fun `checkEmail success - valid`() {
        val mockData = UserProfileCompletionValidate(isValid = true)
        val mockResponse = DsarCheckEmailResponse(data = mockData)

        coEvery { dsarCheckEmailUseCase(any()) } returns mockResponse

        viewModel.checkEmail(testEmail)

        val addEmailResult = viewModel.addEmailModel.getOrAwaitValue()
        val routeToVerification = viewModel.routeToVerification.getOrAwaitValue()

        assert(!addEmailResult.btnLoading)
        assert(addEmailResult.inputText == testEmail)
        assert(addEmailResult.inputError.isEmpty())
        assert(routeToVerification == testEmail)
    }

    @Test
    fun `checkEmail success - is not valid`() {
        val mockData = UserProfileCompletionValidate(isValid = true)
        val mockResponse = DsarCheckEmailResponse(data = mockData)

        coEvery { dsarCheckEmailUseCase(any()) } returns mockResponse

        viewModel.checkEmail(testEmail)

        val addEmailResult = viewModel.addEmailModel.getOrAwaitValue()

        assert(!addEmailResult.btnLoading)
        assert(addEmailResult.inputText == testEmail)
        assert(addEmailResult.inputError.isEmpty())
    }

    @Test
    fun `checkEmail failed - exception`() {
        coEvery { dsarCheckEmailUseCase(any()) } throws testException

        viewModel.checkEmail(testEmail)

        val addEmailResult = viewModel.addEmailModel.getOrAwaitValue()

        assert(!addEmailResult.btnLoading)
        assert(addEmailResult.inputText.isEmpty())
        assert(addEmailResult.inputError == testException.message)
    }

    @Test
    fun `addEmail success - success`() {
        val mockData = UserProfileCompletionUpdateEmail(isSuccess = true)
        val mockResponse = AddEmailResponse(data = mockData)

        coEvery { dsarAddEmailUseCase(any()) } returns mockResponse

        viewModel.addEmail(testEmail, "", "")

        verify {
            routeToSuccess.onChanged(any())
        }
    }

    @Test
    fun `addEmail success - has error`() {
        val error = "error message"
        val mockData = UserProfileCompletionUpdateEmail(isSuccess = true, errorMessage = error)
        val mockResponse = AddEmailResponse(data = mockData)

        coEvery { dsarAddEmailUseCase(any()) } returns mockResponse

        viewModel.addEmail(testEmail, "", "")

        val addEmailResult = viewModel.addEmailModel.getOrAwaitValue()

        assert(!addEmailResult.btnLoading)
        assert(addEmailResult.inputText.isEmpty())
        assert(addEmailResult.inputError == error)
    }

    @Test
    fun `addEmail success - Exception`() {
        coEvery { dsarAddEmailUseCase(any()) } throws testException

        viewModel.addEmail(testEmail, "", "")

        val addEmailResult = viewModel.addEmailModel.getOrAwaitValue()

        assert(!addEmailResult.btnLoading)
        assert(addEmailResult.inputText.isEmpty())
        assert(addEmailResult.inputError.isEmpty())
    }
}
