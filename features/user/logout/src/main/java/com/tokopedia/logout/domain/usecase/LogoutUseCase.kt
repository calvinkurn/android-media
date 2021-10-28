package com.tokopedia.logout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logout.domain.model.LogoutDataModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LogoutUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository):
    CoroutineUseCase<Unit, LogoutDataModel>(Dispatchers.IO) {

    override suspend fun execute(params: Unit): LogoutDataModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation logoutUser {
            logout_user {
                success
                errors {
                    name
                    message
                }
            }
        }
    """.trimIndent()
}