package com.tokopedia.managepassword.haspassword.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import javax.inject.Inject

class GetProfileCompletionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, ProfileDataModel>(dispatchers.io) {

    override suspend fun execute(params: Unit): ProfileDataModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            query user(){
              user {
                createdPassword
              }
            }
        """.trimIndent()
    }
}