package com.tokopedia.accountprofile.settingprofile.changepin.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.accountprofile.settingprofile.changepin.data.model.ResetPinV2Param
import com.tokopedia.accountprofile.settingprofile.changepin.data.model.ResetPinV2Response
import javax.inject.Inject

class ResetPinV2UseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ResetPinV2Param, ResetPinV2Response>(dispatcher.io) {

    override suspend fun execute(params: ResetPinV2Param): ResetPinV2Response {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
	    mutation resetPinV2(${'$'}pin_token: String!, ${'$'}validate_token: String!) {
	        reset_pin_v2(input: {
                pin_token: ${'$'}pin_token,
                validate_token: ${'$'}validate_token
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
