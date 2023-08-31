package com.tokopedia.sellerhomecommon.presentation.adapter

import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.DescriptionWidgetUiModel
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
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationWidgetUiModel

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

    fun type(tickerWidget: TickerWidgetUiModel): Int

    fun type(multiLineGraphWidget: MultiLineGraphWidgetUiModel): Int

    fun type(announcementWidgetUiModel: AnnouncementWidgetUiModel): Int

    fun type(recommendationWidget: RecommendationWidgetUiModel): Int

    fun type(milestoneWidgetUiModel: MilestoneWidgetUiModel): Int

    fun type(calendarWidgetUiModel: CalendarWidgetUiModel): Int

    fun type(unificationWidgetUiModel: UnificationWidgetUiModel): Int

    fun type(richListWidget: RichListWidgetUiModel): Int

    fun type(multiComponentWidget: MultiComponentWidgetUiModel): Int
}
