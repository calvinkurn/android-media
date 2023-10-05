package com.tokopedia.loginregister.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.common.domain.pojo.TickerInfoData
import com.tokopedia.loginregister.common.domain.pojo.TickerInfoPojo
import javax.inject.Inject

class TickerInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, List<TickerInfoPojo>>(dispatchers.io) {

    override suspend fun execute(params: String): List<TickerInfoPojo> {
        val mapParam = mapOf(PAGE_KEY to params)
        val response: TickerInfoData = repository.request(graphqlQuery(), mapParam)
        return response.tickersInfoPojo.listTickerInfoPojo
    }

    override fun graphqlQuery(): String = """
        query tickerLoginRegister(${'$'}page: String!){
          ticker {
            tickers(page:${'$'}page){
              title
              message
              color
            }
          }
        }
    """.trimIndent()

    companion object {
        const val PAGE_KEY: String = "page"
        const val LOGIN_PAGE: String = "login"
        const val REGISTER_PAGE: String = "register"
    }
}
