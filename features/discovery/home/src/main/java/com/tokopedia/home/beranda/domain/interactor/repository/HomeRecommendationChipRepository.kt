package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home_component.usecase.featuredshop.DisplayHeadlineAdsEntity
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

/**
 * @author by devara on 2020-02-18
 */

class HomeRecommendationChipRepository @Inject constructor(
        private val getRecommendationFilterChips: Lazy<GetRecommendationFilterChips>,
        private val userSession: Lazy<UserSessionInterface>
        )
    : HomeRepository<List<RecommendationFilterChipsEntity.RecommendationFilterChip>> {

    companion object {
        const val PAGE_NAME = "pageName"
        const val QUERY_PARAM = "queryParam"
    }

    override suspend fun getRemoteData(bundle: Bundle): List<RecommendationFilterChipsEntity.RecommendationFilterChip> {
        getRecommendationFilterChips.get().setParams(
                userId = if (userSession.get().userId.isEmpty()) 0 else userSession.get().userId.toInt(),
                pageName = bundle.getString(PAGE_NAME, ""),
                queryParam = bundle.getString(QUERY_PARAM, "")
        )
        val recomFilterList = mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()
        recomFilterList.addAll(getRecommendationFilterChips.get().executeOnBackground().filterChip)
        return recomFilterList
    }

    override suspend fun getCachedData(bundle: Bundle): List<RecommendationFilterChipsEntity.RecommendationFilterChip> {
        return listOf()
    }
}