package com.tokopedia.home_recom.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.util.RecommendationDispatcherTest
import com.tokopedia.home_recom.util.Status
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.GetTopadsIsAdsUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
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
    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val viewModel: RecommendationPageViewModel = RecommendationPageViewModel(
            userSessionInterface = userSession,
            dispatcher = RecommendationDispatcherTest(),
            addWishListUseCase = addWishListUseCase,
            getRecommendationUseCase = getRecommendationUseCase,
            removeWishListUseCase = removeWishListUseCase,
            topAdsWishlishedUseCase = topAdsWishlishedUseCase,
            addToCartUseCase = addToCartUseCase,
            getTopadsIsAdsUseCase = getTopadsIsAdsUseCase,
            getPrimaryProductUseCase = getPrimaryProductUseCase
    )
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
    fun `get success data from network with product info`(){
        val slot = slot<Subscriber<List<RecommendationWidget>>>()
        coEvery { getPrimaryProductUseCase.executeOnBackground() } returns PrimaryProductEntity()
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
}