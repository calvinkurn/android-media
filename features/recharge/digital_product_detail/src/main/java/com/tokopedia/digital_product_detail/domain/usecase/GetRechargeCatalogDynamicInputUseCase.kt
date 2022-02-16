package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogDynamicInput
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeCatalogDynamicInputUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<DigitalCatalogDynamicInput>(graphqlRepository){

    private var params: RequestParams  = RequestParams.EMPTY

    override suspend fun executeOnBackground(): DigitalCatalogDynamicInput {
        val gqlRequest = GraphqlRequest(CommonTopupBillsGqlQuery.rechargeCatalogDynamicProductInput, DigitalCatalogDynamicInput::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        val error = gqlResponse.getError(DigitalCatalogDynamicInput::class.java)
        if (error == null || error.isEmpty()){
            return (gqlResponse.getData(DigitalCatalogDynamicInput::class.java) as DigitalCatalogDynamicInput)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun createDynamicInput(menuID: Int, operator: String){
        params = RequestParams.create().apply {
            putInt(PARAM_MENU_ID, menuID)
            putString(PARAM_OPERATOR, operator)
        }
    }

    companion object{
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
    }

}