package com.tokopedia.sellerhomecommon.domain.mapper

import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetUnificationDataResponse
import com.tokopedia.sellerhomecommon.domain.model.UnificationTabModel
import com.tokopedia.sellerhomecommon.domain.model.WidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.LastUpdatedUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetConfigUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

class UnificationMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetUnificationDataResponse, List<UnificationDataUiModel>> {

    companion object {
        private const val ERROR_MESSAGE = "Error : got error from backend without message"
    }

    override fun mapRemoteDataToUiData(
        response: GetUnificationDataResponse,
        isFromCache: Boolean
    ): List<UnificationDataUiModel> {
        return response.navigationTab.data.map { data ->
            return@map UnificationDataUiModel(
                dataKey = data.dataKey,
                error = getErrorMessage(data.isError, data.errorMsg),
                isFromCache = isFromCache,
                showWidget = data.showWidget,
                lastUpdated = getLastUpdated(data.dataKey, isFromCache),
                tabs = data.tabs.map { tab ->
                    getTabUiModel(tab)
                }
            )
        }
    }

    fun getLastUpdated(dataKey: String, isFromCache: Boolean): LastUpdatedUiModel {
        return super.getLastUpdatedMillis(dataKey, isFromCache)
    }

    private fun getErrorMessage(isError: Boolean, errorMsg: String): String {
        return errorMsg.ifBlank {
            if (isError) {
                ERROR_MESSAGE
            } else {
                String.EMPTY
            }
        }
    }

    private fun getTabUiModel(
        tab: UnificationTabModel
    ): UnificationTabUiModel {
        return UnificationTabUiModel(
            data = null,
            title = tab.title,
            isNew = tab.isNew,
            isUnauthorized = tab.isUnauthorized,
            itemCount = tab.itemCount,
            tooltip = tab.tooltip,
            widgetType = tab.content.widgetType,
            dataKey = tab.content.dataKey,
            metricParam = tab.content.metricsParam,
            config = getTabConfig(tab.content.configuration)
        )
    }

    private fun getTabConfig(configStr: String): WidgetConfigUiModel {
        val configModel: WidgetModel = Gson().fromJson(configStr, WidgetModel::class.java)
        return WidgetConfigUiModel(
            title = configModel.title.orEmpty(),
            appLink = configModel.appLink.orEmpty(),
            ctaText = configModel.ctaText.orEmpty(),
            maxData = configModel.maxData.orZero(),
            maxDisplay = configModel.maxDisplay.orZero(),
            emptyStateUiModel = configModel.emptyStateModel.mapToUiModel()
        )
    }
}