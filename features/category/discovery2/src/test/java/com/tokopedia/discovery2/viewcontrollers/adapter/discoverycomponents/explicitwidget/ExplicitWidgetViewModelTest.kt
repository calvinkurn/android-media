package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class ExplicitWidgetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val viewModel: ExplicitWidgetViewModel = spyk(ExplicitWidgetViewModel(application, componentsItem, 99))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `test for explicitViewModel`() {
        val viewModel: ExplicitWidgetViewModel =
                spyk(ExplicitWidgetViewModel(application, componentsItem, 99))

        val explicitViewModel = mockk<ExplicitViewModel>()
        viewModel.explicitViewContract = explicitViewModel

        assert(viewModel.explicitViewContract === explicitViewModel)
    }


    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    /**************************** test for onAttachToViewHolder *******************************************/
    @Test
    fun `test for onAttachToViewHolder`(){
        viewModel.onAttachToViewHolder()

        assert(viewModel.getComponentData().value == viewModel.components)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}