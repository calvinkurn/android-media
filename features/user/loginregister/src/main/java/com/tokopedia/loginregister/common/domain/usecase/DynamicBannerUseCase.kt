package com.tokopedia.loginregister.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.common.domain.pojo.DynamicBannerDataModel
import javax.inject.Inject

class DynamicBannerUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<DynamicBannerUseCase.Page, DynamicBannerDataModel>(dispatchers.io) {

    override suspend fun execute(params: Page): DynamicBannerDataModel {
        val mapParam = mapOf(PARAM_PAGE to params.value)
        return repository.request(graphqlQuery(), mapParam)
    }

    override fun graphqlQuery(): String = """
        query getDynamicBanner(${'$'}page: String!) {
            GetBanner(page: ${'$'}page) {
                URL
                enable
                message
                error_message
            }
        }
    """.trimIndent()

    enum class Page(val value: String) {
        LOGIN("login"), REGISTER("register");
    }

    companion object {
        private const val PARAM_PAGE = "page"
    }
}
