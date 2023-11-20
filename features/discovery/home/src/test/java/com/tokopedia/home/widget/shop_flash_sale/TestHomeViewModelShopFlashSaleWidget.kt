package com.tokopedia.home.widget.shop_flash_sale

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.mapper.ShopFlashSaleMapper
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.viewModel.homepageRevamp.createHomeViewModel
import com.tokopedia.home.viewModel.homepageRevamp.givenGetHomeDataReturn
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.widget.common.isSuccess
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.productcard.ProductCardModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by frenzel
 */
@FlowPreview
@ExperimentalCoroutinesApi
class TestHomeViewModelShopFlashSaleWidget {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val homeRecommendationUseCase = mockk<HomeRecommendationUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    private val mockShopId = "123"
    private val mockChannel = ChannelModel("123", "1234")
    private val mockHeader = ChannelHeader("123", "Channel header")
    private val mockTabList = listOf(
        ShopFlashSaleTabDataModel(ChannelGrid("1")),
        ShopFlashSaleTabDataModel(ChannelGrid("2")),
        ShopFlashSaleTabDataModel(ChannelGrid("3")),
    )
    private val mockSuccessItemList = listOf(
        CarouselProductCardDataModel(grid = ChannelGrid("1"), productModel = ProductCardModel()),
        CarouselProductCardDataModel(grid = ChannelGrid("2"), productModel = ProductCardModel()),
        CarouselProductCardDataModel(grid = ChannelGrid("3"), productModel = ProductCardModel())
    )
    private val mockSuccessItemList2 = listOf(
        CarouselProductCardDataModel(grid = ChannelGrid("3"), productModel = ProductCardModel()),
        CarouselProductCardDataModel(grid = ChannelGrid("2"), productModel = ProductCardModel()),
        CarouselProductCardDataModel(grid = ChannelGrid("1"), productModel = ProductCardModel())
    )

    private val mockInitialShopFlashSaleModel = ShopFlashSaleWidgetDataModel(
        id = "123",
        channelModel = mockChannel,
        channelHeader = mockHeader,
        tabList = mockTabList,
        itemList = mockSuccessItemList
    )

    private val mockSuccessShopFlashSaleRecom = mockInitialShopFlashSaleModel.copy(
        itemList = mockSuccessItemList2
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(ShopFlashSaleMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `given success when getting shop flash sale`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockInitialShopFlashSaleModel)
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeRecommendationUseCase = homeRecommendationUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        coEvery { homeRecommendationUseCase.getShopFlashSaleProducts(any(), any()) } returns mockSuccessShopFlashSaleRecom
        homeViewModel.getShopFlashSale(mockInitialShopFlashSaleModel, mockShopId)

        val newModel = homeViewModel.homeLiveDynamicChannel.value?.list?.find {
            it is ShopFlashSaleWidgetDataModel
        } as ShopFlashSaleWidgetDataModel

        assert(newModel.getStatus().isSuccess())

        coEvery { homeRecommendationUseCase.getShopFlashSaleProducts(any(), any()) } returns mockSuccessShopFlashSaleRecom
        homeViewModel.getShopFlashSale(newModel, mockShopId)
    }
}
