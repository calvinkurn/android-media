package com.tokopedia.journeydebugger.ui.viewholder

import android.view.View
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.journeydebugger.R
import com.tokopedia.journeydebugger.ui.model.JourneyDebuggerUIModel

class JourneyDebuggerViewHolder(itemView: View) : AbstractViewHolder<JourneyDebuggerUIModel>(itemView) {

    private val applinkName: TextView
    private val timestamp: TextView


    init {

        applinkName = itemView.findViewById(R.id.journey_text_name)
        timestamp = itemView.findViewById(R.id.journey_text_timestamp)
    }

    override fun bind(element: JourneyDebuggerUIModel) {
        applinkName.text = element.previewJourney
        timestamp.text = element.timestamp
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_journey_debugger
    }
}
