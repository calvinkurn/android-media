package com.tokopedia.profilecompletion.changepin.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changepin.data.model.ResetPinV2Param
import com.tokopedia.profilecompletion.changepin.data.model.ResetPinV2Response
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ResetPinV2UseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository):
    CoroutineUseCase<ResetPinV2Param, ResetPinV2Response>(Dispatchers.IO){

    override suspend fun execute(params: ResetPinV2Param): ResetPinV2Response {
	return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
	return """
	    mutation resetPinV2(${'$'}inputResetPin: ResetPinV2param) {
	      reset_pin_v2(input: ${'$'}inputResetPin) {
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