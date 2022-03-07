package com.tokopedia.logout.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logout.domain.model.LogoutDataModel
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author rival
 * @created on 2/10/2020
 */

class LogoutViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)
    private val observer = mockk<Observer<Result< LogoutDataModel>>>(relaxed = true)

    lateinit var viewModel: LogoutViewModel

    val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = LogoutViewModel(logoutUseCase, CoroutineTestDispatchersProvider)
        viewModel.logoutResult.observeForever(observer)
    }

    @Test
    fun `do logout - success`() {
        val mockResponse = LogoutDataModel(LogoutDataModel.Response(success = true))

        coEvery {
            logoutUseCase.invoke(Unit)
        } returns mockResponse

        viewModel.doLogout()

        verify {
            observer.onChanged(Success(mockResponse))
        }
    }

    @Test
    fun `do logout - failed`() {
        val mockErrorData = LogoutDataModel.Errors(message = "logout failed")
        val mockResponseData = LogoutDataModel.Response(success = false, errors = mutableListOf(mockErrorData))
        val mockResponse = Throwable(mockResponseData.errors[0].message)

        coEvery {
            logoutUseCase.invoke(Unit)
        } throws mockResponse

        viewModel.doLogout()

        verify {
            observer.onChanged(Fail(mockResponse))
        }
    }

    @Test
    fun `do logout - error`() {

        coEvery {
            logoutUseCase.invoke(Unit)
        } throws mockThrowable


        viewModel.doLogout()

        verify {
            observer.onChanged(Fail(mockThrowable))
        }
    }
}