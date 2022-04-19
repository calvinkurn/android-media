package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.pin.PinStatusParam
import com.tokopedia.sessioncommon.data.pin.PinStatusResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CheckPinHashV2UseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository)
: CoroutineUseCase<PinStatusParam, PinStatusResponse>(Dispatchers.IO){

    override suspend fun execute(params: PinStatusParam): PinStatusResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
	query checkPinV2(${'$'}id: String!, ${'$'}type: String!) {
	  check_pin_v2(id: ${'$'}id, type:${'$'}type) {
	    valid
	    error_message
	  }
	}""".trimIndent()
}