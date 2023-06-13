package com.tokopedia.home_recom.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_recom.util.RecommendationDispatcherTest
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

class TestSimilarProductRecommendationViewModel {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val getSingleRecommendationUseCase = mockk<GetSingleRecommendationUseCase>(relaxed = true)
    private val addToWishlistV2UseCase = mockk<AddToWishlistV2UseCase>(relaxed = true)
    private val deleteWishlistV2UseCase = mockk<DeleteWishlistV2UseCase>(relaxed = true)
    private val getRecommendationFilterChips = mockk<GetRecommendationFilterChips>(relaxed = true)

    private val viewModel = spyk(SimilarProductRecommendationViewModel(
            dispatcher = RecommendationDispatcherTest(),
            singleRecommendationUseCase = getSingleRecommendationUseCase,
            userSessionInterface = userSession,
            getRecommendationFilterChips = getRecommendationFilterChips,
            addToWishlistV2UseCase = addToWishlistV2UseCase,
            deleteWishlistV2UseCase = deleteWishlistV2UseCase
    ), recordPrivateCalls = true)
    private val recommendation = RecommendationItem(productId = 1234)

    @Test
    fun `get success data from network`(){
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
                recommendation = listOf(
                        RecommendationEntity.Recommendation()
                )
        )
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        Assert.assertTrue(viewModel.recommendationItem.value != null && viewModel.recommendationItem.value!!.isSuccess())
    }

    @Test
    fun `get success data from network with default page`(){
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
                recommendation = listOf(
                        RecommendationEntity.Recommendation()
                )
        )
        viewModel.getSimilarProductRecommendation(
                queryParam = "", productId =  "", pageName = "")
        Assert.assertTrue(viewModel.recommendationItem.value != null && viewModel.recommendationItem.value!!.isSuccess())
    }

    @Test
    fun `given error timeout data from network when get recommendation then show error`(){
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns RecommendationFilterChipsEntity.FilterAndSort()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } throws TimeoutException()
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        Assert.assertTrue(viewModel.recommendationItem.value != null)
        Assert.assertTrue(viewModel.recommendationItem.value?.isError() == true)
        Assert.assertTrue(viewModel.recommendationItem.value?.exception is TimeoutException)
    }

    @Test
    fun `add to wishlistv2 returns success` () {
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishlistV2(recommendation.productId.toString(), mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendation.productId.toString(), userSession.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify add to wishlistv2 returns fail`() {
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishlistV2(recommendation.productId.toString(), mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendation.productId.toString(), userSession.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2`(){
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishlistV2(recommendation, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommendation.productId.toString(), userSession.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove from wishlistv2 returns fail`() {
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishlistV2(recommendation, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommendation.productId.toString(), userSession.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `get success quick filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
                recommendation = listOf(
                        RecommendationEntity.Recommendation()
                )
        )

        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
        viewModel.getRecommendationFromQuickFilter("", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
    }

    @Test
    fun `get error quick filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
                recommendation = listOf(
                        RecommendationEntity.Recommendation()
                )
        ) andThenThrows java.lang.Exception()

        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
        viewModel.getRecommendationFromQuickFilter("", "", "")
        assert(viewModel.filterSortChip.value?.isError() == true)
        assert(viewModel.recommendationItem.value?.isError() == true)
    }

    @Test
    fun `get empty recom when quick filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
                recommendation = listOf(
                        RecommendationEntity.Recommendation()
                )
        ) andThen RecommendationEntity.RecommendationData()

        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
        viewModel.getRecommendationFromQuickFilter("", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isEmpty() == true)
    }

    @Test
    fun `given selected full filter when getRecommendationFromQuickFilter then deselect filter`(){
        val filterKey = "os"
        val filterValue = "123"
        val filterActivated = true
        val filterChip = RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = filterKey, value = filterValue, isActivated = filterActivated)))
        val sortKey = "terlaris"
        val sortValue = "234"
        val filterSortChip = RecommendationFilterChipsEntity.FilterAndSort(
            filterChip = listOf(filterChip),
            sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = sortKey, value = sortValue, isSelected = true))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterSortChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
            recommendation = listOf(
                RecommendationEntity.Recommendation()
            )
        )

        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.filterSortChip.value?.data?.filterAndSort?.filterChip?.first()?.options?.first()?.isActivated == filterActivated)
        viewModel.getRecommendationFromQuickFilter("Official Store", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.filterSortChip.value?.data?.filterAndSort?.filterChip?.first()?.options?.first()?.isActivated == !filterActivated)
    }

    @Test
    fun `get null quick filter when quick filter click`(){

        viewModel.getRecommendationFromQuickFilter("", "", "")
        assert(viewModel.filterSortChip.value == null)
        assert(viewModel.recommendationItem.value == null)
    }

    @Test
    fun `get success full filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
                recommendation = listOf(
                        RecommendationEntity.Recommendation()
                )
        )

        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
    }

    @Test
    fun `get error full filter click`(){
        coEvery { getRecommendationFilterChips.executeOnBackground() } throws Exception()
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData() andThenThrows Exception()
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "")
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
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData() andThen RecommendationEntity.RecommendationData()
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "")
        assert(viewModel.filterSortChip.value?.isEmpty() == false)
        assert(viewModel.recommendationItem.value?.isError() == true)
    }


    @Test
    fun `get success remove empty filter click`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
                recommendation = listOf(
                        RecommendationEntity.Recommendation()
                )
        )
        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)
    }

    @Test
    fun `given both selected when get selected sort filter then return key to value mapping`(){
        val filterKey = "os"
        val filterValue = "123"
        val sortKey = "terlaris"
        val sortValue = "234"
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
                filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = filterKey, value = filterValue, isActivated = true)))),
                sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = sortKey, value = sortValue, isSelected = true))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
            recommendation = listOf(RecommendationEntity.Recommendation())
        )
        viewModel.getSimilarProductRecommendation(1, "" , "", "")

        val result = viewModel.getSelectedSortFilter()
        assert(result.isNotEmpty())
        assert(result.size == 2)
        assert(result[filterKey] == filterValue)
        assert(result[SimilarProductRecommendationViewModel.KEY_SORT] == sortValue)
    }

    @Test
    fun `given only filter selected when get selected sort filter then return key to value mapping with sort using default value`(){
        val filterKey = "os"
        val filterValue = "123"
        val sortKey = "terlaris"
        val sortValue = "234"
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
            filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = filterKey, value = filterValue, isActivated = true)))),
            sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = sortKey, value = sortValue, isSelected = false))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
            recommendation = listOf(RecommendationEntity.Recommendation())
        )
        viewModel.getSimilarProductRecommendation(1, "" , "", "")

        val result = viewModel.getSelectedSortFilter()
        assert(result.isNotEmpty())
        assert(result.size == 2)
        assert(result[filterKey] == filterValue)
        assert(result[SimilarProductRecommendationViewModel.KEY_SORT] == SimilarProductRecommendationViewModel.DEFAULT_VALUE_SORT)
    }

    @Test
    fun `given only sort selected when get selected sort filter then return only sort key to value mapping`(){
        val filterKey = "os"
        val filterValue = "123"
        val sortKey = "terlaris"
        val sortValue = "234"
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
            filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = filterKey, value = filterValue, isActivated = false)))),
            sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = sortKey, value = sortValue, isSelected = true))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
            recommendation = listOf(RecommendationEntity.Recommendation())
        )
        viewModel.getSimilarProductRecommendation(1, "" , "", "")

        val result = viewModel.getSelectedSortFilter()
        assert(result.isNotEmpty())
        assert(result.size == 1)
        assert(result[SimilarProductRecommendationViewModel.KEY_SORT] == sortValue)
    }

    @Test
    fun `given no sort and filter selected when get selected sort filter then return only sort key to default value mapping`(){
        val filterKey = "os"
        val filterValue = "123"
        val sortKey = "terlaris"
        val sortValue = "234"
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
            filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = filterKey, value = filterValue, isActivated = false)))),
            sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = sortKey, value = sortValue, isSelected = true))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
            recommendation = listOf(RecommendationEntity.Recommendation())
        )
        viewModel.getSimilarProductRecommendation(1, "" , "", "")

        val result = viewModel.getSelectedSortFilter()
        assert(result.isNotEmpty())
        assert(result.size == 1)
        assert(result[SimilarProductRecommendationViewModel.KEY_SORT] == sortValue)
    }

    @Test
    fun `given duplicate filter selected when get selected sort filter then return filter key to both value mapping`(){
        val filterKey = "city_ids"
        val filterValue = "123"
        val filterValue2 = "1234"
        val sortKey = "terlaris"
        val sortValue = "234"
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
            filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Lokasi", options = listOf(RecommendationFilterChipsEntity.Option(name = "Jabodetabek", key = filterKey, value = filterValue, isActivated = true), RecommendationFilterChipsEntity.Option(name = "Surabaya", key = filterKey, value = filterValue2, isActivated = true)))),
            sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = sortKey, value = sortValue, isSelected = false))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
            recommendation = listOf(RecommendationEntity.Recommendation())
        )
        viewModel.getSimilarProductRecommendation(1, "" , "", "")

        val result = viewModel.getSelectedSortFilter()
        assert(result.isNotEmpty())
        assert(result[filterKey] == "$filterValue#$filterValue2")
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

    @Test
    fun `given empty when full filter click then pass empty value`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
            filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
            sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
            recommendation = listOf(RecommendationEntity.Recommendation())
        ) andThen RecommendationEntity.RecommendationData()

        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)

        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "")
        assert(viewModel.recommendationItem.value?.isEmpty() == true)
    }

    @Test
    fun `given error when full filter click then show error`(){
        val filterChip = RecommendationFilterChipsEntity.FilterAndSort(
            filterChip = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip(title = "Penawaran", options = listOf(RecommendationFilterChipsEntity.Option(name = "Official Store", key = "os", value = "true")))),
            sortChip = listOf(RecommendationFilterChipsEntity.RecommendationSortChip(name = "Terlaris", key = "terlaris", value = "true"))
        )
        val error = MessageErrorException("error")

        coEvery { getRecommendationFilterChips.executeOnBackground() } returns filterChip
        every { getSingleRecommendationUseCase.getRecomParams(any(), any(), any()) } returns RequestParams()
        every { getSingleRecommendationUseCase.createObservable(any()).toBlocking().first() } returns RecommendationEntity.RecommendationData(
            recommendation = listOf(RecommendationEntity.Recommendation())
        ) andThenThrows error

        viewModel.getSimilarProductRecommendation(1, "", "", "")
        assert(viewModel.filterSortChip.value?.isSuccess() == true)
        assert(viewModel.recommendationItem.value?.isSuccess() == true)

        viewModel.getRecommendationFromFullFilter(mapOf("terlaris" to "true"), mapOf("os" to "true"), "", "")
        assert(viewModel.filterSortChip.value?.isError() == true)
        assert(viewModel.filterSortChip.value?.exception == error)
        assert(viewModel.recommendationItem.value?.isError() == true)
        assert(viewModel.recommendationItem.value?.exception?.message == error.message)
    }
}
