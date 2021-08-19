package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.domain.query.GetCentralizedUserAssetConfigQuery
import javax.inject.Inject

open class GetCentralizedUserAssetConfigUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, CentralizedUserAssetDataModel>(dispatcher.io) {

    fun getParams(
        entryPoint: String
    ): Map<String, Any> = mapOf(
        PARAM_ENTRY_POINT to entryPoint
    )

    override fun graphqlQuery(): String {
        return GetCentralizedUserAssetConfigQuery.query
    }

    override suspend fun execute(params: Map<String, Any>): CentralizedUserAssetDataModel {
        return request(repository, params)
    }

    companion object {
        private const val PARAM_ENTRY_POINT = "entryPoint"
    }
}