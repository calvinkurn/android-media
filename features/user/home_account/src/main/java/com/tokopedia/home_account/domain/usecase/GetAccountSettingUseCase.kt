package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.account_settings.data.model.AccountSettingResponse
import javax.inject.Inject

class GetAccountSettingUseCase @Inject constructor(
    @ApplicationContext private val gqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, AccountSettingResponse.Config>(dispatcher.io) {

    override suspend fun execute(params: Unit): AccountSettingResponse.Config {
        val param = mapOf("type" to TYPE_CUSTOMER_APP)
        val response: AccountSettingResponse = gqlRepository.request(graphqlQuery(), param)
        return response.config
    }

    override fun graphqlQuery(): String = """
        query AccountSettingConfig(${'$'}type: Int!) {
          accountSettingConfig(type: ${'$'}type) {
            dataDiri
            daftarAlamat
            dokumenDataDiri
            tokopediaCorner
            ubahKataSandi
          }
        }
    """.trimIndent()

    companion object {
        const val TYPE_CUSTOMER_APP = 11
    }
}
