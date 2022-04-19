package com.tokopedia.profilecompletion.common.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CheckPinV2UseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository)
    : CoroutineUseCase<com.tokopedia.profilecompletion.common.model.CheckPinV2Param, com.tokopedia.profilecompletion.common.model.CheckPinV2Response>(Dispatchers.IO){

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

    override suspend fun execute(params: com.tokopedia.profilecompletion.common.model.CheckPinV2Param): com.tokopedia.profilecompletion.common.model.CheckPinV2Response {
        return repository.request(graphqlQuery(), params)
    }
}