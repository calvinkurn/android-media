package com.tokopedia.loginregister.seamlesslogin.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.seamlesslogin.data.GenerateKeyData
import com.tokopedia.loginregister.seamlesslogin.data.GenerateKeyPojo
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 17/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class GenerateKeyUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GenerateKeyPojo>(graphqlRepository) {

    fun executeCoroutines(onSuccess: (GenerateKeyData) -> kotlin.Unit, onError: (Throwable) -> kotlin.Unit){
        getQuery().let { query ->
            setTypeClass(GenerateKeyPojo::class.java)
            setGraphqlQuery(query)
            setRequestParams(mapOf(PARAM_MODULE to "seamless_apps"))
            execute({
                onSuccess(it.data)
            }, {
                val throwable = Throwable(cause = it)
                onError(throwable)
            })
        }
    }

    private fun getQuery(): String = """
        query generateDummyKey($module: String!) {
            generate_key(module: $module) {
            key
            server_timestamp
            error
        }
    }""".trimIndent()

    companion object {
        private const val module = "\$module"
        private const val PARAM_MODULE = "module"
    }
}
