package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.SectionUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcarditem.ShopCardItemViewModel
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

class ShopCardItemModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: ShopCardItemViewModel =
        spyk(ShopCardItemViewModel(application, componentsItem, 99))

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `component value is present in live data`() {
        assert(viewModel.components == componentsItem)
    }

    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

}