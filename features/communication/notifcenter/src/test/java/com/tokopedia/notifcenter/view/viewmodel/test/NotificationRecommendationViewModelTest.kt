package com.tokopedia.notifcenter.ui.viewmodel.test

import com.tokopedia.notifcenter.ui.NotificationViewModel.Companion.getRecommendationVisitables
import com.tokopedia.notifcenter.ui.viewmodel.base.NotificationViewModelTestFixture
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import rx.Observable

class NotificationRecommendationViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `loadRecommendations should return data properly`() {
        // given
        val listOfRecommWidget = productRecommResponse
            .productRecommendationWidget
            .data
            .mappingToRecommendationModel()

        val expectedValue = listOfRecommWidget.first()

        every {
            getRecommendationUseCase.createObservable(any())
        } returns Observable.just(listOfRecommWidget)

        // when
        viewModel.loadRecommendations(0)

        // then
        Assert.assertEquals(
            getRecommendationVisitables(0, expectedValue),
            viewModel.recommendations.value
        )
    }
}
