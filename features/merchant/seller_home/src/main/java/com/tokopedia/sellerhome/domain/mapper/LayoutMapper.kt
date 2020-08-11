package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.common.WidgetType
import com.tokopedia.sellerhome.domain.model.WidgetModel
import com.tokopedia.sellerhome.view.model.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-02-05
 */

class LayoutMapper @Inject constructor(private val tooltipMapper: TooltipMapper) {

    fun mapRemoteModelToUiModel(widgetList: List<WidgetModel>): List<BaseWidgetUiModel<out BaseDataUiModel>> {
        return widgetList.filter {
            val widgetType = it.widgetType.orEmpty()
            return@filter WidgetType.isValidWidget(widgetType)
        }.map {
            return@map when (it.widgetType.orEmpty()) {
                WidgetType.CARD -> mapToCardWidget(it)
                WidgetType.CAROUSEL -> mapToCarouselWidget(it)
                WidgetType.DESCRIPTION -> mapToDescriptionWidget(it)
                WidgetType.LINE_GRAPH -> mapToLineGraphWidget(it)
                WidgetType.POST_LIST -> mapToPostWidget(it)
                WidgetType.PROGRESS -> mapToProgressWidget(it)
                else -> mapToSectionWidget(it)
            }
        }
    }

    private fun mapToCardWidget(widget: WidgetModel): CardWidgetUiModel {
        return CardWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false
        )
    }

    private fun mapToCarouselWidget(widget: WidgetModel): CarouselWidgetUiModel {
        return CarouselWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = null,
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false
        )
    }

    private fun mapToDescriptionWidget(widget: WidgetModel): DescriptionWidgetUiModel {
        return DescriptionWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false
        )
    }

    private fun mapToLineGraphWidget(widget: WidgetModel): LineGraphWidgetUiModel {
        return LineGraphWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false
        )
    }

    private fun mapToPostWidget(widget: WidgetModel): PostListWidgetUiModel {
        return PostListWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false
        )
    }

    private fun mapToProgressWidget(widget: WidgetModel): ProgressWidgetUiModel {
        return ProgressWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false
        )
    }

    private fun mapToSectionWidget(widget: WidgetModel): SectionWidgetUiModel {
        return SectionWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false
        )
    }
}