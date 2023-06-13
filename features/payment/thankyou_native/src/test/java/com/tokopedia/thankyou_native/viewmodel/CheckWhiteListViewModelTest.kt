package com.tokopedia.thankyou_native.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.thankyou_native.domain.usecase.CheckWhiteListStatusUseCase
import com.tokopedia.thankyou_native.presentation.viewModel.CheckWhiteListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckWhiteListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val checkWhiteListUsecase = mockk<CheckWhiteListStatusUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: CheckWhiteListViewModel
    private val fetchFailedErrorMessage = "Fetch Failed"


    @Before
    fun setUp() {
        viewModel = CheckWhiteListViewModel(
            checkWhiteListUsecase,
            dispatcher
        )
    }


    @Test
    fun successWhiteListTest() {
        val boolean = mockk<Boolean>(relaxed = true)
        coEvery {
            checkWhiteListUsecase.getThankPageData(any(), any())
        } coAnswers {
            firstArg<(Boolean) -> Unit>().invoke(boolean)
        }
        viewModel.registerForSingleAuth()
        Assert.assertEquals((viewModel.whiteListResultLiveData.value as Success).data, boolean)

    }

    @Test
    fun failWhiteListTest() {
        val mockThrowable = Throwable(message = fetchFailedErrorMessage)
        coEvery {
            checkWhiteListUsecase.getThankPageData(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.registerForSingleAuth()
        Assert.assertEquals(
            (viewModel.whiteListResultLiveData.value as Fail).throwable,
            mockThrowable
        )

    }


}
