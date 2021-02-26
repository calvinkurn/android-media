package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.AnnouncementMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetAnnouncementDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 09/11/20
 */

class GetAnnouncementDataUseCase(
        gqlRepository: GraphqlRepository,
        mapper: AnnouncementMapper
) : CloudAndCacheGraphqlUseCase<GetAnnouncementDataResponse, List<AnnouncementDataUiModel>>(gqlRepository, mapper, true, GetAnnouncementDataResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
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