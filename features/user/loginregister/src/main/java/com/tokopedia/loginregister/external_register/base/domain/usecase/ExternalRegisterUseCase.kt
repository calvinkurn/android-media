package com.tokopedia.loginregister.external_register.base.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.ovo.domain.query.OvoRegisterQuery
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 05/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class ExternalRegisterUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<RegisterPojo>)
    : UseCase<RegisterPojo>() {

    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(OvoRegisterQuery.registerOvoQuery)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(RegisterPojo::class.java)
    }

    fun setParams(phoneNumber: String, name: String, goalKey: String, authCode: String){
        params.parameters.clear()
        params.putString(ExternalRegisterConstants.PARAM.MSISDN, phoneNumber)
        params.putString(ExternalRegisterConstants.PARAM.FULLNAME, name)
        params.putString(ExternalRegisterConstants.PARAM.GOAL_KEY, goalKey)
        params.putString(ExternalRegisterConstants.PARAM.AUTH_CODE, authCode)
        params.putString(ExternalRegisterConstants.PARAM.TYPE, "goal")
    }

    override suspend fun executeOnBackground(): RegisterPojo {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }
}
