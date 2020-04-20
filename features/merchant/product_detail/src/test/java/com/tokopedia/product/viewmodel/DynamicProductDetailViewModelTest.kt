package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterTextModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductOpenShopDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.util.JsonFormatter
import com.tokopedia.product.util.TestDispatcherProvider
import com.tokopedia.product.warehouse.model.ProductActionSubmit
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.purchase_platform.common.usecase.SubmitHelpTicketUseCase
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
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.anyBoolean
import org.mockito.Matchers.anyString
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
    lateinit var getCartTypeUseCase: GetCartTypeUseCase
    @RelaxedMockK
    lateinit var toggleNotifyMeUseCase: ToggleNotifyMeUseCase
    @RelaxedMockK
    lateinit var sendTopAdsUseCase: SendTopAdsUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val viewModel by lazy {
        DynamicProductDetailViewModel(TestDispatcherProvider(), stickyLoginUseCase, getPdpLayoutUseCase, getProductInfoP2ShopUseCase, getProductInfoP2LoginUseCase, getProductInfoP2GeneralUseCase, getProductInfoP3RateEstimateUseCase, toggleFavoriteUseCase, removeWishlistUseCase, addWishListUseCase, getRecommendationUseCase,
                moveProductToWarehouseUseCase, moveProductToEtalaseUseCase, trackAffiliateUseCase, submitHelpTicketUseCase, updateCartCounterUseCase, addToCartUseCase, addToCartOcsUseCase, addToCartOccUseCase, getCartTypeUseCase, toggleNotifyMeUseCase, sendTopAdsUseCase, userSessionInterface)
    }

    /**
     * isShopOwner
     */
    @Test
    fun isShopOwnerTrue() {
        val shopId = "123"
        every {
            userSessionInterface.shopId
        } returns shopId

        val isShopOwner = viewModel.isShopOwner()

        Assert.assertTrue(isShopOwner)
    }

    @Test
    fun isShowOwnerFalse() {
        val shopId = "123"
        val anotherShopId = "312"
        every {
            userSessionInterface.shopId
        } returns anotherShopId

        val isShopOwner = viewModel.isShopOwner()

        Assert.assertFalse(isShopOwner)
    }

    /**
     * Recommendation Top Ads
     */
    @Test
    fun `click recom product with zero top ads`() {
        var topAdsClickUrl = ""
        val slotUrl = slot<String>()
        val asdData = JsonFormatter.createMockGraphqlSuccessResponse(RECOM_WIDGET_WITH_ZERO_TOPADS_JSON, RecomendationEntity::class.java)
        val mockRecomData = asdData.productRecommendationWidget?.data!!
        val recomWidget = RecommendationEntityMapper.mappingToRecommendationModel(mockRecomData)

        //Top Ads Counter
        var numberOfTopAdsClicked = 0

        var listOfProductIdIsTopads = recomWidget.first().recommendationItemList.filter {
            it.isTopAds
        }.map {
            it.productId
        }

        //Mock Click TopAds
        recomWidget.first().recommendationItemList.forEach {
            every {
                sendTopAdsUseCase.executeOnBackground(capture(slotUrl))
            } answers {
                topAdsClickUrl = slotUrl.captured
            }

            if (it.isTopAds) {
                numberOfTopAdsClicked++
                viewModel.sendTopAds(it.clickUrl)
                print(it.clickUrl)
                verify {
                    viewModel.sendTopAds(it.clickUrl)
                }
                Assert.assertTrue(it.productId in listOfProductIdIsTopads)
                Assert.assertTrue(topAdsClickUrl == it.clickUrl)
            }

            verify(inverse = true) {
                viewModel.sendTopAds(it.clickUrl)
            }
        }
        Assert.assertTrue(0 == numberOfTopAdsClicked)
    }

    @Test
    fun `click recom product with one top ads`() {
        var topAdsClickUrl = ""
        val slotUrl = slot<String>()
        val asdData = JsonFormatter.createMockGraphqlSuccessResponse(RECOM_WIDGET_WITH_ONE_TOPADS_JSON, RecomendationEntity::class.java)
        val mockRecomData = asdData.productRecommendationWidget?.data!!
        val recomWidget = RecommendationEntityMapper.mappingToRecommendationModel(mockRecomData)

        //Top Ads Counter
        var numberOfTopAdsClicked = 0
        val numberOfTopAdsClickedMock = recomWidget.first().recommendationItemList.count {
            it.isTopAds
        }

        var listOfProductIdIsTopads = recomWidget.first().recommendationItemList.filter {
            it.isTopAds
        }.map {
            it.productId
        }

        //Mock Click TopAds
        recomWidget.first().recommendationItemList.forEach {
            every {
                sendTopAdsUseCase.executeOnBackground(capture(slotUrl))
            } answers {
                topAdsClickUrl = slotUrl.captured
            }

            if (it.isTopAds) {
                numberOfTopAdsClicked++
                viewModel.sendTopAds(it.clickUrl)
                print(it.clickUrl)
                verify {
                    viewModel.sendTopAds(it.clickUrl)
                }
                Assert.assertTrue(it.productId in listOfProductIdIsTopads)
                Assert.assertTrue(topAdsClickUrl == it.clickUrl)
            }
        }
        Assert.assertTrue(numberOfTopAdsClickedMock == numberOfTopAdsClicked)
    }

    @Test
    fun `impress recom product with zero top ads`() {
        var topAdsClickUrl = ""
        val slotUrl = slot<String>()
        val asdData = JsonFormatter.createMockGraphqlSuccessResponse(RECOM_WIDGET_WITH_ZERO_TOPADS_JSON, RecomendationEntity::class.java)
        val mockRecomData = asdData.productRecommendationWidget?.data!!
        val recomWidget = RecommendationEntityMapper.mappingToRecommendationModel(mockRecomData)

        //Top Ads Counter
        var numberOfTopAdsClicked = 0

        var listOfProductIdIsTopads = recomWidget.first().recommendationItemList.filter {
            it.isTopAds
        }.map {
            it.productId
        }

        //Mock Click TopAds
        recomWidget.first().recommendationItemList.forEach {
            every {
                sendTopAdsUseCase.executeOnBackground(capture(slotUrl))
            } answers {
                topAdsClickUrl = slotUrl.captured
            }

            if (it.isTopAds) {
                numberOfTopAdsClicked++
                viewModel.sendTopAds(it.trackerImageUrl)
                verify {
                    viewModel.sendTopAds(it.trackerImageUrl)
                }
                Assert.assertTrue(it.productId in listOfProductIdIsTopads)
                Assert.assertTrue(topAdsClickUrl == it.trackerImageUrl)
            }

            verify(inverse = true) {
                viewModel.sendTopAds(it.trackerImageUrl)
            }
        }
        Assert.assertTrue(0 == numberOfTopAdsClicked)
    }

    @Test
    fun `impress recom with one top ads`() {
        var topAdsClickUrl = ""
        val slotUrl = slot<String>()
        val asdData = JsonFormatter.createMockGraphqlSuccessResponse(RECOM_WIDGET_WITH_ONE_TOPADS_JSON, RecomendationEntity::class.java)
        val mockRecomData = asdData.productRecommendationWidget?.data!!
        val recomWidget = RecommendationEntityMapper.mappingToRecommendationModel(mockRecomData)

        //Top Ads Counter
        var numberOfTopAds = 0
        val numberOfTopAdsMock = recomWidget.first().recommendationItemList.count {
            it.isTopAds
        }

        var listOfProductIdIsTopads = recomWidget.first().recommendationItemList.filter {
            it.isTopAds
        }.map {
            it.productId
        }

        //Mock Click TopAds
        recomWidget.first().recommendationItemList.forEach {
            every {
                sendTopAdsUseCase.executeOnBackground(capture(slotUrl))
            } answers {
                topAdsClickUrl = slotUrl.captured
            }

            if (it.isTopAds) {
                numberOfTopAds++
                viewModel.sendTopAds(it.trackerImageUrl)

                verify {
                    viewModel.sendTopAds(it.trackerImageUrl)
                }

                Assert.assertTrue(it.productId in listOfProductIdIsTopads)
                Assert.assertTrue(topAdsClickUrl == it.trackerImageUrl)
            }
        }
        Assert.assertTrue(numberOfTopAdsMock == numberOfTopAds)
    }

    /**
     * GetProductInfoP1
     */
    @Test
    fun onSuccessGetProductInfo() {
        val data = ProductDetailDataModel(listOfLayout = mutableListOf(ProductSnapshotDataModel()))
        val productParams = ProductParams("", "", "", "", "", "")

        val shopCore = ShopCore(domain = anyString())
        val dataP2Shop = ProductInfoP2ShopData(shopInfo = ShopInfo(shopCore = shopCore), tradeinResponse = TradeinResponse())

        val dataP2Login = ProductInfoP2Login(pdpAffiliate = TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate())
        val dataP2General = ProductInfoP2General()

        val dataP3 = ProductInfoP3(SummaryText(), RatesModel())

        every {
            viewModel.userId
        } returns "123"

        every {
            userSessionInterface.isLoggedIn
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

        Assert.assertNotNull(viewModel.shopInfo)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.shopInfo)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.nearestWarehouse)
        Assert.assertNotNull(viewModel.p2ShopDataResp.value?.tradeinResponse)
        Assert.assertEquals(viewModel.p2ShopDataResp.value?.shopCod, anyBoolean())

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
            getProductInfoP3RateEstimateUseCase.executeOnBackground()
        }
        Assert.assertNotNull(viewModel.productInfoP3RateEstimate.value)
        Assert.assertNotNull(viewModel.productInfoP3RateEstimate.value?.rateEstSummarizeText)
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

        val dataP3 = ProductInfoP3(SummaryText(), RatesModel())

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
        //Make sure not called
        coVerify(inverse = true) {
            getProductInfoP3RateEstimateUseCase.executeOnBackground()
        }

        Assert.assertTrue((viewModel.productLayout.value as Success).data.none {
            it is ProductOpenShopDataModel
        })

    }

    /**
     *  StickyLogin
     */
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
        val request = AddToCartDataModel(errorReporter = ErrorReporterModel(texts = ErrorReporterTextModel(submitDescription = "error")))
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

    /**
     * Job Cancel
     */
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