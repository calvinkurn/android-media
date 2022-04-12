package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyCouponModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: MyCouponViewModel =
        spyk(MyCouponViewModel(application, componentsItem, 99))

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for component passed to VM`(){
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

}