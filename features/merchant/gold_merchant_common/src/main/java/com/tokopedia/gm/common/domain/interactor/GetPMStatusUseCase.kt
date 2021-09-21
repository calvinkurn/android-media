package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatusModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.domain.mapper.PMShopStatusMapper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 16/03/21
 */

class GetPMStatusUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: PMShopStatusMapper
) : BaseGqlUseCase<PMStatusUiModel>() {

    override suspend fun executeOnBackground(): PMStatusUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GoldGetPmOsStatusModel::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GoldGetPmOsStatusModel::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GoldGetPmOsStatusModel>(GoldGetPmOsStatusModel::class.java)
            return if (data.result?.data != null) {
                mapper.mapRemoteModelToUiModel(data.result.data)
            } else {
                throw RuntimeException("power merchant data is null from backend")
            }
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_INCLUDE_OS = "includeOS"

        private val QUERY = """
           query goldGetPMOSStatus(${'$'}shopID: Int!, ${'$'}includeOS: Boolean!) {
             goldGetPMOSStatus(shopID: ${'$'}shopID, includeOS: ${'$'}includeOS) {
               header {
                 messages
                 reason
                 error_code
               }
               data {
                 shopID
                 power_merchant {
                   status
                   expired_time
                   pm_tier
                   auto_extend {
                     status
                   }
                 }
                 official_store {
                   status
                 }
               }
             }
           }
        """.trimIndent()

        fun createParams(shopId: String, shouldIncludeOS: Boolean = true): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putBoolean(KEY_INCLUDE_OS, shouldIncludeOS)
            }
        }
    }
}