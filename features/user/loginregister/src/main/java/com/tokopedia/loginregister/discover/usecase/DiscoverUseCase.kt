package com.tokopedia.loginregister.discover.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo
import javax.inject.Inject

class DiscoverUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, DiscoverPojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return """
            query discover(${'$'}$PARAM_TYPE: String!){
            discover(type: ${'$'}$PARAM_TYPE) {
                providers {
                    id
                    name
                    image
                    url
                    scope
                    color
                }
                url_background_seller
            }
        }
        """.trimIndent()
    }

    override suspend fun execute(params: String): DiscoverPojo {
        val mapParam = mapOf(PARAM_TYPE to params)
        return repository.request(graphqlQuery(), mapParam)
    }

    companion object {
        private const val PARAM_TYPE = "type"
    }
}
