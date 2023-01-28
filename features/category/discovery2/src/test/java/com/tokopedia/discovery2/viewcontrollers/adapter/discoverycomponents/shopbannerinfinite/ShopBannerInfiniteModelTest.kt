package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.bannerinfiniteusecase.BannerInfiniteUseCase
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopBannerInfiniteModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    var context: Context = mockk(relaxed = true)
    private var viewModel: ShopBannerInfiniteViewModel =
        spyk(ShopBannerInfiniteViewModel(application, componentsItem, 99))

    private val bannerInfiniteUseCase: BannerInfiniteUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkStatic(::getComponent)
        every { componentsItem.data } returns null
        val list = ArrayList<DataItem>()
        list.add(mockk(relaxed = true))
        coEvery { componentsItem.data } returns list
        coEvery { componentsItem.id } returns ""
        coEvery { componentsItem.parentComponentId } returns ""
        coEvery { componentsItem.pageEndPoint } returns ""
        coEvery { getComponent(any(), any()) } returns componentsItem
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(::getComponent)
    }

    /**************************** useCase test *******************************************/

    @Test
    fun `test for useCase`() {
        val viewModel: ShopBannerInfiniteViewModel =
                spyk(ShopBannerInfiniteViewModel(application, componentsItem, 99))

        val bannerUseCase = mockk<BannerInfiniteUseCase>()
        viewModel.bannerInfiniteUseCase = bannerUseCase
        assert(viewModel.bannerInfiniteUseCase === bannerUseCase)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @Test
    fun `test for onAttachToViewHolder error case`() {
        viewModel.bannerInfiniteUseCase = bannerInfiniteUseCase
        coEvery {
            bannerInfiniteUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, true)

    }

    @Test
    fun `test for onAttachToViewHolder success when componentList is not empty`() {
        viewModel.bannerInfiniteUseCase = bannerInfiniteUseCase
        coEvery {
            bannerInfiniteUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    /**************************** end of onAttachToViewHolder() *******************************************/


    /****************************************** test for component ****************************************/
    @Test
    fun `test for component passed to VM`(){
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

    /****************************************** test for position ****************************************/
    @Test
    fun `test for component position`(){
        TestCase.assertEquals(viewModel.position, 99)
    }

    @Test
    fun `test for checkForDarkMode`() {
        spyk(context.isDarkMode())

        viewModel.checkForDarkMode(context)

        verify { viewModel.checkForDarkMode(context) }
    }

}
