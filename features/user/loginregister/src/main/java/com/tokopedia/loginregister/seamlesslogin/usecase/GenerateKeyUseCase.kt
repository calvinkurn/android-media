package com.tokopedia.loginregister.seamlesslogin.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.seamlesslogin.data.model.GenerateKeyData
import com.tokopedia.loginregister.seamlesslogin.data.model.GenerateKeyPojo
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginQueryConstant
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 17/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class GenerateKeyUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GenerateKeyPojo>(graphqlRepository) {

    fun executeCoroutines(onSuccess: (GenerateKeyData) -> kotlin.Unit, onError: (Throwable) -> kotlin.Unit){
        rawQueries[SeamlessLoginQueryConstant.QUERY_GET_KEY]?.let { query ->
            setTypeClass(GenerateKeyPojo::class.java)
            setGraphqlQuery(query)
            setRequestParams(mapOf(
                    SeamlessLoginQueryConstant.PARAM_MODULE to "seamless_apps"
            ))
            execute({
                onSuccess(it.data)
            }, {
                val throwable = Throwable(cause = it)
                onError(throwable)
            })
        }
    }
}