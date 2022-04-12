package com.tokopedia.home_recom.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCPMDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.model.entity.Data
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.model.entity.ProductRecommendationProductDetail
import com.tokopedia.home_recom.util.RecomServerLogger
import com.tokopedia.home_recom.util.RecommendationDispatcherTest
import com.tokopedia.home_recom.util.Status
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.GetTopadsIsAdsUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.model.TopadsIsAdsQuery
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingData
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingDataProduct
import com.tokopedia.topads.sdk.domain.model.TopadsProduct
import com.tokopedia.topads.sdk.domain.model.TopadsStatus
import com.tokopedia.topads.sdk.domain.model.Image
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.slot
import io.mockk.verify
import io.mockk.just
import io.mockk.runs
import io.mockk.mockkObject
import kotlinx.coroutines.delay
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Subscriber
import java.util.concurrent.TimeoutException

class TestRecommendationPageViewModel {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val addWishListUseCase = mockk<AddWishListUseCase>(relaxed = true)
    private val removeWishListUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    private val topAdsWishlishedUseCase = mockk<TopAdsWishlishedUseCase>(relaxed = true)
    private val getPrimaryProductUseCase = mockk<GetPrimaryProductUseCase>(relaxed = true)
    private val getTopadsIsAdsUseCase = mockk<GetTopadsIsAdsUseCase>(relaxed = true)
    private val getTopAdsHeadlineUseCase = mockk<GetTopAdsHeadlineUseCase>(relaxed = true)
    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val remoteConfig = mockk<FirebaseRemoteConfigImpl>(relaxed = true)

    private val viewModel: RecommendationPageViewModel = spyk(RecommendationPageViewModel(
        userSessionInterface = userSession,
        dispatcher = RecommendationDispatcherTest(),
        addWishListUseCase = addWishListUseCase,
        getRecommendationUseCase = getRecommendationUseCase,
        removeWishListUseCase = removeWishListUseCase,
        topAdsWishlishedUseCase = topAdsWishlishedUseCase,
        addToCartUseCase = addToCartUseCase,
        getTopadsIsAdsUseCase = getTopadsIsAdsUseCase,
        getPrimaryProductUseCase = getPrimaryProductUseCase,
        getTopAdsHeadlineUseCase = getTopAdsHeadlineUseCase,
        remoteConfig = remoteConfig
    ), recordPrivateCalls = true)
    private val recommendation = RecommendationItem(productId = 1234)
    private val recommendationTopads = RecommendationItem(productId = 1234, isTopAds = true, wishlistUrl = "1234")

    @Test
    fun `get success data from network without product info`(){
        coEvery { getPrimaryProductUseCase.executeOnBackground() } throws Exception()
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(
            RecommendationWidget(
                    recommendationItemList = listOf(RecommendationItem())
            )
        )
        viewModel.getRecommendationList("", "")
        assert(viewModel.recommendationListLiveData.value != null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationItemDataModel>()?.isNotEmpty() == true)
    }

    @Test
    fun `get success data from network with product info`() {
        val slot = slot<Subscriber<List<RecommendationWidget>>>()
        coEvery {
            getPrimaryProductUseCase.executeOnBackground()
        } returns PrimaryProductEntity(
                ProductRecommendationProductDetail(
                        listOf(Data())))
        every { getRecommendationUseCase.getOfficialStoreRecomParams(any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(
                RecommendationWidget(
                        recommendationItemList = listOf(RecommendationItem())
                )
        )
        viewModel.getRecommendationList("", "")
        assert(viewModel.recommendationListLiveData.value != null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.isNotEmpty() == true)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationItemDataModel>()?.isNotEmpty() == true)
    }

    @Test
    fun `get error timeout data from network`(){
        val slot = slot<Subscriber<List<RecommendationWidget>>>()
        coEvery { getPrimaryProductUseCase.executeOnBackground() } throws Exception()
        every { getRecommendationUseCase.getOfficialStoreRecomParams(any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.execute(any(), capture(slot)) } answers {
            slot.captured.onError(TimeoutException())
        }
        viewModel.getRecommendationList("", "")

        assert(viewModel.recommendationListLiveData.value != null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationErrorDataModel>()?.isNotEmpty() == true)
    }

    @Test
    fun `get success add wishlist from network`(){
        var status: Boolean? = null
        val slot = slot<WishListActionListener>()
        every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onSuccessAddWishlist(recommendation.productId.toString())
        }
        viewModel.addWishlist(recommendation.productId.toString(), recommendation.wishlistUrl, recommendation.isTopAds){ state, _ ->
            status = state
        }
        assert(status == true)
    }

    @Test
    fun `get error add wishlist from network`(){
        var status: Boolean? = null
        val slot = slot<WishListActionListener>()
        every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onErrorAddWishList("", recommendation.productId.toString())
        }
        viewModel.addWishlist(recommendation.productId.toString(), recommendation.wishlistUrl, recommendation.isTopAds){ state, _ ->
            status = state
        }
        assert(status == false)
    }

