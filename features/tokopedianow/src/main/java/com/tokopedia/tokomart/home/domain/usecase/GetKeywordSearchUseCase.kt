package com.tokopedia.tokomart.home.domain.usecase

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.home.domain.model.KeywordSearchData
import com.tokopedia.tokomart.home.domain.query.GetKeywordSearch.QUERY
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetKeywordSearchUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<KeywordSearchData>(graphqlRepository) {

    companion object {
        const val FIRST_INSTALL = "firstInstall"
        const val UNIQUE_ID = "uniqueId"
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(KeywordSearchData::class.java)
    }

    suspend fun execute(isFirstInstall: Boolean, deviceId: String, userId: String): KeywordSearchData {
        setRequestParams(RequestParams.create().apply {
            putBoolean(FIRST_INSTALL, isFirstInstall)
            var uniqueId = AuthHelper.getMD5Hash(deviceId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
            }
            putString(UNIQUE_ID, uniqueId)
        }.parameters)
        return executeOnBackground()
    }
}