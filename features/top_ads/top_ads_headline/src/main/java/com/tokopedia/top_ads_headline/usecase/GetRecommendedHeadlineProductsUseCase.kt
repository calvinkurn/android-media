package com.tokopedia.top_ads_headline.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.top_ads_headline.data.GetRecommendedHeadlineProductsData
import com.tokopedia.topads.common.data.internal.ParamObject
import javax.inject.Inject

class GetRecommendedHeadlineProductsUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GetRecommendedHeadlineProductsData>(graphqlRepository) {

    init {
        setTypeClass(GetRecommendedHeadlineProductsData::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetRecommendedHeadlineProducts)
    }

    fun setParams(shopId:String) {
        val queryMap = mutableMapOf(
                ParamObject.SHOP_ID to shopId
        )
        setRequestParams(queryMap)
    }

}

internal object GetRecommendedHeadlineProducts : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsGetRecommendedHeadlineProducts"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(${'$'}shopID:String!){
                $OPERATION_NAME(shopID:${'$'}shopID) {
                    data {
                        products{
                            id
                            name
                            price
                            priceFmt
                            imageURL
                            rating
                            reviewCount
                            category {
                                id
                                name
                            }
                        }
                    }
                    errors {
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
