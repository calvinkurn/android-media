package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import dagger.Lazy
import javax.inject.Inject

/**
 * @author by devara on 2020-02-18
 */

class HomeRecommendationRepository @Inject constructor(
        private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>
        )
    : HomeRepository<List<RecommendationWidget>> {

    companion object {
        const val PAGE_NAME = "pageName"
        const val QUERY_PARAM = "queryParam"
        const val SHOP_ID = "shopIDs"
    }

    override suspend fun getRemoteData(bundle: Bundle): List<RecommendationWidget> {
        val pageName = bundle.getString(PAGE_NAME, "")
        val queryParam = bundle.getString(QUERY_PARAM, "")
        val shopId = bundle.getString(SHOP_ID, "")

        val shopIds = if(shopId.isNotEmpty()) listOf(shopId) else emptyList()

        return getRecommendationUseCase.get().getData(
                GetRecommendationRequestParam(
                        pageName = pageName,
                        queryParam = queryParam,
                        shopIds = shopIds
                )
        )
    }

    override suspend fun getCachedData(bundle: Bundle): List<RecommendationWidget> {
        return listOf()
    }
}
