package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShopHomeBannerProductGroupItemType
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
            getShopProductUseCase, getShopFeaturedProductUseCase, userSession
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
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = true,
                    labelGroups = listOf(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected.data, actual.successOrNull())
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
                    linkId = "0",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = false
                )
            ) // Has vertical banner data
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
                    componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.DISPLAY_SINGLE_COLUMN,
                    imageUrl = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    appLink = "tokopedia://product/1444",
                    id = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema(),
                    verticalBannerHeight = 0
                ),
                ProductItemType(
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = true,
                    labelGroups = emptyList(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema()
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
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = true,
                    labelGroups = listOf(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema()
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
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = true,
                    labelGroups = listOf(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema()
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
            data = listOf() // Has no data
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
        // Given
        val productId = "100"

        coEvery { getShopFeaturedProductUseCase.executeOnBackground() } returns listOf(
            ShopFeaturedProduct(
                productId = productId,
                labelGroupList = listOf(LabelGroup(title = "Terjual 2rb"), LabelGroup(title = "Dilayani Tokopedia"))
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
                    linkId = "8",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.FEATURED_PRODUCT,
                    isShowProductInfo = true
                )
            )
        )
        val widgets = listOf(featuredProduct)

        // When
        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        // Then
        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    productId,
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "tokopedia://product/$productId",
                    showProductInfo = true,
                    labelGroups = listOf(LabelGroup(title = "Terjual 2rb"), LabelGroup(title = "Dilayani Tokopedia")),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = productId,
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)

        coVerify { getShopFeaturedProductUseCase.executeOnBackground() }
    }

    @Test
    fun `When get featured product linkType with overrideTheme true and showProductInfo false, should return correct product item type with with overrideTheme true and showProductInfo false`() {
        // Given
        val overrideTheme = true
        val showProductInfo = false
        val productId = "100"

        coEvery { getShopFeaturedProductUseCase.executeOnBackground() } returns listOf(
            ShopFeaturedProduct(
                productId = productId,
                labelGroupList = listOf(LabelGroup(title = "Terjual 2rb"), LabelGroup(title = "Dilayani Tokopedia"))
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
                    linkId = "8",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.FEATURED_PRODUCT,
                    isShowProductInfo = showProductInfo
                )
            )
        )
        val widgets = listOf(featuredProduct)

        // When
        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        // Then
        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    productId = productId,
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "tokopedia://product/$productId",
                    showProductInfo = showProductInfo,
                    labelGroups = listOf(LabelGroup(title = "Terjual 2rb"), LabelGroup(title = "Dilayani Tokopedia")),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = productId,
                    overrideTheme = overrideTheme,
                    colorSchema = ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)

        coVerify { getShopFeaturedProductUseCase.executeOnBackground() }
    }

    @Test
    fun `When get product linkType with overrideTheme true and showProductInfo false, should return correct product item type with with overrideTheme true and showProductInfo false`() {
        val overrideTheme = true
        val showProductInfo = false

        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "horizontal"
        val products = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = "8",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = showProductInfo
                )
            )
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

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = showProductInfo,
                    labelGroups = listOf(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = overrideTheme,
                    colorSchema = ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `When get showcase linkType with showProductInfo false, should return correct product item type with showProductInfo false`() {
        // Given
        val showProductInfo = false

        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "horizontal"
        val featuredProduct = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = "8",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.SHOWCASE,
                    isShowProductInfo = showProductInfo
                )
            )
        )
        val widgets = listOf(featuredProduct)

        // When
        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        // Then
        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = showProductInfo,
                    labelGroups = listOf(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = overrideTheme,
                    colorSchema = ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)

        coVerify { getShopProductUseCase.executeOnBackground() }
    }

    @Test
    fun `When get showcase linkType with showProductInfo true, should return correct product item type with showProductInfo true`() {
        // Given
        val showProductInfo = true

        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "horizontal"
        val featuredProduct = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = "8",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.SHOWCASE,
                    isShowProductInfo = showProductInfo
                )
            )
        )
        val widgets = listOf(featuredProduct)

        // When
        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        // Then
        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = showProductInfo,
                    labelGroups = listOf(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = overrideTheme,
                    colorSchema = ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)

        coVerify { getShopProductUseCase.executeOnBackground() }
    }

    @Test
    fun `When got showcase product linkType, should get data to remote`() {
        // Given
        val overrideTheme = true

        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val widgetStyle = "horizontal"
        val featuredProduct = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = "8",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.SHOWCASE,
                    isShowProductInfo = false
                )
            )
        )
        val widgets = listOf(featuredProduct)

        // When
        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        // Then
        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        val expected = ShopBannerProductGroupWidgetTabViewModel.UiState.Success(
            listOf(
                ProductItemType(
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = false,
                    labelGroups = listOf(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = true,
                    colorSchema = ShopPageColorSchema()
                )
            )
        )
        assertEquals(expected, actual)

        coVerify { getShopProductUseCase.executeOnBackground() }
    }

    //endregion

    @Test
    fun `When get shop product error, observer should receive error as well`() {
        // Given
        val overrideTheme = true
        val error = MessageErrorException("Server error")

        coEvery { getShopProductUseCase.executeOnBackground() } throws error

        val widgetStyle = "horizontal"
        val featuredProduct = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = "8",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.SHOWCASE,
                    isShowProductInfo = false
                )
            )
        )
        val widgets = listOf(featuredProduct)

        // When
        viewModel.getCarouselWidgets(
            widgets,
            shopId,
            userAddress,
            widgetStyle,
            overrideTheme,
            colorSchema
        )

        // Then
        val actual = viewModel.carouselWidgets.getOrAwaitValue()
        assertEquals(ShopBannerProductGroupWidgetTabViewModel.UiState.Error(error), actual)
        assertEquals(error, actual.errorOrNull())
        coVerify { getShopProductUseCase.executeOnBackground() }
    }

    private fun createProducts(): BannerProductGroupUiModel.Tab.ComponentList {
        return BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 2,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.PRODUCT,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "",
                    ctaLink = "",
                    linkId = "8",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = true
                )
            )
        )
    }

    @Test
    fun `When get product on product carousel vertical banner, should set the vertical banner height to the tallest of product card height`() {
        // Given
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct())
        )

        val verticalBanner = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 1,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.DISPLAY_SINGLE_COLUMN,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    ctaLink = "tokopedia://product/1444",
                    linkId = "0",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = false
                )
            )
        )

        val widgetStyle = BannerProductGroupUiModel.WidgetStyle.VERTICAL.id
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

        // When
        viewModel.refreshVerticalBannerHeight(productCardHeight = 56)

        // Then
        val actual = viewModel.verticalProductCarousel.getOrAwaitValue()
        val expected =
            listOf(
                VerticalBannerItemType(
                    componentId = 1,
                    componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.DISPLAY_SINGLE_COLUMN,
                    imageUrl = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    appLink = "tokopedia://product/1444",
                    id = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema(),
                    verticalBannerHeight = 56
                ),
                ProductItemType(
                    productId = "",
                    imageUrl = "",
                    name = "",
                    price = "",
                    slashedPrice = "",
                    slashedPricePercent = 0,
                    rating = "",
                    appLink = "",
                    showProductInfo = true,
                    labelGroups = listOf(),
                    badges = listOf(),
                    freeOngkir = FreeOngkir(isActive = false, imgUrl = ""),
                    id = "",
                    overrideTheme = false,
                    colorSchema = ShopPageColorSchema()
                )
            )

        assertEquals(expected, actual)
    }

    @Test
    fun `When get product on product carousel vertical banner return error, verticalProductCarousel should have empty list`() {
        // Given
        coEvery { getShopProductUseCase.executeOnBackground() } throws MessageErrorException("Internal Server Error")

        val verticalBanner = BannerProductGroupUiModel.Tab.ComponentList(
            componentId = 1,
            componentName = BannerProductGroupUiModel.Tab.ComponentList.ComponentName.DISPLAY_SINGLE_COLUMN,
            data = listOf(
                BannerProductGroupUiModel.Tab.ComponentList.Data(
                    imageUrl = "https://images.tokopedia.net/img/VqbcmM/2023/8/24/76bf4387-e3f6-4dd3-b041-c03dcfebc9e4.jpg",
                    ctaLink = "tokopedia://product/1444",
                    linkId = "0",
                    linkType = BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType.PRODUCT,
                    isShowProductInfo = false
                )
            )
        )

        val widgetStyle = BannerProductGroupUiModel.WidgetStyle.VERTICAL.id
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

        // When
        viewModel.refreshVerticalBannerHeight(productCardHeight = 56)

        // Then
        val actual = viewModel.verticalProductCarousel.getOrAwaitValue()
        val expected = listOf<ShopHomeBannerProductGroupItemType>()

        assertEquals(expected, actual)
    }

    private fun ShopBannerProductGroupWidgetTabViewModel.UiState.successOrNull(): List<ShopHomeBannerProductGroupItemType>? {
        return when (this) {
            is ShopBannerProductGroupWidgetTabViewModel.UiState.Error -> null
            ShopBannerProductGroupWidgetTabViewModel.UiState.Loading -> null
            is ShopBannerProductGroupWidgetTabViewModel.UiState.Success -> data
        }
    }

    private fun ShopBannerProductGroupWidgetTabViewModel.UiState.errorOrNull(): Throwable? {
        return when (this) {
            is ShopBannerProductGroupWidgetTabViewModel.UiState.Error -> error
            ShopBannerProductGroupWidgetTabViewModel.UiState.Loading -> null
            is ShopBannerProductGroupWidgetTabViewModel.UiState.Success -> null
        }
    }
}
