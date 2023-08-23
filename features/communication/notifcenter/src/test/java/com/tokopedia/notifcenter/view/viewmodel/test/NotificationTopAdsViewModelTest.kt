package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test

class NotificationTopAdsViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun should_load_not_change_topAdsBanner_value_when_empty_result() {
        // Given
        val expectedResult: ArrayList<TopAdsImageViewModel> = arrayListOf()
        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } returns expectedResult

        // When
        viewModel.loadTopAdsBannerData()

        // Then
        coVerify(exactly = 1) {
            getRecommendationUseCase.getData(any())
        }
        Assert.assertEquals(
            null,
            viewModel.topAdsBanner.value
        )
    }

    @Test
    fun should_load_first_page_recommendation_when_fail_get_topadds_banner() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } throws expectedThrowable

        // When
        viewModel.loadTopAdsBannerData()

        // Then
        coVerify(exactly = 1) {
            getRecommendationUseCase.getData(any())
        }
    }
}
