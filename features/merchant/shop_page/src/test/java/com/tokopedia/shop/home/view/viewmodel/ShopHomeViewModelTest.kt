package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherOwner
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetHeaderReminder
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderUiModel
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
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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

    private val getShopProductUseCase: GqlGetShopProductUseCase = mockk(relaxed = true)
    @RelaxedMockK
    lateinit var getCampaignNotifyMeUseCase: Provider<GetCampaignNotifyMeUseCase>
    @RelaxedMockK
    lateinit var checkCampaignNotifyMeUseCase: Provider<CheckCampaignNotifyMeUseCase>
    private val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val userSessionInterface: UserSessionInterface = mockk(relaxed = true)
    private val getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase = mockk(relaxed = true)
    private val getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase = mockk(relaxed = true)
    private val getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase = mockk(relaxed = true)
    private val gqlGetShopSortUseCase: GqlGetShopSortUseCase = mockk(relaxed = true)
    private val shopProductSortMapper: ShopProductSortMapper = mockk(relaxed = true)
    private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase = mockk(relaxed = true)
    private val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)

    @RelaxedMockK
    lateinit var gqlCheckWishlistUseCaseProvider: Provider<GQLCheckWishlistUseCase>

    private lateinit var viewModel: ShopHomeViewModel
    private lateinit var viewModelSpykData: ShopHomeViewModel

    private val mockShopId = "1234"
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

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopHomeViewModel(
                userSessionInterface,
                getShopPageHomeLayoutUseCase,
                getShopProductUseCase,
                testCoroutineDispatcherProvider,
                addToCartUseCase,
                gqlCheckWishlistUseCaseProvider,
                getYoutubeVideoUseCase,
                getCampaignNotifyMeUseCase,
                checkCampaignNotifyMeUseCase,
                getShopFilterBottomSheetDataUseCase,
                getShopFilterProductCountUseCase,
                gqlGetShopSortUseCase,
                shopProductSortMapper,
                getMerchantVoucherListUseCase,
                playWidgetTools
        )
        viewModelSpykData = spyk(viewModel, recordPrivateCalls = true)
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

        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, true)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeLayoutData.value is Success)
        assertTrue((viewModel.shopHomeLayoutData.value as? Success)!!.data.listWidget.filterIsInstance<CarouselPlayWidgetUiModel>().isNotEmpty())
    }

    @Test
    fun `check whether get merchant voucher is success`() {
        val numVoucher = 10
        every { viewModelSpykData.shopHomeLayoutData.value } returns Success(ShopPageHomeLayoutUiModel(
                listWidget = listOf(
                        ShopHomeVoucherUiModel(),
                        ShopHomeDisplayWidgetUiModel()
                )
        ))
        coEvery {
            getMerchantVoucherListUseCase.createObservable(any())
        } returns Observable.just(arrayListOf(MerchantVoucherModel(
                voucherId = 10,
                voucherName = "voucherName",
                voucherCode = "1010002",
                validThru = "1020021",
                merchantVoucherOwner = MerchantVoucherOwner()
        )))
        viewModelSpykData.getMerchantVoucherList(mockShopId, numVoucher)
        coVerify { getMerchantVoucherListUseCase.createObservable(any()) }
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Success)
    }

    @Test
    fun `check whether get merchant voucher is fail`() {
        val numVoucher = 10
        every { viewModelSpykData.shopHomeLayoutData.value } returns Success(ShopPageHomeLayoutUiModel(
                listWidget = listOf(
                        ShopHomeVoucherUiModel()
                )
        ))
        coEvery {
            getMerchantVoucherListUseCase.createObservable(any())
        } throws Throwable()
        viewModelSpykData.getMerchantVoucherList(mockShopId, numVoucher)
        coVerify { getMerchantVoucherListUseCase.createObservable(any()) }
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Fail)
    }

    @Test
    fun `check whether get merchant voucher is null when shopHomeLayoutData is null`() {
        val numVoucher = 10
        every { viewModelSpykData.shopHomeLayoutData.value } returns null
        coEvery {
            getMerchantVoucherListUseCase.createObservable(any())
        } throws Throwable()
        viewModelSpykData.getMerchantVoucherList(mockShopId, numVoucher)
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value == null)
    }

    @Test
    fun `check whether shopHomeMerchantVoucherLayoutData value should be null if voucher layout not exists`() {
        val numVoucher = 10
        every { viewModelSpykData.shopHomeLayoutData.value } returns Success(ShopPageHomeLayoutUiModel())
        viewModelSpykData.getMerchantVoucherList(mockShopId, numVoucher)
        assert(viewModel.shopHomeMerchantVoucherLayoutData.value == null)
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
        val mockCampaignId = "123"
        val mockAction = "action"
        coEvery { checkCampaignNotifyMeUseCase.get().executeOnBackground() } returns CheckCampaignNotifyMeModel()
        viewModel.clickRemindMe(mockCampaignId, mockAction)
        coVerify { checkCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.checkCampaignNplRemindMeStatusData.value is Success)
    }

    @Test
    fun `check whether checkCampaignNplRemindMeStatusData post Fail value`() {
        val mockCampaignId = "123"
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
        viewModel.getFilterResultCount(mockShopId, ShopProductFilterParameter())
        coVerify { getShopFilterProductCountUseCase.executeOnBackground() }
        val shopProductFilterCountValue = viewModel.shopProductFilterCountLiveData.value
        assert(shopProductFilterCountValue is Success)
        assert((shopProductFilterCountValue as? Success)?.data == mockTotalProduct)
    }

    @Test
    fun `check whether playWidgetToggleReminderObservable success value is true`() {
        val mockChannelId = "123"
        val mockPlayWidgetReminder = PlayWidgetReminder(PlayWidgetHeaderReminder())
        coEvery {
            playWidgetTools.setToggleReminder(any(), any(), any())
        } returns mockPlayWidgetReminder
        coEvery {
            playWidgetTools.mapWidgetToggleReminder(mockPlayWidgetReminder)
        } returns PlayWidgetReminderUiModel(success = true)
        viewModel.setToggleReminderPlayWidget(mockChannelId, true, 1)
        coVerify { playWidgetTools.setToggleReminder(any(), any(), any()) }
        assert(viewModel.playWidgetToggleReminderObservable.value?.success == true)
    }

    @Test
    fun `check whether playWidgetToggleReminderObservable success value is false`() {
        val mockChannelId = "123"
        val mockPlayWidgetReminder = PlayWidgetReminder(PlayWidgetHeaderReminder())
        coEvery {
            playWidgetTools.setToggleReminder(any(), any(), any())
        } returns mockPlayWidgetReminder
        coEvery {
            playWidgetTools.mapWidgetToggleReminder(mockPlayWidgetReminder)
        } returns PlayWidgetReminderUiModel(success = false)
        viewModel.setToggleReminderPlayWidget(mockChannelId, true, 1)
        coVerify { playWidgetTools.setToggleReminder(any(), any(), any()) }
        assert(viewModel.playWidgetToggleReminderObservable.value?.success == false)
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
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, true)
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
        viewModel.getShopPageHomeData(mockShopId, shopProductFilterParameter, true)
        assert(viewModel.getSortNameById("").isEmpty())
    }

}