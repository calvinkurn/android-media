package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.domain.model.WidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/06/20
 */

class LayoutMapper @Inject constructor(private val tooltipMapper: TooltipMapper): BaseResponseMapper<GetLayoutResponse, List<BaseWidgetUiModel<out BaseDataUiModel>>> {

    private fun mapToCardWidget(widget: WidgetModel, fromCache: Boolean): CardWidgetUiModel {
        return CardWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToCarouselWidget(widget: WidgetModel, fromCache: Boolean): CarouselWidgetUiModel {
        return CarouselWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToDescriptionWidget(widget: WidgetModel, fromCache: Boolean): DescriptionWidgetUiModel {
        return DescriptionWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToLineGraphWidget(widget: WidgetModel, fromCache: Boolean): LineGraphWidgetUiModel {
        return LineGraphWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToPostWidget(widget: WidgetModel, fromCache: Boolean): PostListWidgetUiModel {
        return PostListWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToProgressWidget(widget: WidgetModel, fromCache: Boolean): ProgressWidgetUiModel {
        return ProgressWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToTableWidget(widget: WidgetModel, fromCache: Boolean): TableWidgetUiModel {
        return TableWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel(),
                tableFilters = widget.searchTableColumnFilters?.mapIndexed { i, filter ->
                    TableFilterUiModel(filter.name.orEmpty(), filter.value.orEmpty(), isSelected = i == 0)
                }.orEmpty()
        )
    }

    private fun mapToSectionWidget(widget: WidgetModel, fromCache: Boolean): SectionWidgetUiModel {
        return SectionWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToPieChartWidget(widget: WidgetModel, fromCache: Boolean): PieChartWidgetUiModel {
        return PieChartWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToBarChartWidget(widget: WidgetModel, fromCache: Boolean): BarChartWidgetUiModel {
        return BarChartWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = fromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToMultiLineGraphWidget(widget: WidgetModel, isFromCache: Boolean): MultiLineGraphWidgetUiModel {
        return MultiLineGraphWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = isFromCache,
                emptyState = widget.emptyStateModel.mapToUiModel(),
                isComparePeriodeOnly = widget.isComparePeriodeOnly
        )
    }

    private fun mapToAnnouncementWidget(widget: WidgetModel, isFromCache: Boolean): BaseWidgetUiModel<out BaseDataUiModel> {
        return AnnouncementWidgetUiModel(
                id = (widget.id ?: 0L).toString(),
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
                isFromCache = isFromCache,
                emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    override fun mapRemoteDataToUiData(response: GetLayoutResponse, isFromCache: Boolean): List<BaseWidgetUiModel<out BaseDataUiModel>> {
        val widgets = response.layout?.widget.orEmpty()
        if (widgets.isNotEmpty()) {
            val mappedList = ArrayList<BaseWidgetUiModel<out BaseDataUiModel>>()
            widgets.onEach {
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
        } else throw RuntimeException("no widget found")
    }
}