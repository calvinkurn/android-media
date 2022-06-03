package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.SaleCountDownTimer
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

    @Test
    fun `onLihatSemuaClicked when data is null`() {
        every { componentsItem.data } returns null

        viewModel.onLihatSemuaClicked(mockk())

        verify(inverse = true) { viewModel.navigate(any(), any()) }
    }
    /**************************** onLihatSemuaClicked() *******************************************/

    /**************************** onStop() *******************************************/

    @Test
    fun onStop() {
        viewModel.onStop()

        verify(exactly = 1) { viewModel.stopTimer() }
    }

    /**************************** onStop() *******************************************/

    /**************************** startTimer() *******************************************/

    @Test
    fun `startTimer`() {
        val list = ArrayList<DataItem>()
        val item = DataItem(ongoingCampaignEndTime = "2032-08-12T20:17:46")
        list.add(item)
        every { componentsItem.data } returns list

        viewModel.startTimer()

        verify(exactly = 1) { viewModel.startTimer() }
    }


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}