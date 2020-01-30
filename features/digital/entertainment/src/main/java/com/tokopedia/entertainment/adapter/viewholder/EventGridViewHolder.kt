package com.tokopedia.entertainment.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.adapter.HomeViewHolder
import com.tokopedia.entertainment.adapter.viewmodel.EventGridViewModel
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventGridViewHolder(itemView: View): HomeViewHolder<EventGridViewModel>(itemView) {

    override fun bind(element: EventGridViewModel) {
        itemView.ent_title_card.text = element.titleCard
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_grid
    }
}