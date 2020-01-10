package com.tokopedia.tokopoints.view.pointhistory

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.view.model.PointHistoryBase
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.model.TokoPointEntity
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import kotlin.reflect.KClass

class PointHistoryViewModelTest {


    val clazz = Loading::class as KClass<Loading<PointHistoryBase>>

    var context = mockk<Context>()
    var repository = mockk<PointHistoryRepository>()
    lateinit var viewModel: PointHistoryViewModel

    var routeManager = mockkStatic(RouteManager::class)
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    internal fun initialize() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        coEvery { repository.getPointsDetail() } returns mockk()
        every { context.getString(any()) } returns "Yuk Belanja"
        viewModel = PointHistoryViewModel(repository)
    }

    @Test
    fun onErrorButtonClicked() {
        runBlockingTest {

            every { RouteManager.route(context, any()) } returns false
            viewModel.onErrorButtonClicked(context = context, toString = "Yuk Belanja")


            coVerify { RouteManager.route(context, any()) }

            //case 2
            val listObserver = mockk<Observer<Resources<PointHistoryBase>>>() {
                every { onChanged(any()) } just Runs
            }

            val dataObserver = mockk<Observer<Resources<TokoPointEntity>>> {
                every { onChanged(any()) } just Runs
            }

            viewModel.listLoading.observeForever(listObserver)
            viewModel.data.observeForever(dataObserver)

            viewModel.onErrorButtonClicked(context = context, toString = "")

            verifyOrder {
                listObserver.onChanged(ofType(Loading::class as KClass<Loading<PointHistoryBase>>))
                dataObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<TokoPointEntity>>))
                listObserver.onChanged(ofType(Loading::class as KClass<Loading<PointHistoryBase>>))
                dataObserver.onChanged(ofType(Loading::class as KClass<Loading<TokoPointEntity>>))
                dataObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<TokoPointEntity>>))
            }
            coVerify { repository.getPointsDetail() }


        }
    }

    @Test
    fun `on Error but click on hitApi success case`() {
        runBlockingTest {
            val listObserver = mockk<Observer<Resources<PointHistoryBase>>>() {
                every { onChanged(any()) } just Runs
            }

            val dataObserver = mockk<Observer<Resources<TokoPointEntity>>> {
                every { onChanged(any()) } just Runs
            }

            viewModel.listLoading.observeForever(listObserver)
            viewModel.data.observeForever(dataObserver)
            val response = mockk<TokoPointEntity>()
            val data = mockk<GraphqlResponse>(relaxed = true) {
                every { getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java) } returns mockk {
                    every { tokoPoints } returns response
                }
            }
            coEvery { repository.getPointsDetail() } returns data
            coEvery { repository.getPointList(1) } returns data
            viewModel.onErrorButtonClicked(context = context, toString = "")

            verifyOrder {
                listObserver.onChanged(ofType(clazz))
                dataObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<TokoPointEntity>>))
                listObserver.onChanged(ofType(Loading::class as KClass<Loading<PointHistoryBase>>))
                dataObserver.onChanged(ofType(Loading::class as KClass<Loading<TokoPointEntity>>))
                dataObserver.onChanged(ofType(Success::class as KClass<Success<TokoPointEntity>>))
            }

            val result = viewModel.data.value as Success
            assert(result.data == response)
            coVerify { repository.getPointsDetail() }
        }
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
    fun `load data case 1`() {
        runBlockingTest {
            val listObserver = mockk<Observer<Resources<PointHistoryBase>>>() {
                every { onChanged(any()) } just Runs
            }
            viewModel.listLoading.observeForever(listObserver)
            viewModel.loadData(1)
            verify { listObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<PointHistoryBase>>)) }
        }
    }

    @Test
    fun `load data case 2`() {
        runBlockingTest {
            val listObserver = mockk<Observer<Resources<PointHistoryBase>>>() {
                every { onChanged(any()) } just Runs
            }
            viewModel.listLoading.observeForever(listObserver)
            val data = mockk<GraphqlResponse>(relaxed = true)
            coEvery { repository.getPointList(1) } returns data
            viewModel.loadData(1)
            verify { listObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<PointHistoryBase>>)) }
        }
    }

    @Test
    fun `load data case 3`() {
        runBlockingTest {
            val listObserver = mockk<Observer<Resources<PointHistoryBase>>>() {
                every { onChanged(any()) } just Runs
            }
            viewModel.listLoading.observeForever(listObserver)
            val data = mockk<GraphqlResponse>(relaxed = true)

            val response = mockk<PointHistoryBase>()
            coEvery { repository.getPointList(1)  } returns data
            every { data.getData<PointHistoryBase>(PointHistoryBase::class.java) } returns response
            viewModel.loadData(1)
            verify { listObserver.onChanged(ofType(Success::class as KClass<Success<PointHistoryBase>>)) }
            val result = viewModel.listLoading.value as Success
            assert(result.data == response)
        }
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }
}