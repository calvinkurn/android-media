package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.FollowerUiModel

/**
 * Created by jegul on 20/05/20
 */
class PlayUserFollowerViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val ivUser = itemView.findViewById<ImageView>(R.id.iv_user)

    fun bind(item: FollowerUiModel.User) {
        ivUser.loadImageCircle(item.imageUrl)
    }

    companion object {
        val LAYOUT = R.layout.item_play_user_follower
    }
}