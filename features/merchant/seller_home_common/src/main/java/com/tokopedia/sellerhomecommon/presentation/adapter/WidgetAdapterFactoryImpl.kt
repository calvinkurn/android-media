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

    override fun type(cardWidget: CardWidgetUiModel): Int = CardViewHolder.RES_LAYOUT

    override fun type(carouselWidgetUiModel: CarouselWidgetUiModel): Int = CarouselViewHolder.RES_LAYOUT

    override fun type(lineGraphWidget: LineGraphWidgetUiModel): Int = LineGraphViewHolder.RES_LAYOUT

    override fun type(descriptionWidget: DescriptionWidgetUiModel): Int = DescriptionViewHolder.RES_LAYOUT

    override fun type(sectionWidget: SectionWidgetUiModel): Int = SectionViewHolder.RES_LAYOUT

    override fun type(postListWidget: PostListWidgetUiModel): Int = PostListViewHolder.RES_LAYOUT

    override fun type(progressWidgetWidget: ProgressWidgetUiModel): Int = ProgressViewHolder.RES_LAYOUT

    override fun type(tableWidgetUiModel: TableWidgetUiModel): Int = TableViewHolder.RES_LAYOUT

    override fun type(pieChartWidget: PieChartWidgetUiModel): Int = PieChartViewHolder.RES_LAYOUT

    override fun type(barChartWidget: BarChartWidgetUiModel): Int = BarChartViewHolder.RES_LAYOUT

    override fun type(footerLayout: WhiteSpaceUiModel): Int = WhiteSpaceViewHolder.RES_LAYOUT

    override fun type(tickerWidget: TickerWidgetUiModel): Int = TickerViewHolder.RES_LAYOUT

    override fun type(multiLineGraphWidget: MultiLineGraphWidgetUiModel): Int = MultiLineGraphViewHolder.RES_LAYOUT

    override fun type(announcementWidgetUiModel: AnnouncementWidgetUiModel): Int = AnnouncementViewHolder.RES_LAYOUT

    override fun type(recommendationWidget: RecommendationWidgetUiModel): Int = RecommendationViewHolder.RES_LAYOUT

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
            else -> super.createViewHolder(parent, type)
        }
    }
}