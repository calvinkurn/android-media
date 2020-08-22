package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.shop.home.view.model.ShopHomePlayCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopPageHomeLayoutUiModel
import com.tokopedia.shop.home.domain.CheckCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetCampaignNotifyMeUseCase
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.util.TestCoroutineDispatcherProviderImpl
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import javax.inject.Provider

@ExperimentalCoroutinesApi
class ShopHomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        TestCoroutineDispatcherProviderImpl
    }

    private val getShopProductUseCase: GqlGetShopProductUseCase = mockk(relaxed = true)
    @RelaxedMockK
    lateinit var getCampaignNotifyMeUseCase: Provider<GetCampaignNotifyMeUseCase>
    @RelaxedMockK
    lateinit var checkCampaignNotifyMeUseCase: Provider<CheckCampaignNotifyMeUseCase>
    private val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val userSessionInterface: UserSessionInterface = mockk(relaxed = true)
    private val getPlayWidgetUseCase: GetPlayWidgetUseCase = mockk(relaxed = true)
    private val playToggleChannelReminderUseCase: PlayToggleChannelReminderUseCase = mockk(relaxed = true)
    @RelaxedMockK
    lateinit var gqlCheckWishlistUseCaseProvider: Provider<GQLCheckWishlistUseCase>

    private lateinit var viewModel: ShopHomeViewModel
    private val mockShopId = "1234"
    private val mockSortId = 2
    private val mockPage = 2

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopHomeViewModel(
                userSessionInterface,
                getShopPageHomeLayoutUseCase,
                getShopProductUseCase,
                testCoroutineDispatcherProvider,
                addToCartUseCase,
                getPlayWidgetUseCase,
                playToggleChannelReminderUseCase,
                addWishListUseCase,
                removeWishListUseCase,
                gqlCheckWishlistUseCaseProvider,
                getCampaignNotifyMeUseCase,
                checkCampaignNotifyMeUseCase
        )
    }

    @Test
    fun `check whether response get home layout success is not null`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget()
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()

        viewModel.getShopPageHomeData(mockShopId, mockSortId)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeLayoutData.value is Success)
        assertTrue(viewModel.initialProductListData.value is Success)
        assertNotNull(viewModel.shopHomeLayoutData.value)
        assertNotNull(viewModel.initialProductListData.value)
    }

    @Test
    fun `check whether response get home layout error is null`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } throws Exception()
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getShopPageHomeData(mockShopId, mockSortId)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
            getShopProductUseCase.executeOnBackground()
        }

        assert(viewModel.shopHomeLayoutData.value is Fail)
        assertNull(viewModel.initialProductListData.value)
    }

    @Test
    fun `check whether response get lazy load product success is not null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()

        viewModel.getNewProductList(mockShopId, mockSortId, mockPage)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.newProductListData.value is Success)
        assertNotNull(viewModel.newProductListData.value)
    }

    @Test
    fun `check whether response get lazy load product failed is null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getNewProductList(mockShopId, mockSortId, mockPage)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.newProductListData.value is Fail)
    }

    @Test
    fun `check get data from play usecase`() {
        val mockShopId = "1234"

        coEvery { getPlayWidgetUseCase.executeOnBackground() } returns PlayBannerCarouselDataModel(
                channelList = listOf(
                        PlayBannerCarouselItemDataModel(),
                        PlayBannerCarouselItemDataModel()
                )
        )
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(
                        ShopLayoutWidget.Widget(
                                type = WidgetType.DYNAMIC,
                                name = WidgetName.PLAY_CAROUSEL_WIDGET
                        )
                )
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()

        viewModel.getShopPageHomeData(mockShopId)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeLayoutData.value is Success)
        assertTrue((viewModel.shopHomeLayoutData.value as? Success)!!.data.listWidget.filterIsInstance<ShopHomePlayCarouselUiModel>().isNotEmpty())
    }

    @Test
    fun `check refresh data from play usecase`() {
        val mockShopId = "1234"
        coEvery { viewModel.shopHomeLayoutData.value } returns Success(
                ShopPageHomeLayoutUiModel(
                        listWidget = listOf(
                                ShopHomePlayCarouselUiModel()
                        )
                )
        )
        coEvery { getPlayWidgetUseCase.executeOnBackground() } returns PlayBannerCarouselDataModel(
                channelList = listOf(
                        PlayBannerCarouselItemDataModel(),
                        PlayBannerCarouselItemDataModel()
                )
        )

        viewModel.onRefreshPlayBanner(mockShopId)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeLayoutData.value is Success && (viewModel.shopHomeLayoutData.value as Success).data.listWidget.filterIsInstance<ShopHomePlayCarouselUiModel>().isNotEmpty())
    }

}