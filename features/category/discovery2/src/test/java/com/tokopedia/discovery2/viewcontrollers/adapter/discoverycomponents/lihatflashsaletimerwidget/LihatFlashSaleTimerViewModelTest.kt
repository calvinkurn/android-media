package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LihatFlashSaleTimerViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val viewModel: LihatFlashSaleTimerViewModel by lazy {
        spyk(LihatFlashSaleTimerViewModel(application, componentsItem, 99))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { componentsItem.data } returns null
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    /**************************** onLihatSemuaClicked() *******************************************/

    @Test
    fun onLihatSemuaClicked() {
        val list = ArrayList<DataItem>()
        list.add(mockk(relaxed = true))
        every { componentsItem.data } returns list
        viewModel.onLihatSemuaClicked(mockk())

        verify(exactly = 1) { viewModel.navigate(any(), any()) }
    }
    /**************************** onLihatSemuaClicked() *******************************************/

    /**************************** onStop() *******************************************/

    @Test
    fun onStop() {
        viewModel.onStop()

        verify(exactly = 1) { viewModel.stopTimer() }
    }

    /**************************** onStop() *******************************************/


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}