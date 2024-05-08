package com.tokopedia.accountprofile.changephonenumber

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.accountprofile.changephonenumber.data.GetWarningDataModel
import com.tokopedia.accountprofile.changephonenumber.features.ChangePhoneNumberViewModel
import com.tokopedia.accountprofile.changephonenumber.usecase.GetWarningUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ChangePhoneNumberViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    val getWarningUseCase = mockk<GetWarningUseCase>(relaxed = true)
    val observerGetWarning = mockk<Observer<Result<GetWarningDataModel>>>(relaxed = true)

    lateinit var viewModel: ChangePhoneNumberViewModel

    @Before
    fun setup() {
        viewModel = ChangePhoneNumberViewModel(getWarningUseCase, dispatcherProviderTest)
        viewModel.getWarning.observeForever(observerGetWarning)
    }

    @After
    fun tearDown() {
        viewModel.getWarning.removeObserver(observerGetWarning)
    }

    @Test
    fun `get warning - success`() {
        val url = "http://"
        val mockResponse = GetWarningDataModel(
            redirectUrl = url
        )

        coEvery {
            getWarningUseCase.executeOnBackground()
        } returns mapOf(
            GetWarningDataModel::class.java to RestResponse(mockResponse, 200, false)
        )

        viewModel.getWarning()

        coVerify {
            observerGetWarning.onChanged(Success(mockResponse))
        }

        assert(viewModel.getWarning.value is Success)
        val response = viewModel.getWarning.value as Success
        assertEquals(response.data.redirectUrl, url)
    }

    @Test
    fun `get warning - fail`() {
        val url = ""
        val mockResponse = GetWarningDataModel(
            redirectUrl = url
        )

        coEvery {
            getWarningUseCase.executeOnBackground()
        } returns mapOf(
            GetWarningDataModel::class.java to RestResponse(mockResponse, 200, false)
        )

        viewModel.getWarning()

        coVerify {
            observerGetWarning.onChanged(Success(mockResponse))
        }

        assert(viewModel.getWarning.value is Success)
        val response = viewModel.getWarning.value as Success
        assert(response.data.redirectUrl.isEmpty())
    }
}
