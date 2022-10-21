package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramListUsecase
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import com.tokopedia.tokomember_seller_dashboard.util.LOADED
import com.tokopedia.tokomember_seller_dashboard.util.REFRESH
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmProgramListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TmProgramListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockThrowable = Throwable(message = "exception")
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TmProgramListViewModel
    private val tokomemberDashGetProgramListUsecase =
        mockk<TokomemberDashGetProgramListUsecase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = TmProgramListViewModel(
            tokomemberDashGetProgramListUsecase,
            dispatcher
        )
    }

    @Test
    fun successProgramList(){
        val data = mockk<ProgramList>(relaxed = true)
        coEvery {
            tokomemberDashGetProgramListUsecase.getProgramList(any(), any(), 0,0,0,0,0)
        } coAnswers {
            firstArg<(ProgramList) -> Unit>().invoke(data)
        }
        viewModel.getProgramList(0,0,0,0,0)

        Assert.assertEquals(
            (viewModel.tokomemberProgramListResultLiveData.value as TokoLiveDataResult).data,
            data
        )
    }

    @Test
    fun failProgramList() {
        coEvery {
            tokomemberDashGetProgramListUsecase.getProgramList(any(), any(), 0,0,0,0,0)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getProgramList(0,0,0,0,0)
        Assert.assertEquals(
            (viewModel.tokomemberProgramListResultLiveData.value as TokoLiveDataResult).error,
            mockThrowable
        )
    }

    @Test
    fun refreshProgramListLoaded(){
        viewModel.refreshProgramList(LOADED)
        Assert.assertEquals(LOADED, viewModel.tmRefreshProgramListLiveData.value)
    }

    @Test
    fun refreshProgramListRefresh(){
        viewModel.refreshProgramList(REFRESH)
        Assert.assertEquals(REFRESH, viewModel.tmRefreshProgramListLiveData.value)
    }


    @Test
    fun programListLoadingState(){
        viewModel.programListLoadingState(1)
        Assert.assertEquals(
            viewModel.tmProgramListLoadingStateLiveData.value,
            1
        )
    }
}
