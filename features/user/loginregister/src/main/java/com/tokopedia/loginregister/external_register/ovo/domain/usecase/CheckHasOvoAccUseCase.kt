package com.tokopedia.loginregister.external_register.ovo.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.query.OvoRegisterQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class CheckHasOvoAccUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<CheckOvoResponse>)
    : UseCase<CheckOvoResponse>() {

    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(OvoRegisterQuery.checkHasOvoQuery)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(CheckOvoResponse::class.java)
    }

    fun setParams(phoneNumber: String){
        params.parameters.clear()
        params.putString(ExternalRegisterConstants.PARAM.PHONE_NO, phoneNumber)
    }

    override suspend fun executeOnBackground(): CheckOvoResponse {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()

    }
}
