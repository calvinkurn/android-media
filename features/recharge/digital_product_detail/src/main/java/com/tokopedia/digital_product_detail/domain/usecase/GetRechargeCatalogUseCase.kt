package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeCatalogUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<CatalogData.Response>(graphqlRepository)  {

    private var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): CatalogData.Response {
        val gqlRequest = GraphqlRequest(CommonTopupBillsGqlQuery.rechargeCatalogProductInput, CatalogData.Response::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        val error = gqlResponse.getError(CatalogData.Response::class.java)
        if (error == null || error.isEmpty()){
            return (gqlResponse.getData(CatalogData.Response::class.java) as CatalogData.Response)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun createProductListParams(menuID: Int, operator: String){
        params = RequestParams.create().apply {
            putInt(KEY_MENU_ID, menuID)
            putString(KEY_OPERATOR, operator)
        }
    }

    companion object{
        private const val KEY_MENU_ID = "menuID"
        private const val KEY_OPERATOR = "operator"
    }
}