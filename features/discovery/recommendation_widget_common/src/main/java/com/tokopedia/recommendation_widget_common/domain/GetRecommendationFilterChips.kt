package com.tokopedia.recommendation_widget_common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Lukas on 04/08/20.
 */

class GetRecommendationFilterChips (
        private val graphqlUseCase: GraphqlUseCase<RecommendationFilterChipsEntity>
): UseCase<RecommendationFilterChipsEntity.FilterAndSort>() {

    private val query = "query RecommendationFilterChipsQuery(${'$'}productIDs: String!, ${'$'}pageName: String!, ${'$'}xSource: String!, ${'$'}queryParam: String!, ${'$'}filterType: String!, ${'$'}injectionID: String!, ${'$'}userID: Int) {\n" +
            "  recommendationFilterChips(productIDs: ${'$'}productIDs, pageName: ${'$'}pageName, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam, filterType: ${'$'}filterType, injectionID: ${'$'}injectionID, userID: ${'$'}userID) {\n" +
            "    data {\n" +
            "      filter {\n" +
            "        title\n" +
            "        name\n" +
            "        templateName\n" +
            "        isActivated\n" +
            "        value\n" +
            "        options {\n" +
            "          name\n" +
            "          icon\n" +
            "          key\n" +
            "          value\n" +
            "          inputType\n" +
            "          isActivated\n" +
            "          isPopular\n" +
            "          child {\n" +
            "            name\n" +
            "            icon\n" +
            "            key\n" +
            "            value\n" +
            "            inputType\n" +
            "            child {\n" +
            "              name\n" +
            "              icon\n" +
            "              key\n" +
            "              value\n" +
            "              inputType\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "      sort {\n" +
            "        name\n" +
            "        key\n" +
            "        value\n" +
            "        inputType\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}"

    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(RecommendationFilterChipsEntity::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): RecommendationFilterChipsEntity.FilterAndSort {
        graphqlUseCase.setRequestParams(params.parameters)
        val recommendationFilterChipsEntity = graphqlUseCase.executeOnBackground()
        return recommendationFilterChipsEntity.recommendationFilterChips.data
    }

    fun setParams(userId: Int=0, productIDs: String="", pageName: String="", xSource: String="", queryParam: String="", type: String=""){
        params.parameters.clear()
        if (userId != 0 ) params.putInt(PARAM_USER_ID, userId)
        if (productIDs.isNotEmpty()) params.putString(PARAM_PRODUCT_IDS, productIDs)
        if (pageName.isNotEmpty()) params.putString(PARAM_PAGE_NAME, pageName)
        if (xSource.isNotEmpty()) params.putString(PARAM_X_SOURCE, xSource)
        if (queryParam.isNotEmpty()) params.putString(PARAM_QUERY_PARAM, queryParam)
        if (type.isNotEmpty()) params.putString(PARAM_FILTER_TYPE, type)
    }

    companion object{
        private const val PARAM_USER_ID = "userId"
        private const val PARAM_PRODUCT_IDS = "productIDs"
        private const val PARAM_PAGE_NAME = "pageName"
        private const val PARAM_X_SOURCE = "xSource"
        private const val PARAM_FILTER_TYPE = "filterType"
        private const val PARAM_QUERY_PARAM = "queryParam"
        const val FULL_FILTER = "full_filter"
        const val QUICK_FILTER = "quick_filter"
    }
}