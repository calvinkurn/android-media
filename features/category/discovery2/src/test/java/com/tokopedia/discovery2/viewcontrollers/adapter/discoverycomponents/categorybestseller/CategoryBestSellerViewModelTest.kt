package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class CategoryBestSellerViewModelTest {

    @get:Rule
    val rule1 = CoroutineTestRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val context = mockk<Context>(relaxed = true)
    private val viewModel: CategoryBestSellerViewModel by lazy {
        spyk(CategoryBestSellerViewModel(application, componentsItem, 99))
    }
    private val productCardsUseCase: ProductCardsUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        coEvery { getComponent(any(), any()) } returns componentsItem
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.components == componentsItem)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @Test
    fun onAttachToViewHolderError() {
        viewModel.productCardsUseCase = productCardsUseCase
        runBlocking {
            val error = Error("error")
            every { componentsItem.id } returns "s"
            every { componentsItem.pageEndPoint } returns "s"
            every { componentsItem.properties } returns null
            coEvery {
                viewModel.productCardsUseCase?.loadFirstPageComponents(
                    any(),
                    any(),
                    any()
                )
            } throws error

            viewModel.onAttachToViewHolder()

            Assert.assertEquals(viewModel.getProductLoadState().value, true)
        }
    }

    @Test
    fun onAttachToViewHolderEmpty() {
        viewModel.productCardsUseCase = productCardsUseCase
        runBlocking {
            val list = mockk<ArrayList<ComponentsItem>>()
            every { componentsItem.id } returns "s"
            every { componentsItem.pageEndPoint } returns "s"
            every { componentsItem.getComponentsItem() } returns list
            every { componentsItem.properties } returns null
            coEvery {
                viewModel.productCardsUseCase?.loadFirstPageComponents(
                    any(),
                    any(),
                    any()
                )
            } returns mockk()

            viewModel.onAttachToViewHolder()

            Assert.assertEquals(viewModel.getProductLoadState().value, true)
        }
    }

    @Test
    fun onUpdateComponent() {
        viewModel.productCardsUseCase = productCardsUseCase
        val list = mockk<ArrayList<ComponentsItem>>()
        every { componentsItem.id } returns "s"
        every { componentsItem.pageEndPoint } returns "s"
        every { componentsItem.getComponentsItem() } returns list
        every { componentsItem.properties } returns null
        coEvery {
            viewModel.productCardsUseCase?.loadFirstPageComponents(
                any(),
                any(),
                any()
            )
        } returns mockk()

        viewModel.onAttachToViewHolder()
        assert(viewModel.getBackgroundImage().value == "")
    }

    @Test
    fun `backgroundImage not present`() {
        viewModel.productCardsUseCase = productCardsUseCase
        val list = mockk<ArrayList<ComponentsItem>>()
        every { componentsItem.id } returns "s"
        every { componentsItem.pageEndPoint } returns "s"
        every { componentsItem.getComponentsItem() } returns list
        val mockProperties = mockk<Properties>()
        every { componentsItem.properties } returns mockProperties
        every { mockProperties.backgroundImageUrl } returns "url"
        coEvery {
            viewModel.productCardsUseCase?.loadFirstPageComponents(
                any(),
                any(),
                any()
            )
        } returns mockk()

        viewModel.onAttachToViewHolder()
        assert(viewModel.getBackgroundImage().value == "url")
    }

    @Test
    fun `backgroundImage is present`() {
        viewModel.productCardsUseCase = productCardsUseCase
        val list = ArrayList<ComponentsItem>()
        val dataList = arrayListOf<DataItem>()
        val dataItem = DataItem(hasNotifyMe = true)
        dataList.add(dataItem)
        val item = ComponentsItem(id = "2", pageEndPoint = "s", data = dataList)
        list.add(item)
        every { componentsItem.id } returns "s"
        every { componentsItem.pageEndPoint } returns "s"
        every { componentsItem.getComponentsItem() } returns list
        every { componentsItem.properties } returns null
        coEvery { application.applicationContext } returns context
        val productArray: ArrayList<ProductCardModel> = mockk(relaxed = true)
        coEvery {
            viewModel.productCardsUseCase?.loadFirstPageComponents(
                any(),
                any(),
                any()
            )
        } returns mockk()

        every {
            runBlocking {
                productArray.getMaxHeightForGridView(context = context, coroutineDispatcher = rule1.dispatchers.default, productImageWidth = 1)
            }
        } answers {
            0
        }

        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentData().value === componentsItem)
    }

    @Test
    fun `backgroundImage is present when list is empty`() {
        viewModel.productCardsUseCase = productCardsUseCase
        val list = ArrayList<ComponentsItem>()
        every { componentsItem.id } returns "s"
        every { componentsItem.pageEndPoint } returns "s"
        every { componentsItem.getComponentsItem() } returns list
        every { componentsItem.properties } returns null
        coEvery {
            viewModel.productCardsUseCase?.loadFirstPageComponents(
                any(),
                any(),
                any()
            )
        } returns mockk()

        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentData().value === componentsItem)
        assert(viewModel.getProductLoadState().value == true)
    }

    @Test
    fun `backgroundImage is present when getComponentsItem is null`() {
        viewModel.productCardsUseCase = productCardsUseCase
        val list = mockk<ArrayList<ComponentsItem>>()
        every { componentsItem.id } returns "s"
        every { componentsItem.pageEndPoint } returns "s"
        every { componentsItem.getComponentsItem() } returns list
        every { componentsItem.properties } returns null
        coEvery {
            viewModel.productCardsUseCase?.loadFirstPageComponents(
                any(),
                any(),
                any()
            )
        } returns mockk()

        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentData().value === componentsItem)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()

        unmockkStatic(::getComponent)
        unmockkConstructor(URLParser::class)
    }
}
