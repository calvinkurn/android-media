package com.tokopedia.home_recom.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_recom.util.RecommendationDispatcherTest
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
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
import rx.Subscriber
import java.util.concurrent.TimeoutException

class TestSimilarProductRecommendationViewModel {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val getSingleRecommendationUseCase = mockk<GetSingleRecommendationUseCase>(relaxed = true)
    private val addWishListUseCase = mockk<AddWishListUseCase>(relaxed = true)
    private val removeWishListUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    private val topAdsWishlishedUseCase = mockk<TopAdsWishlishedUseCase>(relaxed = true)
    private val getRecommendationFilterChips = mockk<GetRecommendationFilterChips>(relaxed = true)

    private val viewModel = SimilarProductRecommendationViewModel(
            dispatcher = RecommendationDispatcherTest(),
            singleRecommendationUseCase = getSingleRecommendationUseCase,
            topAdsWishlishedUseCase = topAdsWishlishedUseCase,
            removeWishListUseCase = removeWishListUseCase,
            addWishListUseCase = addWishListUseCase,
            userSessionInterface = userSession,
            getRecommendationFilterChips = getRecommendationFilterChips
    )
    private val recommendation = RecommendationItem(productId = 1234)

    @Test
    fun `get success data from network`(){
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(RecommendationItem())
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        Assert.assertTrue(viewModel.recommendationItem.value != null && viewModel.recommendationItem.value!!.isSuccess())
    }

    @Test
    fun `get error timeout data from network`(){
        val slot = slot<Subscriber<List<RecommendationItem>>>()
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.execute(any(), capture(slot)) } answers {
            slot.captured.onError(TimeoutException())
        }
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        Assert.assertTrue(viewModel.recommendationItem.value != null && viewModel.recommendationItem.value!!.isError())
    }

    @Test
    fun `get success add wishlist from network`(){
        var status: Boolean? = null
        val slot = slot<WishListActionListener>()
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
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
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
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
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
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
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
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
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
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
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
        every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onErrorRemoveWishlist("", recommendation.productId.toString())
        }
        viewModel.removeWishlist(recommendation){ success, _ ->
            status = success
        }
        assert(status == false)
    }

    @Test
    fun `get success quick filter click`(){
        coEvery { getRecommendationFilterChips.executeOnBackground(any()) } returns RecommendationFilterChipsEntity.FilterAndSort()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(RecommendationItem())
        viewModel.getRecommendationFromQuickFilter("", "", "","")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
    }

    @Test
    fun `get error quick filter click`(){
        coEvery { getRecommendationFilterChips.executeOnBackground() } throws Exception()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } throws Exception()
        viewModel.getRecommendationFromQuickFilter("", "", "", "")
        assert(viewModel.filterSortChip.value?.isError() == true)
        assert(viewModel.recommendationItem.value?.isError() == true)
    }

    @Test
    fun `get empty recom when quick filter click`(){
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf()
        viewModel.getRecommendationFromQuickFilter("", "", "", "")
        assert(viewModel.recommendationItem.value?.isEmpty() == true)
        assert(viewModel.recommendationItem.value?.isEmpty() == true)
    }

    @Test
    fun `get success full filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(RecommendationItem())

        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
    }

    @Test
    fun `get error full filter click`(){
        coEvery { getRecommendationFilterChips.executeOnBackground() } throws Exception()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(RecommendationItem()) andThenThrows Exception()
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "", "")
        assert(viewModel.filterSortChip.value?.isError() == true)
        assert(viewModel.recommendationItem.value?.isError() == true)
    }

    @Test
    fun `get empty recom when full filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(RecommendationItem()) andThen listOf()
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "", "")
        assert(viewModel.filterSortChip.value?.isEmpty() == false)
        assert(viewModel.recommendationItem.value?.isEmpty() == true)
    }


    @Test
    fun `get success remove empty filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(RecommendationItem())
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
    }

    @Test
    fun `get error remove empty click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } throws Exception()
        viewModel.getRecommendationFromEmptyFilter( filterChip.filterChip.first().options.first(), "", "", "")
        assert(viewModel.filterSortChip.value?.isError() == true)
        assert(viewModel.recommendationItem.value?.isError() == true)
    }

    @Test
    fun `get empty recom when empty filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf()
        viewModel.getRecommendationFromEmptyFilter( filterChip.filterChip.first().options.first(), "", "", "")
        assert(viewModel.recommendationItem.value?.isEmpty() == true)
        assert(viewModel.recommendationItem.value?.isEmpty() == true)
    }

    @Test
    fun `get selected sort`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true", isActivated = true)))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true", isSelected = true))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns listOf(RecommendationItem())
        viewModel.getSimilarProductRecommendation(1, "" , "", "")
        assert(viewModel.getSelectedSortFilter().isNotEmpty())
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
    fun `user id equals 1`(){
        every { userSession.userId } returns "1"
        Assert.assertEquals(viewModel.userId(), "1")
    }

}