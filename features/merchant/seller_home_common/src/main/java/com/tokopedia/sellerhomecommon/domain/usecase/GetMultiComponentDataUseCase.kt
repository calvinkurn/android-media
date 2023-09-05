package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.MultiComponentMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetMultiComponentDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentData
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.delay

@GqlQuery("GetMultiComponentDataQuery", GetMultiComponentDataUseCase.QUERY)
class GetMultiComponentDataUseCase(
    gqlRepository: GraphqlRepository,
    mapper: MultiComponentMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetMultiComponentDataResponse, List<MultiComponentDataUiModel>>(
    gqlRepository, mapper, dispatchers, GetMultiComponentDataQuery()
) {

    override suspend fun executeOnBackground(): List<MultiComponentDataUiModel> {
        delay(1500)
        return listOf(
            MultiComponentDataUiModel(
                tabs = listOf(
                    MultiComponentTab(
                        id = "tab_plus",
                        title = "PLUS",
                        components = listOf(
                            MultiComponentData(
                                componentType = "pieChart",
                                dataKey = "plusSelling",
                                configuration = "",
                                metricParam = ""
                            )
                        ),
                        isSelected = false,
                        isError = false,
                        isLoaded = false,
                        data = listOf()
                    ),
                    MultiComponentTab(
                        id = "tab_bebas_ongkir",
                        title = "Bebas Ongkir",
                        components = listOf(
                            MultiComponentData(
                                componentType = "pieChart",
                                dataKey = "bebasOngkirSelling",
                                configuration = "",
                                metricParam = ""
                            )
                        ),
                        isSelected = false,
                        isError = false,
                        isLoaded = false,
                        data = listOf()
                    )
                )
            )
        )


        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetMultiComponentDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(response, isFromCache)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    override val classType: Class<GetMultiComponentDataResponse>
        get() = GetMultiComponentDataResponse::class.java

    companion object {
        internal const val QUERY = """
          query GetMultiComponentDataQuery(${'$'}dataKeys: [dataKey!]!) {
            fetchMultiComponentWidget(dataKeys:${'$'}dataKeys) {
              datakey
              tabs {
                id
                title
                components {
                  componentType
                  datakey
                  configuration
                  metricsParam
                  error
                  errorMsg
                  showWidget
                }
              }
            }
          }
        """

        private const val DATA_KEYS = "dataKeys"

        fun createRequestParams(
            dataKey: List<String>
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(key = it)
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }

}
