package com.tokopedia.loginregister.discover.usecase

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo
import com.tokopedia.loginregister.discover.query.DiscoverQuery
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class DiscoverUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, DiscoverPojo>(dispatcher) {

    override fun graphqlQuery(): String {
        return DiscoverQuery.query
    }

    override suspend fun execute(params: String): DiscoverPojo {
        return repository.request(graphqlQuery(), getParams(params))
    }

    private fun getParams(
        type: String
    ): Map<String, Any> = mapOf(
        PARAM_TYPE to type
    )

    companion object {
        private const val PARAM_TYPE = "type"
    }
}