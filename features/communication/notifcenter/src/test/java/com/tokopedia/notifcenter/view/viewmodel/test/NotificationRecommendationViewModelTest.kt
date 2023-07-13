package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.notifcenter.view.NotificationViewModel.Companion.getRecommendationVisitables
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class NotificationRecommendationViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `loadRecommendations should return data properly`() {
        // given
        val listOfRecommWidget = productRecommResponse
            .productRecommendationWidget
            .data
            .mappingToRecommendationModel()

        val expectedValue = listOfRecommWidget.first()

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOfRecommWidget

        // when
        viewModel.loadRecommendations(0)

        // then
        Assert.assertEquals(
            getRecommendationVisitables(0, expectedValue),
            viewModel.recommendations.value
        )
    }
}
