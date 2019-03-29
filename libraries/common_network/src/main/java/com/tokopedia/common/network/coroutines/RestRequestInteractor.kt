package com.tokopedia.common.network.coroutines

import com.tokopedia.common.network.coroutines.datasource.RestCacheDataStore
import com.tokopedia.common.network.coroutines.datasource.RestCloudDataStore
import com.tokopedia.common.network.coroutines.repository.RestRepositoryImpl
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.common.network.util.RestCacheManager

class RestRequestInteractor private constructor(){

    private val restCacheManager by lazy { RestCacheManager() }
    private val restCacheDataStore by lazy { RestCacheDataStore(restCacheManager,
            NetworkClient.getFingerPrintManager()) }
    private val restCloudDataStore by lazy { RestCloudDataStore(NetworkClient.getApiInterface(),
            restCacheManager, NetworkClient.getFingerPrintManager()) }
    val restRepository by lazy { RestRepositoryImpl(restCloudDataStore, restCacheDataStore) }

    companion object {
        @Volatile private var INSTANCE: RestRequestInteractor? = null

        @JvmStatic
        fun getInstance(): RestRequestInteractor{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: RestRequestInteractor().also { INSTANCE = it }
            }
        }
    }
}