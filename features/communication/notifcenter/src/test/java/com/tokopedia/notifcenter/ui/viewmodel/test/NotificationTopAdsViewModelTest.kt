package com.tokopedia.notifcenter.ui.viewmodel.test

import com.tokopedia.notifcenter.ui.viewmodel.base.NotificationViewModelTestFixture
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class NotificationTopAdsViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun should_load_not_change_topAdsBanner_value_when_empty_result() {
        val expectedResult: ArrayList<TopAdsImageViewModel> = arrayListOf()
        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } returns expectedResult

        viewModel.loadTopAdsBannerData()

        verify(exactly = 1) {
            getRecommendationUseCase.createObservable(any())
        }
        Assert.assertEquals(
            null,
            viewModel.topAdsBanner.value
        )
    }

    @Test
    fun should_load_first_page_recommendation_when_fail_get_topadds_banner() {
        val expectedThrowable = Throwable("Oops!")
        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } throws expectedThrowable

        viewModel.loadTopAdsBannerData()

        verify(exactly = 1) {
            getRecommendationUseCase.createObservable(any())
        }
    }

    @Test
    fun should_invoke_callback_when_success_get_topAds_wishlist() {
        // Given
        val testRecommendationItem = RecommendationItem()
        val testCallback: ((Boolean, Throwable?) -> Unit) = mockk(relaxed = true)
        val expectedResponse = WishlistModel().apply {
            val successData = WishlistModel.Data()
            successData.isSuccess = true
            data = successData
        }
        every {
            topAdsWishlishedUseCase.createObservable(any<RequestParams>()).toBlocking().single()
        } returns expectedResponse

        // When
        viewModel.addWishListTopAds(testRecommendationItem, testCallback)

        // Then
        verify(exactly = 1) {
            testCallback.invoke(any(), any())
        }
    }

    @Test
    fun should_not_invoke_callback_when_success_get_topAds_wishlist_but_empty() {
        // Given
        val testRecommendationItem = RecommendationItem()
        val testCallback: ((Boolean, Throwable?) -> Unit) = mockk(relaxed = true)
        val expectedResponse = WishlistModel()
        every {
            topAdsWishlishedUseCase.createObservable(any<RequestParams>()).toBlocking().single()
        } returns expectedResponse

        // When
        viewModel.addWishListTopAds(testRecommendationItem, testCallback)

        // Then
        verify(exactly = 0) {
            testCallback.invoke(any(), any())
        }
    }
}
