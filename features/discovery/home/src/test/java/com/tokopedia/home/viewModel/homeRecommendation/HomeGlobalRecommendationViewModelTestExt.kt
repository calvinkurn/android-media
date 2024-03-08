package com.tokopedia.home.viewModel.homeRecommendation

import com.tokopedia.home.beranda.domain.interactor.GetHomeGlobalRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.GetGlobalHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeGlobalRecommendationDataModel
import io.mockk.coEvery
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

fun GetHomeGlobalRecommendationUseCase.givenDataReturn(model: HomeGlobalRecommendationDataModel) {
    setParams("", 0, 10, 0, "", sourceType = "")
    coEvery { executeOnBackground() } returns model
}

fun GetHomeGlobalRecommendationUseCase.givenDataReturn(model: HomeGlobalRecommendationDataModel, nextReturnData: HomeGlobalRecommendationDataModel) {
    setParams("", 0, 10, 0, "", sourceType = "")
    coEvery { executeOnBackground() } returns model andThen nextReturnData
}

fun GetHomeGlobalRecommendationUseCase.givenDataReturn(model: HomeGlobalRecommendationDataModel, exception: Exception) {
    setParams("", 0, 10, 0, "", sourceType = "")
    coEvery { executeOnBackground() } returns model andThenThrows exception
}

fun GetHomeGlobalRecommendationUseCase.givenThrowReturn() {
    coEvery { executeOnBackground() } throws TimeoutException()
}

fun GetGlobalHomeRecommendationCardUseCase.givenDataReturn(
    model: HomeGlobalRecommendationDataModel,
    productPage: Int
) {
    coEvery {
        execute(productPage, "", "", "")
    } coAnswers { model }
}

fun GetGlobalHomeRecommendationCardUseCase.givenDataReturnMatch(
    model: HomeGlobalRecommendationDataModel,
    productPage: Int
) {
    coEvery {
        execute(match { it == productPage }, "", "", "")
    } coAnswers { model }
}

fun GetGlobalHomeRecommendationCardUseCase.givenThrows(exception: Throwable, productPage: Int) {
    coEvery { execute(productPage, "", "", "") } throws exception
}
