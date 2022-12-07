package com.tokopedia.home.viewModel.homeRecommendation

import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import io.mockk.coEvery
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

fun GetHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel: HomeRecommendationDataModel) {
    setParams("", 0, 10, 0, sourceType = "")
    coEvery { executeOnBackground() } returns homeRecommendationDataModel
}

fun GetHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel: HomeRecommendationDataModel, nextReturnData: HomeRecommendationDataModel) {
    setParams("", 0, 10, 0, sourceType = "")
    coEvery { executeOnBackground() } returns homeRecommendationDataModel andThen nextReturnData
}

fun GetHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel: HomeRecommendationDataModel, exception: Exception) {
    setParams("", 0, 10, 0, sourceType = "")
    coEvery { executeOnBackground() } returns homeRecommendationDataModel andThenThrows exception
}

fun GetHomeRecommendationUseCase.givenThrowReturn() {
    coEvery { executeOnBackground() } throws TimeoutException()
}

fun TopAdsImageViewUseCase.givenDataReturn(data: ArrayList<TopAdsImageViewModel>) {
    coEvery { getImageData(any()) } returns data
}

fun TopAdsImageViewUseCase.givenDataReturn(data: ArrayList<TopAdsImageViewModel>, secondData: ArrayList<TopAdsImageViewModel>) {
    coEvery { getImageData(any()) } returns data andThen secondData
}

fun TopAdsImageViewUseCase.givenDataReturnAndThenThrows(data: ArrayList<TopAdsImageViewModel>) {
    coEvery { getImageData(any()) } returns data andThenThrows TimeoutException()
}

fun TopAdsImageViewUseCase.givenThrows() {
    coEvery { getImageData(any()) } throws TimeoutException()
}

fun GetTopAdsHeadlineUseCase.givenDataReturn(
    data: TopAdsHeadlineResponse
) {
    coEvery { executeOnBackground() } returns data
}
