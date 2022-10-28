package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class CarouselErrorLoadViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: CarouselErrorLoadViewModel =
            spyk(CarouselErrorLoadViewModel(application, componentsItem, 99))

    private val productCardUseCase: ProductCardsUseCase by lazy {
        mockk()
    }

    private val shopCardUseCase: ShopCardUseCase by lazy {
        mockk()
    }

    private val merchantVoucherUseCase: MerchantVoucherUseCase by lazy {
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
    }


    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for getParentComponentPosition`(){
        mockkObject(Utils)

        assert(viewModel.getParentComponentPosition() == Utils.getParentPosition(componentsItem))

        unmockkObject(Utils)
    }

    @Test
    fun `test for productCardUseCase`() {
        val viewModel: CarouselErrorLoadViewModel =
                spyk(CarouselErrorLoadViewModel(application, componentsItem, 99))

        val productCardUseCase = mockk<ProductCardsUseCase>()
        viewModel.productCardUseCase = productCardUseCase

        assert(viewModel.productCardUseCase === productCardUseCase)
    }

    @Test
    fun `test for shopCardUseCase`() {
        val viewModel: CarouselErrorLoadViewModel =
                spyk(CarouselErrorLoadViewModel(application, componentsItem, 99))

        val shopCardUseCase = mockk<ShopCardUseCase>()
        viewModel.shopCardUseCase = shopCardUseCase

        assert(viewModel.shopCardUseCase === shopCardUseCase)
    }

    /**************************** test for loadData() *******************************************/

    @Test
    fun `test for loadData for ShopCardView when getShopCardPaginatedData returns error`() {
        viewModel.shopCardUseCase = shopCardUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.ShopCardView.componentName
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.loadData()

        TestCase.assertEquals(viewModel.getShowLoaderStatus().value, false)
    }

    @Test
    fun `test for loadData for ShopCardView when getShopCardPaginatedData returns false`() {
        viewModel.shopCardUseCase = shopCardUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.ShopCardView.componentName
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.loadData()

        TestCase.assertEquals(viewModel.syncData.value, false)
    }

    @Test
    fun `test for loadData for ShopCardView when getShopCardPaginatedData returns true`() {
        viewModel.shopCardUseCase = shopCardUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.ShopCardView.componentName
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.loadData()

        TestCase.assertEquals(viewModel.syncData.value, true)

    }

    @Test
    fun `test for loadData for ProductCardCarousel when getCarouselPaginatedData returns error`() {
        viewModel.productCardUseCase = productCardUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery {
            productCardUseCase.getCarouselPaginatedData(
                    componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.loadData()

        TestCase.assertEquals(viewModel.getShowLoaderStatus().value, false)
    }

    @Test
    fun `test for loadData for ProductCardCarousel when getCarouselPaginatedData returns false`() {
        viewModel.productCardUseCase = productCardUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery {
            productCardUseCase.getCarouselPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.loadData()

        TestCase.assertEquals(viewModel.syncData.value, false)
    }

    @Test
    fun `test for loadData for ProductCardCarousel when getCarouselPaginatedData returns true`() {
        viewModel.productCardUseCase = productCardUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery {
            productCardUseCase.getCarouselPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.loadData()

        TestCase.assertEquals(viewModel.syncData.value, true)

    }
    @Test
    fun `test for loadData for MerchantVoucherCarousel when getCarouselPaginatedData returns error`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherCarousel.componentName
        coEvery {
            merchantVoucherUseCase.getCarouselPaginatedData(
                    componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.loadData()

        TestCase.assertEquals(viewModel.getShowLoaderStatus().value, false)
    }

    @Test
    fun `test for loadData for MerchantVoucherCarousel when getCarouselPaginatedData returns false`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherCarousel.componentName
        coEvery {
            merchantVoucherUseCase.getCarouselPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.loadData()

        TestCase.assertEquals(viewModel.syncData.value, false)
    }

    @Test
    fun `test for loadData for MerchantVoucherCarousel when getCarouselPaginatedData returns true`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherCarousel.componentName
        coEvery {
            merchantVoucherUseCase.getCarouselPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.loadData()

        TestCase.assertEquals(viewModel.syncData.value, true)

    }

    /**************************** end of loadData() *******************************************/

}
