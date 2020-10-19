package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.domain.model.WidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/06/20
 */

class LayoutMapper @Inject constructor(private val tooltipMapper: TooltipMapper) {

    fun mapRemoteModelToUiModel(widgetList: List<WidgetModel>, isFromCache: Boolean): List<BaseWidgetUiModel<out BaseDataUiModel>> {
        return widgetList.filter {
            val widgetType = it.widgetType.orEmpty()
            return@filter WidgetType.isValidWidget(widgetType)
        }.map {
            return@map when (it.widgetType.orEmpty()) {
                WidgetType.CARD -> mapToCardWidget(it, isFromCache)
                WidgetType.CAROUSEL -> mapToCarouselWidget(it, isFromCache)
                WidgetType.DESCRIPTION -> mapToDescriptionWidget(it, isFromCache)
                WidgetType.LINE_GRAPH -> mapToLineGraphWidget(it, isFromCache)
                WidgetType.POST_LIST -> mapToPostWidget(it, isFromCache)
                WidgetType.PROGRESS -> mapToProgressWidget(it, isFromCache)
                WidgetType.TABLE -> mapToTableWidget(it, isFromCache)
                WidgetType.PIE_CHART -> mapToPieChartWidget(it, isFromCache)
                WidgetType.BAR_CHART -> mapToBarChartWidget(it, isFromCache)
                else -> mapToSectionWidget(it, isFromCache)
            }
        }
    }

    private fun mapToCardWidget(widget: WidgetModel, fromCache: Boolean): CardWidgetUiModel {
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
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToCarouselWidget(widget: WidgetModel, fromCache: Boolean): CarouselWidgetUiModel {
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
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToDescriptionWidget(widget: WidgetModel, fromCache: Boolean): DescriptionWidgetUiModel {
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
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToLineGraphWidget(widget: WidgetModel, fromCache: Boolean): LineGraphWidgetUiModel {
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
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToPostWidget(widget: WidgetModel, fromCache: Boolean): PostListWidgetUiModel {
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
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToProgressWidget(widget: WidgetModel, fromCache: Boolean): ProgressWidgetUiModel {
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
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToTableWidget(widget: WidgetModel, fromCache: Boolean): TableWidgetUiModel {
        return TableWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToSectionWidget(widget: WidgetModel, fromCache: Boolean): SectionWidgetUiModel {
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
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToPieChartWidget(widget: WidgetModel, fromCache: Boolean): PieChartWidgetUiModel {
        return PieChartWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToBarChartWidget(widget: WidgetModel, fromCache: Boolean): BarChartWidgetUiModel {
        return BarChartWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                url = widget.url.orEmpty(),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }
}