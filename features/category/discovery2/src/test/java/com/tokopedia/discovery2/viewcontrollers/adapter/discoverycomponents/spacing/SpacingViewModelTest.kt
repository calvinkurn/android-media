package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class SpacingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    var context: Context = mockk(relaxed = true)
    var resource: Resources = mockk(relaxed = true)

    private val viewModel: SpacingViewModel by lazy {
        spyk(SpacingViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentData().value == componentsItem)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    /**************************** test for setupSpacingView() *******************************************/

    @Test
    fun `test for setupSpacingView when data is empty`() {
        coEvery { componentsItem.data } returns null

        viewModel.setupSpacingView()

        assert(viewModel.getViewBackgroundColor().value == null)
    }

    @Test
    fun `test for setupSpacingView when data background is empty`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        coEvery { componentsItem.data } returns list

        viewModel.setupSpacingView()

        assert(viewModel.getViewBackgroundColor().value == null)
    }

    @Test
    fun `test for setupSpacingView when background is not empty`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        every { application.applicationContext } returns context
        every { context.resources } returns resource
        item.sizeMobile = "4"
        item.background = "FF0000"
        list.add(item)
        coEvery { componentsItem.data } returns list

        viewModel.setupSpacingView()

        assert(viewModel.getViewHeight().value != null)

    }

    @Test
    fun `test for setupSpacingView when background is empty`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        every { application.applicationContext } returns context
        every { context.resources } returns resource
        item.sizeMobile = "4"
        item.background = ""
        list.add(item)
        coEvery { componentsItem.data } returns list

        viewModel.setupSpacingView()

        assert(viewModel.getViewBackgroundColor().value != null)
    }

    @Test
    fun `test for setupSpacingView when resources throws exception`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        every { application.applicationContext } returns context
        item.sizeMobile = "4"
        item.background = ""
        list.add(item)
        coEvery { componentsItem.data } returns list

        viewModel.setupSpacingView()

        assert(viewModel.getViewBackgroundColor().value != null)
    }

    /**************************** end of setupSpacingView() *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}
