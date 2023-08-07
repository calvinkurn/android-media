package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.LihatSemua
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductCardRevampViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val viewModel: ProductCardRevampViewModel by lazy {
        spyk(ProductCardRevampViewModel(application, componentsItem, 99))
    }
    private val productCardsUseCase: ProductCardsUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
    }

    @Test
    fun `test for useCase`() {
        val viewModel: ProductCardRevampViewModel =
            spyk(ProductCardRevampViewModel(application, componentsItem, 99))

        val productCardsUseCase = mockk<ProductCardsUseCase>()
        viewModel.productCardsUseCase = productCardsUseCase

        assert(viewModel.productCardsUseCase === productCardsUseCase)
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    /**************************** init *******************************************/
    @Test
    fun `Test for init viewModel`() {
        val tempLihatSemua: LihatSemua = mockk(relaxed = true)
        every { componentsItem.lihatSemua } returns tempLihatSemua
        val tempDataItem = DataItem(title = tempLihatSemua.header, subtitle = tempLihatSemua.subheader, btnApplink = tempLihatSemua.applink)
        val tempComponentsItem: ComponentsItem = mockk(relaxed = true)
        every { tempComponentsItem.name } returns ComponentsList.ProductCardCarousel.componentName
        every { tempComponentsItem.creativeName } returns componentsItem.creativeName
        every { tempComponentsItem.data } returns listOf(tempDataItem)

        assert(viewModel.getProductCarouselHeaderData().value != null)
    }
    /**************************** end of init *******************************************/

    /**************************** onAttachToViewHolder() *******************************************/

    @Test
    fun onAttachToViewHolder() {
        coEvery { componentsItem.lihatSemua } returns null
        coEvery { componentsItem.id } returns ""
        coEvery { componentsItem.pageEndPoint } returns ""
        val viewModel = spyk(ProductCardRevampViewModel(application, componentsItem, 99))
        viewModel.productCardsUseCase = productCardsUseCase

        coEvery { viewModel.productCardsUseCase?.loadFirstPageComponents(any(), any()) } returns true

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.syncData.value, true)
    }
    /**************************** onAttachToViewHolder() *******************************************/

    /**************************** onAttachToViewHolderError() *******************************************/

    @Test
    fun onAttachToViewHolderError() {
        runBlocking {
            coEvery { componentsItem.lihatSemua } returns null
            coEvery { componentsItem.id } returns ""
            coEvery { componentsItem.pageEndPoint } returns ""

            val viewModel = spyk(ProductCardRevampViewModel(application, componentsItem, 99))
            viewModel.productCardsUseCase = productCardsUseCase

            val error = Throwable("something")
            coEvery { viewModel.productCardsUseCase?.loadFirstPageComponents(any(), any()) } throws error

//      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
            mockkConstructor(URLParser::class)
            every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
            val comp = spyk<ComponentsItem>()
            mockkStatic(::getComponent)
            coEvery { getComponent(any(), any()) } returns comp

            viewModel.onAttachToViewHolder()
            assertEquals(comp.verticalProductFailState, true)
            assertEquals(viewModel.syncData.value, true)
        }
    }

    /**************************** onAttachToViewHolderError() *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(URLParser::class)
        unmockkStatic(::getComponent)
    }
}
