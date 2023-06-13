package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.sliderbanner

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SliderBannerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        unmockkConstructor(URLParser::class)
    }

    @Test
    fun `test for component value present in live data`() {
        val viewModel: SliderBannerViewModel = spyk(SliderBannerViewModel(application, componentsItem, 99))
        assert(viewModel.getComponentsLiveData().value === componentsItem)
    }

    @Test
    fun `test for ListData Live data`() {
        every { componentsItem.data } returns null
        var viewModel: SliderBannerViewModel = spyk(SliderBannerViewModel(application, componentsItem, 99))
        assert(viewModel.getListDataLiveData().value == null)

        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        viewModel = spyk(SliderBannerViewModel(application, componentsItem, 99))
        assert(viewModel.getListDataLiveData().value?.isEmpty() == true)

//      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        for (i in 0..4) {
            val item = DataItem()
            list.add(item)
        }
        viewModel = spyk(SliderBannerViewModel(application, componentsItem, 99))
        assert(viewModel.getListDataLiveData().value?.firstOrNull()?.name == ComponentNames.SingleBanner.componentName)
        unmockkConstructor(URLParser::class)
    }

    @Test
    fun `test for position passed to VM`() {
        val viewModel: SliderBannerViewModel = spyk(SliderBannerViewModel(application, componentsItem, 99))
        assert(viewModel.position == 99)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(::getComponent)
        unmockkConstructor(URLParser::class)
    }
}
