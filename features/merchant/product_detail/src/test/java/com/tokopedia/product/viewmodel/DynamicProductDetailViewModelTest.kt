package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.product.TestDispatcherProvider
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText
import com.tokopedia.product.detail.updatecartcounter.interactor.UpdateCartCounterUseCase
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.warehouse.model.ProductActionSubmit
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.purchase_platform.common.usecase.SubmitHelpTicketUseCase
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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val viewModel by lazy {
        DynamicProductDetailViewModel(TestDispatcherProvider(), stickyLoginUseCase, getPdpLayoutUseCase, getProductInfoP2ShopUseCase, getProductInfoP2LoginUseCase, getProductInfoP2GeneralUseCase, getProductInfoP3UseCase, toggleFavoriteUseCase, removeWishlistUseCase, addWishListUseCase, getRecommendationUseCase,
                moveProductToWarehouseUseCase, moveProductToEtalaseUseCase, trackAffiliateUseCase, submitHelpTicketUseCase, updateCartCounterUseCase, userSessionInterface)
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
            getProductInfoP3UseCase.executeOnBackground()
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
            getProductInfoP3UseCase.executeOnBackground()
        }
        Assert.assertNotNull(viewModel.productInfoP3resp.value)
        Assert.assertNotNull(viewModel.productInfoP3resp.value?.rateEstSummarizeText)
        Assert.assertNotNull(viewModel.productInfoP3resp.value?.ratesModel)
        Assert.assertEquals(viewModel.productInfoP3resp.value?.userCod, anyBoolean())
        print(viewModel.productLayout.value)

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
            getProductInfoP3UseCase.executeOnBackground()
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
            getProductInfoP3UseCase.executeOnBackground()
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
            getProductInfoP3UseCase.executeOnBackground()
        }
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
        val request = AddToCartDataModel()
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

}