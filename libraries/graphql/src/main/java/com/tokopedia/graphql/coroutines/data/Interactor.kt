package com.tokopedia.graphql.coroutines.data

import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.coroutines.data.repository.RepositoryImpl
import com.tokopedia.graphql.coroutines.data.source.GraphqlCacheDataStore
import com.tokopedia.graphql.coroutines.data.source.GraphqlCloudDataStore
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient

@Deprecated("duplicate class")
class Interactor private constructor(){

    private val graphqlCacheManager by lazy { GraphqlCacheManager() }
    private val graphqlCacheDataStore by lazy { GraphqlCacheDataStore(graphqlCacheManager,
            GraphqlClient.getFingerPrintManager()) }
    private val graphqlCloudDataStore by lazy { GraphqlCloudDataStore(GraphqlClient.getApi(),
            graphqlCacheManager, GraphqlClient.getFingerPrintManager()) }
    val graphqlRepository: GraphqlRepository by lazy {
        RepositoryImpl(graphqlCloudDataStore, graphqlCacheDataStore)
    }

    val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase
        get() = MultiRequestGraphqlUseCase(graphqlRepository)


    companion object {
        @Volatile private var INSTANCE: Interactor? = null

        @JvmStatic
        @Deprecated("This is duplication",
                replaceWith = ReplaceWith("GraphqlInteractor.getInstance()",
                        "com.tokopedia.graphql.coroutines.data.GraphqlInteractor"))
        fun getInstance(): Interactor{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Interactor().also { INSTANCE = it }
            }
        }
    }

}