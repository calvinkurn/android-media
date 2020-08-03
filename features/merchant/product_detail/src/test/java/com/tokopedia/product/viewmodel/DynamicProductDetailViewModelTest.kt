package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterTextModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirection
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionResponse
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.AddressModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.util.JsonFormatter
import com.tokopedia.product.util.TestDispatcherProvider
import com.tokopedia.product.warehouse.model.ProductActionSubmit
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCore
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.model.ProductVariantCommon
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.Matchers.*
import rx.Observable


@ExperimentalCoroutinesApi
class DynamicProductDetailViewModelTest {

    companion object {
        const val RECOM_WIDGET_WITH_ONE_TOPADS_JSON = "json/recom_widget_with_one_topads.json"
        const val RECOM_WIDGET_WITH_ZERO_TOPADS_JSON = "json/recom_widget_with_zero_topads.json"
    }

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var stickyLoginUseCase: StickyLoginUseCase

    @RelaxedMockK
    lateinit var getPdpLayoutUseCase: GetPdpLayoutUseCase

    @RelaxedMockK
    lateinit var getProductInfoP2ShopUseCase: GetProductInfoP2ShopUseCase

    @RelaxedMockK
    lateinit var getProductInfoP2LoginUseCase: GetProductInfoP2LoginUseCase

    @RelaxedMockK
    lateinit var getProductInfoP2GeneralUseCase: GetProductInfoP2GeneralUseCase

    @RelaxedMockK
    lateinit var getProductInfoP3RateEstimateUseCase: GetProductInfoP3RateEstimateUseCase

    @RelaxedMockK
    lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @RelaxedMockK
    lateinit var removeWishlistUseCase: RemoveWishListUseCase

    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var moveProductToWarehouseUseCase: MoveProductToWarehouseUseCase

    @RelaxedMockK
    lateinit var moveProductToEtalaseUseCase: MoveProductToEtalaseUseCase

    @RelaxedMockK
    lateinit var trackAffiliateUseCase: TrackAffiliateUseCase

    @RelaxedMockK
    lateinit var submitHelpTicketUseCase: SubmitHelpTicketUseCase

    @RelaxedMockK
    lateinit var updateCartCounterUseCase: UpdateCartCounterUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var addToCartOcsUseCase: AddToCartOcsUseCase

    @RelaxedMockK
    lateinit var addToCartOccUseCase: AddToCartOccUseCase

    @RelaxedMockK
    lateinit var getProductInfoP3VariantUseCase: GetProductInfoP3VariantUseCase

    @RelaxedMockK
    lateinit var toggleNotifyMeUseCase: ToggleNotifyMeUseCase

    @RelaxedMockK
    lateinit var discussionMostHelpfulUseCase: DiscussionMostHelpfulUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun setupAfter() {
        viewModel.p3VariantResponse.removeObserver { }
        viewModel.productInfoP3RateEstimate.removeObserver { }
    }

    private val viewModel by lazy {
        DynamicProductDetailViewModel(TestDispatcherProvider(), stickyLoginUseCase, getPdpLayoutUseCase, getProductInfoP2ShopUseCase, getProductInfoP2LoginUseCase, getProductInfoP2GeneralUseCase, getProductInfoP3RateEstimateUseCase, toggleFavoriteUseCase, removeWishlistUseCase, addWishListUseCase, getRecommendationUseCase,
                moveProductToWarehouseUseCase, moveProductToEtalaseUseCase, trackAffiliateUseCase, submitHelpTicketUseCase, updateCartCounterUseCase, addToCartUseCase, addToCartOcsUseCase, addToCartOccUseCase, getProductInfoP3VariantUseCase, toggleNotifyMeUseCase, discussionMostHelpfulUseCase, userSessionInterface)
    }

    //=========================================VARIABLE SECTION======================================//
    //==============================================================================================//

    @Test
    fun `has shop authority`() {
        val shopInfo = ShopInfo(isAllowManage = 1)
        every {
            viewModel.isShopOwner()
        } returns true

        viewModel.shopInfo = shopInfo

        val hasShopAuthority = viewModel.hasShopAuthority()

        Assert.assertTrue(hasShopAuthority)
        viewModel.shopInfo = null
    }

    @Test
    fun `has not shop authority`() {
        val shopInfo = ShopInfo(isAllowManage = 0)
        every {
            viewModel.isShopOwner()
        } returns false

        viewModel.shopInfo = shopInfo

        val hasShopAuthority = viewModel.hasShopAuthority()

        Assert.assertFalse(hasShopAuthority)
        viewModel.shopInfo = null
    }

