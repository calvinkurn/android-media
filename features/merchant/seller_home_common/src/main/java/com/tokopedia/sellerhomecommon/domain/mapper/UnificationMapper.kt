package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetUnificationDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.UnificationDataUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

class UnificationMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean
) : BaseResponseMapper<GetUnificationDataResponse, List<UnificationDataUiModel>> {

    override fun mapRemoteDataToUiData(
        response: GetUnificationDataResponse,
        isFromCache: Boolean
    ): List<UnificationDataUiModel> {
        return listOf()
    }
}