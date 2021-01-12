package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val QUERY = """query topAdsAutoTopup(${'$'}shopId: String!) {
    topAdsAutoTopup(shop_id: ${'$'}shopId){
        data {
            status
            status_desc
            tkpd_product_id
            extra_credit_percent
            available_nominal {
                min_credit_fmt
                price_fmt
                tkpd_product_id
            }
        }
        errors {
            Code
            Detail
            Title
        }
    }
}"""

@GqlQuery("TopUpStatus", QUERY)
class TopAdsAutoTopUpUSeCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                                 private val userSessionInterface: UserSessionInterface) : GraphqlUseCase<AutoTopUpData.Response>(graphqlRepository) {

    init {
        setTypeClass(AutoTopUpData.Response::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
    }

    fun setQuery(query: String = TopUpStatus.GQL_QUERY) {
        setGraphqlQuery(query)
    }

    fun setParams() {
        val params = mutableMapOf(ParamObject.SHOP_Id to userSessionInterface.shopId)
        setRequestParams(params)
    }
}