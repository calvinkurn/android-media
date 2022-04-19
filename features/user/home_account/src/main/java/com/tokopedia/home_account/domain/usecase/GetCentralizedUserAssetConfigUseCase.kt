package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.domain.query.GetCentralizedUserAssetConfigQuery
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class GetCentralizedUserAssetConfigUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, CentralizedUserAssetDataModel>(dispatcher) {

    override fun graphqlQuery(): String {
        return GetCentralizedUserAssetConfigQuery.query
    }

    override suspend fun execute(params: String): CentralizedUserAssetDataModel {
        val mapParams = getParams(params)
        return repository.request(graphqlQuery(), mapParams)
    }

    private fun getParams(
        entryPoint: String
    ): Map<String, Any> = mapOf(
        PARAM_ENTRY_POINT to entryPoint
    )

    companion object {
        private const val PARAM_ENTRY_POINT = "entryPoint"
    }
}