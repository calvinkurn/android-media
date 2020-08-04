package com.tokopedia.recommendation_widget_common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.delay

/**
 * Created by Lukas on 04/08/20.
 */

class GetRecommendationFilterChips (
        private val graphqlUseCase: GraphqlUseCase<RecommendationFilterChipsEntity>
): UseCase<RecommendationFilterChipsEntity>() {

    private val query = "query getRecommendationFilterChips(\n" +
            "          \$userId:String,\n" +
            "          \$productIDs:String,\n" +
            "          \$pageName:String,\n" +
            "          \$xSource:String,\n" +
            "          \$queryParam:String){\n" +
            "            recommendationFilterChips(userID:\$userId, productIDs:\$productIDs, pageName:\$pageName, xSource:\$xSource, queryParam:\$queryParam){\n" +
            "                meta {\n" +
            "                    processTime\n" +
            "                    pageName\n" +
            "                }\n" +
            "                data { \n" +
            "                    name\n" +
            "                    icon\n" +
            "                    value\n" +
            "                    inputType\n" +
            "                    isActivated\n" +
            "                    options {\n" +
            "                        name\n" +
            "                        icon\n" +
            "                        value\n" +
            "                        inputType\n" +
            "                        isActivated\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }"

    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(RecommendationFilterChipsEntity::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): RecommendationFilterChipsEntity {
        graphqlUseCase.setRequestParams(params.parameters)

        delay(1000)
        return RecommendationFilterChipsEntity(
                data = listOf(
                        RecommendationFilterChipsEntity.RecommendationFilterChip(
                            name = "Cokelat",
                            value = "&power_badge=true"
                        ),
                        RecommendationFilterChipsEntity.RecommendationFilterChip(
                            name = "Biru",
                            value = "&power_badge=true",
                            isActivated = true
                        ),
                        RecommendationFilterChipsEntity.RecommendationFilterChip(
                            name = "Merah",
                            value = "&power_badge=true"
                        ),
                        RecommendationFilterChipsEntity.RecommendationFilterChip(
                            name = "Hijau",
                            value = "&power_badge=true"
                        ),
                        RecommendationFilterChipsEntity.RecommendationFilterChip(
                            name = "Ungu",
                            value = "&power_badge=true"
                        )
                )
        )
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(userId: String="", productIDs: String="", pageName: String="", xSource: String="", queryParam: String=""){
        params.parameters.clear()
        params.putString(PARAM_USER_ID, userId)
        params.putString(PARAM_PRODUCT_IDS, productIDs)
        params.putString(PARAM_PAGE_NAME, pageName)
        params.putString(PARAM_X_SOURCE, xSource)
        params.putString(PARAM_QUERY_PARAM, queryParam)
    }

    companion object{
        private const val PARAM_USER_ID = "userId"
        private const val PARAM_PRODUCT_IDS = "productIDs"
        private const val PARAM_PAGE_NAME = "pageName"
        private const val PARAM_X_SOURCE = "xSource"
        private const val PARAM_QUERY_PARAM = "queryParam"

    }
}