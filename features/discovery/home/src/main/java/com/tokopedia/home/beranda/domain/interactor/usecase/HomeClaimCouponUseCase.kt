package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home.beranda.data.model.RedeemCouponModel
import com.tokopedia.home.beranda.domain.model.RedeemCouponUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class HomeClaimCouponUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, RedeemCouponUiModel>(dispatchers.io) {

    override suspend fun execute(params: String): RedeemCouponUiModel {
        return try {
            val catalogIdParam = createCatalogIdParam(params)
            val response: RedeemCouponModel = repository.request(graphqlQuery(), catalogIdParam)

            RedeemCouponUiModel(
                isRedeemSucceed = response.hachikoRedeem != null,
                redirectUrl = response.hachikoRedeem?.redirectUrl(),
                errorException = null
            )
        } catch (t: MessageErrorException) {
            RedeemCouponUiModel(
                isRedeemSucceed = false,
                redirectUrl = null,
                errorException = null
            )
        }
    }

    override fun graphqlQuery(): String {
        return """
            mutation hachikoRedeem(${'$'}$PARAM: Int!) {
              hachikoRedeem(catalog_id: ${'$'}$PARAM){
                redeemMessage
                ctaList {
                  url
                  applink
                }
              }
            }
        """.trimIndent()
    }

    private fun createCatalogIdParam(id: String) = mapOf(PARAM to id.toIntOrZero())

    companion object {
        private const val PARAM = "catalogId"
    }
}
