package com.tokopedia.rechargegeneral.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

class GetProductUseCase(val graphqlRepository: GraphqlRepository) {

    suspend fun getOperatorData(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean): Result<RechargeGeneralOperatorCluster> {
        val useCase = GraphqlUseCase<RechargeGeneralOperatorCluster.Response>(graphqlRepository)
        return try {
            useCase.setGraphqlQuery(rawQuery)
            useCase.setRequestParams(mapParams)
            useCase.setTypeClass(RechargeGeneralOperatorCluster.Response::class.java)
            useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build())

            val data = useCase.executeOnBackground().response
            Success(data)
//            throw MessageErrorException("Incorrect data model")
        } catch (throwable: Throwable) {
            useCase.clearCache()
            Fail(throwable)
        }
    }

    suspend fun getProductList(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean): Result<RechargeGeneralProductData> {
        val useCase = GraphqlUseCase<RechargeGeneralProductData.Response>(graphqlRepository)
        return try {
            useCase.setGraphqlQuery(rawQuery)
            useCase.setRequestParams(mapParams)
            useCase.setTypeClass(RechargeGeneralProductData.Response::class.java)
            useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build())

            val data = useCase.executeOnBackground().response
            Success(data)
//            throw MessageErrorException("Incorrect data model")
        } catch (throwable: Throwable) {
            useCase.clearCache()
            Fail(throwable)
        }
    }

}