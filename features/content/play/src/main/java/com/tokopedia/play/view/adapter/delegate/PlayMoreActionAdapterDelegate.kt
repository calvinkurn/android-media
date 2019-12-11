package com.tokopedia.play.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapter_delegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel
import com.tokopedia.play.view.viewholder.PlayMoreActionViewHolder

/**
 * Created by jegul on 10/12/19
 */
class PlayMoreActionAdapterDelegate : TypedAdapterDelegate<PlayMoreActionUiModel, PlayMoreActionUiModel, PlayMoreActionViewHolder>(R.layout.item_play_more_action) {

    override fun onBindViewHolder(item: PlayMoreActionUiModel, holder: PlayMoreActionViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayMoreActionViewHolder {
        return PlayMoreActionViewHolder(basicView)
    }
}