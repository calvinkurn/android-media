package com.tokopedia.home_account.account_settings.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.R
import com.tokopedia.home_account.account_settings.data.model.UserProfileSettingResponse
import java.util.*
import javax.inject.Inject

class UserProfileSafeModeUseCase @Inject constructor(@ApplicationContext var context: Context?,
                                                     graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<UserProfileSettingResponse>(graphqlRepository) {

    private fun getRequestParamsForQuerySafeMode(): HashMap<String, Any> {
        return HashMap()
    }

    private fun getQueryForSafeMode(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_user_safe_mode)
    }

    fun executeQuerySafeMode(onSuccess: (UserProfileSettingResponse) -> Unit, onError: (Throwable) -> Unit) {
        setRequestParams(getRequestParamsForQuerySafeMode())
        setTypeClass(UserProfileSettingResponse::class.java)
        setGraphqlQuery(getQueryForSafeMode())
        execute({
            onSuccess(it)
        }, onError)
    }

}