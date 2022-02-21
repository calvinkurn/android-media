package com.tokopedia.digital.digital_recommendation.domain

import com.tokopedia.digital.digital_recommendation.data.DigitalRecommendationQuery
import com.tokopedia.digital.digital_recommendation.data.DigitalRecommendationResponse
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationUseCase @Inject constructor(
        private val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase,
        private val userSession: UserSessionInterface
) {
    suspend fun execute(page: DigitalRecommendationPage,
                        dgCategories: List<Int>,
                        pgCategories: List<Int>)
            : Result<DigitalRecommendationModel> {
        val params = mapOf(
                PARAM_INPUT to mapOf(
                        PARAM_CHANNEL_NAME to when (page) {
                            DigitalRecommendationPage.DIGITAL_GOODS -> DG_PERSO_CHANNEL_NAME
                            DigitalRecommendationPage.PHYSICAL_GOODS -> PG_PERSO_CHANNEL_NAME
                            DigitalRecommendationPage.RECOMMENDATION_SKELETON -> OD_SKELETON_CHANNEL_NAME
                        },
                        PARAM_CLIENT_NUMBERS to arrayListOf(userSession.phoneNumber),
                        PARAM_DG_CATEGORY_IDS to dgCategories,
                        PARAM_PG_CATEGORY_IDS to pgCategories
                )
        )

        multiRequestGraphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(1 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build()
        )
        multiRequestGraphqlUseCase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(DigitalRecommendationQuery.DG_RECOMMENDATION,
                    DigitalRecommendationResponse::class.java, params)
            multiRequestGraphqlUseCase.addRequest(graphqlRequest)
            val graphqlResponse = multiRequestGraphqlUseCase.executeOnBackground()
            val errors = graphqlResponse.getError(DigitalRecommendationResponse::class.java)

            if (errors != null && errors.isNotEmpty() && errors[0].extensions != null) {
                Fail(MessageErrorException(errors[0].message, errors[0].extensions.code.toString()))
            } else {
                val response = graphqlResponse.getData<DigitalRecommendationResponse>(DigitalRecommendationResponse::class.java)
                val digitalRecommendationItems = DigitalRecommendationMapper.transform(response)
                Success(digitalRecommendationItems)
            }
        } catch (t: Throwable) {
            Fail(t)
        }

    }

    suspend fun getRecommendationPosition(
        page: DigitalRecommendationPage,
        dgCategories: List<Int>,
        pgCategories: List<Int>
    ): Result<List<String>>{
        val params = mapOf(
            PARAM_INPUT to mapOf(
                PARAM_CHANNEL_NAME to when (page) {
                    DigitalRecommendationPage.DIGITAL_GOODS -> DG_PERSO_CHANNEL_NAME
                    DigitalRecommendationPage.PHYSICAL_GOODS -> PG_PERSO_CHANNEL_NAME
                    DigitalRecommendationPage.RECOMMENDATION_SKELETON -> OD_SKELETON_CHANNEL_NAME
                },
                PARAM_CLIENT_NUMBERS to arrayListOf(userSession.phoneNumber),
                PARAM_DG_CATEGORY_IDS to dgCategories,
                PARAM_PG_CATEGORY_IDS to pgCategories
            )
        )

        multiRequestGraphqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(1 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                .setSessionIncluded(true)
                .build()
        )
        multiRequestGraphqlUseCase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(DigitalRecommendationQuery.DG_RECOMMENDATION,
                DigitalRecommendationResponse::class.java, params)
            multiRequestGraphqlUseCase.addRequest(graphqlRequest)
            val graphqlResponse = multiRequestGraphqlUseCase.executeOnBackground()
            val errors = graphqlResponse.getError(DigitalRecommendationResponse::class.java)

            if (errors != null && errors.isNotEmpty() && errors[0].extensions != null) {
                Fail(MessageErrorException(errors[0].message, errors[0].extensions.code.toString()))
            } else {
                val response = graphqlResponse.getData<DigitalRecommendationResponse>(DigitalRecommendationResponse::class.java)
                val digitalRecommendationItems = response.personalizedItems.recommendationItems.map { it.title }
                Success(digitalRecommendationItems)
            }
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    companion object {
        const val DG_RECOM_NAME = "dg_order_detail"
        const val PG_RECOM_NAME = "pg_top_ads"

        const val DG_PERSO_CHANNEL_NAME = "dg_order_detail_dgu"
        const val PG_PERSO_CHANNEL_NAME = "pg_order_detail_dgu"
        const val OD_SKELETON_CHANNEL_NAME = "dg_od_skeleton"

        const val PARAM_CHANNEL_NAME = "channelName"
        const val PARAM_CLIENT_NUMBERS = "clientNumbers"
        const val PARAM_DG_CATEGORY_IDS = "dgCategoryIDs"
        const val PARAM_PG_CATEGORY_IDS = "pgCategoryIDs"
        const val PARAM_INPUT = "input"
    }
}