package com.tokopedia.shop.home.view.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.atc_common.data.model.request.AddToCartBundleRequestParams
import com.tokopedia.atc_common.data.model.request.ProductDetail
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
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
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetV2
import com.tokopedia.shop.home.domain.CheckCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutV2UseCase
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.shop.pageheader.domain.interactor.GqlShopPageGetHomeType
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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var addToCartOccUseCase: AddToCartOccMultiUseCase

    @RelaxedMockK
    lateinit var addToCartBundleUseCase: AddToCartBundleUseCase

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
    lateinit var getShopPageHomeLayoutV2UseCase: Provider<GetShopPageHomeLayoutV2UseCase>

    @RelaxedMockK
    lateinit var gqlShopPageGetHomeType: GqlShopPageGetHomeType

    @RelaxedMockK
    lateinit var shopProductSortMapper: ShopProductSortMapper

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var context: Context
    private lateinit var viewModel: ShopHomeViewModel

    private val mockShopId = "1234"
    private val mockCampaignId = "123"
    private val mockPage = 2
    private val mockProductPerPage = 10
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
    private val addressWidgetData: LocalCacheModel = LocalCacheModel()
    private val playWidgetUiModelMockData = PlayWidgetUiModel.Small(
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

    private val playWidgetMediumUiModelMockData = PlayWidgetUiModel.Medium(
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
        PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
        listOf()
    )


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopHomeViewModel(
                userSessionInterface,
                getShopProductUseCase,
                testCoroutineDispatcherProvider,
                addToCartUseCase,
                addToCartOccUseCase,
                addToCartBundleUseCase,
                gqlCheckWishlistUseCaseProvider,
                getYoutubeVideoUseCase,
                getCampaignNotifyMeUseCase,
                checkCampaignNotifyMeUseCase,
                getShopFilterBottomSheetDataUseCase,
                getShopFilterProductCountUseCase,
                gqlGetShopSortUseCase,
                shopProductSortMapper,
                mvcSummaryUseCase,
                playWidgetTools,
                gqlShopPageGetHomeType,
                getShopPageHomeLayoutV2UseCase
        )
    }

    @Test
    fun `when getShopPageHomeWidgetLayoutData success should return expected results`() {
        coEvery { gqlShopPageGetHomeType.executeOnBackground() } returns ShopPageGetHomeType()
        viewModel.getShopPageHomeWidgetLayoutData(mockShopId)
        coVerify {
            gqlShopPageGetHomeType.executeOnBackground()
        }
        assertTrue(viewModel.shopHomeWidgetLayoutData.value is Success)
    }

    @Test
    fun `when getShopPageHomeWidgetLayoutData error should return expected results`() {
        coEvery { gqlShopPageGetHomeType.executeOnBackground() } throws Exception()
        viewModel.getShopPageHomeWidgetLayoutData(mockShopId)
        coVerify {
            gqlShopPageGetHomeType.executeOnBackground()
        }
        assertTrue(viewModel.shopHomeWidgetLayoutData.value is Fail)
    }

    @Test
    fun `check whether response get lazy load product success is not null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(), ShopProduct())
        )

        viewModel.getNewProductList(mockShopId, mockPage, mockProductPerPage, shopProductFilterParameter, addressWidgetData)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productListData.value is Success)
        assertNotNull(viewModel.productListData.value)
    }

    @Test
    fun `check whether response get lazy load product failed is null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getNewProductList(mockShopId, mockPage, mockProductPerPage, shopProductFilterParameter, addressWidgetData)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productListData.value is Fail)
    }

    @Test
    fun `check whether get merchant voucher is success`() {
        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(resultStatus = ResultStatus(code = ShopHomeViewModel.CODE_STATUS_SUCCESS, null, null, null), null, null, null))
        viewModel.getMerchantVoucherCoupon(mockShopId, context, ShopHomeVoucherUiModel())
        coVerify { mvcSummaryUseCase.getResponse(any()) }
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Success)
    }

    @Test
    fun `check whether get merchant voucher is fail`() {
        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } throws Throwable()
        viewModel.getMerchantVoucherCoupon(mockShopId, context, ShopHomeVoucherUiModel())
        coVerify { mvcSummaryUseCase.getResponse(any()) }
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Fail)
    }


    @Test
    fun `check whether get merchant voucher is Success if TokopointsCatalogMVCSummary or resultStatus is null`() {
        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } returns TokopointsCatalogMVCSummaryResponse(
                TokopointsCatalogMVCSummary(
                        resultStatus = null,
                        null,
                        null,
                        null
                )
        )
        viewModel.getMerchantVoucherCoupon(mockShopId, null, ShopHomeVoucherUiModel())
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Success)

        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } returns TokopointsCatalogMVCSummaryResponse(
                TokopointsCatalogMVCSummary(
                        resultStatus = ResultStatus(
                                code = null,
                                null,
                                null,
                                null
                        ),
                        null,
                        null,
                        null
                )
        )
        viewModel.getMerchantVoucherCoupon(mockShopId, context, ShopHomeVoucherUiModel())
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Success)

        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } returns TokopointsCatalogMVCSummaryResponse(null)
        viewModel.getMerchantVoucherCoupon(mockShopId, context, ShopHomeVoucherUiModel())
        assertTrue(viewModel.shopHomeMerchantVoucherLayoutData.value is Success)
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
        val mockProductId = "456"
        val mockProductName = "product mock"
        val mockDisplayedPrice = "Rp. 1000"
        val onErrorAddToCart: (Throwable) -> Unit = mockk(relaxed = true)
        every { addToCartUseCase.createObservable(any()) } returns Observable.just(
                AddToCartDataModel(
                        data = DataModel(success = 0, message = arrayListOf("Message"))
                ))
        viewModel.addProductToCart(
                ShopHomeProductUiModel().apply {
                    id = mockProductId
                    name = mockProductName
                    displayedPrice = mockDisplayedPrice
                },
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
        coEvery { addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel() } returns AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(success = 1)
        )
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
    fun `check whether onErrorAddToCart is called when addProductToCartOcc response is not success`() {
        val onErrorAddToCart: (Throwable) -> Unit = mockk(relaxed = true)
        coEvery { addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel() } returns AddToCartDataModel(
                status = AddToCartDataModel.STATUS_ERROR,
                data = DataModel(success = 0, message = arrayListOf("Message"))
        )
        viewModel.addProductToCartOcc(
                ShopHomeProductUiModel(),
                mockShopId,
                {},
                onErrorAddToCart
        )
        verify { onErrorAddToCart.invoke(any()) }
    }

    @Test
    fun `check whether onErrorAddToCart is called when call addProductToCartOcc thrown exception`() {
        val onErrorAddToCart: (Throwable) -> Unit = mockk(relaxed = true)
        coEvery { addToCartOccUseCase.setParams(any()).executeOnBackground() } throws Throwable()
        viewModel.addProductToCartOcc(
                ShopHomeProductUiModel(),
                mockShopId,
                {},
                onErrorAddToCart
        )
        verify { onErrorAddToCart.invoke(any()) }
    }

    @Test
    fun `check whether onFinishAddToCart is called when call addBundleToCart success`() {
        val onErrorAddToCart: (Throwable) -> Unit = mockk(relaxed = true)

        val mockBundleProductList = listOf(
                ProductDetail(
                        productId = "111",
                        quantity = 1,
                        shopId = "123",
                        customerId = "321"
                )
        )

        val mockAtcBundleParams = AddToCartBundleRequestParams(
                shopId = "123",
                bundleId = "222",
                bundleQty = 1,
                selectedProductPdp = "0",
                productDetails = mockBundleProductList
        )

        coEvery {
            addToCartBundleUseCase.setParams(mockAtcBundleParams)
            addToCartBundleUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.addBundleToCart(
                shopId = "123",
                userId = "321",
                bundleId = "222",
                productDetails = listOf(ShopHomeBundleProductUiModel()),
                {},
                onErrorAddToCart,
                bundleQuantity = 1
        )

        verify { onErrorAddToCart.invoke(any()) }
    }

    @Test
    fun `check whether onErrorAddToCart is called when call addBundleToCart throw exception`() {
        val onFinishAddToCart: (AddToCartBundleModel) -> Unit = mockk(relaxed = true)

        val mockBundleProductList = listOf(
                ProductDetail(
                        productId = "111",
                        quantity = 1,
                        shopId = "123",
                        customerId = "321"
                )
        )

        val mockAtcBundleParams = AddToCartBundleRequestParams(
                shopId = "123",
                bundleId = "222",
                bundleQty = 1,
                selectedProductPdp = "0",
                productDetails = mockBundleProductList
        )

        coEvery {
            addToCartBundleUseCase.setParams(mockAtcBundleParams)
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(
                status = "OK",
                errorMessage = "",
                addToCartBundleDataModel = AddToCartBundleDataModel(success = 1)
        )
        viewModel.addBundleToCart(
                shopId = "123",
                userId = "321",
                bundleId = "222",
                productDetails = listOf(ShopHomeBundleProductUiModel()),
                onFinishAddToCart = onFinishAddToCart,
                {},
                bundleQuantity = 1
        )

        verify { onFinishAddToCart.invoke(any()) }
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
    fun `check whether checkWishlistData post Success with null value if response throw exception`() {
        val mockProductId = "123"
        coEvery { gqlCheckWishlistUseCaseProvider.get().executeOnBackground() } throws Exception()
        viewModel.getWishlistStatus(listOf(
                ShopHomeCarousellProductUiModel(
                        productList = listOf(
                                ShopHomeProductUiModel().apply { id = null },
                                ShopHomeProductUiModel().apply { id = mockProductId }
                        )
                )
        ))
        assert(viewModel.checkWishlistData.value is Success)
        assertNotNull((viewModel.checkWishlistData.value as Success).data)
    }


    @Test
    fun `check checkWishlistData if exception is thrown`() {
        val mockProductId = "123"
        coEvery { gqlCheckWishlistUseCaseProvider.get().executeOnBackground() } returns listOf(
                CheckWishlistResult()
        )
        val observer = mockk<Observer<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>>>>()
        viewModel.checkWishlistData.observeForever(observer)
        every { observer.onChanged(any<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>>>>()) } throws Exception()
        viewModel.getWishlistStatus(listOf(
                ShopHomeCarousellProductUiModel(
                        productList = listOf(
                                ShopHomeProductUiModel().apply { id = null },
                                ShopHomeProductUiModel().apply { id = mockProductId }
                        )
                )
        ))
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
            gqlShopPageGetHomeType.clearCache()
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
    fun `check whether campaignFlashSaleRemindMeStatusData post Success value`() {
        val mockCampaignId ="12345"
        coEvery { getCampaignNotifyMeUseCase.get().executeOnBackground() } returns GetCampaignNotifyMeModel()
        viewModel.getCampaignFlashSaleRemindMeStatus(mockCampaignId)
        coVerify { getCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.campaignFlashSaleStatusData.value is Success)
    }

    @Test
    fun `check whether campaignFlashSaleRemindMeStatusData value is null when error`() {
        val mockCampaignId ="12345"
        coEvery { getCampaignNotifyMeUseCase.get().executeOnBackground() } throws Throwable()
        viewModel.getCampaignFlashSaleRemindMeStatus(mockCampaignId)
        coVerify { getCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.campaignFlashSaleStatusData.value == null)
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
    fun `check whether checkCampaignFlashSaleRemindMeStatusData post Success value`() {
        val mockAction = "action"
        coEvery { checkCampaignNotifyMeUseCase.get().executeOnBackground() } returns CheckCampaignNotifyMeModel()
        viewModel.clickFlashSaleReminder(mockCampaignId, mockAction)
        coVerify { checkCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.checkCampaignFlashSaleRemindMeStatusData.value is Success)
    }

    @Test
    fun `check whether checkCampaignFlashSaleRemindMeStatusData post Fail value`() {
        val mockAction = "action"
        coEvery { checkCampaignNotifyMeUseCase.get().executeOnBackground() } throws Throwable()
        viewModel.clickFlashSaleReminder(mockCampaignId, mockAction)
        coVerify { checkCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.checkCampaignFlashSaleRemindMeStatusData.value is Fail)
    }

    @Test
    fun `check whether bottomSheetFilterLiveData post Success value`() {
        coEvery { getShopFilterBottomSheetDataUseCase.executeOnBackground() }  returns DynamicFilterModel(
                data = DataValue(
                        listOf(Filter(title = "pengiriman"), Filter(title = "Rating"))
                )
        )
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
        viewModel.getFilterResultCount(mockShopId, mockProductPerPage, ShopProductFilterParameter(), addressWidgetData)
        coVerify { getShopFilterProductCountUseCase.executeOnBackground() }
        val shopProductFilterCountValue = viewModel.shopProductFilterCountLiveData.value
        assert(shopProductFilterCountValue is Success)
        assert((shopProductFilterCountValue as? Success)?.data == mockTotalProduct)
    }

    @Test
    fun `check whether shopProductFilterCountLiveData value is null if exception is thrown`() {
        coEvery { getShopFilterProductCountUseCase.executeOnBackground() } throws Exception()
        viewModel.getFilterResultCount(mockShopId, mockProductPerPage, ShopProductFilterParameter(), addressWidgetData)
        coVerify { getShopFilterProductCountUseCase.executeOnBackground() }
        val shopProductFilterCountValue = viewModel.shopProductFilterCountLiveData.value
        assert(shopProductFilterCountValue == null)
    }

    @Test
    fun `check whether playWidgetToggleReminderObservable success value if mapWidgetToggleReminder return true`() {
        val mockChannelId = "123"
        val mockPlayWidgetReminder = PlayWidgetReminder()

        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))

        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { playWidgetTools.updateToggleReminder(any(), any(), any()) } returns mockPlayWidgetReminder
        coEvery { playWidgetTools.mapWidgetToggleReminder(mockPlayWidgetReminder) } returns true

        viewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, PlayWidgetReminderType.Reminded)

        coVerify { playWidgetTools.updateToggleReminder(any(), any(), any()) }

        assert(viewModel.playWidgetReminderObservable.value is Success)
    }


    @Test
    fun `check whether playWidgetToggleReminderObservable fail value if mapWidgetToggleReminder return false`() {
        val mockChannelId = "123"
        val mockPlayWidgetReminder = PlayWidgetReminder()

        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { playWidgetTools.updateToggleReminder(any(), any(), any()) } returns mockPlayWidgetReminder
        coEvery { playWidgetTools.mapWidgetToggleReminder(any()) } answers {
            false
        }


        viewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, PlayWidgetReminderType.Reminded)
        coVerify { playWidgetTools.updateToggleReminder(any(), any(), any()) }
        coVerify { playWidgetTools.mapWidgetToggleReminder(any()) }

        assert(viewModel.playWidgetReminderObservable.value is Fail)
    }

    @Test
    fun `check whether playWidgetToggleReminderObservable success value is false`() {
        val mockChannelId = "123"

        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { playWidgetTools.updateToggleReminder(any(), any(), any()) } throws Throwable()

        viewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, PlayWidgetReminderType.Reminded)

        coVerify { playWidgetTools.updateToggleReminder(any(), any(), any()) }

        assert(viewModel.playWidgetReminderObservable.value is Fail)
    }

    @Test
    fun `check whether playWidgetReminderEvent value is not null when on login`() {
        val mockChannelId = "123"
        every { userSessionInterface.isLoggedIn } returns false
        viewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, PlayWidgetReminderType.Reminded)
        assert(viewModel.playWidgetReminderEvent.value?.first == mockChannelId)
        assert(viewModel.playWidgetReminderEvent.value?.second == PlayWidgetReminderType.Reminded)
    }

    @Test
    fun `when call getProductGridListWidgetData with no initial product then productListData value should success`() {
        val mockSortId = "123"
        val mockSortName = "sort 1"

        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } returns mutableListOf(
                ShopProductSortModel().apply {
                    value = mockSortId
                    name = mockSortName
                }
        )
        viewModel.getProductGridListWidgetData(
                mockShopId,
                mockProductPerPage,
                shopProductFilterParameter,
                null,
                addressWidgetData
        )
        assert(viewModel.productListData.value is Success)
    }

    @Test
    fun `when call getProductGridListWidgetData with no initial product and productList response is error then value should null`() {
        val mockSortId = "123"
        val mockSortName = "sort 1"
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } returns mutableListOf(
                ShopProductSortModel().apply {
                    value = mockSortId
                    name = mockSortName
                }
        )
        viewModel.getProductGridListWidgetData(
                mockShopId,
                mockProductPerPage,
                shopProductFilterParameter,
                null,
                addressWidgetData
        )
        assert(viewModel.productListData.value == null)
    }

    @Test
    fun `check whether getSortNameById return mocked sortName`() {
        val mockSortId = "123"
        val mockSortName = "sort 1"

        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } returns mutableListOf(
                ShopProductSortModel().apply {
                    value = mockSortId
                    name = mockSortName
                }
        )
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        assert(viewModel.getSortNameById(mockSortId) == mockSortName)
    }

    @Test
    fun `check whether getSortNameById return empty string if sort mapper throw exception`() {
        val mockSortId = "123"
        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } throws Exception()
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        assert(viewModel.getSortNameById(mockSortId).isEmpty())
    }

    @Test
    fun `check if exception is thrown when calling getProductGridListWidgetData`() {
        val observer = mockk<Observer<Result<GetShopHomeProductUiModel>>>()
        viewModel.productListData.observeForever(observer)
        coEvery { observer.onChanged(any<Success<GetShopHomeProductUiModel>>()) } throws Exception()
        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } returns mutableListOf(
                ShopProductSortModel()
        )
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
    }

    @Test
    fun `check whether getSortNameById return empty when sort id didn't matched`() {
        val mockSortId = "123"
        val mockSortName = "sort 1"

        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } returns mutableListOf(
                ShopProductSortModel().apply {
                    value = mockSortId
                    name = mockSortName
                }
        )
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData)
        assert(viewModel.getSortNameById("").isEmpty())
    }

    @Test
    fun `check whether playWidgetObservable value is not null when get data is success`() {
        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }


    @Test
    fun `check whether playWidgetObservable value is not null when get data is success if shopId and userSession shopId match`() {
        every { userSessionInterface.shopId } returns mockShopId
        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }

    @Test
    fun `check whether playWidgetObservable value is not null when get data is success if playWidgetObservable value is null`() {
        every { userSessionInterface.shopId } returns mockShopId
        val playWidgetMock = PlayWidget()
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }

    @Test
    fun `check whether playWidgetObservable value is not null when getPlayWidget called twice`() {
        every { userSessionInterface.shopId } returns mockShopId
        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }

    @Test
    fun `check play widget success delete channel`() {
        val channelId = "123"
        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
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
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
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
                                PlayWidgetPromoType.Default(""),
                                PlayWidgetVideoUiModel("", false, "", ""),
                                PlayWidgetChannelType.Upcoming,
                                false,
                                "",
                                ""
                        )
                )
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        every {
            playWidgetTools.updateTotalView(playWidgetUiModelMockData, mockChannelId, mockTotalView)
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

    @Test
    fun `check if playWidgetObservableplay value is updated when isReminder is changes`() {
        val playWidgetMock = PlayWidget()
        val mockChannelId = "123"
        val mockTotalView = "50"
        val mockReminderType = PlayWidgetReminderType.Reminded
        val playWidgetUiModelMockDataWithReminder = PlayWidgetUiModel.Medium(
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
            PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
            listOf(
                PlayWidgetMediumChannelUiModel(
                    mockChannelId,
                    "",
                    "",
                    "",
                    "",
                    mockTotalView,
                    true,
                    PlayWidgetPromoType.Default(""),
                    mockReminderType,
                    PlayWidgetPartnerUiModel("", ""),
                    PlayWidgetVideoUiModel("", false, "", ""),
                    PlayWidgetChannelType.Upcoming,
                    false,
                    PlayWidgetChannelTypeTransition(PlayWidgetChannelType.Upcoming, PlayWidgetChannelType.Upcoming),
                    PlayWidgetShareUiModel("", false),
                    "",
                    false,
                    "",
                    ""
                )
            )
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetMediumUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        every {
            playWidgetTools.updateActionReminder(playWidgetMediumUiModelMockData, mockChannelId, mockReminderType)
        } returns playWidgetUiModelMockDataWithReminder
        viewModel.updatePlayWidgetReminder(
            mockChannelId,
            true
        )
        val playWidgetUiModel = (viewModel.playWidgetObservable.value?.widgetUiModel as? PlayWidgetUiModel.Medium)
        assert((playWidgetUiModel?.items?.first() as? PlayWidgetMediumChannelUiModel)?.reminderType == mockReminderType)
    }


    @Test
    fun `check if playWidgetObservableplay value is updated when isReminder is change to not reminded`() {
        val playWidgetMock = PlayWidget()
        val mockChannelId = "123"
        val mockTotalView = "50"
        val mockReminderType = PlayWidgetReminderType.NotReminded
        val playWidgetUiModelMockDataWithReminder = PlayWidgetUiModel.Medium(
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
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf(
                        PlayWidgetMediumChannelUiModel(
                                mockChannelId,
                                "",
                                "",
                                "",
                                "",
                                mockTotalView,
                                true,
                                PlayWidgetPromoType.Default(""),
                                mockReminderType,
                                PlayWidgetPartnerUiModel("", ""),
                                PlayWidgetVideoUiModel("", false, "", ""),
                                PlayWidgetChannelType.Upcoming,
                                false,
                                PlayWidgetChannelTypeTransition(PlayWidgetChannelType.Upcoming, PlayWidgetChannelType.Upcoming),
                                PlayWidgetShareUiModel("", false),
                                "",
                                false,
                                "",
                                ""
                        )
                )
        )
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetMediumUiModelMockData
        viewModel.getPlayWidget(mockShopId, CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
        ))
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        every {
            playWidgetTools.updateActionReminder(playWidgetMediumUiModelMockData, mockChannelId, mockReminderType)
        } returns playWidgetUiModelMockDataWithReminder
        viewModel.updatePlayWidgetReminder(
                mockChannelId,
                false
        )
        val playWidgetUiModel = (viewModel.playWidgetObservable.value?.widgetUiModel as? PlayWidgetUiModel.Medium)
        assert((playWidgetUiModel?.items?.first() as? PlayWidgetMediumChannelUiModel)?.reminderType == mockReminderType)
    }

    @Test
    fun `calling updatePlayWidgetReminder(), check if playWidgetObservableplay value is null`() {
        viewModel.updatePlayWidgetReminder(
                null,
                false
        )
        assert(viewModel.playWidgetObservable.value == null)

        viewModel.updatePlayWidgetReminder(
                "123",
                false
        )
        assert(viewModel.playWidgetObservable.value == null)
    }

    @Test
    fun `when getWidgetContentData success should return expected results`() {
        runBlocking {
            val shopHomeWidgetContentData = async {
                viewModel.shopHomeWidgetContentData.first()
            }
            coEvery {
                getShopPageHomeLayoutV2UseCase.get().executeOnBackground()
            } returns ShopLayoutWidgetV2()
            mockkObject(ShopPageHomeMapper)
            every { ShopPageHomeMapper.mapToListShopHomeWidget(any(), any(), any()) } returns listOf(
                    ShopHomeCarousellProductUiModel(widgetId = "1")
            )
            viewModel.getWidgetContentData(
                    listOf(
                            ShopPageHomeWidgetLayoutUiModel.WidgetLayout(
                                    widgetId = "1"
                            ),
                            ShopPageHomeWidgetLayoutUiModel.WidgetLayout(
                                    widgetId = "2"
                            )
                    ),
                    mockShopId,
                    addressWidgetData
            )
            assert(shopHomeWidgetContentData.await() is Success)
            assert((shopHomeWidgetContentData.await() as? Success)?.data?.isNotEmpty() == true)
        }
    }

    @Test
    fun `when getWidgetContentData error should return expected results`() {
        runBlocking {
            val shopHomeWidgetContentData = async {
                viewModel.shopHomeWidgetContentData.first()
            }
            val shopHomeWidgetContentDataError = async {
                viewModel.shopHomeWidgetContentDataError.first()
            }
            coEvery {
                getShopPageHomeLayoutV2UseCase.get().executeOnBackground()
            } throws Exception()
            viewModel.getWidgetContentData(
                    listOf(
                            ShopPageHomeWidgetLayoutUiModel.WidgetLayout()
                    ),
                    mockShopId,
                    addressWidgetData
            )
            assert(shopHomeWidgetContentData.await() is Fail)
            assert(shopHomeWidgetContentDataError.await().isNotEmpty())
        }
    }

}