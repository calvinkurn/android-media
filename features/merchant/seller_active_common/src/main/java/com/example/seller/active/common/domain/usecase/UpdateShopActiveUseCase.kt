package com.example.seller.active.common.domain.usecase

import com.example.seller.active.common.data.model.UpdateShopActiveResponse
import com.example.seller.active.common.data.param.UpdateShopActiveParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class UpdateShopActiveUseCase @Inject constructor(
        repository: GraphqlRepository
): GraphqlUseCase<UpdateShopActiveResponse>(repository) {

    companion object {
        private const val PARAM_INPUT = "input"
    }

    init {
        val query = "mutation updateShopActive(\$input: ParamUpdateLastActive!){\n" +
                "  updateShopActive(input: \$input) {\n" +
                "    success\n" +
                "    message\n" +
                "  }\n" +
                "}"
        setGraphqlQuery(query)
        setTypeClass(UpdateShopActiveResponse::class.java)
    }

    fun setParam(device: String) {
        val updateShopActiveParam = UpdateShopActiveParam(device)
        val params : Map<String, Any?> = mutableMapOf(
                PARAM_INPUT to updateShopActiveParam
        )
        setRequestParams(params)
    }
}