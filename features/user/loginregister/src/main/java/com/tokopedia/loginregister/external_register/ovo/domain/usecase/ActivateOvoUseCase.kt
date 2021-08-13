package com.tokopedia.loginregister.external_register.ovo.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.ovo.data.ActivateOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.query.OvoRegisterQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ActivateOvoUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ActivateOvoResponse>)
    : UseCase<ActivateOvoResponse>() {

    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(OvoRegisterQuery.activateOvoQuery)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(ActivateOvoResponse::class.java)
    }

    fun setParams(phoneNumber: String, name: String, clientId: String = ""){
        params.parameters.clear()
        params.putString(ExternalRegisterConstants.PARAM.PHONE_NO, phoneNumber)
        params.putString(ExternalRegisterConstants.PARAM.NAME, name)
        params.putString(ExternalRegisterConstants.PARAM.CLIENT_ID, clientId)
    }

    override suspend fun executeOnBackground(): ActivateOvoResponse {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).format(Date())
        params.putString(ExternalRegisterConstants.PARAM.DATE, date)
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }
}
