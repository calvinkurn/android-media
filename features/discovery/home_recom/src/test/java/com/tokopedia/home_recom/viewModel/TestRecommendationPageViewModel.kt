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
import com.tokopedia.home_recom.util.RecommendationRollenceController
import com.tokopedia.home_recom.util.Status
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.GetTopadsIsAdsUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.model.TopadsIsAdsQuery
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingData
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingDataProduct
import com.tokopedia.topads.sdk.domain.model.TopadsProduct
import com.tokopedia.topads.sdk.domain.model.TopadsStatus
import com.tokopedia.topads.sdk.domain.model.Image
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
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
    private val addToWishlistV2UseCase = mockk<AddToWishlistV2UseCase>(relaxed = true)
    private val deleteWishlistV2UseCase = mockk<DeleteWishlistV2UseCase>(relaxed = true)
    private val topAdsWishlishedUseCase = mockk<TopAdsWishlishedUseCase>(relaxed = true)
    private val getPrimaryProductUseCase = mockk<GetPrimaryProductUseCase>(relaxed = true)
    private val getTopadsIsAdsUseCase = mockk<GetTopadsIsAdsUseCase>(relaxed = true)
    private val getTopAdsHeadlineUseCase = mockk<GetTopAdsHeadlineUseCase>(relaxed = true)
    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val remoteConfig = mockk<FirebaseRemoteConfigImpl>(relaxed = true)
    private val mockWishlistListener: WishlistV2ActionListener = mockk(relaxed = true)
    private val topAdsAddressHelper: TopAdsAddressHelper = mockk(relaxed = true)

    private val viewModel: RecommendationPageViewModel = spyk(RecommendationPageViewModel(
            userSessionInterface = userSession,
            dispatcher = RecommendationDispatcherTest(),
            getRecommendationUseCase = getRecommendationUseCase,
            addToWishlistV2UseCase = addToWishlistV2UseCase,
            deleteWishlistV2UseCase = deleteWishlistV2UseCase,
            topAdsWishlishedUseCase = topAdsWishlishedUseCase,
            addToCartUseCase = addToCartUseCase,
            getTopadsIsAdsUseCase = getTopadsIsAdsUseCase,
            getPrimaryProductUseCase = getPrimaryProductUseCase,
            getTopAdsHeadlineUseCase = getTopAdsHeadlineUseCase,
            remoteConfig = remoteConfig,
            topAdsAddressHelper = topAdsAddressHelper
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
    fun `given success when add to wishlistV2 then call onSuccessAddWishlist`(){
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        viewModel.addWishlistV2(recommendation.productId.toString(), mockWishlistListener)

        verify { addToWishlistV2UseCase.setParams(recommendation.productId.toString(), userSession.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
        verify { mockWishlistListener.onSuccessAddWishlist(any(), any()) }
    }

    @Test
    fun `given error when add to wishlistv2 then call onErrorAddWishList`() {
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        viewModel.addWishlistV2(recommendation.productId.toString(), mockWishlistListener)

        verify { addToWishlistV2UseCase.setParams(recommendation.productId.toString(), userSession.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
        verify { mockWishlistListener.onErrorAddWishList(any(), any()) }
    }

    @Test
    fun `given success delete wishlist when remove wishlistV2 then call onSuccessRemoveWishlist`(){
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        viewModel.removeWishlistV2(recommendation.productId.toString(), mockWishlistListener)

        verify { deleteWishlistV2UseCase.setParams(recommendation.productId.toString(), userSession.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
        verify { mockWishlistListener.onSuccessRemoveWishlist(any(), any()) }
    }

    @Test
    fun `given failed delete wishlist when remove wishlistV2 then call onErrorRemoveWishlist`(){
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        viewModel.removeWishlistV2(recommendation.productId.toString(), mockWishlistListener)

        verify { deleteWishlistV2UseCase.setParams(recommendation.productId.toString(), userSession.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
        verify { mockWishlistListener.onErrorRemoveWishlist(any(), any()) }
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
    fun `given success when buy now then pass product to buyNowLiveData`(){
        val product = ProductInfoDataModel(productDetailData = ProductDetailData())
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 1
                )
        ))
        viewModel.onBuyNow(product)
        Assert.assertTrue(viewModel.buyNowLiveData.value?.status == Status.SUCCESS)
        Assert.assertTrue(viewModel.buyNowLiveData.value?.data == product)
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
    fun `given success from network with error status when buy now then pass error`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_ERROR
        ))
        viewModel.onBuyNow(ProductInfoDataModel(productDetailData = ProductDetailData()))
        Assert.assertTrue(viewModel.buyNowLiveData.value?.isError() == true)
    }

    @Test
    fun `given error from network when buy now then pass error`(){
        val error = TimeoutException()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.error(error)
        viewModel.onBuyNow(ProductInfoDataModel(productDetailData = ProductDetailData()))
        Assert.assertTrue(viewModel.buyNowLiveData.value?.isError() == true && viewModel.buyNowLiveData.value?.exception == error)
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
    fun `given success getting data when get recommendation list from google shopping and using rollence variant then visitable list should contain RecommendationCPMDataModel`() {
        val queryParam = "?ref=googleshopping"
        val productId = ""
        val topAdsHeadlineResponse = TopAdsHeadlineResponse(displayAds = CpmModel().apply {
            data = mutableListOf(CpmData().apply { id = "1" }, CpmData().apply { id = "2" })
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

        every { getTopAdsHeadlineUseCase.setParams(any(), any()) } just runs
        coEvery { getTopAdsHeadlineUseCase.executeOnBackground() } returns topAdsHeadlineResponse

        mockkObject(RecommendationRollenceController)
        every { RecommendationRollenceController.isRecommendationCPMRollenceVariant() } returns true

        viewModel.getRecommendationList(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value!=null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationCPMDataModel>()?.isNotEmpty()==true)
    }

    @Test
    fun `given success getting data when get recommendation list from google shopping and not using rollence variant then visitable list should not contain RecommendationCPMDataModel`() {
        val queryParam = "?ref=googleshopping"
        val productId = ""
        val topAdsHeadlineResponse = TopAdsHeadlineResponse(displayAds = CpmModel().apply {
            data = mutableListOf(CpmData().apply { id = "1" }, CpmData().apply { id = "2" })
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

        every { getTopAdsHeadlineUseCase.setParams(any(), any()) } just runs
        coEvery { getTopAdsHeadlineUseCase.executeOnBackground() } returns topAdsHeadlineResponse

        mockkObject(RecommendationRollenceController)
        every { RecommendationRollenceController.isRecommendationCPMRollenceVariant() } returns false

        viewModel.getRecommendationList(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value!=null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationCPMDataModel>()?.isEmpty()==true)
    }

    @Test
    fun `given success getting data when get recommendation list from source other than google shopping and using rollence variant then visitable list should not contain RecommendationCPMDataModel`() {
        val queryParam = ""
        val productId = ""
        val topAdsHeadlineResponse = TopAdsHeadlineResponse(displayAds = CpmModel().apply {
            data = mutableListOf(CpmData().apply { id = "1" }, CpmData().apply { id = "2" })
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

        every { getTopAdsHeadlineUseCase.setParams(any(),any()) } just runs
        coEvery { getTopAdsHeadlineUseCase.executeOnBackground() } returns topAdsHeadlineResponse

        mockkObject(RecommendationRollenceController)
        every { RecommendationRollenceController.isRecommendationCPMRollenceVariant() } returns true

        viewModel.getRecommendationList(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value!=null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationCPMDataModel>()?.isEmpty()==true)
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

        every { getTopAdsHeadlineUseCase.setParams(any(), any()) } just runs
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
                pageName = any(),
                src = any()
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
                pageName = any(),
                src = any()
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
                pageName = any(),
                src = any()
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
                pageName = any(),
                src = any()
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

    @Test
    fun `given success getting data when get recommendation list from google shopping then visitable list should contain RecommendationCPMDataModel`() {
        val queryParam = "?ref=googleshopping"
        val productId = ""
        val topAdsHeadlineResponse = TopAdsHeadlineResponse(displayAds = CpmModel().apply {
            data = mutableListOf(CpmData().apply { id = "1" }, CpmData().apply { id = "2" })
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

        every { getTopAdsHeadlineUseCase.setParams(any(), any()) } just runs
        coEvery { getTopAdsHeadlineUseCase.executeOnBackground() } returns topAdsHeadlineResponse

        viewModel.getRecommendationList(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value!=null)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<RecommendationCPMDataModel>()?.isNotEmpty()==true)
    }

    @Test
    fun `given success response when get topads status with empty topads then topads product still false`() {
        val productId = ""
        val queryParam = ""
        val errorCode = 200
        val topadsIsAdsQuery = TopadsIsAdsQuery(
            TopAdsGetDynamicSlottingData(
                productList = listOf(),
                status = TopadsStatus(error_code = errorCode)
            )
        )

        every { remoteConfig.getLong(any(),any()) } returns 5000L
        coEvery { viewModel.recommendationListLiveData.value } returns listOf(ProductInfoDataModel(
            ProductDetailData()
        ))

        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData?.isTopads == false)

        every {
            getTopadsIsAdsUseCase.setParams(
                productId = productId,
                productKey = any(),
                shopDomain = any(),
                urlParam = queryParam,
                pageName = any(),
                src = any()
            )
        } just runs
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns topadsIsAdsQuery

        viewModel.getProductTopadsStatus(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData?.isTopads == false)
    }

    @Test
    fun `given product info with null product detail when get topads data then product detail still null`() {
        val productId = ""
        val queryParam = ""
        val errorCode = 200
        val topadsIsAdsQuery = TopadsIsAdsQuery(
            TopAdsGetDynamicSlottingData(
                productList = listOf(),
                status = TopadsStatus(error_code = errorCode)
            )
        )

        every { remoteConfig.getLong(any(),any()) } returns 5000L
        coEvery { viewModel.recommendationListLiveData.value } returns listOf(ProductInfoDataModel())

        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData == null)

        every {
            getTopadsIsAdsUseCase.setParams(
                productId = productId,
                productKey = any(),
                shopDomain = any(),
                urlParam = queryParam,
                pageName = any(),
                src = any()
            )
        } just runs
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns topadsIsAdsQuery

        viewModel.getProductTopadsStatus(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData == null)
    }

    @Test
    fun `given empty recommendation list when get topads data then recommendation list still empty`() {
        val productId = ""
        val queryParam = ""
        val errorCode = 200
        val topadsIsAdsQuery = TopadsIsAdsQuery(
            TopAdsGetDynamicSlottingData(
                productList = listOf(),
                status = TopadsStatus(error_code = errorCode)
            )
        )

        every { remoteConfig.getLong(any(),any()) } returns 5000L
        coEvery { viewModel.recommendationListLiveData.value } returns listOf()

        every {
            getTopadsIsAdsUseCase.setParams(
                productId = productId,
                productKey = any(),
                shopDomain = any(),
                urlParam = queryParam,
                pageName = any(),
                src = any()
            )
        } just runs
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns topadsIsAdsQuery

        viewModel.getProductTopadsStatus(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value?.isEmpty() == true)
    }

    @Test
    fun `given something`() {
        val queryParam = "?ref=${RecommendationPageViewModel.PARAM_RECOMPUSH}"
        val productId = ""
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
                pageName = any(),
                src = any()
            )
        } just runs
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns topadsIsAdsQuery

        viewModel.getProductTopadsStatus(productId, queryParam)

        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData?.isTopads == isChargeTopAds)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData?.clickUrl == clickUrlTopAds)
        assert(viewModel.recommendationListLiveData.value?.filterIsInstance<ProductInfoDataModel>()?.first()?.productDetailData?.trackerImageUrl == productImageUrl)
    }
}