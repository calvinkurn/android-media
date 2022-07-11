package com.tokopedia.loginregister.discover.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo
import com.tokopedia.loginregister.discover.query.DiscoverQuery
import javax.inject.Inject

class DiscoverUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, DiscoverPojo>(dispatcher.io) {

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