package com.tokopedia.home.account.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.UserProfileDobResponse
import java.util.HashMap
import javax.inject.Inject

class UserProfileDobUseCase @Inject constructor(@ApplicationContext var context: Context?,
                                                graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<UserProfileDobResponse>(graphqlRepository) {

    private fun getRequestParamsForQueryDob(): HashMap<String, Any> {
        return HashMap()
    }

    private fun getQueryForDob(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_user_age)
    }

    fun executeQuerySafeMode(onSuccess: (UserProfileDobResponse) -> Unit, onError: (Throwable) -> Unit) {
        setRequestParams(getRequestParamsForQueryDob())
        setTypeClass(UserProfileDobResponse::class.java)
        setGraphqlQuery(getQueryForDob())
        execute({
            onSuccess(it)
        }, onError)
    }

}