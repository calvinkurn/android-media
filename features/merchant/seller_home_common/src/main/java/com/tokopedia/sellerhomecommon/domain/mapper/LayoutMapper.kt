package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.common.EmptyLayoutException
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.WidgetGridSize
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.domain.model.WidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.DescriptionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetFilterUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/06/20
 */

class LayoutMapper @Inject constructor(
    private val tooltipMapper: TooltipMapper
) : BaseResponseMapper<GetLayoutResponse, List<BaseWidgetUiModel<out BaseDataUiModel>>> {

    companion object {
        private const val EMPTY_WIDGET_MESSAGE =
            "Oops, kamu tidak punya izin untuk melihat kontent di halaman Home"
    }

    override fun mapRemoteDataToUiData(
        response: GetLayoutResponse,
        isFromCache: Boolean
    ): List<BaseWidgetUiModel<out BaseDataUiModel>> {
        val widgets = response.layout?.widget.orEmpty()
        if (widgets.isNotEmpty()) {
            val mappedList = ArrayList<BaseWidgetUiModel<out BaseDataUiModel>>()
            widgets.forEach {
                val widgetType = it.widgetType.orEmpty()
                if (WidgetType.isValidWidget(widgetType)) {
                    mappedList.add(
                        when (widgetType) {
                            WidgetType.CARD -> mapToCardWidget(it, isFromCache)
                            WidgetType.CAROUSEL -> mapToCarouselWidget(it, isFromCache)
                            WidgetType.DESCRIPTION -> mapToDescriptionWidget(it, isFromCache)
                            WidgetType.LINE_GRAPH -> mapToLineGraphWidget(it, isFromCache)
                            WidgetType.POST_LIST -> mapToPostWidget(it, isFromCache)
                            WidgetType.PROGRESS -> mapToProgressWidget(it, isFromCache)
                            WidgetType.TABLE -> mapToTableWidget(it, isFromCache)
                            WidgetType.PIE_CHART -> mapToPieChartWidget(it, isFromCache)
                            WidgetType.BAR_CHART -> mapToBarChartWidget(it, isFromCache)
                            WidgetType.MULTI_LINE_GRAPH -> mapToMultiLineGraphWidget(
                                it,
                                isFromCache
                            )
                            WidgetType.ANNOUNCEMENT -> mapToAnnouncementWidget(it, isFromCache)
                            WidgetType.RECOMMENDATION -> mapToRecommendationWidget(it, isFromCache)
                            WidgetType.MILESTONE -> mapToMilestoneWidget(it, isFromCache)
                            WidgetType.CALENDAR -> mapToCalendarWidget(it, isFromCache)
                            else -> mapToSectionWidget(it, isFromCache)
                        }
                    )
                }
            }
            return mappedList
        } else throw EmptyLayoutException(EMPTY_WIDGET_MESSAGE)
    }

    private fun mapToCardWidget(widget: WidgetModel, fromCache: Boolean): CardWidgetUiModel {
        return CardWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_1),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToCarouselWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): CarouselWidgetUiModel {
        return CarouselWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = null,
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToDescriptionWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): DescriptionWidgetUiModel {
        return DescriptionWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToLineGraphWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): LineGraphWidgetUiModel {
        return LineGraphWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToPostWidget(widget: WidgetModel, fromCache: Boolean): PostListWidgetUiModel {
        return PostListWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            maxData = widget.maxData.orZero(),
            maxDisplay = widget.maxDisplay.orZero(),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            postFilter = widget.postFilter?.mapIndexed { i, filter ->
                WidgetFilterUiModel(
                    filter.name.orEmpty(),
                    filter.value.orEmpty(),
                    isSelected = i.isZero()
                )
            }.orEmpty(),
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToProgressWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): ProgressWidgetUiModel {
        return ProgressWidgetUiModel(
            id = (widget.id ?: 0L).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
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
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            maxData = widget.maxData.orZero(),
            maxDisplay = widget.maxDisplay.orZero(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel(),
            tableFilters = widget.searchTableColumnFilters?.mapIndexed { i, filter ->
                WidgetFilterUiModel(
                    filter.name.orEmpty(),
                    filter.value.orEmpty(),
                    isSelected = i.isZero()
                )
            }.orEmpty()
        )
    }

    private fun mapToSectionWidget(widget: WidgetModel, fromCache: Boolean): SectionWidgetUiModel {
        return SectionWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = WidgetGridSize.GRID_SIZE_4,
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToPieChartWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): PieChartWidgetUiModel {
        return PieChartWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_2),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToBarChartWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): BarChartWidgetUiModel {
        return BarChartWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToMultiLineGraphWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): MultiLineGraphWidgetUiModel {
        return MultiLineGraphWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_2),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = isFromCache,
            emptyState = widget.emptyStateModel.mapToUiModel(),
            isComparePeriodeOnly = widget.isComparePeriodeOnly
        )
    }

    private fun mapToAnnouncementWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): AnnouncementWidgetUiModel {
        return AnnouncementWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = isFromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToRecommendationWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): RecommendationWidgetUiModel {
        return RecommendationWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = isFromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToMilestoneWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): MilestoneWidgetUiModel {
        return MilestoneWidgetUiModel(
            id = (widget.id ?: 0L).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = isFromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun mapToCalendarWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): CalendarWidgetUiModel {
        return CalendarWidgetUiModel(
            id = (widget.id.orZero()).toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = widget.subtitle.orEmpty(),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.dataKey.orEmpty(),
            ctaText = widget.ctaText.orEmpty(),
            gridSize = getGridSize(widget.gridSize.orZero(), WidgetGridSize.GRID_SIZE_4),
            isShowEmpty = widget.isShowEmpty.orFalse(),
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = widget.emptyStateModel.mapToUiModel()
        )
    }

    private fun getGridSize(gridSize: Int, defaultGridSize: Int): Int {
        return if (gridSize == WidgetGridSize.GRID_SIZE_0) {
            defaultGridSize
        } else {
            gridSize
        }
    }
}