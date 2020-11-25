package com.tokopedia.sellerhomecommon.presentation.adapter

import com.tokopedia.sellerhomecommon.presentation.model.*

/**
 * Created By @ilhamsuaib on 19/05/20
 */

interface WidgetAdapterFactory {

    fun type(cardWidget: CardWidgetUiModel): Int

    fun type(carouselWidgetUiModel: CarouselWidgetUiModel): Int

    fun type(lineGraphWidget: LineGraphWidgetUiModel): Int

    fun type(descriptionWidget: DescriptionWidgetUiModel): Int

    fun type(sectionWidget: SectionWidgetUiModel): Int

    fun type(postListWidget: PostListWidgetUiModel): Int

    fun type(progressWidgetWidget: ProgressWidgetUiModel): Int

    fun type(tableWidgetUiModel: TableWidgetUiModel): Int

    fun type(pieChartWidget: PieChartWidgetUiModel): Int

    fun type(barChartWidget: BarChartWidgetUiModel): Int

    fun type(footerLayout: WhiteSpaceUiModel): Int

    fun type(tickerWidget: TickerWidgetUiModel): Int

    fun type(multiLineGraphWidget: MultiLineGraphWidgetUiModel): Int

    fun type(announcementWidgetUiModel: AnnouncementWidgetUiModel): Int
}