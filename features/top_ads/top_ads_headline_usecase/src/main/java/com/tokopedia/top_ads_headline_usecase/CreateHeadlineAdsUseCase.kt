package com.tokopedia.top_ads_headline_usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.top_ads_headline_usecase.model.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline_usecase.model.TopAdsManageHeadlineInput2
import com.tokopedia.top_ads_headline_usecase.model.TopadsManageHeadlineAdResponse
import javax.inject.Inject

const val INPUT = "input"

class CreateHeadlineAdsUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<TopadsManageHeadlineAdResponse.Data>(graphqlRepository) {

    init {
        setTypeClass(TopadsManageHeadlineAdResponse.Data::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(TopAdsCreateHeadlineAds)
    }

    fun setParams(input: TopAdsManageHeadlineInput) {
        val queryMap = mutableMapOf(
                INPUT to input
        )
        setRequestParams(queryMap)
    }

    fun setParams(input: TopAdsManageHeadlineInput2) {
        val queryMap = mutableMapOf(
                INPUT to input
        )
        setRequestParams(queryMap)
    }

}

internal object TopAdsCreateHeadlineAds : GqlQueryInterface{

    private const val OPERATION_NAME = "topadsManageHeadlineAd"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            mutation $OPERATION_NAME(${'$'}input:topadsManageHeadlineAdInput!){
                $OPERATION_NAME(input:${'$'}input) {
                    data {
                        id
                        resourceURL
                    }
                    errors{
                        code
                        title
                        detail
                    }
                }
            }
            """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }

}
