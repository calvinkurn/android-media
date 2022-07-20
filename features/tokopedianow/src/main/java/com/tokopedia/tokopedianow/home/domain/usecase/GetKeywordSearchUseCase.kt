package com.tokopedia.tokopedianow.home.domain.usecase

import android.text.TextUtils
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.KeywordSearchData
import com.tokopedia.tokopedianow.home.domain.query.GetKeywordSearch
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
        setGraphqlQuery(GetKeywordSearch)
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