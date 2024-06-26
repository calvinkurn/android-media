package com.tokopedia.accountprofile.settingprofile.changebiousername.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.accountprofile.settingprofile.changebiousername.data.SubmitBioUsernameResponse
import com.tokopedia.accountprofile.settingprofile.changebiousername.data.SubmitProfileParam
import com.tokopedia.accountprofile.common.SubmitProfileError
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SubmitBioUsernameUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<SubmitProfileParam, SubmitBioUsernameResponse>(dispatchers.io) {

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
        val request = GraphqlRequest(
            graphqlQuery(),
            SubmitBioUsernameResponse::class.java,
            mapOf(paramQuery to param)
        )
        val response = repository.response(listOf(request))
        val error = response.getError(SubmitBioUsernameResponse::class.java)
        if (error != null && error.isNotEmpty()) {
            throw SubmitProfileError(error.first(), error.first().message)
        } else {
            return response.getData(SubmitBioUsernameResponse::class.java)
        }

    }
}
