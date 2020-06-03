package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.FollowerUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayUserFollowerViewHolder

/**
 * Created by jegul on 20/05/20
 */
class PlayUserFollowerAdapterDelegate :
        TypedAdapterDelegate<FollowerUiModel.User, FollowerUiModel, PlayUserFollowerViewHolder>(PlayUserFollowerViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: FollowerUiModel.User, holder: PlayUserFollowerViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayUserFollowerViewHolder {
        return PlayUserFollowerViewHolder(basicView)
    }
}