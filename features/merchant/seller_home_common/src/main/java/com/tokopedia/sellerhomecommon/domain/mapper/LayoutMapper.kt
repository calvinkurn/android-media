package com.tokopedia.sellerhomecommon.domain.mapper

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.asLowerCase
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.common.DataTemplateUtils
import com.tokopedia.sellerhomecommon.common.DismissibleState
import com.tokopedia.sellerhomecommon.common.EmptyLayoutException
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.WidgetGridSize
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.domain.model.TabModel
import com.tokopedia.sellerhomecommon.domain.model.WidgetModel
import com.tokopedia.sellerhomecommon.domain.usecase.GetLayoutUseCase
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.DescriptionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.FilterTabWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RichListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ShopStateUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetEmptyStateUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetFilterUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetLayoutUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/06/20
 */

class LayoutMapper @Inject constructor(
    private val tooltipMapper: TooltipMapper
) : BaseResponseMapper<GetLayoutResponse, WidgetLayoutUiModel> {

    companion object {
        private const val EMPTY_WIDGET_MESSAGE =
            "Oops, kamu tidak punya izin untuk melihat kontent di halaman Home"
        private const val DOUBLE_DOTS_CALENDAR_TITLE = ":"

        private const val TAB_TAG = "{tab}"
        private const val INVALID_SECTION_WIDGET_ID = "0"
    }

    override fun mapRemoteDataToUiData(
        response: GetLayoutResponse,
        isFromCache: Boolean
    ): WidgetLayoutUiModel {
        val widgets = response.layout.widget.orEmpty()
        if (widgets.isNotEmpty()) {
            val mappedList = getMappedWidgetList(widgets, isFromCache)
            return WidgetLayoutUiModel(
                widgetList = mappedList,
                shopState = getShopState(response.layout.shopState),
                personaStatus = response.layout.personaStatus.orZero()
            )
        } else {
            throw EmptyLayoutException(EMPTY_WIDGET_MESSAGE)
        }
    }

    override fun mapRemoteDataToUiData(
        response: GetLayoutResponse,
        isFromCache: Boolean,
        extra: Map<String, Any?>
    ): WidgetLayoutUiModel {
        val isContainKeyPage = extra[GetLayoutUseCase.KEY_PAGE] != null
        return if (isContainKeyPage) {
            val selectedPageExtra = extra[GetLayoutUseCase.KEY_PAGE]
            val widgets = response.layout.widget.orEmpty()
            if (widgets.isNotEmpty()) {
                var mappedList = getMappedWidgetList(widgets, isFromCache)
                (selectedPageExtra as? String)?.let { selectedPage ->
                    response.layout.tabs?.takeIf { it.isNotEmpty() }?.let {
                        val selectedTab = it.firstOrNull { tab ->
                            tab.page == selectedPage
                        }
                        val filterTabUiModel = mapToFilterTabWidget(it, selectedTab, isFromCache)
                        mappedList.add(Int.ZERO, filterTabUiModel)
                    }
                }

                val tableSort = extra[GetLayoutUseCase.KEY_TABLE_SORT] as? String
                if (!tableSort.isNullOrBlank()) {
                    mappedList = mappedList.updateTableWidget(tableSort)
                }
                WidgetLayoutUiModel(
                    widgetList = mappedList,
                    shopState = getShopState(response.layout.shopState),
                    personaStatus = response.layout.personaStatus.orZero()
                )
            } else {
                throw EmptyLayoutException(EMPTY_WIDGET_MESSAGE)
            }
        } else {
            mapRemoteDataToUiData(response, isFromCache)
        }
    }

    private fun getWidgetByWidgetType(
        widgetType: String,
        widget: WidgetModel,
        isFromCache: Boolean
    ): BaseWidgetUiModel<out BaseDataUiModel>? {
        return when (widgetType.asLowerCase()) {
            WidgetType.CARD.asLowerCase() -> mapToCardWidget(widget, isFromCache)
            WidgetType.CAROUSEL.asLowerCase() -> mapToCarouselWidget(widget, isFromCache)
            WidgetType.DESCRIPTION.asLowerCase() -> mapToDescriptionWidget(widget, isFromCache)
            WidgetType.LINE_GRAPH.asLowerCase() -> mapToLineGraphWidget(widget, isFromCache)
            WidgetType.POST_LIST.asLowerCase() -> mapToPostWidget(widget, isFromCache)
            WidgetType.PROGRESS.asLowerCase() -> mapToProgressWidget(widget, isFromCache)
            WidgetType.TABLE.asLowerCase() -> mapToTableWidget(widget, isFromCache)
            WidgetType.PIE_CHART.asLowerCase() -> mapToPieChartWidget(widget, isFromCache)
            WidgetType.BAR_CHART.asLowerCase() -> mapToBarChartWidget(widget, isFromCache)
            WidgetType.MULTI_LINE_GRAPH.asLowerCase() -> {
                mapToMultiLineGraphWidget(widget, isFromCache)
            }

            WidgetType.ANNOUNCEMENT.asLowerCase() -> mapToAnnouncementWidget(widget, isFromCache)
            WidgetType.RECOMMENDATION.asLowerCase() -> mapToRecommendationWidget(
                widget,
                isFromCache
            )

            WidgetType.MILESTONE.asLowerCase() -> mapToMilestoneWidget(widget, isFromCache)
            WidgetType.CALENDAR.asLowerCase() -> mapToCalendarWidget(widget, isFromCache)
            WidgetType.UNIFICATION.asLowerCase() -> mapToUnificationWidget(widget, isFromCache)
            WidgetType.RICH_LIST.asLowerCase() -> mapToRichListWidget(widget, isFromCache)
            WidgetType.SECTION.asLowerCase() -> {
                if (widget.title.isNullOrBlank() && widget.subtitle.isNullOrBlank()) {
                    null
                } else {
                    mapToSectionWidget(widget, isFromCache)
                }
            }
            WidgetType.MULTI_COMPONENT.asLowerCase() -> {
                mapToMultiComponentWidget(widget, isFromCache)
            }

            else -> null
        }
    }

    private fun getShopState(shopState: Long): ShopStateUiModel {
        return when (shopState) {
            ShopStateUiModel.NEW_REGISTERED_SHOP -> ShopStateUiModel.NewRegisteredShop
            ShopStateUiModel.ADDED_PRODUCT -> ShopStateUiModel.AddedProduct
            ShopStateUiModel.VIEWED_PRODUCT -> ShopStateUiModel.ViewedProduct
            ShopStateUiModel.HAS_ORDER -> ShopStateUiModel.HasOrder
            else -> ShopStateUiModel.None
        }
    }

    private fun mapToRichListWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): RichListWidgetUiModel {
        return RichListWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            isFromCache = isFromCache,
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToUnificationWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): UnificationWidgetUiModel {
        return UnificationWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            isFromCache = isFromCache,
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToCardWidget(widget: WidgetModel, fromCache: Boolean): CardWidgetUiModel {
        return CardWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToCarouselWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): CarouselWidgetUiModel {
        return CarouselWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToDescriptionWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): DescriptionWidgetUiModel {
        return DescriptionWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToLineGraphWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): LineGraphWidgetUiModel {
        return LineGraphWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToPostWidget(widget: WidgetModel, fromCache: Boolean): PostListWidgetUiModel {
        return PostListWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            maxDisplay = if (widget.maxDisplay.isMoreThanZero()) {
                widget.maxDisplay.orZero()
            } else {
                PostMapper.MAX_ITEM_PER_PAGE
            },
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            isDismissible = widget.isDismissible,
            dismissibleState = when (widget.dismissibleState) {
                DismissibleState.ALWAYS.value -> DismissibleState.ALWAYS
                DismissibleState.TRIGGER.value -> DismissibleState.TRIGGER
                else -> DismissibleState.NONE
            },
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToProgressWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): ProgressWidgetUiModel {
        return ProgressWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToTableWidget(widget: WidgetModel, fromCache: Boolean): TableWidgetUiModel {
        return TableWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            }.orEmpty(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToSectionWidget(widget: WidgetModel, fromCache: Boolean): SectionWidgetUiModel {
        return SectionWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty(),
            subtitle = DataTemplateUtils.parseDateTemplate(widget.subtitle.orEmpty()),
            tooltip = tooltipMapper.mapRemoteModelToUiModel(widget.tooltip),
            tag = widget.tag.orEmpty(),
            appLink = widget.appLink.orEmpty(),
            dataKey = widget.id.orZero().toString(),
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
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToBarChartWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): BarChartWidgetUiModel {
        return BarChartWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToMultiLineGraphWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): MultiLineGraphWidgetUiModel {
        return MultiLineGraphWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            isComparePeriodOnly = widget.isComparePeriodOnly,
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToAnnouncementWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): AnnouncementWidgetUiModel {
        return AnnouncementWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            isDismissible = widget.isDismissible,
            dismissibleState = when (widget.dismissibleState) {
                DismissibleState.ALWAYS.value -> DismissibleState.ALWAYS
                DismissibleState.TRIGGER.value -> DismissibleState.TRIGGER
                else -> DismissibleState.NONE
            },
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToRecommendationWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): RecommendationWidgetUiModel {
        return RecommendationWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToMilestoneWidget(
        widget: WidgetModel,
        isFromCache: Boolean
    ): MilestoneWidgetUiModel {
        return MilestoneWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToCalendarWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): CalendarWidgetUiModel {
        return CalendarWidgetUiModel(
            id = widget.id.toString(),
            sectionId = widget.sectionId.toString(),
            widgetType = widget.widgetType.orEmpty(),
            title = widget.title.orEmpty() + DOUBLE_DOTS_CALENDAR_TITLE,
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime
        )
    }

    private fun mapToMultiComponentWidget(
        widget: WidgetModel,
        fromCache: Boolean
    ): MultiComponentWidgetUiModel {
        return MultiComponentWidgetUiModel(
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
            emptyState = widget.emptyStateModel.mapToUiModel(),
            useRealtime = widget.useRealtime,
            sectionId = widget.sectionId.toString()
        )
    }

    private fun mapToFilterTabWidget(
        tabs: List<TabModel>,
        selectedFilterTab: TabModel?,
        fromCache: Boolean
    ): FilterTabWidgetUiModel {
        return FilterTabWidgetUiModel(
            id = String.EMPTY,
            widgetType = FilterTabWidgetUiModel.WIDGET_TYPE,
            title = String.EMPTY,
            subtitle = String.EMPTY,
            tooltip = null,
            tag = String.EMPTY,
            appLink = String.EMPTY,
            dataKey = String.EMPTY,
            ctaText = String.EMPTY,
            gridSize = WidgetGridSize.GRID_SIZE_4,
            isShowEmpty = false,
            data = null,
            isLoaded = false,
            isLoading = false,
            isFromCache = fromCache,
            emptyState = WidgetEmptyStateUiModel(),
            useRealtime = false,
            filterTabs = tabs,
            selectedFilterPage = selectedFilterTab?.page,
            filterTabMessage = getReplacementText(
                selectedFilterTab?.tabTitle.orEmpty(),
                selectedFilterTab?.tabName.orEmpty()
            )
        )
    }

    private fun getGridSize(gridSize: Int, defaultGridSize: Int): Int {
        return if (gridSize == WidgetGridSize.GRID_SIZE_0) {
            defaultGridSize
        } else {
            gridSize
        }
    }

    private fun getMappedWidgetList(
        widgets: List<WidgetModel>,
        isFromCache: Boolean
    ): ArrayList<BaseWidgetUiModel<out BaseDataUiModel>> {
        val mappedList = ArrayList<BaseWidgetUiModel<out BaseDataUiModel>>()
        widgets.forEach { widget ->
            val widgetType = widget.widgetType.orEmpty()
            if (WidgetType.isValidWidget(widgetType)) {
                val mappedWidget = getWidgetByWidgetType(widgetType, widget, isFromCache)
                mappedWidget?.let {
                    mappedList.add(it)
                }
            }
        }
        return removeEmptySection(mappedList)
    }

    private fun ArrayList<BaseWidgetUiModel<out BaseDataUiModel>>.updateTableWidget(selectedSort: String): ArrayList<BaseWidgetUiModel<out BaseDataUiModel>> {
        val mappedList = map { widget ->
            if (widget is TableWidgetUiModel) {
                widget.copy(
                    tableFilters = widget.tableFilters.updateSelectedSort(selectedSort)
                )
            } else {
                widget
            }
        }
        return ArrayList(mappedList)
    }

    private fun List<WidgetFilterUiModel>.updateSelectedSort(selectedSort: String): List<WidgetFilterUiModel> {
        val selectedIndex = indexOfFirst { it.value == selectedSort }
        val shouldUpdateFilter = selectedIndex > Int.ZERO // This means either the filter is not found or already at the first index
        return if (shouldUpdateFilter) {
            val mappedFilters = this.map {
                it.copy(
                    isSelected = it.value == selectedSort
                )
            }
            return mappedFilters
        } else {
            this
        }
    }

    /**
     * Remove the empty section widget :
     * in case, we have a section widget without child or
     * there is a section with invalid widget type (not yet registered).
     * So, it should be removed to avoid stacked section widget.
     * */
    private fun removeEmptySection(widgetList: ArrayList<BaseWidgetUiModel<out BaseDataUiModel>>): ArrayList<BaseWidgetUiModel<out BaseDataUiModel>> {
        val widgetGroups: Map<String, List<BaseWidgetUiModel<*>>> = widgetList.groupBy {
            it.sectionId
        }
        val emptySection = mutableListOf<SectionWidgetUiModel>()
        widgetList.forEach { widget ->
            (widget as? SectionWidgetUiModel)?.let { section ->
                val sectionWidgetId = section.id
                val isSectionEmpty = widgetGroups[sectionWidgetId].isNullOrEmpty()
                if (isSectionEmpty && sectionWidgetId != INVALID_SECTION_WIDGET_ID) {
                    emptySection.add(section)
                }
            }
        }
        if (emptySection.isEmpty()) return widgetList

        emptySection.forEach {
            val sectionIndex = widgetList.indexOf(it)
            if (sectionIndex != RecyclerView.NO_POSITION) {
                widgetList.remove(it)
            }
        }

        return widgetList
    }

    private fun getReplacementText(tabTitle: String, tabName: String): String {
        val replacementString = "<b>$tabName</b>"
        return tabTitle.replace(TAB_TAG, replacementString)
    }
}