    @Test
    fun `is shop owner true`() {
        val shopId = "123"
        val getDynamicProductInfo = DynamicProductInfoP1(BasicInfo(shopID = shopId))
        viewModel.getDynamicProductInfoP1 = getDynamicProductInfo

        every {
            userSessionInterface.shopId
        } returns shopId

        every {
            viewModel.isUserSessionActive
        } returns true

        val isShopOwner = viewModel.isShopOwner()

        Assert.assertTrue(isShopOwner)
        viewModel.getDynamicProductInfoP1 = null
    }

    @Test
    fun `is shop owner false`() {
        val anotherShopId = "312"
        val getDynamicProductInfo = DynamicProductInfoP1(BasicInfo(shopID = "123"))
        viewModel.getDynamicProductInfoP1 = getDynamicProductInfo

        every {
            userSessionInterface.shopId
        } returns anotherShopId

        every {
            viewModel.isUserSessionActive
        } returns false

        val isShopOwner = viewModel.isShopOwner()

        Assert.assertFalse(isShopOwner)
        viewModel.getDynamicProductInfoP1 = null
    }
    //===================================END OF VARIABLE SECTION====================================//
    //==============================================================================================//

    //=========================================ATC SECTION==========================================//
    //==============================================================================================//
    @Test
    fun `on success normal atc`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartRequestParams()
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.createObservable(any()).toBlocking()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }

    @Test
    fun `on error normal atc`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartRequestParams()
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.createObservable(any()).toBlocking()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
    }

    @Test
    fun `on success ocs atc`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartOcsRequestParams()
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")

        coEvery {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.createObservable(any()).toBlocking()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }

    @Test
    fun `on error ocs atc`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartOcsRequestParams()
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.createObservable(any()).toBlocking()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
    }

    @Test
    fun `on success occ atc`() = runBlockingTest {
        val addToCartOccRequestParams = AddToCartOccRequestParams("123", "123", "1")
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")

        coEvery {
            addToCartOccUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOccRequestParams)

        coVerify {
            addToCartOccUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }

    @Test
    fun `on error occ atc`() = runBlockingTest {
        val addToCartOccRequestParams = AddToCartOccRequestParams("123", "123", "1")
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartOccUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.addToCart(addToCartOccRequestParams)

        coVerify {
            addToCartOccUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
    }
    //==================================END OF ATC SECTION==========================================//
    //==============================================================================================//

    //==================================TOP ADS SECTION=============================================//
    //==============================================================================================//
    /**
     * RecommendationWidget
     */
    @Test
    fun onSuccessLoadRecommendation() {
        val listOfRecom = arrayListOf(RecommendationWidget(), RecommendationWidget())
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.loadRecommendation()

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertEquals((viewModel.loadTopAdsProduct.value as Success).data, listOfRecom)
    }

    @Test
    fun onErrorLoadRecommendation() {
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking()
        } throws Throwable()

        viewModel.loadRecommendation()

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }
        print(viewModel.loadTopAdsProduct.value)
        Assert.assertTrue(viewModel.loadTopAdsProduct.value is Fail)
    }
    //==================================END OF TOP ADS SECTION======================================//
    //==============================================================================================//


    //======================================PDP SECTION=============================================//
    //==============================================================================================//
    /**
     * GetProductInfoP1
     */
    @Test
    fun onSuccessGetProductInfo() {
        val data = ProductDetailDataModel(listOfLayout = mutableListOf(ProductSnapshotDataModel()))
        val productParams = ProductParams("", "", "", "", "", "")

        val cartRedirectionData = CartRedirectionResponse(CartRedirection(data = listOf(CartTypeData(), CartTypeData())))
        val shopCore = ShopCore(domain = anyString())
        val dataP2Shop = ProductInfoP2ShopData(shopInfo = ShopInfo(shopCore = shopCore), tradeinResponse = TradeinResponse(), cartRedirectionResponse = cartRedirectionData)

        val dataP2Login = ProductInfoP2Login(pdpAffiliate = TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate())
        val dataP2General = ProductInfoP2General()

        val dataP3RateEstimate = ProductInfoP3(SummaryText(), RatesModel(), false, AddressModel())
        val dataP3Variant = ProductInfoP3Variant(CartRedirectionResponse())

        viewModel.p3VariantResponse.observeForever { }
        viewModel.productInfoP3RateEstimate.observeForever { }

        every {
            viewModel.userId
        } returns "123"

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns true

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns data

        coEvery {
            getProductInfoP2ShopUseCase.executeOnBackground()
        } returns dataP2Shop

        coEvery {
            getProductInfoP2LoginUseCase.executeOnBackground()
        } returns dataP2Login

        coEvery {
            getProductInfoP2GeneralUseCase.executeOnBackground()
        } returns dataP2General

        coEvery {
            getProductInfoP3RateEstimateUseCase.executeOnBackground()
        } returns dataP3RateEstimate

        coEvery {
            getProductInfoP3VariantUseCase.executeOnBackground()
        } returns dataP3Variant

        viewModel.getProductP1(productParams)

        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.productLayout.value is Success)

        //P2
        coVerify {
            getProductInfoP2ShopUseCase.executeOnBackground()
        }

        Assert.assertNotNull(viewModel.shopInfo)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.shopInfo)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.nearestWarehouse)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.tradeinResponse)
        Assert.assertEquals(viewModel.p2ShopDataResp.value?.shopCod, anyBoolean())
        Assert.assertTrue(viewModel.p2ShopDataResp.value?.cartRedirectionResponse?.cartRedirection?.data?.size != 0)

        coVerify {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }
        Assert.assertNotNull(viewModel.p2Login.value)
        Assert.assertNotNull(viewModel.p2Login.value?.pdpAffiliate)

        coVerify {
            getProductInfoP2GeneralUseCase.executeOnBackground()
        }
        Assert.assertNotNull(viewModel.p2General.value)

        //P3
        coVerify {
            getProductInfoP3VariantUseCase.executeOnBackground()
        }
        coVerify {
            getProductInfoP3RateEstimateUseCase.executeOnBackground()
        }

        Assert.assertNotNull(viewModel.p3VariantResponse.value)
        Assert.assertNotNull(viewModel.productInfoP3RateEstimate.value)
        Assert.assertNotNull(viewModel.productInfoP3RateEstimate.value?.rateEstSummarizeText)
        Assert.assertNotNull(viewModel.productInfoP3RateEstimate.value?.ratesModel)
        Assert.assertEquals(viewModel.productInfoP3RateEstimate.value?.userCod, anyBoolean())
    }

    @Test
    fun onErrorGetProductInfo() {
        val productParams = ProductParams("", "", "", "", "", "")

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getProductP1(productParams)
        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.productLayout.value is Fail)

        //P2
        coVerify(inverse = true) {
            getProductInfoP2ShopUseCase.executeOnBackground()
        }

        coVerify(inverse = true) {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

        coVerify(inverse = true) {
            getProductInfoP2GeneralUseCase.executeOnBackground()
        }

        coVerify(inverse = true) { getProductInfoP3VariantUseCase.executeOnBackground() }

        //P3
        coVerify(inverse = true) {
            getProductInfoP3RateEstimateUseCase.executeOnBackground()
        }
    }

    @Test
    fun onSuccessGetProductInfoNonLogin() {
        val data = ProductDetailDataModel(listOfLayout = mutableListOf(ProductSnapshotDataModel()))
        val productParams = ProductParams("", "", "", "", "", "")

        val shopCore = ShopCore(domain = anyString())
        val dataP2Shop = ProductInfoP2ShopData(shopInfo = ShopInfo(shopCore = shopCore), tradeinResponse = TradeinResponse())

        val dataP2Login = ProductInfoP2Login(pdpAffiliate = TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate())
        val dataP2General = ProductInfoP2General()

        val dataP3Variant = ProductInfoP3Variant(CartRedirectionResponse())
        val dataP3 = ProductInfoP3(SummaryText(), RatesModel())

        viewModel.p3VariantResponse.observeForever { }

        every {
            userSessionInterface.isLoggedIn
        } returns false

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns data

        coEvery {
            getProductInfoP2ShopUseCase.executeOnBackground()
        } returns dataP2Shop

        coEvery {
            getProductInfoP2LoginUseCase.executeOnBackground()
        } returns dataP2Login

        coEvery {
            getProductInfoP2GeneralUseCase.executeOnBackground()
        } returns dataP2General

        coEvery {
            getProductInfoP3VariantUseCase.executeOnBackground()
        } returns dataP3Variant

        coEvery {
            getProductInfoP3RateEstimateUseCase.executeOnBackground()
        } returns dataP3

        viewModel.getProductP1(productParams)

        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.productLayout.value is Success)

        //P2
        coVerify {
            getProductInfoP2ShopUseCase.executeOnBackground()
        }
        Assert.assertNotNull(viewModel.p2ShopDataResp.value)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.shopInfo)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.nearestWarehouse)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.tradeinResponse)
        Assert.assertEquals(viewModel.p2ShopDataResp.value?.shopCod, anyBoolean())
        //Make sure not called
        coVerify(inverse = true) {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

        coVerify {
            getProductInfoP2GeneralUseCase.executeOnBackground()
        }
        Assert.assertNotNull(viewModel.p2General.value)

        //P3
        //Variant
        coVerify { getProductInfoP3VariantUseCase.executeOnBackground() }
        Assert.assertNotNull(viewModel.p3VariantResponse.value)

        //RateEstimate
        //Make sure not called
        coVerify(inverse = true) {
            getProductInfoP3RateEstimateUseCase.executeOnBackground()
        }
    }

    /**
     *  Variant Section
     */
    @Test
    fun `process initial variant`() {
        viewModel.processVariant(ProductVariantCommon(), mutableMapOf())

        Assert.assertTrue(viewModel.initialVariantData.value != null)
    }

    @Test
    fun `variant clicked not partial`() {
        val partialySelect = false
        val imageVariant = "image"
        viewModel.onVariantClicked(ProductVariantCommon(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value == null)
    }

    @Test
    fun `variant clicked partialy with image not blank`() {
        val partialySelect = true
        val imageVariant = "image"
        viewModel.listOfParentMedia = mutableListOf(Media(uRLOriginal = "gambar 1"))
        viewModel.onVariantClicked(ProductVariantCommon(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value == null)
        Assert.assertTrue(viewModel.updatedImageVariant.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value?.second?.first()?.uRLOriginal == imageVariant)
    }

    @Test
    fun `variant clicked partialy with blank image`(){
        val partialySelect = true
        val imageVariant = "gambar gan"
        viewModel.listOfParentMedia = null
        viewModel.onVariantClicked(ProductVariantCommon(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value == null)
        Assert.assertTrue(viewModel.updatedImageVariant.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value?.second?.isEmpty() == true)
    }

    /**
     * Notify me
     */
    @Test
    fun `on success toggle notify me`() = runBlockingTest {
        val result = true

        coEvery {
            toggleNotifyMeUseCase.executeOnBackground().result.isSuccess
        } returns result

        viewModel.toggleTeaserNotifyMe(0, 0, "")
        coVerify { toggleNotifyMeUseCase.executeOnBackground() }

        Assert.assertTrue(viewModel.toggleTeaserNotifyMe.value is Success)
    }

    @Test
    fun `on error toggle notify me`() = runBlockingTest {
        val result = false

        coEvery {
            toggleNotifyMeUseCase.executeOnBackground().result.isSuccess
        } returns result

        viewModel.toggleTeaserNotifyMe(0, 0, "")
        coVerify { toggleNotifyMeUseCase.executeOnBackground() }

        Assert.assertTrue(!(viewModel.toggleTeaserNotifyMe.value as Success).data)
    }

    /**
     * Hit Affiliate, most of it do no op call
     */
    @Test
    fun onSuccessHitAffiliateTracker() {
        every {
            trackAffiliateUseCase.execute(captureLambda(), any())
        } answers {

        }

        viewModel.hitAffiliateTracker(anyString(), anyString())

        verify { viewModel.hitAffiliateTracker(anyString(), anyString()) }
    }

    @Test
    fun onErrorHitAffiliateTracker() {
        every {
            trackAffiliateUseCase.execute(any(), captureLambda())
        } answers {

        }

        viewModel.hitAffiliateTracker(anyString(), anyString())

        verify { viewModel.hitAffiliateTracker(anyString(), anyString()) }
    }


    /**
     * UpdateCartCounter
     */
    @Test
    fun onSuccessUpdateCartCounter() {
        val data = 10
        every {
            updateCartCounterUseCase.createObservable(RequestParams.EMPTY)
        } returns Observable.just(data)


        viewModel.updateCartCounerUseCase {
            Assert.assertEquals(it, data)
        }

        verify {
            updateCartCounterUseCase.createObservable(RequestParams.EMPTY)
        }
    }

    /**
     * HitSubmitTicket
     */
    @Test
    fun onSuccessHitSubmitTicket() {
        val data = SubmitTicketResult()
        val request = AddToCartDataModel()
        val requestParams = RequestParams()
        val onError = mockk<((Throwable?) -> Unit)>()

        every {
            submitHelpTicketUseCase.createObservable(requestParams)
        } returns Observable.just(data)

        viewModel.hitSubmitTicket(request, onError, {
            Assert.assertEquals(it, data)
        })
    }

    @Test
    fun onErrorHitSubmitTicket() {
        val data = SubmitTicketResult()
        val request = AddToCartDataModel(errorReporter = ErrorReporterModel(texts = ErrorReporterTextModel(submitDescription = "error")), errorMessage = arrayListOf("error ganteng"))
        val requestParams = RequestParams()
        val onSuccess = mockk<((SubmitTicketResult) -> Unit)>()

        every {
            submitHelpTicketUseCase.createObservable(requestParams)
        } returns Observable.just(data)

        viewModel.hitSubmitTicket(request, {
            Assert.assertTrue(it is Throwable)
        }, onSuccess)
    }

    /**
     * ToggleFavorite
     */
    @Test
    fun onSuccessToggleFavoriteShop() {
        val shopId = "1234"
        coEvery {
            toggleFavoriteUseCase.executeOnBackground().followShop.isSuccess
        } returns anyBoolean()

        viewModel.toggleFavorite(shopId)

        verify {
            toggleFavoriteUseCase.createRequestParam(shopId)
            toggleFavoriteUseCase.createRequestParam(shopId)
        }
        coVerify {
            toggleFavoriteUseCase.executeOnBackground()
        }

        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data, anyBoolean())
    }

    @Test
    fun onErrorToggleFavoriteShop() {
        val shopId = "1234"
        coEvery {
            toggleFavoriteUseCase.executeOnBackground().followShop.isSuccess
        } throws Throwable()

        viewModel.toggleFavorite(shopId)

        verify {
            toggleFavoriteUseCase.createRequestParam(shopId)
        }
        coVerify {
            toggleFavoriteUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.toggleFavoriteResult.value is Fail)
    }

    /**
     * MoveToWareHouse
     */
    @Test
    fun onSuccessMoveProductToWareHouse() {
        val productId = "123"
        val productActionSubmit = ProductActionSubmit()

        coEvery {
            moveProductToWarehouseUseCase.executeOnBackground()
        } returns productActionSubmit

        viewModel.moveProductToWareHouse(productId)
        coVerify {
            moveProductToWarehouseUseCase.executeOnBackground()
        }

        Assert.assertEquals((viewModel.moveToWarehouseResult.value as Success).data, anyBoolean())
    }

    @Test
    fun onErrorMoveProductToWareHouse() {
        //Given
        coEvery {
            moveProductToWarehouseUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.moveProductToWareHouse(anyString())

        verify {
            moveProductToWarehouseUseCase.createParams(anyString(), anyString(), anyString())
        }
        coVerify {
            moveProductToWarehouseUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.moveToWarehouseResult.value is Fail)
    }

    /**
     * MoveToEtalase
     */
    @Test
    fun onSuccessMoveProductToEtalase() {
        coEvery {
            moveProductToEtalaseUseCase.executeOnBackground()
        } returns ProductActionSubmit()

        viewModel.moveProductToEtalase(anyString(), anyString(), anyString())

        verify {
            moveProductToEtalaseUseCase.createParams(anyString(), anyString(), anyString(), anyString(), anyString())
        }

        coVerify {
            moveProductToEtalaseUseCase.executeOnBackground()
        }

        Assert.assertEquals((viewModel.moveToEtalaseResult.value as Success).data, anyBoolean())
    }

    @Test
    fun onErrorMoveProductToEtalase() {
        coEvery {
            moveProductToEtalaseUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.moveProductToEtalase(anyString(), anyString(), anyString())

        verify {
            moveProductToEtalaseUseCase.createParams(anyString(), anyString(), anyString(), anyString(), anyString())
        }

        coVerify {
            moveProductToEtalaseUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.moveToEtalaseResult.value is Fail)
    }

    /**
     * Add/Remove Wishlist
     */
    @Test
    fun onSuccessAddWishlist() {
        val productId = "123"
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessAddWishlist(productId)
        }

        viewModel.addWishList(productId, null, {
            Assert.assertEquals(it, productId)
        })
    }

    @Test
    fun onErrorAddWishlist() {
        val productId = ""
        val errorMessage = ""
        every {
            (addWishListUseCase.createObservable(any(), any(), any()))
        }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorAddWishList(errorMessage, productId)
        }

        viewModel.addWishList(productId, null, {
            Assert.assertEquals(it, errorMessage)
        })
    }

    @Test
    fun onSuccessRemoveWishlist() {
        val productId = "123"
        every { (removeWishlistUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessRemoveWishlist(productId)
        }

        viewModel.removeWishList(productId, null, {
            Assert.assertEquals(it, productId)
        })
    }

    @Test
    fun onErrorRemoveWishlist() {
        val productId = ""
        val errorMessage = ""
        every {
            (removeWishlistUseCase.createObservable(any(), any(), any()))
        }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorRemoveWishlist(errorMessage, productId)
        }

        viewModel.removeWishList(productId, null, {
            Assert.assertEquals(it, errorMessage)
        })
    }

    @Test
    fun onSuccessCancelWarehouseJob() {
        viewModel.cancelWarehouseUseCase()
        verify { viewModel.cancelWarehouseUseCase() }
    }

    @Test
    fun onSuccessCancelEtalaseJob() {
        viewModel.cancelEtalaseUseCase()
        verify { viewModel.cancelEtalaseUseCase() }
    }

    /**
     * Discussion Most Helpful
     */
    @Test
    fun `on success getDiscussionMostHelpful`() = runBlockingTest {
        val expectedResponse = DiscussionMostHelpfulResponseWrapper()

        coEvery {
            discussionMostHelpfulUseCase.executeOnBackground()
        } returns expectedResponse

        viewModel.getDiscussionMostHelpful("", "")
        coVerify { discussionMostHelpfulUseCase.executeOnBackground() }

        Assert.assertEquals(expectedResponse, (viewModel.discussionMostHelpful.value as Success).data)
    }

    @Test
    fun `on error getDiscussionMostHelpful`() = runBlockingTest {
        val expectedError = Throwable()

        coEvery {
            discussionMostHelpfulUseCase.executeOnBackground()
        } throws expectedError

        viewModel.getDiscussionMostHelpful("", "")
        coVerify { discussionMostHelpfulUseCase.executeOnBackground() }

        Assert.assertTrue(viewModel.discussionMostHelpful.value is Fail)
    }
    //======================================END OF PDP SECTION=======================================//
    //==============================================================================================//

    //======================================STICKY LOGIN SECTION=====================================//
    //==============================================================================================//
    @Test
    fun onSuccessStickyLogin() {
        val stickyList = StickyLoginTickerPojo(listOf(StickyLoginTickerPojo.TickerDetail(message = "", layout = StickyLoginConstant.LAYOUT_FLOATING)))
        val data = StickyLoginTickerPojo.TickerResponse(stickyList)
        val onError = mockk<((Throwable) -> Unit)>()

        every {
            stickyLoginUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(StickyLoginTickerPojo.TickerResponse) -> Unit>()
            onSuccess.invoke(data)
        }


        viewModel.getStickyLoginContent({
            Assert.assertEquals(it, data.response.tickers.first())
        }, onError)

        verify {
            stickyLoginUseCase.execute(any(), any())
        }
    }

    @Test
    fun onErrorStickyLogin() {
        val throwable = Throwable()
        val onSuccess = mockk<((StickyLoginTickerPojo.TickerDetail) -> Unit)>()
        every {
            stickyLoginUseCase.execute(any(), captureLambda())
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(throwable)
        }

        viewModel.getStickyLoginContent(onSuccess, {
            Assert.assertEquals(it, throwable)
        })

        verify {
            stickyLoginUseCase.execute(any(), any())
        }
    }
    //======================================END OF STICKY LOGIN SECTION==============================//
    //==============================================================================================//

    @Test
    fun flush() {
        viewModel.flush()
        verify {
            stickyLoginUseCase.cancelJobs()
        }

        verify {
            getPdpLayoutUseCase.cancelJobs()
        }

        verify {
            getProductInfoP2ShopUseCase.cancelJobs()
        }

        verify {
            getProductInfoP2LoginUseCase.cancelJobs()
        }

        verify {
            getProductInfoP2GeneralUseCase.cancelJobs()
        }

        verify {
            getProductInfoP3RateEstimateUseCase.cancelJobs()
        }

        verify {
            toggleFavoriteUseCase.cancelJobs()
        }

        verify {
            trackAffiliateUseCase.cancelJobs()
        }

        verify {
            moveProductToWarehouseUseCase.cancelJobs()
        }

        verify {
            moveProductToEtalaseUseCase.cancelJobs()
        }

        verify {
            getRecommendationUseCase.unsubscribe()
        }

        verify {
            removeWishlistUseCase.unsubscribe()
        }
    }
}