package com.tokopedia.home_recom.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_recom.util.RecommendationDispatcherTest
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.util.concurrent.TimeoutException

class TestRecommendationPageViewModel {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val addWishListUseCase = mockk<AddWishListUseCase>(relaxed = true)
    private val removeWishListUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    private val topAdsWishlishedUseCase = mockk<TopAdsWishlishedUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val viewModel: RecommendationPageViewModel = RecommendationPageViewModel(
            userSessionInterface = userSession,
            dispatcher = RecommendationDispatcherTest(),
            addWishListUseCase = addWishListUseCase,
            getRecommendationUseCase = getRecommendationUseCase,
            removeWishListUseCase = removeWishListUseCase,
            topAdsWishlishedUseCase = topAdsWishlishedUseCase
    )
    private val recommendation = RecommendationItem(productId = 1234)

    @Test
    fun `get success data from network`(){
        val slot = slot<Subscriber<List<RecommendationWidget>>>()
        every { getRecommendationUseCase.getOfficialStoreRecomParams(any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.execute(any(), capture(slot)) } answers {
            slot.captured.onNext(listOf(
                    RecommendationWidget(
                            recommendationItemList = listOf(RecommendationItem())
                    )
            ))
        }
        viewModel.getRecommendationList(listOf(), "")
        assert(viewModel.recommendationListLiveData.value != null && viewModel.recommendationListLiveData.value!!.isSuccess())
    }

    @Test
    fun `get error timeout data from network`(){
        val slot = slot<Subscriber<List<RecommendationWidget>>>()
        every { getRecommendationUseCase.getOfficialStoreRecomParams(any(), any(), any()) } returns RequestParams()
        every { getRecommendationUseCase.execute(any(), capture(slot)) } answers {
            slot.captured.onError(TimeoutException())
        }
        viewModel.getRecommendationList(listOf(), "")
        assert(viewModel.recommendationListLiveData.value != null && viewModel.recommendationListLiveData.value!!.isError())
    }

    @Test
    fun `get success add wishlist from network`(){
        var status: Boolean? = null
        val slot = slot<WishListActionListener>()
        every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onSuccessAddWishlist(recommendation.productId.toString())
        }
        viewModel.addWishlist(recommendation){ success, _ ->
            status = success
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
        viewModel.addWishlist(recommendation){ success, _ ->
            status = success
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
        viewModel.addWishlist(recommendation.copy(isTopAds = true)){ success, _ ->
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
        viewModel.addWishlist(recommendation.copy(isTopAds = true)){ success, _ ->
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
        viewModel.removeWishlist(recommendation){ success, _ ->
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
        viewModel.removeWishlist(recommendation){ success, _ ->
            status = success
        }
        assert(status == false)
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