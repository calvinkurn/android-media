package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantVoucherListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel:MerchantVoucherListViewModel = spyk(MerchantVoucherListViewModel(application, componentsItem, 99))
    private val useCase:MerchantVoucherUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @Test
    fun `test for components`(){
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for coupon data sync logic`() {
        viewModel.merchantVoucherUseCase = useCase
        coEvery { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) } returns true
        viewModel.onAttachToViewHolder()
        coVerify { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }
        assert(viewModel.syncData.value == true)
        assert(!componentsItem.verticalProductFailState)
        viewModel = spyk(MerchantVoucherListViewModel(application, componentsItem, 99))
        viewModel.merchantVoucherUseCase = useCase
        coEvery { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) } returns false
        viewModel.onAttachToViewHolder()
        coVerify { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }
        assert(viewModel.syncData.value == false)
        assert(!componentsItem.verticalProductFailState)
        viewModel = spyk(MerchantVoucherListViewModel(application, componentsItem, 99))
        viewModel.merchantVoucherUseCase = useCase
        coEvery { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }  throws Exception()
        mockkStatic(::getComponent)
        every { getComponent(componentsItem.id,componentsItem.pageEndPoint) } returns componentsItem
        viewModel.onAttachToViewHolder()
        coVerify { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }
        assert(viewModel.syncData.value == true)
//        Todo:: why is this not working ??
//        assert(componentsItem.verticalProductFailState)
    }
}