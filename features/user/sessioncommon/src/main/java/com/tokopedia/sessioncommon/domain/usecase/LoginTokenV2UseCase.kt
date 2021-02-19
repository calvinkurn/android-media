package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.domain.query.LoginTokenV2Query
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 16/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class LoginTokenV2UseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<LoginTokenPojoV2>):
        UseCase<LoginTokenPojoV2>() {

    init {
        graphqlUseCase.setGraphqlQuery(LoginTokenV2Query.loginEmailQuery)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(LoginTokenPojoV2::class.java)
    }

    private val params = RequestParams.create()

    fun setParams(email: String, password: String, hash: String) {
        params.putString(PARAM_USERNAME, email)
        params.putString(PARAM_PASSWORD, password)
        params.putString(PARAM_GRANT_TYPE, TYPE_PASSWORD)
        params.putString(PARAM_HASH, hash)
    }

    override suspend fun executeOnBackground(): LoginTokenPojoV2 {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    companion object {

        private val PARAM_GRANT_TYPE: String = "grant_type"
        private val PARAM_USERNAME: String = "username"
        private val PARAM_PASSWORD: String = "password"
        private val PARAM_HASH: String = "h"

        private val TYPE_PASSWORD:String = "password"
    }

}