package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.domain.gql.searchHint.KeywordSearchHintUniversalQuery
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeKeywordSearchRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<KeywordSearchData>
): UseCase<KeywordSearchData>(), HomeRepository<KeywordSearchData>{

    var params : Map<String, Any> = mapOf()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(KeywordSearchData::class.java)
    }

    fun createParams(isFirstInstall: Boolean, deviceId: String, userId: String): Map<String, Any> {
        return mutableMapOf<String, Any>().apply {
            put(FIRST_INSTALL, isFirstInstall)
            var uniqueId = AuthHelper.getMD5Hash(deviceId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
            }
            put(UNIQUE_ID, uniqueId)
        }
    }

    override suspend fun executeOnBackground(): KeywordSearchData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(KeywordSearchHintUniversalQuery())
        graphqlUseCase.setRequestParams(params)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): KeywordSearchData {
        createParams(
                isFirstInstall = bundle.getBoolean(FIRST_INSTALL, false),
                deviceId = bundle.getString(DEVICE_ID, ""),
                userId = bundle.getString(USER_ID, "")
        )
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): KeywordSearchData {
        return getRemoteData(bundle)
    }

    companion object {
        const val FIRST_INSTALL = "firstInstall"
        const val UNIQUE_ID = "uniqueId"
        const val DEVICE_ID = "deviceId"
        const val USER_ID = "userId"
    }
}