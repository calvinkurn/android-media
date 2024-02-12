package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home.beranda.domain.model.RedeemCouponModel
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class HomeClaimCouponUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, Pair<Boolean, String>>(dispatchers.io) {

    override suspend fun execute(params: String): Pair<Boolean, String> {
        return try {
            val response: RedeemCouponModel = repository.request(graphqlQuery(), params)
            val isSucceed = response.hachikoRedeem != null
            Pair(isSucceed, "")
        } catch (t: MessageErrorException) {
            Pair(false, t.message ?: "")
        }
    }

    override fun graphqlQuery(): String {
        return """
            mutation hachikoRedeem(${'$'}catalogId: Int!) {
              hachikoRedeem(catalog_id: ${'$'}catalogId){
                redeemMessage
              }
            }
        """.trimIndent()
    }
}
