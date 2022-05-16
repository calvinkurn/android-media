package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TokopointsItemViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var context: Context = mockk()

    private val viewModel: TokopointsItemViewModel by lazy {
        spyk(TokopointsItemViewModel(application, componentsItem, 99))
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

    /**************************** test for component value *******************************************/
    @Test
    fun `component value is present in live data`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list
        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getDataItemValue().value?.slug == componentsItem.data?.firstOrNull()?.slug, true)

    }
    /**************************** end of component value *******************************************/

    /**************************** test for onTokopointsItemClicked() *******************************************/
    @Test
    fun `test for onTokopointsItemClicked`() {
        val list = mutableListOf(DataItem(slug = "xyz"))
        every { componentsItem.data } returns list
        every { viewModel.navigate(any(),any()) } just runs
        viewModel.onAttachToViewHolder()

        viewModel.onTokopointsItemClicked(context)

        verify { viewModel.navigate(any(),any()) }
    }
    /**************************** end of onTokopointsItemClicked() *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}