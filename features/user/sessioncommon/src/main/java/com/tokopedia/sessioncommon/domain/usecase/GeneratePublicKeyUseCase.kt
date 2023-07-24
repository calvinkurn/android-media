package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 17/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */
class GeneratePublicKeyUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, GenerateKeyPojo>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query generate_key(${'$'}$PARAM_MODULE: String!) {
            generate_key(module: ${'$'}$PARAM_MODULE) {
                key
                server_timestamp
                h
            }
        }
    """.trimIndent()

    override suspend fun execute(params: String): GenerateKeyPojo {
        val mapParam = mapOf(PARAM_MODULE to params)
        return repository.request(graphqlQuery(), mapParam)
    }

    suspend operator fun invoke(): GenerateKeyPojo {
        return execute(SessionConstants.GenerateKeyModule.PASSWORD.value)
    }

    companion object {
        const val PARAM_MODULE = "module"
    }
}
