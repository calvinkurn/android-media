package com.tokopedia.profilecompletion.changebiousername.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changebiousername.data.UsernameValidationResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ValidateUsernameUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository
) : CoroutineUseCase<String, UsernameValidationResponse>(Dispatchers.IO) {

    private val usernameParam = "username"

    override fun graphqlQuery(): String {
	return """query feedXProfileValidateUsername(${'$'}username: String!) {
                    feedXProfileValidateUsername(username: ${'$'}username) {
                        isValid
                        notValidInformation
                        }
                    }""".trimIndent()
    }

    override suspend fun execute(params: String): UsernameValidationResponse {
	return repository.request(graphqlQuery(), mapOf(usernameParam to params))
    }
}
