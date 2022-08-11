package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.pin.ValidatePinV2Param
import com.tokopedia.sessioncommon.data.pin.ValidatePinV2Response
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ValidatePinV2UseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository):
    CoroutineUseCase<ValidatePinV2Param, ValidatePinV2Response>(Dispatchers.IO){

    override suspend fun execute(params: ValidatePinV2Param): ValidatePinV2Response {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            query validatePinV2(${'$'}pin: String!, ${'$'}h: String!){
              validate_pin_v2(
                pin:${'$'}pin,
                h:${'$'}h
              ){
                valid
                error_message
                pin_attempted
                max_pin_attempt
              }
            }
        """.trimIndent()
    }
}