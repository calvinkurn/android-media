package com.tokopedia.logout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logout.domain.model.LogoutDataModel
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.sessioncommon.data.ocl.OclPreference
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
    private val oclPref = mockk<OclPreference>(relaxed = true)
    private val observer = mockk<Observer<Result<LogoutDataModel>>>(relaxed = true)

    lateinit var viewModel: LogoutViewModel

    @Before
    fun setup() {
        viewModel = LogoutViewModel(logoutUseCase, oclPref, CoroutineTestDispatchersProvider)
        viewModel.logoutResult.observeForever(observer)
    }

    @Test
    fun `do logout - success`() {
        val mockResponse = LogoutDataModel(LogoutDataModel.Response(success = true))
        coEvery { logoutUseCase.invoke(any()) } returns mockResponse

        viewModel.doLogout(LogoutUseCase.PARAM_SAVE_SESSION)

        verify { observer.onChanged(Success(mockResponse)) }
    }

    @Test
    fun `do logout - failed`() {
        val mockErrorData = LogoutDataModel.Errors(message = "logout failed")
        val mockResponseData = LogoutDataModel(
            LogoutDataModel.Response(
                success = false,
                errors = mutableListOf(mockErrorData)
            )
        )

        coEvery { logoutUseCase.invoke(any()) } returns mockResponseData

        viewModel.doLogout(LogoutUseCase.PARAM_SAVE_SESSION)

        verify(exactly = 1) { observer.onChanged(match { it is Fail }) }
    }

    @Test
    fun `do logout - error`() {
        val mockThrowable = Exception()
        val token = "123"
        coEvery { oclPref.getToken() } returns token
        coEvery { logoutUseCase.invoke(any()) } throws mockThrowable

        viewModel.doLogout(LogoutUseCase.PARAM_SAVE_SESSION)

        verify { observer.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `do logout - without save token()`() {
        val mockResponse = LogoutDataModel(LogoutDataModel.Response(success = true))
        coEvery { logoutUseCase.invoke(any()) } returns mockResponse

        viewModel.doLogout()

        verify(exactly = 0) { oclPref.storeToken(any()) }
        verify { observer.onChanged(Success(mockResponse)) }
    }

    @Test
    fun `do logout - with save token()`() {
        val mockResponse = LogoutDataModel(LogoutDataModel.Response(success = true))
        coEvery { logoutUseCase.invoke(any()) } returns mockResponse

        viewModel.doLogout(LogoutUseCase.PARAM_SAVE_SESSION)

        verify(exactly = 1) { oclPref.storeToken(any()) }
        verify { observer.onChanged(Success(mockResponse)) }
    }
}
