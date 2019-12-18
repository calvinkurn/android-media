package com.tokopedia.tokopoints.view.pointhistory

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.view.util.ErrorMessage
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock

class PointHistoryViewModelTest {


    var context = mockk<Context>()
    var repository = mockk<PointHistoryRepository>()
    lateinit var viewModel: PointHistoryViewModel

    var routeManager = mockkStatic(RouteManager::class)
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    internal fun initialize() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        coEvery { repository.getPointsDetail(any()) } just Runs
        every { context.getString(any()) } returns "Yuk Belanja"
        viewModel = PointHistoryViewModel(repository)
    }

    @Test
    fun onErrorButtonClicked() {
        viewModel.onErrorButtonClicked(context = context, toString = "")

        coVerify(exactly = 2) { repository.getPointsDetail(any()) }


        every { RouteManager.route(context, any()) } returns false
        viewModel.onErrorButtonClicked(context = context, toString = "Yuk Belanja")


        coVerify { RouteManager.route(context, any()) }
    }

    @Test
    fun onError() {
        viewModel.listLoading.value = null
        viewModel.onError(2)
        assert(viewModel.listLoading.value == null)
        viewModel.onError(1)
        val result = viewModel.listLoading.value
        assert(result is ErrorMessage && result.data.equals("n/a"))
    }


    @Test
    fun loadData(){
        coEvery{repository.getPointList(1, any()) } just Runs
        viewModel.loadData(1)
        coVerify { repository.getPointList(1, any()) }
    }

    @After
    fun cleanUp(){
        Dispatchers.resetMain()
    }
}