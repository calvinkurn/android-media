package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R

/**
 * Created by jegul on 24/06/20
 */
class PlayCoverCameraViewHolder(
        itemView: View,
        private val listener: Listener
) : BaseViewHolder(itemView) {

    init {
        itemView.setOnClickListener { listener.onCameraButtonClicked() }
    }

    interface Listener {
        fun onCameraButtonClicked()
    }

    companion object {
        val LAYOUT = R.layout.item_play_cover_from_camera
    }
}