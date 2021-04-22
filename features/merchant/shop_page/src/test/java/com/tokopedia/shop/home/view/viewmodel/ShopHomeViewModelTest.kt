package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.play.widget.data.*
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.common.constant.PMAX_PARAM_KEY
import com.tokopedia.shop.common.constant.PMIN_PARAM_KEY
import com.tokopedia.shop.common.constant.RATING_PARAM_KEY
import com.tokopedia.shop.common.constant.SORT_PARAM_KEY
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.domain.CheckCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import javax.inject.Provider

@ExperimentalCoroutinesApi
class ShopHomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    @RelaxedMockK
    lateinit var getShopProductUseCase: GqlGetShopProductUseCase
    @RelaxedMockK
    lateinit var getCampaignNotifyMeUseCase: Provider<GetCampaignNotifyMeUseCase>
    @RelaxedMockK
    lateinit var checkCampaignNotifyMeUseCase: Provider<CheckCampaignNotifyMeUseCase>
    @RelaxedMockK
    lateinit var getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase
    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase
    @RelaxedMockK
    lateinit var addToCartOccUseCase: AddToCartOccUseCase
    @RelaxedMockK
    lateinit var getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase
    @RelaxedMockK
    lateinit var getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase
    @RelaxedMockK
    lateinit var getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase
    @RelaxedMockK
    lateinit var gqlGetShopSortUseCase: GqlGetShopSortUseCase
    @RelaxedMockK
    lateinit var mvcSummaryUseCase: MVCSummaryUseCase
    @RelaxedMockK
    lateinit var gqlCheckWishlistUseCaseProvider: Provider<GQLCheckWishlistUseCase>
    @RelaxedMockK
    lateinit var playWidgetTools: PlayWidgetTools
    @RelaxedMockK
    lateinit var shopProductSortMapper: ShopProductSortMapper
    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface
    private lateinit var viewModel: ShopHomeViewModel

    private val mockShopId = "1234"
    private val mockCampaignId = "123"
    private val mockPage = 2
    private val shopProductFilterParameter = ShopProductFilterParameter().apply {
        setSortId("6")
        setMapData(
                mutableMapOf(
                        Pair(SORT_PARAM_KEY, "2"),
                        Pair(SORT_PARAM_KEY, "3"),
                        Pair(RATING_PARAM_KEY, "5"),
                        Pair(PMIN_PARAM_KEY, "2"),
                        Pair(PMAX_PARAM_KEY, "5")
                )
        )
    }
    private val addressWidgetData: LocalCacheModel =LocalCacheModel()
    private val playWidgetUiModelMockData  = PlayWidgetUiModel.Small(
            "title",
            "action title",
            "applink",
            true,
            PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
            ),
            true,
            listOf()
    )


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopHomeViewModel(
                userSessionInterface,
                getShopPageHomeLayoutUseCase,
                getShopProductUseCase,
                testCoroutineDispatcherProvider,
                addToCartUseCase,
                addToCartOccUseCase,
                gqlCheckWishlistUseCaseProvider,
                getYoutubeVideoUseCase,
                getCampaignNotifyMeUseCase,
                checkCampaignNotifyMeUseCase,
                getShopFilterBottomSheetDataUseCase,
                getShopFilterProductCountUseCase,
                gqlGetShopSortUseCase,
                shopProductSortMapper,
                mvcSummaryUseCase,
                playWidgetTools
        )
    }
    @Test
    fun `check whether home layout and product list response is success if initial product list data is null`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget()
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, null, addressWidgetData)
        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
            getShopProductUseCase.executeOnBackground()
        }
        assertTrue(viewModel.shopHomeLayoutData.value is Success)
        assertTrue(viewModel.productListData.value is Success)
        assertNotNull(viewModel.shopHomeLayoutData.value)
        assertNotNull(viewModel.productListData.value)
    }

    @Test
    fun `check whether home layout and product list response is success if initial product list data is not null`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget()
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
        }
        assertTrue(viewModel.shopHomeLayoutData.value is Success)
        assertTrue(viewModel.productListData.value is Success)
        assertNotNull(viewModel.shopHomeLayoutData.value)
        assertNotNull(viewModel.productListData.value)
    }

    @Test
    fun `check whether home layout and product list value is fail if response throw exception`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } throws Exception()
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, null, addressWidgetData)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
            getShopProductUseCase.executeOnBackground()
        }

        assert(viewModel.shopHomeLayoutData.value is Fail)
        assertNull(viewModel.productListData.value)
    }

    @Test
    fun `check whether response get lazy load product success is not null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(),ShopProduct())
        )

        viewModel.getNewProductList(mockShopId, mockPage, shopProductFilterParameter, addressWidgetData)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productListData.value is Success)
        assertNotNull(viewModel.productListData.value)
    }

    @Test
    fun `check whether response get lazy load product failed is null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getNewProductList(mockShopId, mockPage, shopProductFilterParameter, addressWidgetData)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productListData.value is Fail)
    }

    @Test
    fun `check get data from play usecase`() {
        coEvery { playWidgetTools.getWidgetFromNetwork(PlayWidgetUseCase.WidgetType.ShopPage("")) } returns PlayWidget()

        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(
                        ShopLayoutWidget.Widget(
                                type = WidgetType.DYNAMIC,
                                name = WidgetName.PLAY_CAROUSEL_WIDGET
                        )
                )
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeLayoutData.value is Success)
        assertTrue((viewModel.shopHomeLayoutData.value as? Success)!!.data.listWidget.filterIsInstance<CarouselPlayWidgetUiModel>().isNotEmpty())
    }

    @Test
    fun `check whether get merchant voucher is success`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(
                        ShopLayoutWidget.Widget(
                                name = WidgetName.VOUCHER_STATIC
                        )
                )
        )
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)

        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } returns TokopointsCatalogMVCSummaryResponse(null)
        viewModel.getMerchantVoucherCoupon(mockShopId)
        coVerify { mvcSummaryUseCase.getResponse(any()) }
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Success)
    }

    @Test
    fun `check whether get merchant voucher is fail`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(
                        ShopLayoutWidget.Widget(
                                name = WidgetName.VOUCHER_STATIC
                        )
                )
        )
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)

        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } throws Throwable()
        viewModel.getMerchantVoucherCoupon(mockShopId)
        coVerify { mvcSummaryUseCase.getResponse(any()) }
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Fail)
    }

    @Test
    fun `get merchant voucher and if it fails getting shopHomeLayoutData`() {
        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } returns TokopointsCatalogMVCSummaryResponse(null)
        viewModel.getMerchantVoucherCoupon(mockShopId)
    }

    @Test
    fun `get merchant voucher and it has no voucher static widget`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(
                        ShopLayoutWidget.Widget(
                                name = WidgetName.PRODUCT
                        )
                )
        )
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        viewModel.getMerchantVoucherCoupon(mockShopId)
        assertTrue(viewModel.shopHomeLayoutData.value is Success)
    }

    @Test
    fun `check whether userSessionShopId return empty string when userSession shopId return null`() {
        every { userSessionInterface.shopId } returns null
        assert(viewModel.userSessionShopId == "")
    }

    @Test
    fun `check whether onSuccessAddToCart is called`() {
        val mockProductId = "456"
        val mockProductName = "product mock"
        val mockDisplayedPrice = "Rp. 1000"

        val onSuccessAddToCart: (DataModel) -> Unit = mockk(relaxed = true)
        every { addToCartUseCase.createObservable(any()) } returns Observable.just(AddToCartDataModel(
                data = DataModel(success = 1)
        ))
        viewModel.addProductToCart(
                ShopHomeProductUiModel().apply {
                    id = mockProductId
                    name = mockProductName
                    displayedPrice = mockDisplayedPrice
                },
                mockShopId,
                onSuccessAddToCart,
                {}
        )
        verify { onSuccessAddToCart.invoke(any()) }
    }

    @Test
    fun `check whether onErrorAddToCart is called when not success`() {
        val onErrorAddToCart: (Throwable) -> Unit = mockk(relaxed = true)
        every { addToCartUseCase.createObservable(any()) } returns Observable.just(AddToCartDataModel(
                data = DataModel(success = 0)
        ))
        viewModel.addProductToCart(
                ShopHomeProductUiModel(),
                mockShopId,
                {},
                onErrorAddToCart
        )
        verify { onErrorAddToCart.invoke(any()) }
    }

    @Test
    fun `check whether onErrorAddToCart is called when throw exception`() {
        val onErrorAddToCart: (Throwable) -> Unit = mockk(relaxed = true)
        every { addToCartUseCase.createObservable(any()) } throws Throwable()
        viewModel.addProductToCart(
                ShopHomeProductUiModel(),
                mockShopId,
                {},
                onErrorAddToCart
        )
        verify { onErrorAddToCart.invoke(any()) }
    }

    @Test
    fun `check whether onSuccessAddToCart is called when call addProductToCartOcc success`() {
        val mockProductId = "456"
        val mockProductName = "product mock"
        val mockDisplayedPrice = "Rp. 1000"
        val onSuccessAddToCart: (DataModel) -> Unit = mockk(relaxed = true)
        every { addToCartOccUseCase.createObservable(any()) } returns Observable.just(AddToCartDataModel(
                data = DataModel(success = 1)
        ))
        viewModel.addProductToCartOcc(
                ShopHomeProductUiModel().apply {
                    id = mockProductId
                    name = mockProductName
                    displayedPrice = mockDisplayedPrice
                },
                mockShopId,
                onSuccessAddToCart,
                {}
        )
        verify { onSuccessAddToCart.invoke(any()) }
    }

    @Test
    fun `check whether onErrorAddToCart is called when call addProductToCartOcc error`() {
        val onErrorAddToCart: (Throwable) -> Unit = mockk(relaxed = true)
        every { addToCartOccUseCase.createObservable(any()) } throws Throwable()
        viewModel.addProductToCartOcc(
                ShopHomeProductUiModel(),
                mockShopId,
                {},
                onErrorAddToCart
        )
        verify { onErrorAddToCart.invoke(any()) }
    }

    @Test
    fun `check whether getShopProductUseCase clearCache is called`() {
        viewModel.clearGetShopProductUseCase()
        verify { getShopProductUseCase.clearCache() }
    }

    @Test
    fun `check whether checkWishlistData post Success value`() {
        val mockProductId = "123"
        coEvery { gqlCheckWishlistUseCaseProvider.get().executeOnBackground() } returns listOf(
                CheckWishlistResult()
        )
        viewModel.getWishlistStatus(listOf(
                ShopHomeCarousellProductUiModel(
                        productList = listOf(
                                ShopHomeProductUiModel().apply { id = null },
                                ShopHomeProductUiModel().apply { id = mockProductId }
                        )
                )
        ))
        assert(viewModel.checkWishlistData.value is Success)
    }

    @Test
    fun `check whether videoYoutube has Success value`() {
        val mockVideoUrl = "mock video url"
        val mockWidgetId = "11"
        coEvery { getYoutubeVideoUseCase.executeOnBackground() } returns mapOf(
                YoutubeVideoDetailModel::class.java to RestResponse(
                        YoutubeVideoDetailModel(),
                        1,
                        true
                )
        )
        viewModel.getVideoYoutube(mockVideoUrl, mockWidgetId)
        coVerify { getYoutubeVideoUseCase.executeOnBackground() }
        assert(viewModel.videoYoutube.value?.second is Success)
    }

    @Test
    fun `check whether videoYoutube has Fail value if YoutubeVideoDetailModel key not found on the map`() {
        val mockVideoUrl = "mock video url"
        val mockWidgetId = "11"
        coEvery { getYoutubeVideoUseCase.executeOnBackground() } returns mapOf()
        viewModel.getVideoYoutube(mockVideoUrl, mockWidgetId)
        coVerify { getYoutubeVideoUseCase.executeOnBackground() }
        assert(viewModel.videoYoutube.value?.second is Fail)
    }

    @Test
    fun `check whether required function called when clearCache`() {
        viewModel.clearCache()
        coVerify {
            getShopProductUseCase.clearCache()
            getShopPageHomeLayoutUseCase.clearCache()
        }
    }

    @Test
    fun `check whether campaignNplRemindMeStatusData post Success value`() {
        coEvery { getCampaignNotifyMeUseCase.get().executeOnBackground() } returns GetCampaignNotifyMeModel()
        viewModel.getCampaignNplRemindMeStatus(ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem())
        coVerify { getCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.campaignNplRemindMeStatusData.value is Success)
    }

    @Test
    fun `check whether campaignNplRemindMeStatusData value is null when error`() {
        coEvery { getCampaignNotifyMeUseCase.get().executeOnBackground() } throws Throwable()
        viewModel.getCampaignNplRemindMeStatus(ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem())
        coVerify { getCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.campaignNplRemindMeStatusData.value == null)
    }

    @Test
    fun `check whether checkCampaignNplRemindMeStatusData post Success value`() {
        val mockAction = "action"
        coEvery { checkCampaignNotifyMeUseCase.get().executeOnBackground() } returns CheckCampaignNotifyMeModel()
        viewModel.clickRemindMe(mockCampaignId, mockAction)
        coVerify { checkCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.checkCampaignNplRemindMeStatusData.value is Success)
    }

    @Test
    fun `check whether checkCampaignNplRemindMeStatusData post Fail value`() {
        val mockAction = "action"
        coEvery { checkCampaignNotifyMeUseCase.get().executeOnBackground() } throws Throwable()
        viewModel.clickRemindMe(mockCampaignId, mockAction)
        coVerify { checkCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.checkCampaignNplRemindMeStatusData.value is Fail)
    }

    @Test
    fun `check whether bottomSheetFilterLiveData post Success value`() {
        coEvery { getShopFilterBottomSheetDataUseCase.executeOnBackground() } returns DynamicFilterModel()
        viewModel.getBottomSheetFilterData()
        coVerify { getShopFilterBottomSheetDataUseCase.executeOnBackground() }
        assert(viewModel.bottomSheetFilterLiveData.value is Success)
    }

    @Test
    fun `check whether bottomSheetFilterLiveData value is null when error`() {
        coEvery { getShopFilterBottomSheetDataUseCase.executeOnBackground() } throws Throwable()
        viewModel.getBottomSheetFilterData()
        coVerify { getShopFilterBottomSheetDataUseCase.executeOnBackground() }
        assert(viewModel.bottomSheetFilterLiveData.value == null)
    }

    @Test
    fun `check whether shopProductFilterCountLiveData post Success value`() {
        val mockTotalProduct = 10
        coEvery { getShopFilterProductCountUseCase.executeOnBackground() } returns mockTotalProduct
        viewModel.getFilterResultCount(mockShopId, ShopProductFilterParameter(), addressWidgetData)
        coVerify { getShopFilterProductCountUseCase.executeOnBackground() }
        val shopProductFilterCountValue = viewModel.shopProductFilterCountLiveData.value
        assert(shopProductFilterCountValue is Success)
        assert((shopProductFilterCountValue as? Success)?.data == mockTotalProduct)
    }

    @Test
    fun `check whether playWidgetToggleReminderObservable success value is true`() {
        val mockChannelId = "123"
        val mockPlayWidgetReminder = PlayWidgetReminder()

        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { playWidgetTools.updateToggleReminder(any(), any(), any()) } returns mockPlayWidgetReminder
        coEvery { playWidgetTools.mapWidgetToggleReminder(mockPlayWidgetReminder) } returns true

        viewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, PlayWidgetReminderType.Remind)

        coVerify { playWidgetTools.updateToggleReminder(any(), any(), any()) }

        assert(viewModel.playWidgetReminderObservable.value is Success)
    }

    @Test
    fun `check whether playWidgetToggleReminderObservable success value is false`() {
        val mockChannelId = "123"

        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { playWidgetTools.updateToggleReminder(any(), any(), any()) } throws Throwable()

        viewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, PlayWidgetReminderType.Remind)

        coVerify { playWidgetTools.updateToggleReminder(any(), any(), any()) }

        assert(viewModel.playWidgetReminderObservable.value is Fail)
    }

    @Test
    fun `check whether playWidgetReminderEvent value is not null when on login`() {
        val mockChannelId = "123"
        every { userSessionInterface.isLoggedIn } returns false
        viewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, PlayWidgetReminderType.Remind)
        assert(viewModel.playWidgetReminderEvent.value?.first == mockChannelId)
        assert(viewModel.playWidgetReminderEvent.value?.second == PlayWidgetReminderType.Remind)
    }

    @Test
    fun `check whether getSortNameById return mocked sortName`() {
        val mockSortId = "123"
        val mockSortName = "sort 1"

        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget()
        coEvery { gqlGetShopSortUseCase.executeOnBackground()} returns listOf()
        coEvery { shopProductSortMapper.convertSort(any())} returns mutableListOf(
                ShopProductSortModel().apply {
                    value  = mockSortId
                    name  = mockSortName
                }
        )
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        assert(viewModel.getSortNameById(mockSortId) == mockSortName)
    }

    @Test
    fun `check whether getSortNameById return empty when sort id didn't matched`() {
        val mockSortId = "123"
        val mockSortName = "sort 1"

        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget()
        coEvery { gqlGetShopSortUseCase.executeOnBackground()} returns listOf()
        coEvery { shopProductSortMapper.convertSort(any())} returns mutableListOf(
                ShopProductSortModel().apply {
                    value  = mockSortId
                    name  = mockSortName
                }
        )
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        assert(viewModel.getSortNameById("").isEmpty())
    }

    @Test
    fun `check whether playWidgetObservable value is not null when get data is success`() {
        val playWidgetMock = PlayWidget()
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(ShopLayoutWidget.Widget(type ="dynamic" ))
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId)
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }

    @Test
    fun `check whether playWidgetObservable value is null when shopHomeLayoutData value is fail`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } throws Exception()
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        viewModel.getPlayWidget(mockShopId)
        assert(viewModel.playWidgetObservable.value == null)
    }

    @Test
    fun `check whether playWidgetObservable value is null when play widget not found on shopHomeLayoutData`() {
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(ShopLayoutWidget.Widget(
                        type = "promo",
                        data = listOf(ShopLayoutWidget.Widget.Data())
                ))
        )
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        viewModel.getPlayWidget(mockShopId)
        assert(viewModel.playWidgetObservable.value == null)
    }

    @Test
    fun `check whether isCampaignFollower return true if matched campaign found and dynamicRuleDescription is not empty`() {
        (viewModel.shopHomeLayoutData as MutableLiveData<Result<ShopPageHomeLayoutUiModel>>).value = Success(ShopPageHomeLayoutUiModel(
                listWidget = listOf(ShopHomeNewProductLaunchCampaignUiModel(
                        data = listOf(ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem(
                                campaignId = mockCampaignId,
                                dynamicRule = ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.DynamicRule(
                                        descriptionHeader = "header"
                                )
                        ))
                ))
        ))
        assert(viewModel.isCampaignFollower(mockCampaignId))
    }

    @Test
    fun `check whether isCampaignFollower return false if matched campaign found but dynamicRuleDescription is empty`() {
        (viewModel.shopHomeLayoutData as MutableLiveData<Result<ShopPageHomeLayoutUiModel>>).value = Success(ShopPageHomeLayoutUiModel(
                listWidget = listOf(ShopHomeNewProductLaunchCampaignUiModel(
                        data = listOf(ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem(
                                campaignId = mockCampaignId,
                                dynamicRule = ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.DynamicRule(
                                        descriptionHeader = ""
                                )
                        ))
                ))
        ))
        assert(!viewModel.isCampaignFollower(mockCampaignId))
    }

    @Test
    fun `check whether isCampaignFollower return false if home layout data is fail`() {
        (viewModel.shopHomeLayoutData as MutableLiveData<Result<ShopPageHomeLayoutUiModel>>).value = Fail(Exception())
        assert(!viewModel.isCampaignFollower(mockCampaignId))
    }

    @Test
    fun `check whether isCampaignFollower return false if ui model data value is null`() {
        (viewModel.shopHomeLayoutData as MutableLiveData<Result<ShopPageHomeLayoutUiModel>>).value = Success(ShopPageHomeLayoutUiModel(
                listWidget = listOf(ShopHomeNewProductLaunchCampaignUiModel(data = null)))
        )
        assert(!viewModel.isCampaignFollower(mockCampaignId))
    }

    @Test
    fun `check play widget success delete channel`() {
        val channelId = "123"
        val playWidgetMock = PlayWidget()
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(ShopLayoutWidget.Widget(type ="dynamic" ))
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        coEvery {
            playWidgetTools.updateDeletedChannel(any(), channelId)
        } returns playWidgetUiModelMockData
        viewModel.deleteChannel("123")
        coVerify {
            playWidgetTools.deleteChannel(channelId, any())
        }
        assert(viewModel.playWidgetObservable.value?.actionEvent?.peekContent() is CarouselPlayWidgetUiModel.Action.Delete)
    }

    @Test
    fun `check play widget fail delete channel`() {
        val channelId = "123"
        val playWidgetMock = PlayWidget()
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(ShopLayoutWidget.Widget(type ="dynamic" ))
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        coEvery {
            playWidgetTools.updateDeletedChannel(any(), channelId)
        } throws Exception()
        viewModel.deleteChannel("123")
        coVerify {
            playWidgetTools.deleteChannel(channelId, any())
        }
        assert(viewModel.playWidgetObservable.value?.actionEvent?.peekContent() is CarouselPlayWidgetUiModel.Action.DeleteFailed)
    }

    @Test
    fun `check if playWidgetObservableplay value is updated`() {
        val playWidgetMock = PlayWidget()
        val mockChannelId = "123"
        val mockTotalView = "50"
        val playWidgetUiModelMockDataWithTotalView = PlayWidgetUiModel.Small(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                        true,
                        1000,
                        true,
                        1,
                        1,
                        2,
                        1
                ),
                true,
                listOf(
                        PlayWidgetSmallChannelUiModel(
                                mockChannelId,
                                "",
                                "",
                                "",
                                "",
                                mockTotalView,
                                true,
                                true,
                                PlayWidgetVideoUiModel("", false, "", ""),
                                PlayWidgetChannelType.Upcoming
                        )
                )
        )
        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget(
                listWidget = listOf(ShopLayoutWidget.Widget(type = "dynamic"))
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId)
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        every {
            playWidgetTools.updateTotalView(playWidgetUiModelMockData,mockChannelId,mockTotalView)
        } returns playWidgetUiModelMockDataWithTotalView
        viewModel.updatePlayWidgetTotalView(
                mockChannelId,
                mockTotalView
        )
        val playWidgetUiModel = (viewModel.playWidgetObservable.value?.widgetUiModel as? PlayWidgetUiModel.Small)
        assert((playWidgetUiModel?.items?.first() as? PlayWidgetSmallChannelUiModel)?.totalView == mockTotalView)
    }

    @Test
    fun `check if playWidgetObservableplay value is null when channelId or totalView is null`() {
        val mockChannelId = "123ch"
        val mockTotalView = "600"
        viewModel.updatePlayWidgetTotalView(
                null,
                null
        )
        assert(viewModel.playWidgetObservable.value == null)
        viewModel.updatePlayWidgetTotalView(
                mockChannelId,
                null
        )
        assert(viewModel.playWidgetObservable.value == null)
        viewModel.updatePlayWidgetTotalView(
                null,
                mockTotalView
        )
        assert(viewModel.playWidgetObservable.value == null)
    }


}