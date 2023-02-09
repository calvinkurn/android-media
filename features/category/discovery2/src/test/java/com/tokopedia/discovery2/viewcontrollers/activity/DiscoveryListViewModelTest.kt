package com.tokopedia.discovery2.viewcontrollers.activity

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiscoveryListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    var componentsItem: ComponentsItem = mockk()
    val applicationContext: Application = mockk()
    var discoveryListViewModel: DiscoveryListViewModel = DiscoveryListViewModel(applicationContext)
    var componentsItem2: ComponentsItem = mockk()

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


    /**************************** test for getViewHolderModel() *******************************************/
    @Test
    fun `test for getViewHolderModel`() {
        val vm = discoveryListViewModel.getViewHolderModel({ application, components, position ->
            MultiBannerViewModel(
                application,
                components,
                position
            )
        }, componentsItem, 1)
        assert(vm != null)
        val vmRef = discoveryListViewModel.getViewModelAtPosition(1)
        assert(vm === vmRef)
        assert(vmRef is MultiBannerViewModel)
    }

    /**************************** test for getViewModelAtPosition()'s logic *******************************************/
    @Test
    fun `test for getViewModelAtPosition`() {
        val v = discoveryListViewModel.getViewHolderModel({ application, components, position ->
            MultiBannerViewModel(
                application,
                components,
                position
            )
        }, componentsItem, 1)
        val vm = discoveryListViewModel.getViewHolderModel({ application, components, position ->
            MultiBannerViewModel(
                application,
                components,
                position
            )
        }, componentsItem2, 2)
        assert(vm != null)
        val vmRef1 = discoveryListViewModel.getViewModelAtPosition(1)
        val vmRef2 = discoveryListViewModel.getViewModelAtPosition(2)
        assert(vmRef1 != null)
        assert(vmRef2 != null)
        val vm1 = vmRef1 as MultiBannerViewModel
        val vm2 = vmRef2 as MultiBannerViewModel
        assert(vm1.position == 1)
        assert(vm2.position == 2)
        assert(vm1.components === componentsItem)
        assert(vm2.components === componentsItem2)
    }

    /**************************** test for clearViewModels logic *******************************************/
    @Test
    fun `test for clearViewModels`() {
        discoveryListViewModel.getViewHolderModel({ application, components, position ->
            MultiBannerViewModel(
                application,
                components,
                position
            )
        }, componentsItem, 1)
        discoveryListViewModel.getViewHolderModel({ application, components, position ->
            MultiBannerViewModel(
                application,
                components,
                position
            )
        }, componentsItem2, 2)

//      viewmodels present before calling clear
        val vmRef1 = discoveryListViewModel.getViewModelAtPosition(1)

//        After calling clear
        discoveryListViewModel.clearList()
        discoveryListViewModel.getViewHolderModel({ application, components, position ->
            MultiBannerViewModel(
                application,
                components,
                position
            )
        }, componentsItem2, 1)
        val vmRef2 = discoveryListViewModel.getViewModelAtPosition(1)
        assert(vmRef1 != vmRef2)
    }


}
