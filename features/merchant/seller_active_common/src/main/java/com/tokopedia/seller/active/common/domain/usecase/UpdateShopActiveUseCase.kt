package com.tokopedia.seller.active.common.domain.usecase

import com.tokopedia.config.GlobalConfig
import com.tokopedia.seller.active.common.data.model.UpdateShopActiveResponse
import com.tokopedia.seller.active.common.data.query.param.UpdateShopActiveParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seller.active.common.data.query.UpdateShopActiveQuery
import javax.inject.Inject

class UpdateShopActiveUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<UpdateShopActiveResponse>(repository) {

    companion object {
        private const val PARAM_INPUT = "input"
    }

    init {
        setGraphqlQuery(UpdateShopActiveQuery)
        setTypeClass(UpdateShopActiveResponse::class.java)
    }

    fun setParam() {
        val updateShopActiveParam = UpdateShopActiveParam("android-${GlobalConfig.VERSION_NAME}")
        val params: Map<String, Any?> = mutableMapOf(
            PARAM_INPUT to updateShopActiveParam
        )
        setRequestParams(params)
    }
}