package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.*

/**
 * Created By @ilhamsuaib on 19/05/20
 */

class WidgetAdapterFactoryImpl(
    private val listener: WidgetListener
) : BaseAdapterTypeFactory(), WidgetAdapterFactory {

    override fun type(cardWidget: CardWidgetUiModel): Int {
        return CardViewHolder.RES_LAYOUT
    }

    override fun type(carouselWidgetUiModel: CarouselWidgetUiModel): Int {
        return CarouselViewHolder.RES_LAYOUT
    }

    override fun type(lineGraphWidget: LineGraphWidgetUiModel): Int {
        return LineGraphViewHolder.RES_LAYOUT
    }

    override fun type(descriptionWidget: DescriptionWidgetUiModel): Int {
        return DescriptionViewHolder.RES_LAYOUT
    }

    override fun type(sectionWidget: SectionWidgetUiModel): Int {
        return SectionViewHolder.RES_LAYOUT
    }

    override fun type(postListWidget: PostListWidgetUiModel): Int {
        return PostListViewHolder.RES_LAYOUT
    }

    override fun type(progressWidgetWidget: ProgressWidgetUiModel): Int {
        return ProgressViewHolder.RES_LAYOUT
    }

    override fun type(tableWidgetUiModel: TableWidgetUiModel): Int {
        return TableViewHolder.RES_LAYOUT
    }

    override fun type(pieChartWidget: PieChartWidgetUiModel): Int {
        return PieChartViewHolder.RES_LAYOUT
    }

    override fun type(barChartWidget: BarChartWidgetUiModel): Int {
        return BarChartViewHolder.RES_LAYOUT
    }

    override fun type(footerLayout: WhiteSpaceUiModel): Int {
        return WhiteSpaceViewHolder.RES_LAYOUT
    }

    override fun type(tickerWidget: TickerWidgetUiModel): Int {
        return TickerViewHolder.RES_LAYOUT
    }

    override fun type(multiLineGraphWidget: MultiLineGraphWidgetUiModel): Int {
        return MultiLineGraphViewHolder.RES_LAYOUT
    }

    override fun type(announcementWidgetUiModel: AnnouncementWidgetUiModel): Int {
        return AnnouncementViewHolder.RES_LAYOUT
    }

    override fun type(recommendationWidget: RecommendationWidgetUiModel): Int {
        return RecommendationViewHolder.RES_LAYOUT
    }

    override fun type(milestoneWidgetUiModel: MilestoneWidgetUiModel): Int {
        return MilestoneViewHolder.RES_LAYOUT
    }

    override fun type(calendarWidgetUiModel: CalendarWidgetUiModel): Int {
        return CalendarViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SectionViewHolder.RES_LAYOUT -> SectionViewHolder(parent, listener)
            CardViewHolder.RES_LAYOUT -> CardViewHolder(parent, listener)
            LineGraphViewHolder.RES_LAYOUT -> LineGraphViewHolder(parent, listener)
            CarouselViewHolder.RES_LAYOUT -> CarouselViewHolder(parent, listener)
            DescriptionViewHolder.RES_LAYOUT -> DescriptionViewHolder(parent, listener)
            ProgressViewHolder.RES_LAYOUT -> ProgressViewHolder(parent, listener)
            PostListViewHolder.RES_LAYOUT -> PostListViewHolder(parent, listener)
            TableViewHolder.RES_LAYOUT -> TableViewHolder(parent, listener)
            PieChartViewHolder.RES_LAYOUT -> PieChartViewHolder(parent, listener)
            BarChartViewHolder.RES_LAYOUT -> BarChartViewHolder(parent, listener)
            WhiteSpaceViewHolder.RES_LAYOUT -> WhiteSpaceViewHolder(parent)
            TickerViewHolder.RES_LAYOUT -> TickerViewHolder(parent, listener)
            MultiLineGraphViewHolder.RES_LAYOUT -> MultiLineGraphViewHolder(parent, listener)
            AnnouncementViewHolder.RES_LAYOUT -> AnnouncementViewHolder(parent, listener)
            RecommendationViewHolder.RES_LAYOUT -> RecommendationViewHolder(parent, listener)
            MilestoneViewHolder.RES_LAYOUT -> MilestoneViewHolder(parent, listener)
            CalendarViewHolder.RES_LAYOUT -> CalendarViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}