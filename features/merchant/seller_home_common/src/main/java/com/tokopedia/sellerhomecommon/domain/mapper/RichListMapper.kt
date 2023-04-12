package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetRichListDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.RichListDataUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 12/04/23.
 */

class RichListMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetRichListDataResponse, List<RichListDataUiModel>> {

    override fun mapRemoteDataToUiData(
        response: GetRichListDataResponse,
        isFromCache: Boolean
    ): List<RichListDataUiModel> {
        return response.data.widgetData.map { data ->
            RichListDataUiModel(
                dataKey = data.dataKey,
                error = data.errorMsg,
                showWidget = data.shouldShowWidget,
                isFromCache = isFromCache,
                lastUpdated = getLastUpdatedMillis(data.dataKey, isFromCache)
            )
        }
    }
}