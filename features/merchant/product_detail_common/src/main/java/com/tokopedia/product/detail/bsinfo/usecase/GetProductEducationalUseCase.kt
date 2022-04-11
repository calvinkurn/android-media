package com.tokopedia.product.detail.bsinfo.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.product.detail.bsinfo.data.ProductEducational
import javax.inject.Inject

class GetProductEducationalUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ProductEducational>(graphqlRepository) {

    companion object {
        private const val TYPE_KEY = "type"
        const val QUERY = """
            query PdpGetEducationalBottomsheet(${'$'}type: String!) {
                pdpGetEducationalBottomsheet(type: ${'$'}type){
              	title
              	description
              	icon
            	buttons {
            		buttonTitle
                  	color
            		appLink
            		webLink
            	}
            }
        """

        fun createParams(type: String) = mutableMapOf<String, Any>().apply {
            put(TYPE_KEY, type)
        }
    }

    init {
        setGraphqlQuery(GetPdpEducational())
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
                .setSessionIncluded(true)
                .build())
        setTypeClass(ProductEducational::class.java)
    }

    @GqlQuery("GetPdpEducational", QUERY)
    suspend fun executeOnBackground(requestParams: Map<String, Any>): ProductEducational {
        setRequestParams(requestParams)
        return executeOnBackground()
    }
}