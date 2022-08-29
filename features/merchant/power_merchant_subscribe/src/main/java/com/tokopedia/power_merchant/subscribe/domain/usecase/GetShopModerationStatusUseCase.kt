package com.tokopedia.power_merchant.subscribe.domain.usecase

import com.tokopedia.gm.common.data.source.cloud.model.ParamShopInfoByID
import com.tokopedia.gm.common.domain.interactor.BaseGqlUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.power_merchant.subscribe.domain.model.GetShopInfoByIdResponse
import com.tokopedia.power_merchant.subscribe.view.model.ModerationShopStatusUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 22/05/21
 */

@GqlQuery("GetShopModerationStatusGqlQuery", GetShopModerationStatusUseCase.QUERY)
class GetShopModerationStatusUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<ModerationShopStatusUiModel>() {

    override suspend fun executeOnBackground(): ModerationShopStatusUiModel {
        val gqlRequest = GraphqlRequest(
            GetShopModerationStatusGqlQuery(),
            GetShopInfoByIdResponse::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GetShopInfoByIdResponse::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val data =
                gqlResponse.getData<GetShopInfoByIdResponse>(GetShopInfoByIdResponse::class.java)
            val shopStatusId: Int? =
                data?.shopInfoById?.result?.firstOrNull()?.statusInfo?.shopStatus
            if (shopStatusId != null) {
                return ModerationShopStatusUiModel(shopStatusId)
            } else {
                throw RuntimeException()
            }
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        const val QUERY = """
            query shopInfoByID(${'$'}input: ParamShopInfoByID!) {
              shopInfoByID(input: ${'$'}input) {
                result {
                  statusInfo {
                    shopStatus
                  }
                }
              }
            }
        """
        private const val PARAM_INPUT = "input"

        fun createParam(shopId: Long): RequestParams {
            val filedStatusInfo = "status"
            val inputParams = ParamShopInfoByID(
                shopIDs = listOf(shopId),
                fields = listOf(filedStatusInfo)
            )
            return RequestParams.create().apply {
                putObject(PARAM_INPUT, inputParams)
            }
        }
    }
}