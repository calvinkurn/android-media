package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.StockReminderQuery
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.CreateUpdateStockReminderParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import javax.inject.Inject

@GqlQuery("ImsCreateStockAlertThresholdGqlQuery", StockReminderQuery.CREATE_QUERY)
class CreateStockReminderDataUseCase @Inject constructor(
        repository: GraphqlRepository
): GraphqlUseCase<CreateStockReminderResponse>(repository) {

    companion object{
        private const val PARAM_INPUT = "input"
    }

    init {
        setGraphqlQuery(ImsCreateStockAlertThresholdGqlQuery())
        setTypeClass(CreateStockReminderResponse::class.java)
    }

    fun setParams(shopId: String,listProductWarehouseParam : ArrayList<ProductWarehouseParam>) {

        val createStockReminderParam = CreateUpdateStockReminderParam(shopId, true, listProductWarehouseParam)
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_INPUT to createStockReminderParam
        )
        setRequestParams(params)
    }

}