package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokopedianow.common.domain.model.GetHomeBannerV2DataResponse.GetHomeBannerV2Response
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.removeItem
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel

object HomeBannerMapper {

    fun MutableList<HomeLayoutItemUiModel?>.mapHomeBanner(
        item: BannerDataModel,
        response: GetHomeBannerV2Response
    ) {
        if(response.banners.isNotEmpty()) {
            updateItemById(item.visitableId()) {
                val channelModel = item.channelModel?.mapChannelGrids(response)
                val bannerDataModel = item.copy(channelModel = channelModel)
                HomeLayoutItemUiModel(bannerDataModel, HomeLayoutItemState.LOADED)
            }
        } else {
            removeItem(item.visitableId())
        }
    }

    private fun ChannelModel.mapChannelGrids(response: GetHomeBannerV2Response): ChannelModel {
        return copy(channelGrids = response.banners.map {
            ChannelGrid(
                id = it.id,
                imageUrl = it.imageUrl,
                applink = it.applink,
                url = it.url,
                attribution = it.title,
                backColor = it.backColor
            )
        })
    }
}
