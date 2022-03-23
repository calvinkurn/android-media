package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TextComponentViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: TextComponentViewModel by lazy {
        spyk(TextComponentViewModel(application, componentsItem, 99))
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
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list
        TestCase.assertEquals(viewModel.getTextComponentLiveData().value == componentsItem.data?.firstOrNull(), true)

        every { componentsItem.data } returns null
        val viewModelTest = spyk(TextComponentViewModel(application, componentsItem, 0))
        assert(viewModelTest.getTextComponentLiveData().value == null)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}