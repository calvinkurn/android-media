package com.tokopedia.sellerhomecommon.domain.usecase

import com.google.gson.Gson
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
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationWidgetUiModel
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

@GqlQuery("GetUnificationDataGqlQuery", GetUnificationDataUseCase.QUERY)
class GetUnificationDataUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
    private val unificationMapper: UnificationMapper,
    private val getTableDataUseCase: GetTableDataUseCase,
) : CloudAndCacheGraphqlUseCase<GetUnificationDataResponse, List<UnificationDataUiModel>>(
    gqlRepository, unificationMapper, dispatchers, GetUnificationDataGqlQuery()
) {

    private var shopId: String = String.EMPTY
    private var widgets: List<UnificationWidgetUiModel> = emptyList()
    private var dynamicParameter: DynamicParameterModel = DynamicParameterModel()

    override val classType: Class<GetUnificationDataResponse>
        get() = GetUnificationDataResponse::class.java

    override suspend fun executeOnBackground(): List<UnificationDataUiModel> {
        val existingWidget = widgets.filter { !it.data?.tabs.isNullOrEmpty() }
        val isWidgetExist = existingWidget.isNotEmpty()
        return if (isWidgetExist) {
            getExistingWidgetData()
        } else {
            fetchNewWidgetData()
        }
    }

    fun setParam(
        shopId: String,
        widgets: List<UnificationWidgetUiModel>,
        dynamicParameter: DynamicParameterModel
    ) {
        this.shopId = shopId
        this.widgets = widgets
        this.dynamicParameter = dynamicParameter
    }

    private suspend fun getExistingWidgetData(): List<UnificationDataUiModel> {
        val unificationDataList = widgets.filter { it.data != null }.map { it.data!! }
        return fetchTabData(
            unificationDataList,
            isExistingWidgetDataFetch = true,
            isFromCache = false
        )
    }

    private suspend fun fetchNewWidgetData(): List<UnificationDataUiModel> {
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
            return fetchTabData(unificationUiModel, false, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    private fun initRequestParam() {
        val dataKeys = widgets.map { widget ->
            DataKeyModel(
                key = widget.dataKey,
                jsonParams = String.format(UNIFICATION_PARAMS, shopId)
            )
        }
        this.params = RequestParams.create().apply {
            putObject(DATA_KEYS, dataKeys)
        }
    }

    private suspend fun fetchTabData(
        unificationUiModels: List<UnificationDataUiModel>,
        isExistingWidgetDataFetch: Boolean,
        isFromCache: Boolean
    ): List<UnificationDataUiModel> {
        return unificationUiModels
            .filter { it.tabs.isNotEmpty() }
            .map { model ->
                val tab = if (isExistingWidgetDataFetch) {
                    model.tabs.firstOrNull { it.isSelected } ?: model.tabs.first()
                } else {
                    model.tabs.first()
                }

                val dataKeyModel = TableAndPostDataKey(
                    dataKey = tab.dataKey,
                    filter = String.EMPTY,
                    maxData = tab.config.maxData,
                    maxDisplayPerPage = tab.config.maxDisplay
                )

                return@map coroutineScope {
                    async {
                        val tabData = fetchTableData(tab, dataKeyModel, isFromCache)
                        return@async model.copy(
                            tabs = model.tabs.map tab@{
                                if (tab.dataKey == it.dataKey) {
                                    it.data = tabData
                                    it.isSelected = true
                                    it.isVisited = true
                                } else {
                                    it.isSelected = false
                                }
                                return@tab it
                            },
                            lastUpdated = unificationMapper.getLastUpdated(
                                model.dataKey, isFromCache
                            )
                        )
                    }
                }
            }.awaitAll()
    }

    private suspend fun fetchTableData(
        tab: UnificationTabUiModel,
        dataKeyModel: TableAndPostDataKey,
        isFromCache: Boolean
    ): TableDataUiModel {
        val metricParam = getMetricParamFromTab(tab.metricParam)
        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(
            listOf(dataKeyModel), dynamicParameter.copy(
                subPageSource = metricParam.subPageSource
            )
        )
        getTableDataUseCase.setUseCache(isFromCache)
        val tableResult = try {
            val data = getTableDataUseCase.executeOnBackground()
            if (data.isEmpty()) {
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
        return tableResult
    }

    private fun getMetricParamFromTab(metricParam: String): DynamicParameterModel {
        return Gson().fromJson(metricParam, DynamicParameterModel::class.java)
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
                    isUnauthorized
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