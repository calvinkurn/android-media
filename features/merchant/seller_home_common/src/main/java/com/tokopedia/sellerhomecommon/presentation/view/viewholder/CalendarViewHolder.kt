package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.CalendarWidgetUiModel

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

class CalendarViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<CalendarWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_calendar_widget
    }

    override fun bind(element: CalendarWidgetUiModel) {

    }

    interface Listener : BaseViewHolderListener {
        fun showCalendarWidgetDateFilter(element: CalendarWidgetUiModel) {}
    }
}