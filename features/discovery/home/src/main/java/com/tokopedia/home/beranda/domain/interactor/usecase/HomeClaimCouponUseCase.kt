package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home.beranda.data.mapper.ClaimCouponMapper
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
            val response: RedeemCouponModel = repository.request(gqlQuery(), catalogIdParam)
            val jsonMetaData = ClaimCouponMapper.extractMetaData(response.hachikoRedeem?.jsonMetaData())

            RedeemCouponUiModel(
                isRedeemSucceed = response.hachikoRedeem != null,
                redirectUrl = jsonMetaData.url,
                redirectAppLink = jsonMetaData.appLink,
                errorException = null
            )
        } catch (t: MessageErrorException) {
            RedeemCouponUiModel(
                isRedeemSucceed = false,
                redirectUrl = "",
                redirectAppLink = "",
                errorException = null
            )
        }
    }

    override fun graphqlQuery(): String {
        return """
            mutation $OPERATION_NAME(${'$'}$PARAM: Int!) {
              $OPERATION_NAME(catalog_id: ${'$'}$PARAM){
                redeemMessage
                ctaList {
                  url
                  applink
                }
              }
            }
        """.trimIndent()
    }

    // Uses [GqlQueryInterface] as lint suggestion due to fixes performance issue
    private fun gqlQuery() = object : GqlQueryInterface {
        override fun getQuery() = graphqlQuery()
        override fun getOperationNameList() = listOf(OPERATION_NAME)
        override fun getTopOperationName() = OPERATION_NAME
    }

    private fun createCatalogIdParam(id: String) = mapOf(PARAM to id.toIntOrZero())

    companion object {
        private const val OPERATION_NAME = "hachikoRedeem"
        private const val PARAM = "catalogId"
    }
}
