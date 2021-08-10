package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationChipsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `test for ListData Live data`(){
        every { componentsItem.data } returns null
        var viewModel: NavigationChipsViewModel = spyk(NavigationChipsViewModel(application, componentsItem, 99))
        assert(viewModel.getListDataLiveData().value?.isEmpty() == true)

        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        viewModel = spyk(NavigationChipsViewModel(application, componentsItem, 99))
        assert(viewModel.getListDataLiveData().value?.isEmpty() == true)

//      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        for (i in 0..4) {
            val item = DataItem()
            list.add(item)
        }
        viewModel = spyk(NavigationChipsViewModel(application, componentsItem, 99))
        assert(viewModel.getListDataLiveData().value?.size == 5)
    }
}