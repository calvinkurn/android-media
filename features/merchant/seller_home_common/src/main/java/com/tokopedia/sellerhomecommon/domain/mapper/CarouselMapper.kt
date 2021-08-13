package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.domain.model.CarouselItemModel
import com.tokopedia.sellerhomecommon.domain.model.GetCarouselDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CarouselDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class CarouselMapper @Inject constructor(): BaseResponseMapper<GetCarouselDataResponse, List<CarouselDataUiModel>> {

    override fun mapRemoteDataToUiData(response: GetCarouselDataResponse, isFromCache: Boolean): List<CarouselDataUiModel> {
        return response.carouselData.data.map {
            CarouselDataUiModel(
                    dataKey = it.dataKey,
                    items = it.items.map { item ->
                        mapItemRemoteModelToItemUiModel(item)
                    },
                    error = it.errorMsg,
                    isFromCache = isFromCache,
                    showWidget = it.showWidget
            )
        }
    }

    private fun mapItemRemoteModelToItemUiModel(item: CarouselItemModel): CarouselItemUiModel {
        return CarouselItemUiModel(
                id = item.id,
                url = item.url,
                creativeName = item.creativeName,
                appLink = item.appLink,
                featuredMediaURL = item.mediaUrl,
                impressHolder = ImpressHolder()
        )
    }
}