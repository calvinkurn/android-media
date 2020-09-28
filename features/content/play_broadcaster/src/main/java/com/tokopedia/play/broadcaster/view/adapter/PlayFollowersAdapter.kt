package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.FollowerUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayUnknownFollowerAdapterDelegate
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayUserFollowerAdapterDelegate

/**
 * Created by jegul on 20/05/20
 */
class PlayFollowersAdapter : BaseDiffUtilAdapter<FollowerUiModel>() {

    override fun areItemsTheSame(oldItem: FollowerUiModel, newItem: FollowerUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FollowerUiModel, newItem: FollowerUiModel): Boolean {
        return oldItem == newItem
    }

    init {
        delegatesManager
                .addDelegate(PlayUnknownFollowerAdapterDelegate())
                .addDelegate(PlayUserFollowerAdapterDelegate())
    }
}