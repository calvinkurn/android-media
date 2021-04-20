package com.tokopedia.home_account.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.SetUserProfileSettingResponse
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 28/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

private const val PARAM_SAFE_MODE = "safeMode"
class SafeSettingProfileUseCase @Inject constructor(@ApplicationContext var context: Context?,
                                                        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<SetUserProfileSettingResponse>(graphqlRepository) {

    private fun getRequestParamsForSetSafeMode(savedValue: Boolean): HashMap<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[PARAM_SAFE_MODE] = savedValue
        return requestParams
    }

    private fun getQueryForSetSafeMode(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.mutation_user_safe_mode)
    }

    fun executeQuerySetSafeMode(onSuccess: (SetUserProfileSettingResponse) -> Unit, onError: (Throwable) -> Unit, savedValue: Boolean) {
        setRequestParams(getRequestParamsForSetSafeMode(savedValue))
        setTypeClass(SetUserProfileSettingResponse::class.java)
        setGraphqlQuery(getQueryForSetSafeMode())
        execute({
            onSuccess(it)
        }, onError)
    }

}