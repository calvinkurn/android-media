package com.tokopedia.home.viewModel.homeRecommendation

import com.tokopedia.home.beranda.domain.interactor.usecase.GetGlobalHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeGlobalRecommendationDataModel
import io.mockk.coEvery

/**
 * Created by Lukas on 14/05/20.
 */

fun GetGlobalHomeRecommendationCardUseCase.givenDataReturn(
    model: HomeGlobalRecommendationDataModel,
    productPage: Int
) {
    coEvery {
        execute(productPage, any(), any(), any(), any(), any(), any(), any())
    } coAnswers { model }
}

fun GetGlobalHomeRecommendationCardUseCase.givenDataReturnMatch(
    model: HomeGlobalRecommendationDataModel,
    productPage: Int
) {
    coEvery {
        execute(match { it == productPage }, any(), any(), any(), any(), any(), any(), any())
    } coAnswers { model }
}

fun GetGlobalHomeRecommendationCardUseCase.givenThrows(exception: Throwable, productPage: Int) {
    coEvery { execute(productPage, any(), any(), any(), any(), any(), any(), any()) } throws exception
}
