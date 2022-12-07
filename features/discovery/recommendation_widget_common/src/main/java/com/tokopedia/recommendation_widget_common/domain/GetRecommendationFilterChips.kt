package com.tokopedia.recommendation_widget_common.domain

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.query.RecommendationFilterChipsQuery
import com.tokopedia.recommendation_widget_common.domain.query.RecommendationFilterChipsQueryV2
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Lukas on 04/08/20.
 */

class GetRecommendationFilterChips @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<RecommendationFilterChipsEntity>,
    private val context: Context
) : UseCase<RecommendationFilterChipsEntity.FilterAndSort>() {

    private val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)

    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(
            if (remoteConfig.getBoolean(
                    RemoteConfigKey.RECOM_USE_GQL_FED_QUERY,
                    true
                )
            ) {
                RecommendationFilterChipsQueryV2()
            } else {
                RecommendationFilterChipsQuery()
            }
//            RecommendationFilterChipsQuery()
        )
        graphqlUseCase.setTypeClass(RecommendationFilterChipsEntity::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): RecommendationFilterChipsEntity.FilterAndSort {
        graphqlUseCase.setRequestParams(params.parameters)
        val recommendationFilterChipsEntity = graphqlUseCase.executeOnBackground()
        return recommendationFilterChipsEntity.recommendationFilterChips.data
    }

    fun setParams(userId: Int = 0, productIDs: String = "", pageName: String = "", xSource: String = "", queryParam: String = "", type: String = "", isTokonow: Boolean = false) {
        params.parameters.clear()
        if (userId != 0) params.putInt(PARAM_USER_ID, userId)
        if (productIDs.isNotEmpty()) params.putString(PARAM_PRODUCT_IDS, productIDs)
        if (pageName.isNotEmpty()) params.putString(PARAM_PAGE_NAME, pageName)
        if (xSource.isNotEmpty()) params.putString(PARAM_X_SOURCE, xSource)
        if (queryParam.isNotEmpty()) params.putString(PARAM_QUERY_PARAM, queryParam)
        if (type.isNotEmpty()) params.putString(PARAM_FILTER_TYPE, type)
        if (isTokonow) params.putBoolean(PARAM_TOKONOW, isTokonow)
    }

    companion object {
        private const val PARAM_USER_ID = "userId"
        private const val PARAM_PRODUCT_IDS = "productIDs"
        private const val PARAM_PAGE_NAME = "pageName"
        private const val PARAM_X_SOURCE = "xSource"
        private const val PARAM_FILTER_TYPE = "filterType"
        private const val PARAM_QUERY_PARAM = "queryParam"
        private const val PARAM_TOKONOW = "tokoNow"
        const val FULL_FILTER = "full_filter"
        const val QUICK_FILTER = "quick_filter"
    }
}
