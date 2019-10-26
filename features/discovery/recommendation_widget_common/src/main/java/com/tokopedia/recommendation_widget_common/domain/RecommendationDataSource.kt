package com.tokopedia.recommendation_widget_common.domain

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper.Companion.convertToRecommendationWidget
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.DEFAULT_VALUE_X_DEVICE
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.DEFAULT_VALUE_X_SOURCE
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.PAGE_NAME
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.PAGE_NUMBER
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.PRODUCT_IDS
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.QUERY_PARAM
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.USER_ID
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.X_DEVICE
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase.Companion.X_SOURCE
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecommendationDataSource(
        private val recomRawString: String,
        private val graphqlRepository: GraphqlRepository,
        private val userSession: UserSessionInterface
) {
    suspend fun load(queryParam: String = "", xSource: String = "", pageName: String = "", productIds: List<String> = listOf(), pageNumber: Int = 0) : List<RecommendationWidget> {
        val data = withContext(Dispatchers.IO){
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
            val productIdsString = TextUtils.join(",", productIds)

            val params = mapOf(
                    PAGE_NUMBER to pageNumber,
                    PAGE_NAME to pageName,
                    PRODUCT_IDS to productIdsString,
                    QUERY_PARAM to queryParam,
                    X_DEVICE to DEFAULT_VALUE_X_DEVICE,
                    USER_ID to if(userSession.isLoggedIn) userSession.userId.toInt() else 0,
                    X_SOURCE to if(xSource.isEmpty()) DEFAULT_VALUE_X_SOURCE else xSource
            )

            val gqlRecommendationRequest = GraphqlRequest(
                    recomRawString,
                    RecomendationEntity::class.java,
                    params
            )

            graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)

        }
        data.getError(RecomendationEntity::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)){
                    throw Throwable(it[0].message)
                }
            }
        }
        return data.getSuccessData<RecomendationEntity>().productRecommendationWidget?.data?.map { convertToRecommendationWidget(it) } ?: listOf()
    }
}