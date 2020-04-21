package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.domain.model.CarouselDataModel
import com.tokopedia.sellerhome.domain.model.CarouselItemModel
import com.tokopedia.sellerhome.view.model.CarouselDataUiModel
import com.tokopedia.sellerhome.view.model.CarouselItemUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-02-11
 */

class CarouselMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(list: List<CarouselDataModel>): List<CarouselDataUiModel> {
        return list.map {
            CarouselDataUiModel(
                    dataKey = it.dataKey,
                    items = it.items.map { item ->
                        mapItemRemoteModelToItemUiModel(item)
                    },
                    error = it.errorMsg
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