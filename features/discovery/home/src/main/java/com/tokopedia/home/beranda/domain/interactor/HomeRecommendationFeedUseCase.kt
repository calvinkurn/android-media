package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.usecase.coroutines.UseCase

open class HomeRecommendationFeedUseCase : UseCase<HomeRecommendationDataModel>() {
    open fun setParams(tabName: String, recomId: Int, count: Int, page: Int, location: String = "", sourceType: String) { }
    override suspend fun executeOnBackground(): HomeRecommendationDataModel = HomeRecommendationDataModel()
}
