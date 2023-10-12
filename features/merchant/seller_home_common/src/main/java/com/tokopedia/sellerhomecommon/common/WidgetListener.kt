package com.tokopedia.sellerhomecommon.common

import com.tokopedia.sellerhomecommon.presentation.view.viewholder.AnnouncementViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.BarChartViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.CalendarViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.CardViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.CarouselViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.DescriptionViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.FilterTabViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.LineGraphViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.MilestoneViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.MultiLineGraphViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.PieChartViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.PostListViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.ProgressViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RecommendationViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.SectionViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TickerViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.UnificationViewHolder

/**
 * Created By @ilhamsuaib on 19/05/20
 */

interface WidgetListener : CardViewHolder.Listener, CarouselViewHolder.Listener,
    SectionViewHolder.Listener, LineGraphViewHolder.Listener, ProgressViewHolder.Listener,
    PostListViewHolder.Listener, DescriptionViewHolder.Listener, TableViewHolder.Listener,
    PieChartViewHolder.Listener, BarChartViewHolder.Listener, TickerViewHolder.Listener,
    MultiLineGraphViewHolder.Listener, AnnouncementViewHolder.Listener,
    RecommendationViewHolder.Listener, MilestoneViewHolder.Listener, CalendarViewHolder.Listener,
    UnificationViewHolder.Listener, RichListViewHolder.Listener, FilterTabViewHolder.Listener
