package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.viewholder.PlayUnknownFollowerViewHolder
import com.tokopedia.play.broadcaster.view.uimodel.FollowerUiModel

/**
 * Created by jegul on 20/05/20
 */
class PlayUnknownFollowerAdapterDelegate :
        TypedAdapterDelegate<FollowerUiModel.Unknown, FollowerUiModel, PlayUnknownFollowerViewHolder>(R.layout.item_unknown_follower) {

    override fun onBindViewHolder(item: FollowerUiModel.Unknown, holder: PlayUnknownFollowerViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayUnknownFollowerViewHolder {
        return PlayUnknownFollowerViewHolder(basicView)
    }
}