package com.tokopedia.home.beranda.domain.interactor

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.domain.gql.searchHint.KeywordSearchHintQuery
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetKeywordSearchUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<KeywordSearchData>
): UseCase<KeywordSearchData>(){

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
        graphqlUseCase.setGraphqlQuery(KeywordSearchHintQuery.query)
        graphqlUseCase.setRequestParams(params)
        return graphqlUseCase.executeOnBackground()
    }

    companion object {
        const val FIRST_INSTALL = "firstInstall"
        const val UNIQUE_ID = "uniqueId"
    }
}