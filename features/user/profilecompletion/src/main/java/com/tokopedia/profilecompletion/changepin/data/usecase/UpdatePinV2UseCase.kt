package com.tokopedia.profilecompletion.changepin.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changepin.data.model.ChangePinV2Param
import com.tokopedia.profilecompletion.changepin.data.model.UpdatePinV2Response
import javax.inject.Inject

class UpdatePinV2UseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ChangePinV2Param, UpdatePinV2Response>(dispatcher.io) {

    override suspend fun execute(params: ChangePinV2Param): UpdatePinV2Response {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            mutation updatePinV2(${'$'}pin: String!, ${'$'}pin_old: String!, ${'$'}pin_confirm: String!, ${'$'}h: String!) {
              update_pin_v2(input: {
                pin: ${'$'}pin,
                pin_old: ${'$'}pin_old,
                pin_confirm: ${'$'}pin_confirm,
                h: ${'$'}h
              }) {
                success
                errors {
                  name
                  message
                }
              }
            }
        """.trimIndent()
    }
}