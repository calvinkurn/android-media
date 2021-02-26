package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.GetAnnouncementDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementDataUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/11/20
 */

class AnnouncementMapper @Inject constructor() : BaseResponseMapper<GetAnnouncementDataResponse, List<AnnouncementDataUiModel>> {
    override fun mapRemoteDataToUiData(response: GetAnnouncementDataResponse, isFromCache: Boolean): List<AnnouncementDataUiModel> {
        return response.fetchAnnouncementWidgetData?.data.orEmpty().map {
            AnnouncementDataUiModel(
                    dataKey = it.dataKey.orEmpty(),
                    error = it.errorMsg.orEmpty(),
                    title = it.subtitle.orEmpty(),
                    subtitle = it.subtitle.orEmpty(),
                    appLink = it.cta?.appLink.orEmpty(),
                    imgUrl = it.imageUrl.orEmpty(),
                    isFromCache = isFromCache
            )
        }
    }
}