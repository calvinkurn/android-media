package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashHomeUsecase
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashHomeViewmodel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TokomemberHomeViewmodelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockThrowable = Throwable(message = "exception")
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TokomemberDashHomeViewmodel
    private val tokomemberDashHomeUsecase = mockk<TokomemberDashHomeUsecase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = TokomemberDashHomeViewmodel(
            tokomemberDashHomeUsecase,
            dispatcher
        )
    }

    @Test
    fun successHomeData() {
        val data = mockk<ProgramList>(relaxed = true)
        coEvery {
            tokomemberDashHomeUsecase.getHomeData(any(), any(), 0,0,-1,1,10)
        } coAnswers {
            firstArg<(ProgramList) -> Unit>().invoke(data)
        }
        viewModel.getHomePageData(0,0,-1,1,10)

        Assert.assertEquals(
            (viewModel.tokomemberHomeResultLiveData.value as Success).data,
            data
        )
    }

    @Test
    fun failHomeData() {
        coEvery {
            tokomemberDashHomeUsecase.getHomeData(any(), any(), 0,0,-1,1,10)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getHomePageData(0,0,-1,1,10)
        Assert.assertEquals(
            (viewModel.tokomemberHomeResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

}