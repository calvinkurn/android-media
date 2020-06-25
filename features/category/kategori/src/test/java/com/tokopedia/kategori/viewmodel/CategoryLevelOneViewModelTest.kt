package com.tokopedia.kategori.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kategori.repository.KategoriRepository
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CategoryLevelOneViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val mKategoriRepository: KategoriRepository = mockk(relaxed = true)

    private val viewModel: CategoryLevelOneViewModel by lazy {
        spyk(CategoryLevelOneViewModel(mKategoriRepository))
    }


    @RelaxedMockK
    lateinit var performanceMonitoringListener: PerformanceMonitoringListener


    @RelaxedMockK
    lateinit var requestParams: RequestParams

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @Test
    fun ` category list call fails`() {
        val exception = Exception("adfad")
        every { viewModel.createRequestParams(2,true) } returns requestParams

        coEvery { mKategoriRepository.getCategoryListItems(requestParams.parameters) } throws exception

        viewModel.bound(performanceMonitoringListener)
        Assert.assertEquals(viewModel.categoryListLiveData.value, Fail(exception))
    }


    @Test
    fun `verify calls`() {

        every { viewModel.createRequestParams(2,true) } returns requestParams

        coEvery { mKategoriRepository.getCategoryListItems(requestParams.parameters) } returns mockk()

        viewModel.bound(performanceMonitoringListener)

        coVerify {
            performanceMonitoringListener.startRenderPerformanceMonitoring()
        }

    }
}

