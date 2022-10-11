package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsingle

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
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

class ProductCardSingleViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: ProductCardSingleViewModel =
        spyk(ProductCardSingleViewModel(application, componentsItem, 99))
    var productCardsUseCase: ProductCardsUseCase = mockk()
    private var context: Context = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    //<<        Tests just to increase coverage     >>
    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`() {
        assert(viewModel.application === application)
    }

    @Test
    fun `test for productCardUseCase`() {
        viewModel.productCardsUseCase = productCardsUseCase
        assert(viewModel.productCardsUseCase === productCardsUseCase)
    }
//<<        Tests just to increase coverage     >>

    @Test
    fun `test handle error function to set live data in case there was an error before`() {
        every { componentsItem.verticalProductFailState } returns true
        viewModel.onAttachToViewHolder()
        assert(viewModel.showErrorState.value == true)
    }

    @Test
    fun `test handle error function to set live data in case there was no error before`() {
        every { componentsItem.verticalProductFailState } returns false
        viewModel.onAttachToViewHolder()
        assert(viewModel.showErrorState.value == false)
    }

    @Test
    fun `test for mixLeft Data value to be set in live Data`() {
        every { componentsItem.properties } returns null
        viewModel.onAttachToViewHolder()
        assert(viewModel.getMixLeftData().value == null)

        every { componentsItem.properties?.mixLeft } returns null
        viewModel.onAttachToViewHolder()
        assert(viewModel.getMixLeftData().value == null)

        val mockedMixLeft: MixLeft = mockk()
        every { componentsItem.properties?.mixLeft } returns mockedMixLeft
        viewModel.onAttachToViewHolder()
        assert(viewModel.getMixLeftData().value === mockedMixLeft)

    }

    @Test
    fun `test for fetchProductData for success`() {
        viewModel.productCardsUseCase = productCardsUseCase
        viewModel.onAttachToViewHolder()
        coVerify {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        }

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        viewModel.onAttachToViewHolder()
        assert(childComponentsItem.properties?.template == Constant.ProductTemplate.LIST)
        assert(viewModel.getProductData().value === childComponentsItem)
    }

    @Test
    fun `test for fetchProductData for success and atc is allowed but product is OOS`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.atcButtonCTA } returns Constant.ATCButtonCTATypes.GENERAL_CART
        every { dataItem.isActiveProductCard } returns false
        viewModel.onAttachToViewHolder()
        assert(!dataItem.hasATCWishlist)
    }

    @Test
    fun `test for fetchProductData for success and atc is allowed but product is active`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.atcButtonCTA } returns Constant.ATCButtonCTATypes.GENERAL_CART
        every { dataItem.isActiveProductCard } returns true
        viewModel.onAttachToViewHolder()
        assert(dataItem.hasATCWishlist)
    }

    @Test
    fun `test for fetchProductData for success and atc is not allowed and product is OOS`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.atcButtonCTA } returns ""
        every { dataItem.isActiveProductCard } returns false
        viewModel.onAttachToViewHolder()
        assert(!dataItem.hasATCWishlist)
    }

    @Test
    fun `test for fetchProductData for success and atc is not allowed and product is active`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.atcButtonCTA } returns ""
        every { dataItem.isActiveProductCard } returns true
        viewModel.onAttachToViewHolder()
        assert(!dataItem.hasATCWishlist)
    }

    @Test
    fun `test for fetchProductData for success and product is active and targetComponentId is not present`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.isActiveProductCard } returns true
        every { dataItem.targetComponentId } returns ""
        viewModel.onAttachToViewHolder()
        assert(dataItem.hasSimilarProductWishlist == false||dataItem.hasSimilarProductWishlist == null)
    }

    @Test
    fun `test for fetchProductData for success and product is OOS and targetComponentId is not present`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.isActiveProductCard } returns false
        every { dataItem.targetComponentId } returns ""
        viewModel.onAttachToViewHolder()
        assert(dataItem.hasSimilarProductWishlist == false||dataItem.hasSimilarProductWishlist == null)
    }

    @Test
    fun `test for fetchProductData for success and product is active and targetComponentId is present`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.isActiveProductCard } returns true
        every { dataItem.targetComponentId } returns "3"
        viewModel.onAttachToViewHolder()
        assert(dataItem.hasSimilarProductWishlist == false||dataItem.hasSimilarProductWishlist == null)
    }

    @Test
    fun `test for fetchProductData for success and product is OOS and targetComponentId is present`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.isActiveProductCard } returns false
        every { dataItem.targetComponentId } returns "3"
        viewModel.onAttachToViewHolder()
        assert(dataItem.hasSimilarProductWishlist == true)
    }

    @Test
    fun `test for fetchProductData for success and 3dots is not allowed`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.show3Dots } returns false
        viewModel.onAttachToViewHolder()
        assert(!dataItem.hasThreeDotsWishlist)
    }

    @Test
    fun `test for fetchProductData for success and 3dots is allowed`() {
        viewModel.productCardsUseCase = productCardsUseCase

        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
        every { dataItem.show3Dots } returns true
        viewModel.onAttachToViewHolder()
        assert(dataItem.hasThreeDotsWishlist)
    }

    @Test
    fun `testing error flow of non-Network Exceptions fetchData`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val application: Application = mockk()
        val viewModel: ProductCardSingleViewModel =
            spyk(ProductCardSingleViewModel(application, componentsItem, 99))
        val productCardsUseCase: ProductCardsUseCase = mockk()
        viewModel.productCardsUseCase = productCardsUseCase

        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        }.throws(NullPointerException())

        viewModel.onAttachToViewHolder()
        assert(componentsItem.noOfPagesLoaded == 1)
        assert(viewModel.hideView.value == true)
        coVerify {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        }

    }

    @Test
    fun `testing error flow of Network Exceptions fetchData`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val application: Application = mockk()
        val viewModel: ProductCardSingleViewModel =
            spyk(ProductCardSingleViewModel(application, componentsItem, 99))
        val productCardsUseCase: ProductCardsUseCase = mockk()
        viewModel.productCardsUseCase = productCardsUseCase

        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        }.throws(UnknownHostException())

        viewModel.onAttachToViewHolder()
        assert(componentsItem.noOfPagesLoaded == 1)
        assert(componentsItem.verticalProductFailState)
        assert(viewModel.showErrorState.value == true)
        coVerify {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        }

    }

    @Test
    fun `test for reload functionality`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()

        val componentsItem: ComponentsItem = spyk()
        val viewModel: ProductCardSingleViewModel =
            spyk(ProductCardSingleViewModel(application, componentsItem, 99))
        val productCardsUseCase: ProductCardsUseCase = mockk()
        viewModel.productCardsUseCase = productCardsUseCase

        coEvery {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        } returns true
//        in case of error we have noOfPagesLoaded as 1
        componentsItem.noOfPagesLoaded = 1

        viewModel.reload()
        assert(componentsItem.noOfPagesLoaded == 0)
        coVerify {
            productCardsUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint,
                PRODUCT_PER_PAGE
            )
        }

    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}