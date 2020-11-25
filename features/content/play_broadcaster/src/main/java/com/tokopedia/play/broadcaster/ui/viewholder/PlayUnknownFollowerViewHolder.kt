package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.FollowerUiModel
import com.tokopedia.play.broadcaster.view.custom.CirclePersonView

/**
 * Created by jegul on 20/05/20
 */
class PlayUnknownFollowerViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val cpvFollower = itemView.findViewById<CirclePersonView>(R.id.cpv_follower)

    fun bind(item: FollowerUiModel.Unknown) {
        cpvFollower.setColor(item.colorRes)
    }

    companion object {
        val LAYOUT = R.layout.item_play_unknown_follower
    }
}