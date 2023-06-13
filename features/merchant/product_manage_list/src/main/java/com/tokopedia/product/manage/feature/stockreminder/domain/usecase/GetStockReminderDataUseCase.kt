package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.StockReminderQuery
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import javax.inject.Inject

@GqlQuery("GetStockReminderGqlQuery", StockReminderQuery.GET_QUERY)
class GetStockReminderDataUseCase @Inject constructor(
        repository: GraphqlRepository
): GraphqlUseCase<GetStockReminderResponse>(repository) {

    companion object{
        private const val PARAMS_PRODUCT_IDS = "productIds"
    }

    init {
        setGraphqlQuery(GetStockReminderGqlQuery())
        setTypeClass(GetStockReminderResponse::class.java)
    }

    fun setParams(productIds: String) {
        val params = mutableMapOf(
            PARAMS_PRODUCT_IDS to productIds
        )
        setRequestParams(params)
    }
}