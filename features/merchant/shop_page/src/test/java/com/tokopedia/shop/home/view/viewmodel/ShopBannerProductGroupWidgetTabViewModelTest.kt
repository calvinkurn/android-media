package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopBannerProductGroupWidgetTabViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }
    
    @RelaxedMockK
    lateinit var getShopProductUseCase: GqlGetShopProductUseCase
    
    @RelaxedMockK
    lateinit var getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase
    
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface
    
    private lateinit var viewModel: ShopBannerProductGroupWidgetTabViewModel
    private val shopId = "6555194"
    private val userAddress = LocalCacheModel()
    private val overrideTheme = false
    private val colorSchema = ShopPageColorSchema()


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopBannerProductGroupWidgetTabViewModel(
            testCoroutineDispatcherProvider,
            getShopProductUseCase, 
            getShopFeaturedProductUseCase, 
            userSession
        )
    }

    @Test
    fun `When get horizontal widget style, should return product item type only`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )
        
        val widgetStyle = "horizontal"
        val products = createProducts()
        val widgets = listOf(products)
        
        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    "",
                    true,
                    "",
                    false,
                    ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `When get vertical widget style and vertical banner exist, should return vertical banner item type and product item type at the same time to the UI`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "vertical"
        val verticalBanner = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 1,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.DISPLAY_SINGLE_COLUMN,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    ctaLink = "tokopedia://product/1444",
                    linkId = 0,
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = false
                )
            ) //Has vertical banner data
        )
        val products = createProducts()
        val widgets = listOf(verticalBanner, products)

        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                VerticalBannerItemType(
                    componentId = 1,
                    componentName =BannerProductGroupUiModel.Tab.ComponentList.ComponentName.DISPLAY_SINGLE_COLUMN ,
                    imageUrl = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    appLink = "tokopedia://product/1444",
                    id = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema()
                ),
                ProductItemType(
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    "",
                    true,
                    "",
                    false,
                    ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)
    }
    
    @Test
    fun `When get vertical widget style but no banner data found, should return product item type only`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "vertical"
        val verticalBanner = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 1,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.DISPLAY_SINGLE_COLUMN,
            data = listOf() // No vertical banner data
        )
        val products = createProducts()
        val widgets = listOf(verticalBanner, products)

        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    "",
                    true,
                    "",
                    false,
                    ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `When no vertical banner item type exist on widgets, should return product item type only`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "vertical"
        val products = createProducts()
        val widgets = listOf(products)

        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    "",
                    true,
                    "",
                    false,
                    ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)
    }
    
    @Test
    fun `When no vertical banner or product item type exist on widgets, should not fetch data to remote`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "vertical"
        val widgets = emptyList<BannerProductGroupUiModel.Tab.ComponentList>()

        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        coVerify(exactly = 0) {
            getShopProductUseCase.executeOnBackground()
        }
    }

    @Test
    fun `When product component is exist on widgets but has no data, should return null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "vertical"
        val products = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf() //Has no data
        )
        val widgets = listOf(products)

        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        coVerify(exactly = 0) {
            getShopProductUseCase.executeOnBackground()
        }
    }
    
    //region getProductsByProductMetadata
    @Test
    fun `When got featured product linkType, should return correct product with linkType FEATURED_PRODUCT`() {
        //Given
        val productId = "100"
        
        coEvery { getShopFeaturedProductUseCase.executeOnBackground() } returns listOf(
            ShopFeaturedProduct(
                productId = productId,
                labelGroupList = listOf(LabelGroup(title = "Terjual 2rb"))
            )
        )

        val widgetStyle = "horizontal"
        val featuredProduct = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = 8,
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.FEATURED_PRODUCT,
                    isShowProductInfo = true
                )
            )
        )
        val widgets = listOf(featuredProduct)

        //When
        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        //Then
        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    productId,
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "Terjual 2rb",
                    "tokopedia://product/$productId",
                    true,
                    productId,
                    false,
                    ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)
        
        coVerify { getShopFeaturedProductUseCase.executeOnBackground() }
    }
    //endregion
    private fun createFeaturedProducts(): BannerProductGroupUiModel.Tab.ComponentList {
        return BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = 8,
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = true
                )
            )
        )
    }

    private fun createShowcaseProducts(): BannerProductGroupUiModel.Tab.ComponentList {
        return BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = 8,
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = true
                )
            )
        )
    }
    
    private fun createProducts(): BannerProductGroupUiModel.Tab.ComponentList {
        return BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = 8,
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = true
                )
            )
        )
    }

}
