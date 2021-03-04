package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.AnnouncementMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetAnnouncementDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 09/11/20
 */

class GetAnnouncementDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: AnnouncementMapper
) : BaseGqlUseCase<List<AnnouncementDataUiModel>>() {

    override suspend fun executeOnBackground(): List<AnnouncementDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetAnnouncementDataResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(GetAnnouncementDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetAnnouncementDataResponse>()
            val announcementData = response.fetchAnnouncementWidgetData?.data.orEmpty()
            return mapper.mapRemoteModelToUiModel(announcementData, cacheStrategy.type == CacheType.CACHE_ONLY)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun createRequestParams(
                dataKey: List<String>
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                        key = it,
                        jsonParams = "{}"
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }

        val QUERY = """
            query fetchAnnouncementWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchAnnouncementWidgetData(dataKeys:${'$'}dataKeys) {
                data {
                  dataKey
                  errorMsg
                  title
                  subtitle
                  imageUrl
                  button {
                    applink
                  }
                }
              }
            }
        """.trimIndent()
    }
}