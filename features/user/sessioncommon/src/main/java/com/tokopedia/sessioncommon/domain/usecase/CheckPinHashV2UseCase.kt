package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.pin.PinStatusParam
import com.tokopedia.sessioncommon.data.pin.PinStatusResponse
import javax.inject.Inject

class CheckPinHashV2UseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<PinStatusParam, PinStatusResponse>(dispatcher.io) {

    override suspend fun execute(params: PinStatusParam): PinStatusResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query checkPinV2(${'$'}id: String!, ${'$'}type: String!) {
          pinV2Check(id: ${'$'}id, type:${'$'}type) {
            uh
            error_message
          }
        }""".trimIndent()
}