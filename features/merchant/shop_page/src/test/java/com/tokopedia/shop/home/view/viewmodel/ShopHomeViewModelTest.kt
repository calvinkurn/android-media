package com.tokopedia.shop.home.view.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.request.AddToCartBundleRequestParams
import com.tokopedia.atc_common.data.model.request.ProductDetail
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.common.constant.PMAX_PARAM_KEY
import com.tokopedia.shop.common.constant.PMIN_PARAM_KEY
import com.tokopedia.shop.common.constant.RATING_PARAM_KEY
import com.tokopedia.shop.common.constant.SORT_PARAM_KEY
import com.tokopedia.shop.common.constant.ShopPageConstant.CODE_STATUS_SUCCESS
import com.tokopedia.shop.common.data.model.*
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetDynamicTabUseCase
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetHomeType
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetV2
import com.tokopedia.shop.home.domain.CheckCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutV2UseCase
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.pageheader.util.ShopPageTabName
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.shop_widget.common.uimodel.DynamicHeaderUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
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
import org.junit.Assert.assertFalse
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
    lateinit var addToCartUseCaseRx: AddToCartUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

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
    lateinit var getShopDynamicTabUseCase: Provider<GqlShopPageGetDynamicTabUseCase>

    @RelaxedMockK
    lateinit var gqlShopPageGetHomeType: GqlShopPageGetHomeType

    @RelaxedMockK
    lateinit var shopProductSortMapper: ShopProductSortMapper

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var context: Context
    private lateinit var viewModel: ShopHomeViewModel

    private val mockExtParam = "fs_widget%3D23600"
    private val mockShopId = "1234"
    private val mockCampaignId = "123"
    private val mockPage = 2
    private val mockProductPerPage = 10
    private val mockIsDirectPurchaseTrue = true
    private val mockIsDirectPurchaseFalse = false
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
    private val playWidgetStateMockData = PlayWidgetState(
        model = PlayWidgetUiModel(
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
        ),
        widgetType = PlayWidgetType.Small,
        isLoading = false
    )

    private val playWidgetStateMediumMockData = PlayWidgetState(
        model = PlayWidgetUiModel(
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
        ),
        widgetType = PlayWidgetType.Medium,
        isLoading = false
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopHomeViewModel(
            userSessionInterface,
            getShopProductUseCase,
            testCoroutineDispatcherProvider,
            addToCartUseCaseRx,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
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
            getShopPageHomeLayoutV2UseCase,
            getShopDynamicTabUseCase
        )
    }

    @Test
    fun `when getShopPageHomeWidgetLayoutData success should return expected results`() {
        coEvery { gqlShopPageGetHomeType.executeOnBackground() } returns ShopPageGetHomeType()
        viewModel.getShopPageHomeWidgetLayoutData(mockShopId, mockExtParam)
        coVerify {
            gqlShopPageGetHomeType.executeOnBackground()
        }
        assertTrue(viewModel.shopHomeWidgetLayoutData.value is Success)
    }

    @Test
    fun `when getShopPageHomeWidgetLayoutData error should return expected results`() {
        coEvery { gqlShopPageGetHomeType.executeOnBackground() } throws Exception()
        viewModel.getShopPageHomeWidgetLayoutData(mockShopId, mockExtParam)
        coVerify {
            gqlShopPageGetHomeType.executeOnBackground()
        }
        assertTrue(viewModel.shopHomeWidgetLayoutData.value is Fail)
    }

    @Test
    fun `check whether response get lazy load product success is not null when isEnableDirect purchase is true`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct(), ShopProduct())
        )

        viewModel.getNewProductList(mockShopId, mockPage, mockProductPerPage, shopProductFilterParameter, addressWidgetData, mockIsDirectPurchaseTrue)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        val liveDataValue = viewModel.productListData.value
        assertTrue(liveDataValue is Success)
        assertNotNull(liveDataValue)
        assert(
            (liveDataValue as? Success)?.data?.listShopProductUiModel?.all {
                it.isEnableDirectPurchase
            } == mockIsDirectPurchaseTrue
        )
    }

    @Test
    fun `check whether response get lazy load product success is not null when isEnableDirect purchase is false`() {
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct(), ShopProduct())
        )

        viewModel.getNewProductList(mockShopId, mockPage, mockProductPerPage, shopProductFilterParameter, addressWidgetData, mockIsDirectPurchaseFalse)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productListData.value is Success)
        assertNotNull(viewModel.productListData.value)
        assert(
            (viewModel.productListData.value as? Success)?.data?.listShopProductUiModel?.all {
                it.isEnableDirectPurchase
            } == mockIsDirectPurchaseFalse
        )
    }

    @Test
    fun `check whether response get lazy load product failed is null`() {
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getNewProductList(mockShopId, mockPage, mockProductPerPage, shopProductFilterParameter, addressWidgetData, mockIsDirectPurchaseTrue)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productListData.value is Fail)
    }

    @Test
    fun `check whether get merchant voucher is success`() {
        coEvery {
            mvcSummaryUseCase.getResponse(any())
        } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(resultStatus = ResultStatus(code = CODE_STATUS_SUCCESS, null, null, null), null, null, null))
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
        every { addToCartUseCaseRx.createObservable(any()) } returns Observable.just(
            AddToCartDataModel(
                data = DataModel(success = 1)
            )
        )
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
        every { addToCartUseCaseRx.createObservable(any()) } returns Observable.just(
            AddToCartDataModel(
                data = DataModel(success = 0, message = arrayListOf("Message"))
            )
        )
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
        every { addToCartUseCaseRx.createObservable(any()) } throws Throwable()
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
            productQuantity = 1
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
            productQuantity = 1
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
        viewModel.getWishlistStatus(
            listOf(
                ShopHomeCarousellProductUiModel(
                    productList = listOf(
                        ShopHomeProductUiModel().apply { id = "" },
                        ShopHomeProductUiModel().apply { id = mockProductId }
                    )
                )
            )
        )
        assert(viewModel.checkWishlistData.value is Success)
    }

    @Test
    fun `check whether checkWishlistData post Success with null value if response throw exception`() {
        val mockProductId = "123"
        coEvery { gqlCheckWishlistUseCaseProvider.get().executeOnBackground() } throws Exception()
        viewModel.getWishlistStatus(
            listOf(
                ShopHomeCarousellProductUiModel(
                    productList = listOf(
                        ShopHomeProductUiModel().apply { id = "" },
                        ShopHomeProductUiModel().apply { id = mockProductId }
                    )
                )
            )
        )
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
        viewModel.getWishlistStatus(
            listOf(
                ShopHomeCarousellProductUiModel(
                    productList = listOf(
                        ShopHomeProductUiModel().apply { id = "" },
                        ShopHomeProductUiModel().apply { id = mockProductId }
                    )
                )
            )
        )
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
        val mockCampaignId = "12345"
        coEvery { getCampaignNotifyMeUseCase.get().executeOnBackground() } returns GetCampaignNotifyMeModel()
        viewModel.getCampaignFlashSaleRemindMeStatus(mockCampaignId)
        coVerify { getCampaignNotifyMeUseCase.get().executeOnBackground() }
        assert(viewModel.campaignFlashSaleStatusData.value is Success)
    }

    @Test
    fun `check whether campaignFlashSaleRemindMeStatusData value is null when error`() {
        val mockCampaignId = "12345"
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
        coEvery { getShopFilterBottomSheetDataUseCase.executeOnBackground() } returns DynamicFilterModel(
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
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )

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
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
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
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
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
    fun `when call getProductGridListWidgetData with no initial product and is direct purchase true then productListData value should success`() {
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
            addressWidgetData,
            mockIsDirectPurchaseTrue
        )
        val liveDataValue = viewModel.productListData.value
        assert(liveDataValue is Success)
        assert(
            (liveDataValue as? Success)?.data?.listShopProductUiModel?.all {
                it.isEnableDirectPurchase
            } == true
        )
    }

    @Test
    fun `when call getProductGridListWidgetData with no initial product and is direct purchase false then productListData value should success`() {
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
            addressWidgetData,
            mockIsDirectPurchaseFalse
        )
        val liveDataValue = viewModel.productListData.value
        assert(liveDataValue is Success)
        assert(
            (liveDataValue as? Success)?.data?.listShopProductUiModel?.all {
                !it.isEnableDirectPurchase
            } == true
        )
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
            addressWidgetData,
            mockIsDirectPurchaseTrue
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
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData, mockIsDirectPurchaseTrue)
        assert(viewModel.getSortNameById(mockSortId) == mockSortName)
    }

    @Test
    fun `check whether getSortNameById return empty string if sort mapper throw exception`() {
        val mockSortId = "123"
        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } throws Exception()
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData, mockIsDirectPurchaseTrue)
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
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData, mockIsDirectPurchaseTrue)
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
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, ShopProduct.GetShopProduct(), addressWidgetData, mockIsDirectPurchaseTrue)
        assert(viewModel.getSortNameById("").isEmpty())
    }

    @Test
    fun `check whether getProductGridListWidgetData with initial product list data and shop direct purchase true, should product list with direct purchase value of true`() {
        val mockSortId = "123"
        val mockSortName = "sort 1"
        val mockShopProductList = ShopProduct.GetShopProduct(
            data = listOf(
                ShopProduct(),
                ShopProduct(),
                ShopProduct()
            )
        )

        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } returns mutableListOf(
            ShopProductSortModel().apply {
                value = mockSortId
                name = mockSortName
            }
        )
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, mockShopProductList, addressWidgetData, mockIsDirectPurchaseTrue)
        assert(
            (viewModel.productListData.value as? Success)?.data?.listShopProductUiModel?.all {
                it.isEnableDirectPurchase
            } == true
        )
    }

    @Test
    fun `check whether getProductGridListWidgetData with initial product list data and shop direct purchase false, should product list with direct purchase value of false`() {
        val mockSortId = "123"
        val mockSortName = "sort 1"
        val mockShopProductList = ShopProduct.GetShopProduct(
            data = listOf(
                ShopProduct(),
                ShopProduct(),
                ShopProduct()
            )
        )
        coEvery { gqlGetShopSortUseCase.executeOnBackground() } returns listOf()
        coEvery { shopProductSortMapper.convertSort(any()) } returns mutableListOf(
            ShopProductSortModel().apply {
                value = mockSortId
                name = mockSortName
            }
        )
        viewModel.getProductGridListWidgetData(mockShopId, mockProductPerPage, shopProductFilterParameter, mockShopProductList, addressWidgetData, mockIsDirectPurchaseFalse)
        assert(
            (viewModel.productListData.value as? Success)?.data?.listShopProductUiModel?.all {
                !it.isEnableDirectPurchase
            } == true
        )
    }

    @Test
    fun `check whether playWidgetObservable value is not null when get data is success`() {
        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }

    @Test
    fun `check whether playWidgetObservable value is not null when get data is success if shopId and userSession shopId match`() {
        every { userSessionInterface.shopId } returns mockShopId
        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }

    @Test
    fun `check whether playWidgetObservable value is not null when get data is success if playWidgetObservable value is null`() {
        every { userSessionInterface.shopId } returns mockShopId
        val playWidgetMock = PlayWidget()
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }

    @Test
    fun `check whether playWidgetObservable value is not null when getPlayWidget called twice`() {
        every { userSessionInterface.shopId } returns mockShopId
        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        assert(viewModel.playWidgetObservable.value != null)
    }

    @Test
    fun `check play widget success delete channel`() {
        val channelId = "123"
        val playWidgetMock = PlayWidget()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coEvery {
            playWidgetTools.updateDeletedChannel(any(), channelId)
        } returns playWidgetStateMockData
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
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
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
        val mockTotalView = PlayWidgetTotalView("50", true)
        val playWidgetUiModelMockDataWithTotalView = PlayWidgetState(
            model = PlayWidgetUiModel(
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
                    PlayWidgetChannelUiModel(
                        mockChannelId,
                        "",
                        "",
                        "",
                        mockTotalView,
                        PlayWidgetPromoType.Default("", false),
                        PlayWidgetReminderType.NotReminded,
                        PlayWidgetPartnerUiModel("", ""),
                        PlayWidgetVideoUiModel("", false, "", ""),
                        PlayWidgetChannelType.Upcoming,
                        false,
                        PlayWidgetShareUiModel("", false),
                        "",
                        "",
                        "",
                        false,
                        PlayWidgetChannelTypeTransition(null, PlayWidgetChannelType.Upcoming)
                    )
                )
            ),
            isLoading = false
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        every {
            playWidgetTools.updateTotalView(playWidgetStateMockData, mockChannelId, mockTotalView.totalViewFmt)
        } returns playWidgetUiModelMockDataWithTotalView
        viewModel.updatePlayWidgetTotalView(
            mockChannelId,
            mockTotalView.totalViewFmt
        )
        val playWidgetUiModel = viewModel.playWidgetObservable.value?.playWidgetState?.model
        assert((playWidgetUiModel?.items?.first() as? PlayWidgetChannelUiModel)?.totalView == mockTotalView)
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
        val mockTotalView = PlayWidgetTotalView("50", true)
        val mockReminderType = PlayWidgetReminderType.Reminded
        val playWidgetUiModelMockDataWithReminder = PlayWidgetState(
            model = PlayWidgetUiModel(
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
                    PlayWidgetChannelUiModel(
                        mockChannelId,
                        "",
                        "",
                        "",
                        mockTotalView,
                        PlayWidgetPromoType.Default("", false),
                        mockReminderType,
                        PlayWidgetPartnerUiModel("", ""),
                        PlayWidgetVideoUiModel("", false, "", ""),
                        PlayWidgetChannelType.Upcoming,
                        true,
                        PlayWidgetShareUiModel("", false),
                        "",
                        "",
                        "",
                        false,
                        PlayWidgetChannelTypeTransition(PlayWidgetChannelType.Upcoming, PlayWidgetChannelType.Upcoming)
                    )
                )
            )
        )
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMediumMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        every {
            playWidgetTools.updateActionReminder(playWidgetStateMediumMockData, mockChannelId, mockReminderType)
        } returns playWidgetUiModelMockDataWithReminder
        viewModel.updatePlayWidgetReminder(
            mockChannelId,
            true
        )
        val playWidgetUiModel = viewModel.playWidgetObservable.value?.playWidgetState?.model
        assert((playWidgetUiModel?.items?.first() as? PlayWidgetChannelUiModel)?.reminderType == mockReminderType)
    }

    @Test
    fun `check if playWidgetObservableplay value is updated when isReminder is change to not reminded`() {
        val playWidgetMock = PlayWidget()
        val mockChannelId = "123"
        val mockTotalView = PlayWidgetTotalView("50", true)
        val mockReminderType = PlayWidgetReminderType.NotReminded
        val playWidgetUiModelMockDataWithReminder = PlayWidgetState(
            model = PlayWidgetUiModel(
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
                    PlayWidgetChannelUiModel(
                        mockChannelId,
                        "",
                        "",
                        "",
                        mockTotalView,
                        PlayWidgetPromoType.Default("", false),
                        mockReminderType,
                        PlayWidgetPartnerUiModel("", ""),
                        PlayWidgetVideoUiModel("", false, "", ""),
                        PlayWidgetChannelType.Upcoming,
                        false,
                        PlayWidgetShareUiModel("", false),
                        "",
                        "",
                        "",
                        false,
                        PlayWidgetChannelTypeTransition(PlayWidgetChannelType.Upcoming, PlayWidgetChannelType.Upcoming)
                    )
                )
            ),
            isLoading = false
        )
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns playWidgetMock
        coEvery { playWidgetTools.mapWidgetToModel(playWidgetMock, any()) } returns playWidgetStateMediumMockData
        viewModel.getPlayWidget(
            mockShopId,
            CarouselPlayWidgetUiModel(
                "",
                0,
                "",
                "",
                BaseShopHomeWidgetUiModel.Header()
            )
        )
        coVerify { playWidgetTools.getWidgetFromNetwork(any(), any()) }
        every {
            playWidgetTools.updateActionReminder(playWidgetStateMediumMockData, mockChannelId, mockReminderType)
        } returns playWidgetUiModelMockDataWithReminder
        viewModel.updatePlayWidgetReminder(
            mockChannelId,
            false
        )
        val playWidgetUiModel = viewModel.playWidgetObservable.value?.playWidgetState?.model
        assert((playWidgetUiModel?.items?.first() as? PlayWidgetChannelUiModel)?.reminderType == mockReminderType)
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
            every { ShopPageHomeMapper.mapToListShopHomeWidget(any(), any(), any(), false, any(), any()) } returns listOf(
                ShopHomeCarousellProductUiModel(widgetId = "1")
            )
            viewModel.getWidgetContentData(
                listOf(
                    ShopPageWidgetLayoutUiModel(
                        widgetId = "1"
                    ),
                    ShopPageWidgetLayoutUiModel(
                        widgetId = "2"
                    )
                ),
                mockShopId,
                addressWidgetData,
                false,
                mockIsDirectPurchaseTrue
            )
            assert(shopHomeWidgetContentData.await() is Success)
            assert((shopHomeWidgetContentData.await() as? Success)?.data?.isNotEmpty() == true)
        }
    }

    @Test
    fun `when getWidgetContentData success should return expected thematic widget`() {
        runBlocking {
            val widgetName = WidgetName.BIG_CAMPAIGN_THEMATIC
            val widgetType = WidgetType.CAMPAIGN
            val widgetId = "2"
            val layoutOrder = 0
            val resultWidget = ThematicWidgetUiModel(
                name = widgetName,
                type = widgetType,
                widgetId = widgetId,
                layoutOrder = layoutOrder,
                header = DynamicHeaderUiModel()
            )

            val shopHomeWidgetContentData = async {
                viewModel.shopHomeWidgetContentData.first()
            }
            coEvery {
                getShopPageHomeLayoutV2UseCase.get().executeOnBackground()
            } returns ShopLayoutWidgetV2(
                listWidget = listOf(
                    ShopLayoutWidget.Widget(
                        name = widgetName,
                        type = widgetType,
                        widgetID = widgetId
                    )
                )
            )

            mockkObject(ShopPageHomeMapper)
            every { ShopPageHomeMapper.mapToListShopHomeWidget(any(), any(), any(), false, any(), any()) } returns listOf(
                resultWidget
            )

            viewModel.getWidgetContentData(
                listOf(
                    ShopPageWidgetLayoutUiModel(
                        widgetId = widgetId,
                        widgetType = widgetType,
                        widgetName = widgetName
                    )
                ),
                mockShopId,
                addressWidgetData,
                false,
                mockIsDirectPurchaseTrue
            )
            assert((shopHomeWidgetContentData.await() as? Success)?.data?.values?.first() == resultWidget)
        }
    }

    @Test
    fun `when getWidgetContentData success should return null`() {
        runBlocking {
            val shopHomeWidgetContentData = async {
                viewModel.shopHomeWidgetContentData.first()
            }
            coEvery {
                getShopPageHomeLayoutV2UseCase.get().executeOnBackground()
            } returns ShopLayoutWidgetV2()

            mockkObject(ShopPageHomeMapper)
            every { ShopPageHomeMapper.mapToListShopHomeWidget(any(), any(), any(), false, any(), any()) } returns listOf(
                ProductCardUiModel()
            )

            viewModel.getWidgetContentData(
                listOf(
                    ShopPageWidgetLayoutUiModel(
                        widgetId = "2",
                        widgetType = WidgetType.CAMPAIGN,
                        widgetName = WidgetName.BIG_CAMPAIGN_THEMATIC
                    )
                ),
                mockShopId,
                addressWidgetData,
                false,
                mockIsDirectPurchaseTrue
            )
            assert((shopHomeWidgetContentData.await() as? Success)?.data?.values?.first() == null)
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
                listOf(ShopPageWidgetLayoutUiModel()),
                mockShopId,
                addressWidgetData,
                false,
                mockIsDirectPurchaseTrue
            )
            assert(shopHomeWidgetContentData.await() is Fail)
            assert(shopHomeWidgetContentDataError.await().isNotEmpty())
        }
    }

    @Test
    fun `when call getShopWidgetDataWithUpdatedQuantity with mocked mini cart data, then product in cart should match with mini cart data`() {
        val mockMiniCartSimplifiedData = getMockMiniCartSimplifiedData()
        val mockShopHomeWidgetData = getMockShopHomeWidgetData()
        viewModel.setMiniCartData(mockMiniCartSimplifiedData)
        viewModel.getShopWidgetDataWithUpdatedQuantity(mockShopHomeWidgetData)
        viewModel.updatedShopHomeWidgetQuantityData.value?.onEach { widgetLayoutData ->
            when (widgetLayoutData) {
                is ShopHomeCarousellProductUiModel -> {
                    widgetLayoutData.productList.onEach {
                        checkProductListDataShouldMatchWithMatchedMiniCartData(
                            it,
                            mockMiniCartSimplifiedData
                        )
                    }
                }

                is ShopHomeFlashSaleUiModel -> {
                    widgetLayoutData.data?.firstOrNull()?.productList?.onEach {
                        checkProductListDataShouldMatchWithMatchedMiniCartData(
                            it,
                            mockMiniCartSimplifiedData
                        )
                    }
                }

                is ShopHomeProductUiModel -> {
                    checkProductListDataShouldMatchWithMatchedMiniCartData(
                        widgetLayoutData,
                        mockMiniCartSimplifiedData
                    )
                }
            }
        }
    }

    private fun getMatchedMiniCartItem(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        miniCartData: MiniCartSimplifiedData
    ): List<MiniCartItem.MiniCartItemProduct> {
        return miniCartData.let { miniCartSimplifiedData ->
            val isVariant = shopHomeProductUiModel.isVariant
            val listMatchedMiniCartItemProduct = if (isVariant) {
                miniCartSimplifiedData.miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>()
                    .filter { it.productParentId == shopHomeProductUiModel.parentId }
            } else {
                val childProductId = shopHomeProductUiModel.id.orEmpty()
                miniCartSimplifiedData.miniCartItems.getMiniCartItemProduct(childProductId)?.let {
                    listOf(it)
                }.orEmpty()
            }
            listMatchedMiniCartItemProduct.filter { !it.isError }
        }
    }

    private fun checkProductListDataShouldMatchWithMatchedMiniCartData(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        val matchedMiniCartItem = getMatchedMiniCartItem(
            shopHomeProductUiModel,
            miniCartSimplifiedData
        )
        if (matchedMiniCartItem.isNotEmpty()) {
            val quantityOnMiniCart = matchedMiniCartItem.sumOf { it.quantity }
            assert(quantityOnMiniCart == shopHomeProductUiModel.productInCart)
        } else {
            assert(shopHomeProductUiModel.productInCart.isZero())
        }
    }

    @Test
    fun `when call getShopWidgetDataWithUpdatedQuantity without mocked mini cart data, then product in cart should be zero`() {
        val mockShopHomeWidgetData = getMockShopHomeWidgetData()
        viewModel.getShopWidgetDataWithUpdatedQuantity(mockShopHomeWidgetData)
        viewModel.updatedShopHomeWidgetQuantityData.value?.onEach { widgetLayoutData ->
            when (widgetLayoutData) {
                is ShopHomeCarousellProductUiModel -> {
                    widgetLayoutData.productList.onEach {
                        assert(it.productInCart.isZero())
                    }
                }

                is ShopHomeFlashSaleUiModel -> {
                    widgetLayoutData.data?.firstOrNull()?.productList?.onEach {
                        assert(it.productInCart.isZero())
                    }
                }

                is ShopHomeProductUiModel -> {
                    assert(widgetLayoutData.productInCart.isZero())
                }
            }
        }
    }

    private fun getMockShopHomeWidgetData(): MutableList<Visitable<*>> {
        return mutableListOf(
            ShopHomeCarousellProductUiModel(
                type = WidgetType.PRODUCT,
                productList = listOf(
                    ShopHomeProductUiModel().apply {
                        id = "1"
                        productInCart = 3
                    },
                    ShopHomeProductUiModel().apply {
                        id = "8"
                    },
                    ShopHomeProductUiModel().apply {
                        parentId = "12"
                        isVariant = true
                    },
                    ShopHomeProductUiModel().apply {
                        parentId = "13"
                        isVariant = true
                    },
                    ShopHomeProductUiModel().apply {
                        parentId = "14"
                        isVariant = true
                    }
                )
            ),
            ShopHomeCarousellProductUiModel(
                type = WidgetType.PRODUCT,
                productList = listOf(
                    ShopHomeProductUiModel().apply {
                        id = "167"
                    },
                    ShopHomeProductUiModel().apply {
                        parentId = "11"
                        isVariant = true
                        productInCart = 10
                    }
                )
            ),
            ShopHomeCarousellProductUiModel(
                type = WidgetType.PRODUCT
            ),
            ShopHomeCarousellProductUiModel(
                type = WidgetType.PERSONALIZATION,
                name = WidgetName.REMINDER
            ),
            ShopHomeCarousellProductUiModel(
                type = WidgetType.PERSONALIZATION,
                name = WidgetName.RECENT_ACTIVITY
            ),
            ShopHomeCarousellProductUiModel(
                type = WidgetType.PERSONALIZATION,
                name = WidgetName.BUY_AGAIN
            ),
            ShopHomeCarousellProductUiModel(),
            ShopHomeFlashSaleUiModel(
                widgetId = "",
                layoutOrder = 0,
                name = "",
                type = "",
                header = BaseShopHomeWidgetUiModel.Header()
            ),
            ShopHomeFlashSaleUiModel(
                widgetId = "",
                layoutOrder = 0,
                name = "",
                type = "",
                header = BaseShopHomeWidgetUiModel.Header(),
                data = listOf(
                    ShopHomeFlashSaleUiModel.FlashSaleItem(
                        productList = listOf(
                            ShopHomeProductUiModel().apply {
                                id = "1"
                                productInCart = 3
                            }
                        )
                    )
                )
            ),
            ShopHomeProductUiModel()
        )
    }

    private fun getMockMiniCartSimplifiedData(): MiniCartSimplifiedData {
        return MiniCartSimplifiedData(
            miniCartItems = hashMapOf(
                MiniCartItemKey("1") to MiniCartItem.MiniCartItemProduct(productId = "1", quantity = 3),
                MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(productId = "2", productParentId = "12", quantity = 5),
                MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(productId = "3", productParentId = "12", quantity = 5),
                MiniCartItemKey("4") to MiniCartItem.MiniCartItemProduct(productId = "4", productParentId = "13", quantity = 5, isError = true),
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(productId = "5", productParentId = "13", quantity = 5),
                MiniCartItemKey("6") to MiniCartItem.MiniCartItemProduct(productId = "6", productParentId = "14", quantity = 5, isError = true),
                MiniCartItemKey("7") to MiniCartItem.MiniCartItemProduct(productId = "7", productParentId = "14", quantity = 5, isError = true),
                MiniCartItemKey("8") to MiniCartItem.MiniCartItemProduct(productId = "8", isError = true),
                MiniCartItemKey("9") to MiniCartItem.MiniCartItemParentProduct()
            )
        )
    }

    @Test
    fun `when call handleAtcFlow on add item to cart state is success, then miniCartAdd value should be success`() {
        val mockQuantity = 5
        val mockComponentName = "Product"
        val mockShopHomeProductUiModel = ShopHomeProductUiModel().apply {
            id = "33"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(
                AddToCartDataModel(
                    data = DataModel(
                        success = 1,
                        productId = "33"
                    )
                )
            )
        }
        viewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopHomeProductUiModel
        )
        assert(viewModel.miniCartAdd.value is Success)
        assert(viewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.ADD)
        assert(viewModel.createAffiliateCookieAtcProduct.value != null)
    }

    @Test
    fun `when call handleAtcFlow on add item to cart state is error, then miniCartAdd value should be fail`() {
        val mockQuantity = 5
        val mockComponentName = "Product"
        val mockShopHomeProductUiModel = ShopHomeProductUiModel().apply {
            id = "33"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        viewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopHomeProductUiModel
        )
        assert(viewModel.miniCartAdd.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow on remove item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 0
        val mockComponentName = "Product"
        val mockShopHomeProductUiModel = ShopHomeProductUiModel().apply {
            id = "1"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(
                RemoveFromCartData(
                    data = Data(
                        success = 1
                    )
                )
            )
        }
        viewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopHomeProductUiModel
        )
        assert(viewModel.miniCartRemove.value is Success)
        assert(viewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.REMOVE)
        assert(viewModel.createAffiliateCookieAtcProduct.value == null)
    }

    @Test
    fun `when call handleAtcFlow on remove item from cart state is error, then miniCartRemove value should be fail`() {
        val mockQuantity = 0
        val mockComponentName = "Product"
        val mockShopHomeProductUiModel = ShopHomeProductUiModel().apply {
            id = "1"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        viewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopHomeProductUiModel
        )
        assert(viewModel.miniCartRemove.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow on update add item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 10
        val mockComponentName = "Product"
        val mockShopHomeProductUiModel = ShopHomeProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(
                UpdateCartV2Data(
                    data = com.tokopedia.cartcommon.data.response.updatecart.Data(
                        status = true
                    )
                )
            )
        }
        viewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopHomeProductUiModel
        )
        assert(viewModel.miniCartUpdate.value is Success)
        assert(viewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.UPDATE_ADD)
        assert(viewModel.createAffiliateCookieAtcProduct.value != null)
    }

    @Test
    fun `when call handleAtcFlow on update remove item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopHomeProductUiModel = ShopHomeProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(
                UpdateCartV2Data(
                    data = com.tokopedia.cartcommon.data.response.updatecart.Data(
                        status = true
                    )
                )
            )
        }
        viewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopHomeProductUiModel
        )
        assert(viewModel.miniCartUpdate.value is Success)
        assert(viewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.UPDATE_REMOVE)
        assert(viewModel.createAffiliateCookieAtcProduct.value == null)
    }

    @Test
    fun `when call handleAtcFlow on update item from cart state is success, then miniCartRemove value should be fail`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopHomeProductUiModel = ShopHomeProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        viewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopHomeProductUiModel
        )
        assert(viewModel.miniCartUpdate.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow without set mini cart data, then miniCartAdd, miniCartRemove ,miniCartUpdate and shopPageAtcTracker value should be null`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopHomeProductUiModel = ShopHomeProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        viewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopHomeProductUiModel
        )
        assert(viewModel.miniCartAdd.value == null)
        assert(viewModel.miniCartUpdate.value == null)
        assert(viewModel.miniCartUpdate.value == null)
        assert(viewModel.shopPageAtcTracker.value == null)
    }

    @Test
    fun `when widget type is bundle expect isWidgetBundle to return true`() {
        val testData = ShopPageWidgetLayoutUiModel(widgetType = WidgetType.BUNDLE)
        assertTrue(viewModel.isWidgetBundle(testData))
    }

    @Test
    fun `when widget type is not bundle expect isWidgetBundle to return false`() {
        val testData = ShopPageWidgetLayoutUiModel(widgetType = WidgetType.CAMPAIGN)
        assertFalse(viewModel.isWidgetBundle(testData))
    }

    @Test
    fun `when call getLatestShopHomeWidgetData success and tab data is empty, then latestShopHomeWidgetData value should be success with empty list`() {
        coEvery {
            getShopDynamicTabUseCase.get().executeOnBackground()
        } returns ShopPageGetDynamicTabResponse()
        viewModel.getLatestShopHomeWidgetLayoutData(
            mockShopId,
            mockExtParam,
            addressWidgetData
        )
        val result = viewModel.latestShopHomeWidgetLayoutData.value
        assert(result is Success)
        assert((result as? Success)?.data?.listWidgetLayout?.isEmpty() == true)
    }

    @Test
    fun `when call getLatestShopHomeWidgetData success and with home tab data exists, then latestShopHomeWidgetData value should be success with no empty list`() {
        coEvery {
            getShopDynamicTabUseCase.get().executeOnBackground()
        } returns ShopPageGetDynamicTabResponse(
            shopPageGetDynamicTab = ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab(
                tabData = listOf(
                    ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData(
                        name = ShopPageTabName.HOME,
                        data = ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData.Data(
                            homeLayoutData = HomeLayoutData(
                                widgetIdList = listOf(
                                    WidgetIdList(
                                        widgetId = "1"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        viewModel.getLatestShopHomeWidgetLayoutData(
            mockShopId,
            mockExtParam,
            addressWidgetData
        )
        val result = viewModel.latestShopHomeWidgetLayoutData.value
        assert(result is Success)
        assert((result as? Success)?.data?.listWidgetLayout?.isEmpty() != true)
    }

    @Test
    fun `when call getLatestShopHomeWidgetData success and with home tab data not exists, then latestShopHomeWidgetData value should be success with no empty list`() {
        coEvery {
            getShopDynamicTabUseCase.get().executeOnBackground()
        } returns ShopPageGetDynamicTabResponse(
            shopPageGetDynamicTab = ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab(
                tabData = listOf(
                    ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData(
                        name = "other tab",
                        data = ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData.Data(
                            homeLayoutData = HomeLayoutData(
                                widgetIdList = listOf(
                                    WidgetIdList(
                                        widgetId = "1"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        viewModel.getLatestShopHomeWidgetLayoutData(
            mockShopId,
            mockExtParam,
            addressWidgetData
        )
        val result = viewModel.latestShopHomeWidgetLayoutData.value
        assert(result is Success)
        assert((result as? Success)?.data?.listWidgetLayout?.isEmpty() == true)
    }

    @Test
    fun `when call getLatestShopHomeWidgetData error, then latestShopHomeWidgetData value should be fail`() {
        coEvery {
            getShopDynamicTabUseCase.get().executeOnBackground()
        } throws Exception()
        viewModel.getLatestShopHomeWidgetLayoutData(
            mockShopId,
            mockExtParam,
            addressWidgetData
        )
        val result = viewModel.latestShopHomeWidgetLayoutData.value
        assert(result is Fail)
    }
}
