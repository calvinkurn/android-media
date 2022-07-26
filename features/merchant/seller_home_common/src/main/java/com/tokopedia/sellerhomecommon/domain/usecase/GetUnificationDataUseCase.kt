package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.UnificationMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetUnificationDataResponse
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.domain.model.UnificationDataFetchModel
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationDataUiModel
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

@GqlQuery("GetUnificationDataGqlQuery", GetUnificationDataUseCase.QUERY)
class GetUnificationDataUseCase(
    gqlRepository: GraphqlRepository,
    unificationMapper: UnificationMapper,
    dispatchers: CoroutineDispatchers,
    private val getTableDataUseCase: GetTableDataUseCase
) : CloudAndCacheGraphqlUseCase<GetUnificationDataResponse, List<UnificationDataUiModel>>(
    gqlRepository, unificationMapper, dispatchers, GetUnificationDataGqlQuery()
) {

    private var unificationParams: List<UnificationDataFetchModel> = emptyList()
    private var dynamicParameter: DynamicParameterModel = DynamicParameterModel()

    override val classType: Class<GetUnificationDataResponse>
        get() = GetUnificationDataResponse::class.java

    override suspend fun executeOnBackground(): List<UnificationDataUiModel> {
        initRequestParam()
        if (params.parameters.isEmpty()) {
            throw RuntimeException(PARAM_ERROR_MESSAGE)
        }

        val gqlRequest = GraphqlRequest(
            graphqlQuery,
            classType,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data: GetUnificationDataResponse = gqlResponse.getData(classType)
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            val unificationUiModel = mapper.mapRemoteDataToUiData(data, isFromCache)
            return fetchTabData(unificationUiModel, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    fun setParam(
        params: List<UnificationDataFetchModel>,
        dynamicParameter: DynamicParameterModel
    ) {
        this.unificationParams = params
        this.dynamicParameter = dynamicParameter
    }

    private fun initRequestParam() {
        val dataKeys = unificationParams.map {
            DataKeyModel(
                key = it.unificationDataKey,
                jsonParams = String.format(UNIFICATION_PARAMS, it.shopId)
            )
        }
        this.params = RequestParams.create().apply {
            putObject(DATA_KEYS, dataKeys)
        }
    }

    private suspend fun fetchTabData(
        unificationUiModels: List<UnificationDataUiModel>,
        isFromCache: Boolean
    ): List<UnificationDataUiModel> {
        return unificationUiModels
            .filter { it.tabs.isNotEmpty() }
            .map { model ->
                val param = unificationParams.firstOrNull {
                    model.dataKey == it.unificationDataKey
                }
                val tab = model.tabs.firstOrNull {
                    it.dataKey == param?.tabDataKey
                } ?: model.tabs.first()

                val dataKeyModel = TableAndPostDataKey(
                    dataKey = tab.dataKey,
                    filter = String.EMPTY,
                    maxData = tab.config.maxData,
                    maxDisplayPerPage = tab.config.maxDisplay
                )

                return@map coroutineScope {
                    async {
                        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(
                            listOf(dataKeyModel), dynamicParameter
                        )
                        getTableDataUseCase.setUseCache(isFromCache)
                        val tableResult = try {
                            val data = getTableDataUseCase.executeOnBackground()
                            if (data.isNullOrEmpty()) {
                                throw MessageErrorException(EMPTY_DATA_ERROR_MESSAGE)
                            } else {
                                data.first()
                            }
                        } catch (e: Exception) {
                            TableDataUiModel(
                                dataKey = tab.dataKey,
                                error = e.localizedMessage.orEmpty()
                            )
                        }
                        return@async model.copy(
                            tabs = model.tabs.map tab@{
                                if (tab.dataKey == it.dataKey) {
                                    it.data = tableResult
                                    it.isSelected = true
                                } else {
                                    it.isSelected = false
                                }
                                return@tab it
                            }
                        )
                    }
                }
            }.awaitAll()
    }

    companion object {
        internal const val QUERY = """
            query fetchNavigationTabWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchNavigationTabWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  tabs {
                    title
                    isNew
                    itemCount
                    tooltip
                    content {
                      widget_type
                      datakey
                      configuration
                      metricsParam
                    }
                  }
                  error
                  showWidget
                }
              }
            }
        """

        private const val DATA_KEYS = "dataKeys"
        private const val UNIFICATION_PARAMS = "{\"shop_id\":%s}"
        private const val PARAM_ERROR_MESSAGE =
            "the parameter still empty, please use the setParam(...) method to assign it"
        private const val EMPTY_DATA_ERROR_MESSAGE = "ups, return empty data from backend"
    }
}