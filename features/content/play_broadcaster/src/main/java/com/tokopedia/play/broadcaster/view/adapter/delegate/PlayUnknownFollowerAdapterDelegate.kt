package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.FollowerUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayUnknownFollowerViewHolder

/**
 * Created by jegul on 20/05/20
 */
class PlayUnknownFollowerAdapterDelegate :
        TypedAdapterDelegate<FollowerUiModel.Unknown, FollowerUiModel, PlayUnknownFollowerViewHolder>(PlayUnknownFollowerViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: FollowerUiModel.Unknown, holder: PlayUnknownFollowerViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayUnknownFollowerViewHolder {
        return PlayUnknownFollowerViewHolder(basicView)
    }
}