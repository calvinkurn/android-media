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
        val mappedList = ArrayList<BaseWidgetUiModel<out BaseDataUiModel>>()
        widgetList.onEach {
            val widgetType = it.widgetType.orEmpty()
            if (WidgetType.isValidWidget(widgetType)) {
                mappedList.add(when (widgetType) {
                    WidgetType.CARD -> mapToCardWidget(it, isFromCache)
                    WidgetType.CAROUSEL -> mapToCarouselWidget(it, isFromCache)
                    WidgetType.DESCRIPTION -> mapToDescriptionWidget(it, isFromCache)
                    WidgetType.LINE_GRAPH -> mapToLineGraphWidget(it, isFromCache)
                    WidgetType.POST_LIST -> mapToPostWidget(it, isFromCache)
                    WidgetType.PROGRESS -> mapToProgressWidget(it, isFromCache)
                    WidgetType.TABLE -> mapToTableWidget(it, isFromCache)
                    WidgetType.PIE_CHART -> mapToPieChartWidget(it, isFromCache)
                    WidgetType.BAR_CHART -> mapToBarChartWidget(it, isFromCache)
                    WidgetType.MULTI_LINE_GRAPH -> mapToMultiLineGraphWidget(it, isFromCache)
                    WidgetType.ANNOUNCEMENT -> mapToAnnouncementWidget(it, isFromCache)
                    else -> mapToSectionWidget(it, isFromCache)
                })
            }
        }
        return mappedList
    }

    private fun mapToCardWidget(widget: WidgetModel, fromCache: Boolean): CardWidgetUiModel {
        return CardWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
                data = null,
                postFilter = widget.postFilter?.mapIndexed { i, filter ->
                    PostFilterUiModel(filter.name.orEmpty(), filter.value.orEmpty(), isSelected = i == 0)
                }.orEmpty(),
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
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
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = fromCache
        )
    }

    private fun mapToMultiLineGraphWidget(widget: WidgetModel, isFromCache: Boolean): MultiLineGraphWidgetUiModel {
        return MultiLineGraphWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = isFromCache
        )
    }

    private fun mapToAnnouncementWidget(widget: WidgetModel, isFromCache: Boolean): BaseWidgetUiModel<out BaseDataUiModel> {
        return AnnouncementWidgetUiModel(
                widgetType = widget.widgetType.orEmpty(),
                title = widget.title.orEmpty(),
                subtitle = widget.subtitle.orEmpty(),
                tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
                appLink = widget.appLink.orEmpty(),
                dataKey = widget.dataKey.orEmpty(),
                ctaText = widget.ctaText.orEmpty(),
                isShowEmpty = widget.isShowEmpty ?: false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = isFromCache
        )
    }
}