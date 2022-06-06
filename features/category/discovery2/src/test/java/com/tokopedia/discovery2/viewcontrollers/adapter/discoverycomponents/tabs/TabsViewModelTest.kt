package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.tabsusecase.DynamicTabsUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TabsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: TabsViewModel by lazy {
        spyk(TabsViewModel(application, componentsItem, 99))
    }

    private val dynamicTabsUseCase: DynamicTabsUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `test for useCase`() {
        val viewModel: TabsViewModel =
                spyk(TabsViewModel(application, componentsItem, 99))

        val dynamicTabsUseCase = mockk<DynamicTabsUseCase>()
        viewModel.dynamicTabsUseCase = dynamicTabsUseCase

        assert(viewModel.dynamicTabsUseCase === dynamicTabsUseCase)
    }



    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    @Test
    fun `test for reInitTabComponentData`(){
        viewModel.reInitTabComponentData()

        assert(viewModel.reInitTabComponentData() == componentsItem.reInitComponentItems())
    }

    @Test
    fun `test for reInitTabTargetComponents`(){
        viewModel.dynamicTabsUseCase = dynamicTabsUseCase
        coEvery { dynamicTabsUseCase.updateTargetProductComponent(any(),any()) } returns true

        viewModel.reInitTabTargetComponents()

        assert(dynamicTabsUseCase.updateTargetProductComponent(componentsItem.id, componentsItem.pageEndPoint))
    }


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}