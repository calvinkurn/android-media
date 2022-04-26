package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class BannerTimerViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val viewModel: BannerTimerViewModel by lazy {
        spyk(BannerTimerViewModel(application, componentsItem, 99))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        every { componentsItem.data } returns null
        mockkObject(Utils.Companion)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponent() == componentsItem)
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    /**************************** startTimer() *******************************************/
    @Test
    fun startTimer() {
        var timeDiff : Long = 0
        every { Utils.getElapsedTime(any()) } returns timeDiff

        viewModel.startTimer(mockk())

        assertEquals(viewModel.syncData.value, true)

        timeDiff = 1
        val date = Date(2022,1,20)
        every { Utils.getElapsedTime(any()) } returns timeDiff
        every { Utils.parseData(any(), any()) } returns date

        viewModel.startTimer(mockk(relaxed = true))
    }

    /**************************** startTimer() *******************************************/

    /**************************** getBannerUrlHeight() *******************************************/

    @Test
    fun getBannerUrlHeight() {
        mockkObject(Utils.Companion)
        every { Utils.extractDimension(any()) } returns 1

        assertEquals(viewModel.getBannerUrlHeight(), 1)
    }

    /**************************** getBannerUrlHeight() *******************************************/

    /**************************** getBannerUrlWidth() *******************************************/

    @Test
    fun getBannerUrlWidth() {
        mockkObject(Utils.Companion)
        every { Utils.extractDimension(any(), any()) } returns 1

        assertEquals(viewModel.getBannerUrlWidth(), 1)
    }

    /**************************** getBannerUrlWidth() *******************************************/

    /**************************** getApplink() *******************************************/

    @Test
    fun getApplink() {
        val dataItem = arrayListOf(DataItem(applinks = "applink"))
        every { componentsItem.data } returns dataItem

        assertEquals(viewModel.getApplink(), "applink")
    }

    /**************************** getApplink() *******************************************/

    /**************************** onStop() *******************************************/

    @Test
    fun onStop() {
        viewModel.onStop()

        verify(exactly = 1) { viewModel.stopTimer() }
    }

    /**************************** onStop() *******************************************/

    /**************************** onDestroy() *******************************************/

    @Test
    fun onDestroy() {
        viewModel.onDestroy()

        verify(exactly = 1) { viewModel.stopTimer() }
    }

    /**************************** onDestroy() *******************************************/

    /**************************** onResume() *******************************************/

    @Test
    fun onResume() {
        viewModel.onStop()
        viewModel.onResume()

        assertEquals(viewModel.getRestartTimerAction().value, true)
    }

    /**************************** onResume() *******************************************/

    /**************************** checkTimerEnd() *******************************************/

    @Test
    fun `checkTimerEnd when timeDiff is 1`() {
        val timeDiff: Long = 1
        every { Utils.getElapsedTime(any()) } returns timeDiff
        viewModel.checkTimerEnd()

        assertEquals(viewModel.syncData.value, null)
    }

    @Test
    fun `checkTimerEnd when timeDiff is 0`() {
        val timeDiff: Long = 0
        every { Utils.getElapsedTime(any()) } returns timeDiff
        viewModel.checkTimerEnd()

        assertEquals(viewModel.syncData.value, true)
    }

    /**************************** checkTimerEnd() *******************************************/

    /**************************** getTimerVariant() *******************************************/

    @Test
    fun `getTimerVariant when data variant is main and color is redDark`() {
        val dataItem = arrayListOf(DataItem(variant = "main", color = "redDark"))
        every { componentsItem.data } returns dataItem
        val variant = 0
        assertEquals(viewModel.getTimerVariant(), variant)
    }

    @Test
    fun `getTimerVariant when data variant is main and color is redLight`() {
        val dataItem = arrayListOf(DataItem(variant = "main", color = "redLight"))
        every { componentsItem.data } returns dataItem
        val variant = 1

        assertEquals(viewModel.getTimerVariant(), variant)
    }

    @Test
    fun `getTimerVariant when data variant is alternate`() {
        val dataItem = arrayListOf(DataItem(variant = "alternate"))
        every { componentsItem.data } returns dataItem
        val variant = 2

        assertEquals(viewModel.getTimerVariant(), variant)
    }

    /**************************** getTimerVariant() *******************************************/

}