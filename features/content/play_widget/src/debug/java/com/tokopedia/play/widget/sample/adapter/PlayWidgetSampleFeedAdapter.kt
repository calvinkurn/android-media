package com.tokopedia.play.widget.sample.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.PlayWidgetState

/**
 * Created by meyta.taliti on 28/01/22.
 */
class PlayWidgetSampleFeedAdapter  : BaseDiffUtilAdapter<PlayWidgetState>(isFlexibleType = true) {

    override fun areItemsTheSame(oldItem: PlayWidgetState, newItem: PlayWidgetState): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: PlayWidgetState, newItem: PlayWidgetState): Boolean {
        return oldItem == newItem
    }
}