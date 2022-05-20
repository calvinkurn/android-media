package com.tokopedia.gm.common.domain.usecase

import com.tokopedia.gm.common.data.source.cloud.model.ShopLevelResponse
import com.tokopedia.gm.common.domain.mapper.ShopLevelInfoMapper
import com.tokopedia.gm.common.domain.model.ShopLevelParamModel
import com.tokopedia.gm.common.presentation.model.ShopLevelUiModel
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

class GetShopLevelUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: ShopLevelInfoMapper
) : UseCase<ShopLevelUiModel>() {

    private var requestParams: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): ShopLevelUiModel {
        val request =
            GraphqlRequest(GqlQuery, ShopLevelResponse::class.java, requestParams.parameters)
        val gqlResponse = gqlRepository.response(listOf(request))
        val result = gqlResponse.getData<ShopLevelResponse>(ShopLevelResponse::class.java)
        val errorResult = gqlResponse.getError(ShopLevelResponse::class.java)
        if (errorResult.isNullOrEmpty()) {
            return mapper.mapToUiModel(result.shopLevel.result)
        } else {
            throw MessageErrorException(errorResult.firstOrNull()?.message.orEmpty())
        }
    }

    suspend fun execute(shopId: String): ShopLevelUiModel {
        requestParams = RequestParams.create().apply {
            val shopLevelParam = ShopLevelParamModel(shopId)
            putObject(GqlQuery.PARAM_INPUT, shopLevelParam)
        }
        return executeOnBackground()
    }

    object GqlQuery : GqlQueryInterface {

        const val PARAM_INPUT = "input"

        private const val OPERATION_NAME = "shopLevel"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}input: ShopLevelParam!){
              $OPERATION_NAME(input: ${'$'}input){
                result {
                  period
                  nextUpdate
                  shopLevel
                  itemSold
                  niv
                }
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = QUERY

        override fun getTopOperationName(): String = OPERATION_NAME
    }
}