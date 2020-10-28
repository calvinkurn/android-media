package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherOwner
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.shop.common.constant.PMAX_PARAM_KEY
import com.tokopedia.shop.common.constant.PMIN_PARAM_KEY
import com.tokopedia.shop.common.constant.RATING_PARAM_KEY
import com.tokopedia.shop.common.constant.SORT_PARAM_KEY
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
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
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.util.TestCoroutineDispatcherProviderImpl
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import rx.Observable
import java.lang.IllegalArgumentException
import java.lang.reflect.Field
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
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
    private val getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase = mockk(relaxed = true)
    private val getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase = mockk(relaxed = true)
    private val getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase = mockk(relaxed = true)
    private val gqlGetShopSortUseCase: GqlGetShopSortUseCase = mockk(relaxed = true)
    private val shopProductSortMapper: ShopProductSortMapper = mockk(relaxed = true)
    private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase = mockk(relaxed = true)

    @RelaxedMockK
    lateinit var gqlCheckWishlistUseCaseProvider: Provider<GQLCheckWishlistUseCase>

    private lateinit var viewModel: ShopHomeViewModel
    private val mockShopId = "1234"
    private val mockPage = 2
    private val shopProductFilterParameter = ShopProductFilterParameter().apply {
        setSortId("6")
        setMapData(
                mutableMapOf(
                        Pair(SORT_PARAM_KEY, "2"),
                        Pair(SORT_PARAM_KEY, "3"),
                        Pair(RATING_PARAM_KEY, "5"),
                        Pair(PMIN_PARAM_KEY,"2"),
                        Pair(PMAX_PARAM_KEY,"5")
                )
        )
    }

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
                gqlCheckWishlistUseCaseProvider,
                getYoutubeVideoUseCase,
                getCampaignNotifyMeUseCase,
                checkCampaignNotifyMeUseCase,
                getShopFilterBottomSheetDataUseCase,
                getShopFilterProductCountUseCase,
                gqlGetShopSortUseCase,
                shopProductSortMapper,
                getMerchantVoucherListUseCase
        )
    }

    @Test
    fun `check whether response get home layout success is not null`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget()
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter)

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

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter)

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

        viewModel.getNewProductList(mockShopId, mockPage, shopProductFilterParameter)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.newProductListData.value is Success)
        assertNotNull(viewModel.newProductListData.value)
    }

    @Test
    fun `check whether response get lazy load product failed is null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getNewProductList(mockShopId, mockPage, shopProductFilterParameter)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.newProductListData.value is Fail)
    }

    @Test
    fun `check get data from play usecase`() {
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

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, true)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeLayoutData.value is Success)
        assertTrue((viewModel.shopHomeLayoutData.value as? Success)!!.data.listWidget.filterIsInstance<ShopHomePlayCarouselUiModel>().isNotEmpty())
    }

    @Test
    fun `check refresh data from play usecase`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(
                        ShopLayoutWidget.Widget(
                                type = WidgetType.DYNAMIC,
                                name = WidgetName.PLAY_CAROUSEL_WIDGET
                        )
                )
        )

        coEvery { getPlayWidgetUseCase.executeOnBackground() } returns PlayBannerCarouselDataModel(
                channelList = listOf(
                        PlayBannerCarouselItemDataModel(),
                        PlayBannerCarouselItemDataModel()
                )
        )

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, true)

        val shopHomeLayoutData = viewModel.shopHomeLayoutData.observeAwaitValue()

        shopHomeLayoutData?.let {
            viewModel.onRefreshPlayBanner(mockShopId)
        }

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getPlayWidgetUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeLayoutData.value is Success && (viewModel.shopHomeLayoutData.value as Success).data.listWidget.filterIsInstance<ShopHomePlayCarouselUiModel>().isNotEmpty())
    }

    @Test
    fun `check whether get merchant voucher is success`() {
        val numVoucher = 10

        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(
                        ShopLayoutWidget.Widget(
                                type = WidgetType.VOUCHER,
                                name = WidgetName.VOUCHER,
                                data = listOf(ShopLayoutWidget.Widget.Data())
                        ),
                        ShopLayoutWidget.Widget(
                                type = WidgetType.DYNAMIC,
                                name = WidgetName.PLAY_CAROUSEL_WIDGET,
                                data = listOf(ShopLayoutWidget.Widget.Data())
                        )
                )
        )

        coEvery { getPlayWidgetUseCase.executeOnBackground() } returns PlayBannerCarouselDataModel(
                channelList = listOf(
                        PlayBannerCarouselItemDataModel(),
                        PlayBannerCarouselItemDataModel()
                )
        )

        coEvery {
            getMerchantVoucherListUseCase.createObservable(any())
        } returns Observable.just(arrayListOf(MerchantVoucherModel(
                voucherId = 10,
                voucherName = "voucherName",
                voucherCode = "1010002",
                validThru = "1020021",
                merchantVoucherOwner = MerchantVoucherOwner()
        )))

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, true)

        val shopHomeLayoutData = viewModel.shopHomeLayoutData.observeAwaitValue()

        shopHomeLayoutData?.let {
            viewModel.getMerchantVoucherList(mockShopId, numVoucher)
        }

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getPlayWidgetUseCase.executeOnBackground()
        }

        coVerify {
            getMerchantVoucherListUseCase.createObservable(any())
        }

        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Success)
    }

    @Test
    fun `check whether get merchant voucher is fail`() {
        val numVoucher = 10

        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(
                        ShopLayoutWidget.Widget(
                                type = WidgetType.VOUCHER,
                                name = WidgetName.VOUCHER
                        )
                )
        )

        coEvery {
            getMerchantVoucherListUseCase.createObservable(GetMerchantVoucherListUseCase.createRequestParams(mockShopId, numVoucher))
        } throws IllegalArgumentException()

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, true)

        val shopHomeLayoutData = viewModel.shopHomeLayoutData.observeAwaitValue()

        shopHomeLayoutData?.let {
            viewModel.getMerchantVoucherList(mockShopId, numVoucher)
        }

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Fail)
    }
}