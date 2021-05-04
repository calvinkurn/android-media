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
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimate
import com.tokopedia.product.detail.data.model.ratesestimate.UserLocationRequest
import com.tokopedia.product.detail.data.model.restrictioninfo.BebasOngkir
import com.tokopedia.product.detail.data.model.restrictioninfo.BebasOngkirImage
import com.tokopedia.product.detail.data.model.restrictioninfo.BebasOngkirProduct
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.util.DynamicProductDetailTalkGoToWriteDiscussion
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.util.ProductDetailTestUtil
import com.tokopedia.product.warehouse.model.ProductActionSubmit
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.FollowShop
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.model.ProductVariant
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Lazy
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

    @RelaxedMockK
    lateinit var topAdsImageViewUseCase: TopAdsImageViewUseCase

    @RelaxedMockK
    lateinit var getRecommendationFilterChips: GetRecommendationFilterChips

    private lateinit var spykViewModel: DynamicProductDetailViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(RemoteConfigInstance::class)
        spykViewModel = spyk(DynamicProductDetailViewModel(CoroutineTestDispatchersProvider, Lazy { getPdpLayoutUseCase }, Lazy { getProductInfoP2LoginUseCase }, Lazy { getProductInfoP2OtherUseCase }, Lazy { getProductInfoP2DataUseCase }, Lazy { getProductInfoP3UseCase }, Lazy { toggleFavoriteUseCase }, Lazy { removeWishlistUseCase }, Lazy { addWishListUseCase }, Lazy { getRecommendationUseCase },
                Lazy { getRecommendationFilterChips }, Lazy { moveProductToWarehouseUseCase }, Lazy { moveProductToEtalaseUseCase }, Lazy { trackAffiliateUseCase }, Lazy { submitHelpTicketUseCase }, Lazy { updateCartCounterUseCase }, Lazy { addToCartUseCase }, Lazy { addToCartOcsUseCase }, Lazy { addToCartOccUseCase }, Lazy { toggleNotifyMeUseCase }, Lazy { discussionMostHelpfulUseCase }, Lazy { topAdsImageViewUseCase }, userSessionInterface)
        )
    }

    @After
    fun setupAfter() {
        viewModel.productInfoP3.removeObserver { }
    }

    private val viewModel by lazy {
        DynamicProductDetailViewModel(CoroutineTestDispatchersProvider, Lazy { getPdpLayoutUseCase }, Lazy { getProductInfoP2LoginUseCase }, Lazy { getProductInfoP2OtherUseCase }, Lazy { getProductInfoP2DataUseCase }, Lazy { getProductInfoP3UseCase }, Lazy { toggleFavoriteUseCase }, Lazy { removeWishlistUseCase }, Lazy { addWishListUseCase }, Lazy { getRecommendationUseCase },
                Lazy { getRecommendationFilterChips }, Lazy { moveProductToWarehouseUseCase }, Lazy { moveProductToEtalaseUseCase }, Lazy { trackAffiliateUseCase }, Lazy { submitHelpTicketUseCase }, Lazy { updateCartCounterUseCase }, Lazy { addToCartUseCase }, Lazy { addToCartOcsUseCase }, Lazy { addToCartOccUseCase }, Lazy { toggleNotifyMeUseCase }, Lazy { discussionMostHelpfulUseCase }, Lazy { topAdsImageViewUseCase }, userSessionInterface)
    }

    //=========================================VARIABLE SECTION======================================//
    //==============================================================================================//
    @Test
    fun `on success get user location cache`() {
        viewModel.getProductP1(ProductParams(),userLocationLocal = LocalCacheModel("123"))

        val data = viewModel.getUserLocationCache()
        Assert.assertTrue(data.address_id == "123")
    }


    @Test
    fun `on success clear cache`() {
        viewModel.clearCacheP2Data()

        verify {
            getProductInfoP2DataUseCase.clearCache()
        }
    }

    @Test
    fun `success update video tracker data`() {
        viewModel.updateVideoTrackerData(10L, 120L)
        Assert.assertTrue(viewModel.videoTrackerData?.first == 10L)
        Assert.assertTrue(viewModel.videoTrackerData?.second == 120L)
    }

    //region getShopInfo
    @Test
    fun `get shop info from P2 when data null`() {
        every {
            spykViewModel.p2Data.value?.shopInfo
        } returns null
        val shopInfo = spykViewModel.getShopInfo()

        Assert.assertNotNull(shopInfo)
    }

    @Test
    fun `get shop info from P2 when data not null`() {
        every {
            spykViewModel.p2Data.value?.shopInfo
        } returns ShopInfo()

        val shopInfo = spykViewModel.getShopInfo()

        Assert.assertNotNull(shopInfo)
    }
    //endregion

    //region getCartTypeByProductId
    @Test
    fun `get cart type by product id when data not null`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(basic = BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value?.cartRedirection
        } returns mapOf("123" to CartTypeData())

        val cartRedirection = spykViewModel.getCartTypeByProductId()

        Assert.assertNotNull(cartRedirection)
    }

    @Test
    fun `get cart type by product id when data null`() {
        spykViewModel.getDynamicProductInfoP1 = null
        every {
            spykViewModel.p2Data.value?.cartRedirection
        } returns mapOf("321" to CartTypeData())

        val cartRedirection = spykViewModel.getCartTypeByProductId()

        Assert.assertNull(cartRedirection)
    }

    @Test
    fun `on success update variable p1`() {
        viewModel.updateDynamicProductInfoData(DynamicProductInfoP1())

        Assert.assertNotNull(viewModel.getDynamicProductInfoP1)
    }
    //endregion

    //region getMultiOriginByProductId
    @Test
    fun `get multi origin but p1 data is null`() {
        spykViewModel.getDynamicProductInfoP1 = null
        val data = viewModel.getMultiOriginByProductId()
        Assert.assertEquals(data.id, "")
    }

    @Test
    fun `get multi origin but p1 data not null`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1()
        val data = spykViewModel.getMultiOriginByProductId()
        Assert.assertNotNull(data.id)
    }
    //endregion

    //region getP2RatesEstimateByProductId
    @Test
    fun `get rates with success result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(ratesEstimate = listOfNotNull(P2RatesEstimate(listfProductId = listOf("123"))))

        val data = spykViewModel.getP2RatesEstimateByProductId()
        Assert.assertNotNull(data)
    }

    @Test
    fun `get rates with null result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(ratesEstimate = listOfNotNull(P2RatesEstimate(listfProductId = listOf("321"))))

        val data = spykViewModel.getP2RatesEstimateByProductId()
        Assert.assertNull(data)
    }
    //endregion

    //region getP2RatesBottomSheetData
    @Test
    fun `get bottom sheet rates error with success result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(ratesEstimate = listOfNotNull(P2RatesEstimate(listfProductId = listOf("123"))))

        val data = spykViewModel.getP2RatesBottomSheetData()
        Assert.assertNotNull(data)
    }

    @Test
    fun `get bottom sheet rates error with null result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(ratesEstimate = listOfNotNull(P2RatesEstimate(listfProductId = listOf("321"))))

        val data = spykViewModel.getP2RatesBottomSheetData()
        Assert.assertNull(data)
    }
    //endregion

    //region getBebasOngkirDataByProductId
    @Test
    fun `get bebas ongkir data with success result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        val imageUrl = "gambar boe gan"
        val boType = 1
        every {
            spykViewModel.p2Data.value?.bebasOngkir
        } returns BebasOngkir(boProduct = listOf(BebasOngkirProduct(boType, productId = "123")), boImages = listOf(BebasOngkirImage(boType, imageUrl)))

        val data = spykViewModel.getBebasOngkirDataByProductId()
        Assert.assertTrue(data.imageURL == "gambar boe gan")
        Assert.assertTrue(data.boType == 1)
    }

    @Test
    fun `get bebas ongkir data with null result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        val imageUrl = "gambar boe gan"
        val boType = 1
        every {
            spykViewModel.p2Data.value?.bebasOngkir
        } returns BebasOngkir(boProduct = listOf(BebasOngkirProduct(boType, productId = "312")), boImages = listOf(BebasOngkirImage(boType, imageUrl)))

        val data = spykViewModel.getBebasOngkirDataByProductId()
        Assert.assertTrue(data.imageURL == "")
        Assert.assertTrue(data.boType == 0)
    }
    //endregion

    @Test
    fun `update variable p1 with null`() {
        viewModel.updateDynamicProductInfoData(null)
        Assert.assertNull(viewModel.getDynamicProductInfoP1)
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
    fun onSuccessLoadRecommendationWithEmptyFilter() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        val listOfRecom = arrayListOf(recomWidget)
        val listOfFilter = listOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()
        val pageName = "pdp3"

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        coEvery {
            getRecommendationFilterChips.executeOnBackground().filterChip
        } returns listOfFilter

        (1..2).forEach { _ ->
            viewModel.loadRecommendation(pageName)
        }

        coVerify(exactly = 1) {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertTrue((viewModel.loadTopAdsProduct.value as Success).data.tid == recomWidget.tid)
    }

    @Test
    fun onSuccessLoadRecommendationWithNonEmptyFilter() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        val listOfRecom = arrayListOf(recomWidget)
        val listOfFilter = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip())
        val pageName = "pdp3"
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        coEvery {
            getRecommendationFilterChips.executeOnBackground().filterChip
        } returns listOfFilter

        viewModel.loadRecommendation(pageName)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertEquals((viewModel.loadTopAdsProduct.value as Success).data.tid, recomWidget.tid)
    }

    @Test
    fun onErrorLoadRecommendation() {
        val pageName = "pdp3"
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking()
        } throws Throwable()

        coEvery {
            getRecommendationFilterChips.executeOnBackground().filterChip
        } returns listOf()

        viewModel.loadRecommendation(pageName)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }
        Assert.assertTrue(viewModel.loadTopAdsProduct.value is Fail)
    }

    @Test
    fun `success get recommendation with exist list`() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        val listOfRecom = arrayListOf(recomWidget)
        val recomDataModel = ProductRecommendationDataModel(filterData = listOf(AnnotationChip(
                RecommendationFilterChipsEntity.RecommendationFilterChip(isActivated = true)
        )))

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.getRecommendation(recomDataModel, AnnotationChip(), 1, 1)

        coVerify {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        }

        Assert.assertTrue(viewModel.filterTopAdsProduct.value?.isRecomenDataEmpty == false)
        Assert.assertTrue((viewModel.statusFilterTopAdsProduct.value as Success).data)
    }

    @Test
    fun `success get recommendation with empty list`() {
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns emptyList()

        viewModel.getRecommendation(ProductRecommendationDataModel(), AnnotationChip(), 1, 1)

        coVerify {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        }

        Assert.assertNull(viewModel.filterTopAdsProduct.value?.recomWidgetData)
        Assert.assertFalse((viewModel.statusFilterTopAdsProduct.value as Success).data)
    }

    @Test
    fun `error get recommendation`() {
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } throws Throwable()

        viewModel.getRecommendation(ProductRecommendationDataModel(), AnnotationChip(), 1, 1)

        coVerify {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        }

        Assert.assertNull(viewModel.filterTopAdsProduct.value?.recomWidgetData)
        Assert.assertTrue(viewModel.statusFilterTopAdsProduct.value is Fail)
    }
    //==================================END OF TOP ADS SECTION======================================//
    //==============================================================================================//


    //======================================PDP SECTION=============================================//
    //==============================================================================================//
    /**
     * GetProductInfoP1
     */
    @Test
    fun `test correct product id parameter pdplayout`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val productId = "123"
        val productParams = ProductParams(productId, "", "", "", "", "")
        val userLocation = UserLocationRequest("123")
        `mock localization rollence`(true)
        `co every p1 success`(dataP1)

        coEvery {
            getPdpLayoutUseCase.requestParams
        } returns GetPdpLayoutUseCase.createParams(productParams.productId
                ?: "", productParams.shopDomain ?: "", productParams.productName
                ?: "", productParams.warehouseId ?: "", "", userLocation)

        viewModel.getProductP1(productParams, true, false, "", userLocationLocal = getUserLocationCache())

        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_ID, "") == productId)
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_KEY, "").isEmpty())
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_SHOP_DOMAIN, "").isEmpty())
        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_USER_LOCATION) as? UserLocationRequest) != null)
    }

    @Test
    fun `test correct shop domain and shop key parameter pdplayout`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val shopDomain = "shopYehez"
        val productKey = "productYehez"
        val productParams = ProductParams("", shopDomain, productKey, "", "", "")
        val userLocation = UserLocationRequest("123")

        `co every p1 success`(dataP1)
        `mock localization rollence`(true)
        coEvery {
            getPdpLayoutUseCase.requestParams
        } returns GetPdpLayoutUseCase.createParams(productParams.productId
                ?: "", productParams.shopDomain ?: "", productParams.productName
                ?: "", productParams.warehouseId ?: "", "", userLocation)

        viewModel.getProductP1(productParams, true, false, " ", userLocationLocal = getUserLocationCache())

        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_ID, "").isEmpty())
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_KEY, "") == productKey)
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_SHOP_DOMAIN, "") == shopDomain)
        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_USER_LOCATION) as? UserLocationRequest)?.districtID == "123")
    }

    @Test
    fun `on success get product info login`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")

        viewModel.productInfoP3.observeForever { }
        `mock localization rollence`(false)
        every {
            viewModel.userId
        } returns "123"

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns true

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, true, false, "", false, userLocationLocal = getUserLocationCache())

        `co verify p1 success`()

        Assert.assertTrue(viewModel.productLayout.value is Success)
        Assert.assertNotNull(viewModel.p2Data.value)
        Assert.assertNotNull(viewModel.p2Other.value)
        Assert.assertNotNull(viewModel.p2Login.value)
        Assert.assertNotNull(viewModel.productInfoP3.value)
        Assert.assertTrue(viewModel.topAdsImageView.value is Success)

        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.TRADE_IN } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.BY_ME } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.REPORT } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.SHIPMENT } == 0)
    }

    private fun `co verify p1 success`() {
        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getProductInfoP2DataUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getProductInfoP3UseCase.executeOnBackground(any(), any(), any(), any())
        }

        coVerify {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }
    }

    private fun `co every p1 success`(dataP1: ProductDetailDataModel) {
        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns dataP1

        coEvery {
            getProductInfoP2LoginUseCase.executeOnBackground()
        } returns ProductInfoP2Login()

        coEvery {
            getProductInfoP3UseCase.executeOnBackground(any(), any(), any(), any())
        } returns ProductInfoP3()

        coEvery {
            getProductInfoP2DataUseCase.executeOnBackground(any(), any())
        } returns ProductInfoP2UiData()

        coEvery {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        } returns ProductInfoP2Other()

        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } returns arrayListOf(TopAdsImageViewModel())
    }

    @Test
    fun `on error get product info login`() {
        val productParams = ProductParams("", "", "", "", "", "")
        `mock localization rollence`(true)
        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache())
        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.productLayout.value is Fail)
        Assert.assertNull(viewModel.productInfoP3.value)
        Assert.assertNull(viewModel.p2Data.value)
        Assert.assertNull(viewModel.p2Login.value)
        Assert.assertNull(viewModel.p2Other.value)

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
        `mock localization rollence`(true)
        every {
            userSessionInterface.isLoggedIn
        } returns false

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache())

        //P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getProductInfoP2DataUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getProductInfoP3UseCase.executeOnBackground(any(), any(), any(), any())
        }

        coVerify(inverse = true) {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.productLayout.value is Success)
        Assert.assertNotNull(viewModel.p2Data.value)
        Assert.assertNotNull(viewModel.p2Other.value)
        Assert.assertNull(viewModel.p2Login.value)
        Assert.assertNotNull(viewModel.productInfoP3.value)
        Assert.assertNotNull(viewModel.shouldHideFloatingButton())
    }

    @Test
    fun `on success remove unused component`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpThatShouldRemoveUnusedComponent()
        val productParams = ProductParams("", "", "", "", "", "")

        `mock localization rollence`(true)
        every {
            viewModel.userId
        } returns "123"

        every {
            viewModel.isShopOwner()
        } returns true

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, refreshPage = true, isAffiliate = true, isUseOldNav = true, userLocationLocal = getUserLocationCache())

        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.TRADE_IN } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.VALUE_PROP } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.VARIANT_OPTIONS } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.BY_ME } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.REPORT } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.SHIPMENT } == 1)
    }

    private fun `mock localization rollence`(expectedValue: Boolean) {
        val rollenceKey = if (expectedValue) "hyperlocal_android" else ""
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(ChooseAddressConstant.CHOOSE_ADDRESS_ROLLENCE_KEY, "")
        } returns rollenceKey
    }

    /**
     *  Variant Section
     */
    @Test
    fun `process initial variant`() {
        viewModel.processVariant(ProductVariant(), mutableMapOf())

        Assert.assertTrue(viewModel.initialVariantData.value != null)
    }

    @Test
    fun `variant clicked not partial`() {
        val partialySelect = false
        val imageVariant = "image"
        viewModel.onVariantClicked(ProductVariant(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value == null)
    }

    @Test
    fun `variant clicked partialy with image not blank`() {
        val partialySelect = true
        val imageVariant = "image"
        viewModel.listOfParentMedia = mutableListOf(Media(uRLOriginal = "gambar 1"))
        viewModel.onVariantClicked(ProductVariant(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value == null)
        Assert.assertTrue(viewModel.updatedImageVariant.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value?.second?.first()?.uRLOriginal == imageVariant)
    }

    @Test
    fun `variant clicked partialy with blank image`() {
        val partialySelect = true
        val imageVariant = "gambar gan"
        viewModel.listOfParentMedia = null
        viewModel.onVariantClicked(ProductVariant(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
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

        viewModel.toggleTeaserNotifyMe(0L, 0L, "")
        coVerify { toggleNotifyMeUseCase.executeOnBackground() }

        Assert.assertTrue(viewModel.toggleTeaserNotifyMe.value is Success)
    }

    @Test
    fun `on error toggle notify me`() = runBlockingTest {
        val result = false

        coEvery {
            toggleNotifyMeUseCase.executeOnBackground().result.isSuccess
        } returns result

        viewModel.toggleTeaserNotifyMe(0L, 0L, "")
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
        val data = FollowShop()
        data.isSuccess = true

        coEvery {
            toggleFavoriteUseCase.executeOnBackground(any()).followShop
        } returns data

        viewModel.toggleFavorite(shopId)

        coVerify {
            toggleFavoriteUseCase.executeOnBackground(any())
        }

        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data.first, true)
        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data.second, false)
    }

    @Test
    fun onSuccessToggleFavoriteShopNpl() {
        val shopId = "1234"
        val isNpl = true
        val data = FollowShop()
        data.isSuccess = true

        coEvery {
            toggleFavoriteUseCase.executeOnBackground(any()).followShop
        } returns data

        viewModel.toggleFavorite(shopId, isNpl)

        coVerify {
            toggleFavoriteUseCase.executeOnBackground(any())
        }

        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data.first, true)
        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data.second, isNpl)
    }

    @Test
    fun onErrorToggleFavoriteShop() {
        val shopId = "1234"
        coEvery {
            toggleFavoriteUseCase.executeOnBackground(any()).followShop?.isSuccess
        } throws Throwable()

        viewModel.toggleFavorite(shopId)

        coVerify {
            toggleFavoriteUseCase.executeOnBackground(any())
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

    @Test
    fun flush() {
        viewModel.flush()

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

    private fun getUserLocationCache(): LocalCacheModel {
        return LocalCacheModel("123", "123", "123", "123")
    }

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_DOMAIN = "shopDomain"
        const val PARAM_PRODUCT_KEY = "productKey"
        const val PARAM_USER_LOCATION = "userLocation"
    }
}