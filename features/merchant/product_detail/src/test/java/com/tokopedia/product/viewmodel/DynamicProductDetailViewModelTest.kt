package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.util.DynamicProductDetailTalkGoToWriteDiscussion
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.util.ProductDetailTestUtil
import com.tokopedia.product.util.TestDispatcherProvider
import com.tokopedia.product.warehouse.model.ProductActionSubmit
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
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

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var stickyLoginUseCase: StickyLoginUseCase

    @RelaxedMockK
    lateinit var getPdpLayoutUseCase: GetPdpLayoutUseCase

    @RelaxedMockK
    lateinit var getProductInfoP2LoginUseCase: GetProductInfoP2LoginUseCase

    @RelaxedMockK
    lateinit var getProductInfoP2OtherUseCase: GetProductInfoP2OtherUseCase

    @RelaxedMockK
    lateinit var getProductInfoP3UseCase: GetProductInfoP3UseCase

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
    lateinit var toggleNotifyMeUseCase: ToggleNotifyMeUseCase

    @RelaxedMockK
    lateinit var discussionMostHelpfulUseCase: DiscussionMostHelpfulUseCase

    @RelaxedMockK
    lateinit var getProductInfoP2DataUseCase: GetProductInfoP2DataUseCase

    private lateinit var spykViewModel : DynamicProductDetailViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        spykViewModel = spyk(DynamicProductDetailViewModel(TestDispatcherProvider(), stickyLoginUseCase, getPdpLayoutUseCase, getProductInfoP2LoginUseCase, getProductInfoP2OtherUseCase, getProductInfoP2DataUseCase, getProductInfoP3UseCase, toggleFavoriteUseCase, removeWishlistUseCase, addWishListUseCase, getRecommendationUseCase,
                moveProductToWarehouseUseCase, moveProductToEtalaseUseCase, trackAffiliateUseCase, submitHelpTicketUseCase, updateCartCounterUseCase, addToCartUseCase, addToCartOcsUseCase, addToCartOccUseCase, toggleNotifyMeUseCase, discussionMostHelpfulUseCase, userSessionInterface)
        )
    }

    @After
    fun setupAfter() {
        viewModel.productInfoP3.removeObserver { }
    }

    private val viewModel by lazy {
        DynamicProductDetailViewModel(TestDispatcherProvider(), stickyLoginUseCase, getPdpLayoutUseCase, getProductInfoP2LoginUseCase, getProductInfoP2OtherUseCase, getProductInfoP2DataUseCase, getProductInfoP3UseCase, toggleFavoriteUseCase, removeWishlistUseCase, addWishListUseCase, getRecommendationUseCase,
                moveProductToWarehouseUseCase, moveProductToEtalaseUseCase, trackAffiliateUseCase, submitHelpTicketUseCase, updateCartCounterUseCase, addToCartUseCase, addToCartOcsUseCase, addToCartOccUseCase, toggleNotifyMeUseCase, discussionMostHelpfulUseCase, userSessionInterface)
    }

    //=========================================VARIABLE SECTION======================================//
    //==============================================================================================//

    @Test
    fun `on success update variable p1`() {
        viewModel.updateDynamicProductInfoData(DynamicProductInfoP1())

        Assert.assertNotNull(viewModel.getDynamicProductInfoP1)
    }

    @Test
    fun `on success update notify me data`() {
        viewModel.updateNotifyMeData()
    }

    @Test
    fun `on success update talk action`() {
        viewModel.updateLastAction(DynamicProductDetailTalkGoToWriteDiscussion)
        Assert.assertTrue(viewModel.talkLastAction is DynamicProductDetailTalkGoToWriteDiscussion)
    }

    @Test
    fun `has shop authority`() {
        val mockAllowManage = ShopInfo(isAllowManage = 1)

        every {
            spykViewModel.getShopInfo()
        } returns mockAllowManage

        every {
            spykViewModel.isShopOwner()
        } returns true

        val hasShopAuthority = spykViewModel.hasShopAuthority()

        Assert.assertTrue(hasShopAuthority)
    }

    @Test
    fun `has not shop authority shopowner`() {
        val mockAllowManage = ShopInfo(isAllowManage = 1)

        every {
            spykViewModel.getShopInfo()
        } returns mockAllowManage

        every {
            spykViewModel.isShopOwner()
        } returns false

        val hasShopAuthority = spykViewModel.hasShopAuthority()

        Assert.assertTrue(hasShopAuthority)
    }

    @Test
    fun `has not shop authority allow manage`() {
        val mockAllowManage = ShopInfo(isAllowManage = 0)

        every {
            spykViewModel.getShopInfo()
        } returns mockAllowManage

        every {
            spykViewModel.isShopOwner()
        } returns true

        val hasShopAuthority = spykViewModel.hasShopAuthority()

        Assert.assertTrue(hasShopAuthority)
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
    fun `test correct product id parameter pdplayout`(){
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val productId = "123"
        val productParams = ProductParams(productId, "", "", "", "", "")

        `co every p1 success`(dataP1)

        coEvery {
            getPdpLayoutUseCase.requestParams
        } returns GetPdpLayoutUseCase.createParams(productParams.productId
                ?: "", productParams.shopDomain ?: "", productParams.productName ?: "", productParams.warehouseId ?: "")

        viewModel.getProductP1(productParams, true, false)

        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_ID, "") == productId)
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_KEY, "").isEmpty())
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_SHOP_DOMAIN, "").isEmpty())
    }

    @Test
    fun `test correct shop domain and shop key parameter pdplayout`(){
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val shopDomain = "shopYehez"
        val productKey = "productYehez"
        val productParams = ProductParams("", shopDomain, productKey, "", "", "")

        `co every p1 success`(dataP1)

        coEvery {
            getPdpLayoutUseCase.requestParams
        } returns GetPdpLayoutUseCase.createParams(productParams.productId
                ?: "", productParams.shopDomain ?: "", productParams.productName ?: "", productParams.warehouseId ?: "")

        viewModel.getProductP1(productParams, true, false)

        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_ID, "").isEmpty())
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_KEY, "") == productKey)
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_SHOP_DOMAIN, "") == shopDomain)
    }

    @Test
    fun `on success get product info login`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")

        viewModel.productInfoP3.observeForever { }

        every {
            viewModel.userId
        } returns "123"

        viewModel.enableCaching = false

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns true

       `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, true, false)

        `co verify p1 success`()

        Assert.assertTrue(viewModel.productLayout.value is Success)
        Assert.assertNotNull(viewModel.p2Data.value)
        Assert.assertNotNull(viewModel.p2Other.value)
        Assert.assertNotNull(viewModel.p2Login.value)
        Assert.assertNotNull(viewModel.productInfoP3.value)

        Assert.assertFalse(viewModel.enableCaching)

        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.VALUE_PROP } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.TRADE_IN } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.BY_ME } == 1)
    }

    private fun `co verify p1 success`(){
        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getProductInfoP2DataUseCase.executeOnBackground(any(), any(), any())
        }

        coVerify {
            getProductInfoP3UseCase.executeOnBackground(any(), any(), any())
        }

        coVerify {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }
    }

    private fun `co every p1 success`(dataP1 : ProductDetailDataModel){
        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns dataP1

        coEvery {
            getProductInfoP2LoginUseCase.executeOnBackground()
        } returns ProductInfoP2Login()

        coEvery {
            getProductInfoP3UseCase.executeOnBackground(any(), any(), any())
        } returns ProductInfoP3()

        coEvery {
            getProductInfoP2DataUseCase.executeOnBackground(any(), any(), any())
        } returns ProductInfoP2UiData()

        coEvery {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        } returns ProductInfoP2Other()
    }

    @Test
    fun `on error get product info login`() {
        val productParams = ProductParams("", "", "", "", "", "")
        viewModel.enableCaching = true

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getProductP1(productParams)
        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.productLayout.value is Fail)
        Assert.assertNull(viewModel.productInfoP3.value)
        Assert.assertNull(viewModel.p2Data.value)
        Assert.assertNull(viewModel.p2Login.value)
        Assert.assertNull(viewModel.p2Other.value)

        Assert.assertTrue(viewModel.enableCaching)

        coVerify(inverse = true) {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

        coVerify(inverse = true) {
            getProductInfoP2DataUseCase.executeOnBackground()
        }

        coVerify(inverse = true) {
            getProductInfoP2OtherUseCase.executeOnBackground()
        }
        coVerify(inverse = true) {
            getProductInfoP3UseCase.executeOnBackground()
        }
    }

    @Test
    fun `on success get product info non login`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")

        viewModel.productInfoP3.observeForever { }

        every {
            userSessionInterface.isLoggedIn
        } returns false

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams)

        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getProductInfoP2DataUseCase.executeOnBackground(any(), any(), any())
        }

        coVerify {
            getProductInfoP3UseCase.executeOnBackground(any(), any(), any())
        }

        coVerify(inverse = true) {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.productLayout.value is Success)
        Assert.assertNotNull(viewModel.p2Data.value)
        Assert.assertNotNull(viewModel.p2Other.value)
        Assert.assertNull(viewModel.p2Login.value)
        Assert.assertNotNull(viewModel.productInfoP3.value)
    }

    @Test
    fun `on success remove unused component`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpThatShouldRemoveUnusedComponent()
        val productParams = ProductParams("", "", "", "", "", "")

        every {
            viewModel.userId
        } returns "123"

        every {
            viewModel.isShopOwner()
        } returns true

        viewModel.enableCaching = false

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, refreshPage = true, isAffiliate = true)

        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.TRADE_IN} == 0 )
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.VALUE_PROP } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.VARIANT_OPTIONS } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.BY_ME } == 0)
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
    fun `variant clicked partialy with blank image`() {
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
    fun `on call sticky login login user`() {
        every {
            viewModel.isUserSessionActive
        } returns true

        stickyLoginUseCase.execute({}, {})

        verify(inverse = true) {
            stickyLoginUseCase.execute({}, {})
        }
    }

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
            getProductInfoP2LoginUseCase.cancelJobs()
        }

        verify {
            getProductInfoP2OtherUseCase.cancelJobs()
        }

        verify {
            getProductInfoP3UseCase.cancelJobs()
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

    companion object{
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_DOMAIN = "shopDomain"
        const val PARAM_PRODUCT_KEY = "productKey"
    }
}