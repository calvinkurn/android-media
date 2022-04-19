package com.tokopedia.profilecompletion.addpin.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.CreatePinV2Param
import com.tokopedia.profilecompletion.changepin.data.model.CreatePinV2Response
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CreatePinV2UseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository):
    CoroutineUseCase<CreatePinV2Param, CreatePinV2Response>(Dispatchers.IO){

    override suspend fun execute(params: CreatePinV2Param): CreatePinV2Response {
	return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
	return """
	    mutation createPinV2(${'$'}inputCreatePin: CreatePinV2Param) {
	      create_pin_v2(input: ${'$'}inputCreatePin) {
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