package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokomart.home.constant.HomeAdditionalWidgetId
import com.tokopedia.tokomart.home.constant.HomeLayoutType
import com.tokopedia.tokomart.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.tokomart.home.domain.mapper.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeChooseAddressWidgetUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel

object HomeLayoutMapper {

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        HomeLayoutType.LEGO_3_IMAGE,
        HomeLayoutType.BANNER_CAROUSEL
    )

    val ADDITIONAL_WIDGETS = listOf(
        HomeAdditionalWidgetId.CHOOSE_ADDRESS_WIDGET_ID,
        HomeAdditionalWidgetId.TICKER_WIDGET_ID
    )

    fun mapHomeLayoutList(response: List<HomeLayoutResponse>): List<Visitable<*>> {
        val layoutList = mutableListOf<Visitable<*>>()
        layoutList.add(HomeChooseAddressWidgetUiModel(id = HomeAdditionalWidgetId.CHOOSE_ADDRESS_WIDGET_ID))

        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach {
            val item = mapToHomeUiModel(it)
            layoutList.add(item)
        }

        return layoutList
    }

    fun mapHomeLayoutData(layoutList: List<Visitable<*>>, response: HomeLayoutResponse): List<Visitable<*>> {
        val item = mapToHomeUiModel(response)
        return layoutList.getItemIndex(item)?.let { index ->
            layoutList.toMutableList().let {
                it[index] = item
                it
            }
        } ?: layoutList
    }

    private fun mapToHomeUiModel(response: HomeLayoutResponse): Visitable<*> {
        return when (response.layout) {
            HomeLayoutType.LEGO_3_IMAGE -> mapLegoBannerDataModel(response)
            HomeLayoutType.BANNER_CAROUSEL -> mapSliderBannerModel(response)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }

    private fun List<Visitable<*>>.getItemIndex(item: Visitable<*>): Int? {
        return firstOrNull { it.getChannelId() == item.getChannelId() }?.let { indexOf(it) }
    }

    private fun Visitable<*>.getChannelId(): String? {
        return when (this) {
            is TokoMartHomeLayoutUiModel -> channelId
            is HomeComponentVisitable -> visitableId()
            else -> throw TypeNotSupportedException.create("Component not supported")
        }
    }
}
