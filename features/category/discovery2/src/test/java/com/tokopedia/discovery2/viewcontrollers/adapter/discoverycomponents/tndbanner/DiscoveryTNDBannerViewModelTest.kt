package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tndbanner

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner.DiscoveryTDNBannerViewModel
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class DiscoveryTNDBannerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: DiscoveryTDNBannerViewModel by lazy {
        spyk(DiscoveryTDNBannerViewModel(application, componentsItem, 99))
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
        assert(viewModel.componentLiveData.value == componentsItem)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}