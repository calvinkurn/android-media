package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.pin.PinStatusParam
import com.tokopedia.sessioncommon.data.pin.PinStatusResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetPinStatusUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository)
: CoroutineUseCase<PinStatusParam, PinStatusResponse>(Dispatchers.IO){

    override suspend fun execute(params: PinStatusParam): PinStatusResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
	mutation pin_v2_check(${'$'}id: String!, ${'$'}type: String!) {
	  pinV2Check(id: ${'$'}id, type:${'$'}type) {
	    uh
	    error_message
	  }
	}""".trimIndent()
}