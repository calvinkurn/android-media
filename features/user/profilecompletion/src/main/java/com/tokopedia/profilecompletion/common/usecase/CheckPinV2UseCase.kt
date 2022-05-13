package com.tokopedia.profilecompletion.common.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.common.model.CheckPinV2Param
import com.tokopedia.profilecompletion.common.model.CheckPinV2Response
import javax.inject.Inject

class CheckPinV2UseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<CheckPinV2Param, CheckPinV2Response>(dispatcher.io) {

    override fun graphqlQuery(): String {
	return """
            query checkPinV2(${'$'}pin: String!, ${'$'}h: String!, ${'$'}validate_token: String, ${'$'}action: String, ${'$'}user_id: String){
              check_pin_v2(
                id: ${'$'}id, 
                type:${'$'}type,
                action:${'$'}action,
                validate_token:${'$'}validate_token,
                user_id:${'$'}user_id
              ) {
                valid    
                error_message  
              }
            }
        """.trimIndent()
    }

    override suspend fun execute(params: CheckPinV2Param): CheckPinV2Response {
	return repository.request(graphqlQuery(), params)
    }
}