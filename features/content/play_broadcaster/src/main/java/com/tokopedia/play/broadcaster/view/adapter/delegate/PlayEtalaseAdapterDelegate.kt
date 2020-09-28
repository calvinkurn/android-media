package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.EtalaseContentUiModel
import com.tokopedia.play.broadcaster.ui.model.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalaseAdapterDelegate(
        private val listener: PlayEtalaseViewHolder.Listener
) : TypedAdapterDelegate<EtalaseContentUiModel, EtalaseUiModel, PlayEtalaseViewHolder>(PlayEtalaseViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: EtalaseContentUiModel, holder: PlayEtalaseViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayEtalaseViewHolder {
        return PlayEtalaseViewHolder(basicView, listener)
    }
}