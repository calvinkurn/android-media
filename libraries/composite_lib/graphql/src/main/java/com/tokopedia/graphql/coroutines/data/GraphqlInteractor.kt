package com.tokopedia.graphql.coroutines.data

import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.coroutines.data.repository.GraphqlRepositoryImpl
import com.tokopedia.graphql.coroutines.data.source.GraphqlCacheDataStore
import com.tokopedia.graphql.coroutines.data.source.GraphqlCloudDataStore
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient

class GraphqlInteractor private constructor(){

    private val graphqlCacheManager by lazy { GraphqlCacheManager() }
    private val graphqlCacheDataStore by lazy { GraphqlCacheDataStore(graphqlCacheManager,
            GraphqlClient.getFingerPrintManager()) }
    private val graphqlCloudDataStore by lazy { GraphqlCloudDataStore(GraphqlClient.getApiInterface(),
            graphqlCacheManager, GraphqlClient.getFingerPrintManager()) }
    val graphqlRepository: GraphqlRepository by lazy {
        GraphqlRepositoryImpl(graphqlCloudDataStore, graphqlCacheDataStore)
    }

    val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase
        get() = MultiRequestGraphqlUseCase(graphqlRepository)


    companion object {
        @Volatile private var INSTANCE: GraphqlInteractor? = null

        @JvmStatic
        fun getInstance(): GraphqlInteractor{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: GraphqlInteractor().also { INSTANCE = it }
            }
        }
    }

}