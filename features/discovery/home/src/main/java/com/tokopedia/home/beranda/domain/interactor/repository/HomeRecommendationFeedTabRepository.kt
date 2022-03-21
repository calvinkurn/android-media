package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.GetRecommendationTabUseCase
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel
import dagger.Lazy
import javax.inject.Inject

/**
 * @author by devara on 2020-02-18
 */

class HomeRecommendationFeedTabRepository @Inject constructor(
        private val getRecommendationTabUseCase: Lazy<GetRecommendationTabUseCase>
        )
    : HomeRepository<List<RecommendationTabDataModel>> {

    companion object {
        const val LOCATION_PARAM = "locationParam"
    }

    override suspend fun getRemoteData(bundle: Bundle): List<RecommendationTabDataModel> {
        getRecommendationTabUseCase.get().setParams(bundle.getString(
                LOCATION_PARAM, ""
        ))

        return getRecommendationTabUseCase.get().executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): List<RecommendationTabDataModel> {
        return listOf()
    }
}