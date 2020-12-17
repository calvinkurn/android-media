package com.tokopedia.logout.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logout.domain.model.LogoutDataModel
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import io.mockk.mockk
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
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

    @ExperimentalCoroutinesApi
    val dispatcher = TestCoroutineDispatcher()

    private val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)
    private val observer = mockk<Observer<Result< LogoutDataModel>>>(relaxed = true)

    lateinit var viewModel: LogoutViewModel

    val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = LogoutViewModel(logoutUseCase, dispatcher)
        viewModel.logoutResult.observeForever(observer)
    }

    @Test
    fun `do logout - success`() {
        val mockResponse = LogoutDataModel(LogoutDataModel.Response(success = true))

        every {
            logoutUseCase.execute(any(), any())
        } answers {
            firstArg<(LogoutDataModel) -> Unit>().invoke(mockResponse)
        }

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

        every {
            logoutUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockResponse)
        }

        viewModel.doLogout()

        verify {
            observer.onChanged(Fail(mockResponse))
        }
    }

    @Test
    fun `do logout - error`() {

        every {
            logoutUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.doLogout()

        verify {
            observer.onChanged(Fail(mockThrowable))
        }
    }
}