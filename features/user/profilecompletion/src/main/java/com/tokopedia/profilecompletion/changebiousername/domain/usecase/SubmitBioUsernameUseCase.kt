package com.tokopedia.profilecompletion.changebiousername.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.common.SubmitProfileError
import com.tokopedia.profilecompletion.changebiousername.data.SubmitBioUsernameResponse
import com.tokopedia.profilecompletion.changebiousername.data.SubmitProfileParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SubmitBioUsernameUseCase @Inject constructor(private val repository: GraphqlRepository) :
    CoroutineUseCase<SubmitProfileParam, SubmitBioUsernameResponse>(Dispatchers.IO) {

    private val paramQuery = "param"

    override fun graphqlQuery(): String {
        return """mutation feedXProfileSubmit(${'$'}param: feedXProfileSubmitRequest!) {
                       feedXProfileSubmit(req: ${'$'}param) {
                           status
                       }
                   }""".trimIndent()
    }

    override suspend fun execute(params: SubmitProfileParam): SubmitBioUsernameResponse {
        val param = params.toMapParam()
        val request = GraphqlRequest(graphqlQuery(), SubmitBioUsernameResponse::class.java, mapOf(paramQuery to param))
        val response = repository.response(listOf(request))
        val error = response.getError(SubmitBioUsernameResponse::class.java)
        if ( error.isNotEmpty() || error != null) {
            throw SubmitProfileError(error.first(), error.first().message)
        } else {
            return response.getData(SubmitBioUsernameResponse::class.java)
        }

    }
}