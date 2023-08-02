package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcard

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.contentCard.LandingPage
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardItemViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContentCardItemModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var context: Context = mockk()
    private var viewModel: ContentCardItemViewModel =
        spyk(ContentCardItemViewModel(application, componentsItem, 99))

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { application.applicationContext } returns context
        mockkObject(Utils)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkObject(Utils)
    }

    @Test
    fun `test for onAttachToViewHolder`() {
        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentLiveData().value == componentsItem)
    }

    @Test
    fun `test for getStartDate when data is non null`() {
        val dataItem = arrayListOf(DataItem(startDate = "01011999"))
        every { componentsItem.data } returns dataItem
        assert((viewModel.getStartDate() == "01011999"))
    }

    @Test
    fun `test for getStartDate when data is null`() {
        every { componentsItem.data } returns null
        assert((viewModel.getStartDate() == ""))
    }

    @Test
    fun `test for getEndDate when data is non null`() {
        val dataItem = arrayListOf(DataItem(endDate = "02022000"))
        every { componentsItem.data } returns dataItem
        assert((viewModel.getEndDate() == "02022000"))
    }

    @Test
    fun `test for getEndDate when data is null`() {
        every { componentsItem.data } returns null
        assert((viewModel.getEndDate() == ""))
    }

    @Test
    fun `test for getNavigationUrl when list is null`() {
        val list = null
        every { componentsItem.data } returns list

        assert(viewModel.getNavigationUrl() == null)
    }

    @Test
    fun `test for getNavigationUrl when list is empty`() {
        val list = mutableListOf<DataItem>()
        every { componentsItem.data } returns list

        assert(viewModel.getNavigationUrl() == null)
    }

    @Test
    fun `test for getNavigationUrl`() {
        val list = mutableListOf(DataItem(landingPage = LandingPage(appLink = "xyz")))
        every { componentsItem.data } returns list
        assert(viewModel.getNavigationUrl() == "xyz")
    }

    @Test
    fun `test for startTimer`() {
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { application.applicationContext.resources } returns resource
        every { viewModel.getStartDate() } returns "2021-11-01T10:00:00+07:00"
        every { Utils.parseFlashSaleDate(any()) } returns "2029-11-01T10:00:00+07:00"
        every { Utils.isFutureSale(any()) } returns true

        viewModel.startTimer()

        assert(viewModel.getTimerText().value != null)
    }

    @Test
    fun `test for startTimer when saleTimeMillis is less than 0`() {
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { application.applicationContext.resources } returns resource
        every { viewModel.getStartDate() } returns "2021-11-01T10:00:00+07:00"
        every { Utils.parseFlashSaleDate(any()) } returns "2019-11-01T10:00:00+07:00"
        every { Utils.isFutureSale(any()) } returns true

        viewModel.startTimer()

        assert(viewModel.getTimerText().value != null)
    }
}
