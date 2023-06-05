package com.tokopedia.logout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logout.domain.model.LogoutDataModel
import com.tokopedia.logout.domain.model.LogoutParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LogoutUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository):
    CoroutineUseCase<LogoutParam, LogoutDataModel>(Dispatchers.IO) {

    override suspend fun execute(params: LogoutParam): LogoutDataModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation logoutUser(${'$'}save_session: String!, ${'$'}ocl_jwt_token: String!) {
            logout_user(save_session: ${'$'}save_session, ocl_jwt_token: ${'$'}ocl_jwt_token) {
                success
                ocl_jwt_token
                errors {
                    name
                    message
                }
            }
        }
    """.trimIndent()

    companion object {
        const val PARAM_SAVE_SESSION = "1"
    }
}
