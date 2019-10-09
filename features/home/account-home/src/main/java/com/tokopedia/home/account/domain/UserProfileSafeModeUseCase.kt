package com.tokopedia.home.account.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.UserProfileSettingResponse
import java.util.HashMap

class UserProfileSafeModeUseCase constructor(var context: Context?,
                                             graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<UserProfileSettingResponse>(graphqlRepository) {
    companion object{
        private const val PARAM_SAFE_MODE = "safeMode"
    }

    private fun getRequestParamsForQuerySafeMode(): HashMap<String, Any> {
        return HashMap()
    }

    private fun getQueryForSafeMode(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_user_safe_mode)
    }

    fun executeQuerySafeMode(onSuccess: (UserProfileSettingResponse) -> Unit, onError: (Throwable) -> Unit){
            setRequestParams(getRequestParamsForQuerySafeMode())
            setTypeClass(UserProfileSettingResponse::class.java)
            setGraphqlQuery(getQueryForSafeMode())
            execute({
                onSuccess(it)
            }, onError)
    }

}