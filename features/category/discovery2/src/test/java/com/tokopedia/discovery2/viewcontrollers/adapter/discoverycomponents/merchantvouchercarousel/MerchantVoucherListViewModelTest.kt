package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.ErrorState
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

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

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()

        unmockkStatic(::getComponent)
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
    fun `test for coupon data sync logic when we get load value as true and also get data`() {
        viewModel.merchantVoucherUseCase = useCase
        coEvery { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) } returns true
        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val comp: ComponentsItem = spyk()
        every { getComponent(componentsItem.id,componentsItem.pageEndPoint) } returns comp
        every { comp.getComponentsItem() } returns listOf(mockk())
        viewModel.onAttachToViewHolder()
        coVerify { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }
        assert(viewModel.syncData.value == true)
        assert(!comp.verticalProductFailState)
    }

    @Test
    fun `test for coupon data sync logic when we get load value as true and we don't get data`() {
        viewModel = spyk(MerchantVoucherListViewModel(application, componentsItem, 99))
        viewModel.merchantVoucherUseCase = useCase
        coEvery { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) } returns true
        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val comp: ComponentsItem = spyk()
        every { getComponent(componentsItem.id,componentsItem.pageEndPoint) } returns comp
        every { comp.getComponentsItem() } returns listOf()
        viewModel.onAttachToViewHolder()
        coVerify { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }
        assert(viewModel.syncData.value == true)
        assert(comp.verticalProductFailState)
        assert(comp.errorState == ErrorState.EmptyComponentState)
    }

    @Test
    fun `test for coupon data sync logic when we get load value as false`() {
        viewModel = spyk(MerchantVoucherListViewModel(application, componentsItem, 99))
        viewModel.merchantVoucherUseCase = useCase
        coEvery { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) } returns false
        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val comp: ComponentsItem = spyk()
        every { getComponent(componentsItem.id,componentsItem.pageEndPoint) } returns comp
        viewModel.onAttachToViewHolder()
        coVerify { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }
        assert(viewModel.syncData.value == false)
        assert(!comp.verticalProductFailState)
    }

    @Test
    fun `test for coupon data sync logic when we get exception`(){
        viewModel = spyk(MerchantVoucherListViewModel(application, componentsItem, 99))
        viewModel.merchantVoucherUseCase = useCase
        coEvery { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }  throws UnknownHostException()
        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val comp: ComponentsItem = spyk()
        every { getComponent(componentsItem.id,componentsItem.pageEndPoint) } returns comp
        viewModel.onAttachToViewHolder()
        coVerify { useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint) }
        assert(viewModel.syncData.value == true)
        assert(comp.errorState == ErrorState.NetworkErrorState)
        assert(comp.verticalProductFailState)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(URLParser::class)
        unmockkStatic(::getComponent)
    }
}