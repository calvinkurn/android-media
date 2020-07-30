package com.tokopedia.home.viewModel.homeRecommendation

import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import io.mockk.coEvery
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

fun GetHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel: HomeRecommendationDataModel) {
    setParams("", 0, 10, 0)
    coEvery { executeOnBackground() } returns homeRecommendationDataModel
}

fun GetHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel: HomeRecommendationDataModel, nextReturnData: HomeRecommendationDataModel) {
    setParams("", 0, 10, 0)
    coEvery { executeOnBackground() } returns homeRecommendationDataModel andThen nextReturnData
}

fun GetHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel: HomeRecommendationDataModel, exception: Exception) {
    setParams("", 0, 10, 0)
    coEvery { executeOnBackground() } returns homeRecommendationDataModel andThenThrows exception
}

fun GetHomeRecommendationUseCase.givenThrowReturn() {
    coEvery { executeOnBackground() } throws TimeoutException()
}