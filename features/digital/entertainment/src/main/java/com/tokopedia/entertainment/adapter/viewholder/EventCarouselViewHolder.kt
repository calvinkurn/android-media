package com.tokopedia.entertainment.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.adapter.HomeViewHolder
import com.tokopedia.entertainment.adapter.viewmodel.EventCarouselViewModel

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselViewHolder: HomeViewHolder<EventCarouselViewModel> {

    constructor(itemView: View) : super(itemView)

    override fun bind(element: EventCarouselViewModel) {

    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_carouse
    }
}