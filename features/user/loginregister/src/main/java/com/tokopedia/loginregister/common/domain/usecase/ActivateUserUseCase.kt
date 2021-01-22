package com.tokopedia.loginregister.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.loginregister.common.DispatcherProvider
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.query.QueryActivateUser
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 16/12/20.
 */

class ActivateUserUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: DispatcherProvider
) : BaseLoginRegisterUseCase<ActivateUserPojo>(dispatcher) {

    @JvmOverloads
    fun getParams(
            email: String = "",
            validateToken: String = ""
    ): Map<String, Any> = mapOf(
            PARAM_EMAIL to email,
            PARAM_VALIDATE_TOKEN to validateToken

    )

    override suspend fun getData(parameter: Map<String, Any>): ActivateUserPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                QueryActivateUser.query,
                ActivateUserPojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        const val PARAM_EMAIL = "email"
        const val PARAM_VALIDATE_TOKEN = "validateToken"
    }
}