    @Test
    fun `get success add topads wishlist from network`(){
        var status: Boolean? = null
        val slot = slot<Subscriber<WishlistModel>>()
        val mockWishlistModel = mockk<WishlistModel>(relaxed = true)
        val mockData = mockk<WishlistModel.Data>(relaxed = true)

        every { mockWishlistModel.data } returns mockData
        every { mockData.isSuccess } returns true
        every { topAdsWishlishedUseCase.execute(any(), capture(slot)) } answers {
            slot.captured.onNext(mockWishlistModel)
        }
        viewModel.addWishlist(recommendationTopads.productId.toString(), recommendationTopads.wishlistUrl, true){ success, _ ->
            status = success
        }
        assert(status == true)
    }

    @Test
    fun `get error add topads wishlist from network`(){
        var status: Boolean? = null
        val slot = slot<Subscriber<WishlistModel>>()

        every { topAdsWishlishedUseCase.execute(any(), capture(slot)) } answers {
            slot.captured.onError(mockk())
        }
        viewModel.addWishlist(recommendationTopads.productId.toString(), recommendationTopads.wishlistUrl, true){ success, _ ->
            status = success
        }
        assert(status == false)
    }

    @Test
    fun `get success remove wishlist from network`(){
        var status: Boolean? = null
        val slot = slot<WishListActionListener>()
        every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onSuccessRemoveWishlist(recommendation.productId.toString())
        }
        viewModel.removeWishlist(recommendation.productId.toString()){ success, _ ->
            status = success
        }
        assert(status == true)
    }

    @Test
    fun `get error remove wishlist from network`(){
        var status: Boolean? = null
        val slot = slot<WishListActionListener>()
        every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onErrorRemoveWishlist("", recommendation.productId.toString())
        }
        viewModel.removeWishlist(recommendation.productId.toString()){ success, _ ->
            status = success
        }
        assert(status == false)
    }


    @Test
    fun `success atc`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 1
                )
        ))
        viewModel.onAddToCart(ProductInfoDataModel(productDetailData = ProductDetailData()))
        Assert.assertTrue(viewModel.addToCartLiveData.value?.status == Status.SUCCESS)
    }

    @Test
    fun `error atc`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_ERROR,
                data = DataModel(
                        success = 0
                )
        ))
        viewModel.onAddToCart(ProductInfoDataModel(productDetailData = ProductDetailData()))
        Assert.assertTrue(viewModel.addToCartLiveData.value?.status == Status.ERROR)
    }

    @Test
    fun `null product info atc`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_ERROR,
                data = DataModel(
                        success = 0
                )
        ))
        viewModel.onAddToCart(ProductInfoDataModel())
        Assert.assertTrue(viewModel.addToCartLiveData.value == null)
    }

    @Test
    fun `throw error atc`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.error(TimeoutException())
        viewModel.onAddToCart(ProductInfoDataModel(productDetailData = ProductDetailData()))
        Assert.assertTrue(viewModel.addToCartLiveData.value?.status == Status.ERROR)
    }

    @Test
    fun `success buy now`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 1
                )
        ))
        viewModel.onBuyNow(ProductInfoDataModel(productDetailData = ProductDetailData()))
        Assert.assertTrue(viewModel.buyNowLiveData.value?.status == Status.SUCCESS)
    }

    @Test
    fun `null product info buy now`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_ERROR,
                data = DataModel(
                        success = 0
                )
        ))
        viewModel.onBuyNow(ProductInfoDataModel())
        Assert.assertTrue(viewModel.addToCartLiveData.value == null)
    }

    @Test
    fun `error buy now`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 0
                )
        ))
        viewModel.onBuyNow(ProductInfoDataModel(productDetailData = ProductDetailData()))
        Assert.assertTrue(viewModel.buyNowLiveData.value?.status == Status.ERROR)
    }

    @Test
    fun `is login true`(){
        every { userSession.isLoggedIn } returns true
        Assert.assertTrue(viewModel.isLoggedIn())
    }

    @Test
    fun `is login false`(){
        every { userSession.isLoggedIn } returns false
        Assert.assertTrue(!viewModel.isLoggedIn())
    }

    @Test
    fun `given eligible to show headline CPM and success getting data when get recommendation list then visitable list should contain RecommendationCPMDataModel`() {
        val queryParam = ""
        val productId = ""
        val topAdsHeadlineResponse = TopAdsHeadlineResponse(displayAds = CpmModel().apply {
            data = listOf(CpmData().apply { id = "1" }, CpmData().apply { id = "2" })
        })

        every { getPrimaryProductUseCase.setParameter(any(), any()) } just runs
        coEvery { getPrimaryProductUseCase.executeOnBackground() } returns PrimaryProductEntity(
            ProductRecommendationProductDetail(
                listOf(Data())))
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(
            RecommendationWidget(
                recommendationItemList = listOf(RecommendationItem())
            )
        )
        every { viewModel invoke "eligibleToShowHeadlineCPM" withArguments listOf(queryParam) } returns true

        every { getTopAdsHeadlineUseCase.setParams(any()) } just runs
        coEvery { getTopAdsHeadlineUseCase.executeOnBackground() } returns topAdsHeadlineResponse

        viewModel.getRecommendationList(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value!=null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationCPMDataModel>()?.isNotEmpty()==true)
    }

    @Test
    fun `given eligible to show headline CPM and thrown exception when get topads headline then should add error to visitable list`() {
        val queryParam = ""
        val productId = ""

        every { getPrimaryProductUseCase.setParameter(any(), any()) } just runs
        coEvery { getPrimaryProductUseCase.executeOnBackground() } returns PrimaryProductEntity()
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.createObservable(any()).toBlocking().first() } returns emptyList()
        every { viewModel invoke "eligibleToShowHeadlineCPM" withArguments listOf(queryParam) } returns true

        every { getTopAdsHeadlineUseCase.setParams(any()) } just runs
        coEvery { getTopAdsHeadlineUseCase.executeOnBackground() } throws Exception()

        viewModel.getRecommendationList(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value!=null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationErrorDataModel>()?.isNotEmpty()==true)
    }

    @Test
    fun `given thrown exception when get recommendation list then should add error to visitable list`() {
        val queryParam = ""
        val productId = ""
        val error = Exception()

        every { getPrimaryProductUseCase.setParameter(any(), any()) } just runs
        coEvery { getPrimaryProductUseCase.executeOnBackground() } returns PrimaryProductEntity()
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.createObservable(any()).toBlocking().first() } throws error

        viewModel.getRecommendationList(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value != null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationErrorDataModel>()?.isNotEmpty() == true)
    }

    @Test
    fun `given null and empty result when get recommendation list then should add timeout exception to visitable list`() {
        val queryParam = ""
        val productId = ""
        val error = Exception()

        every { getPrimaryProductUseCase.setParameter(any(), any()) } just runs
        coEvery { getPrimaryProductUseCase.executeOnBackground() } throws error
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.createObservable(any()) } returns Observable.empty()

        viewModel.getRecommendationList(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value != null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationErrorDataModel>()?.isNotEmpty() == true)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationErrorDataModel>()?.first()?.throwable is TimeoutException)
    }

    @Test
    fun `given success response when get topads status then should update isTopads, clickUrl, and trackerImageUrl of product detail data`() {
        val productId = ""
        val queryParam = ""
        val isChargeTopAds = true
        val clickUrlTopAds = "url_test"
        val productImageUrl = "image_url_test"
        val errorCode = 200
        val topadsIsAdsQuery = TopadsIsAdsQuery(
            TopAdsGetDynamicSlottingData(
                productList = listOf(
                    TopAdsGetDynamicSlottingDataProduct(
                        isCharge = isChargeTopAds,
                        clickUrl = clickUrlTopAds,
                        product = TopadsProduct(image = Image(m_url = productImageUrl))
                    )),
                status = TopadsStatus(error_code = errorCode)
            )
        )

        every { remoteConfig.getLong(any(),any()) } returns 5000L
        coEvery { viewModel.recommendationListLiveData.value } returns listOf(ProductInfoDataModel(
            ProductDetailData()
        ))
        every {
            getTopadsIsAdsUseCase.setParams(
                productId = productId,
                productKey = any(),
                shopDomain = any(),
                urlParam = queryParam,
                pageName = any()
            )
        } just runs
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns topadsIsAdsQuery

        viewModel.getProductTopadsStatus(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData?.isTopads == isChargeTopAds)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData?.clickUrl == clickUrlTopAds)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData?.trackerImageUrl == productImageUrl)
    }

    @Test
    fun `given response code 400 when get topads status then should send log be error`() {
        val productId = ""
        val queryParam = ""
        val errorCode = 400
        val topadsIsAdsQuery = TopadsIsAdsQuery(TopAdsGetDynamicSlottingData(status = TopadsStatus(error_code = errorCode)))
        mockkObject(RecomServerLogger)

        every { remoteConfig.getLong(any(),any()) } returns 5000L
        coEvery { viewModel.recommendationListLiveData.value } returns listOf(ProductInfoDataModel(
            ProductDetailData()
        ))
        every {
            getTopadsIsAdsUseCase.setParams(
                productId = productId,
                productKey = any(),
                shopDomain = any(),
                urlParam = queryParam,
                pageName = any()
            )
        } just runs
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns topadsIsAdsQuery

        viewModel.getProductTopadsStatus(productId, queryParam)

        verify {
            RecomServerLogger.logServer(
                tag = RecomServerLogger.TOPADS_RECOM_PAGE_BE_ERROR,
                reason = "Error code $errorCode",
                productId = productId,
                queryParam = queryParam
            )
        }
    }

    @Test
    fun `given timeout when get topads status then should send log timeout`() {
        val productId = ""
        val queryParam = ""
        val errorCode = 400
        val topadsIsAdsQuery = TopadsIsAdsQuery(TopAdsGetDynamicSlottingData(status = TopadsStatus(error_code = errorCode)))
        mockkObject(RecomServerLogger)

        every { remoteConfig.getLong(any(),any()) } returns 5000L
        coEvery { viewModel.recommendationListLiveData.value } returns listOf(ProductInfoDataModel(
            ProductDetailData()
        ))
        every {
            getTopadsIsAdsUseCase.setParams(
                productId = productId,
                productKey = any(),
                shopDomain = any(),
                urlParam = queryParam,
                pageName = any()
            )
        } just runs

        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } coAnswers { delay(6000L); topadsIsAdsQuery }

        viewModel.getProductTopadsStatus(productId, queryParam)

        verify(timeout = 6000L) {
            RecomServerLogger.logServer(
                tag = RecomServerLogger.TOPADS_RECOM_PAGE_TIMEOUT_EXCEEDED,
                productId = productId,
                queryParam = queryParam
            )
        }
    }

    @Test
    fun `given thrown error when get topads status then should send log general error`() {
        val productId = ""
        val queryParam = ""
        mockkObject(RecomServerLogger)

        every { remoteConfig.getLong(any(),any()) } returns 5000L
        coEvery { viewModel.recommendationListLiveData.value } returns listOf(ProductInfoDataModel(
            ProductDetailData()
        ))
        every {
            getTopadsIsAdsUseCase.setParams(
                productId = productId,
                productKey = any(),
                shopDomain = any(),
                urlParam = queryParam,
                pageName = any()
            )
        } just runs
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } throws Exception()

        viewModel.getProductTopadsStatus(productId, queryParam)

        verify {
            RecomServerLogger.logServer(
                tag = RecomServerLogger.TOPADS_RECOM_PAGE_GENERAL_ERROR,
                throwable = any(),
                productId = productId,
                queryParam = queryParam
            )
        }
    }
}