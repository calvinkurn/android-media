package com.tokopedia.loginregister.common.view.banner.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import javax.inject.Inject

/**
 * @author rival
 * @created on 20/02/2020
 */

open class DynamicBannerUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, DynamicBannerDataModel>(dispatchers.io) {

    override suspend fun execute(params: String): DynamicBannerDataModel {
        val mapParam = mapOf(PARAM_PAGE to params)
        return repository.request(graphqlQuery(), mapParam)
    }

    override fun graphqlQuery(): String = """
        query getDynamicBanner(${'$'}page: String!) {
            GetBanner(page: ${'$'}page}) {
                URL
                enable
                message
                error_message
            }
        }
    """.trimIndent()

    companion object {
        private const val PARAM_PAGE = "page"
    }
}
