package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.domain.query.GenerateKeyQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Yoris Prayogo on 17/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */
open class GeneratePublicKeyUseCase(private val graphqlUseCase: GraphqlUseCase<GenerateKeyPojo>): UseCase<GenerateKeyPojo>() {

    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(GenerateKeyQuery.generateKeyQuery)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(GenerateKeyPojo::class.java)
    }

    override suspend fun executeOnBackground(): GenerateKeyPojo {
        params.putString(PARAM_MODULE, "pwd")
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    companion object {
        const val PARAM_MODULE = "module"
    }
}