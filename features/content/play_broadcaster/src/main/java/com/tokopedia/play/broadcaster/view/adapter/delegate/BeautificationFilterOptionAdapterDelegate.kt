package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.BeautificationFilterOptionViewHolder
import com.tokopedia.play_common.R as commonR

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
class BeautificationFilterOptionAdapterDelegate(
    private val listener: BeautificationFilterOptionViewHolder.Listener,
) : TypedAdapterDelegate<FaceFilterUiModel, FaceFilterUiModel, BeautificationFilterOptionViewHolder>(
    commonR.layout.view_play_empty) {

    override fun onBindViewHolder(item: FaceFilterUiModel, holder: BeautificationFilterOptionViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): BeautificationFilterOptionViewHolder {
        return BeautificationFilterOptionViewHolder.create(parent, listener)
    }
}
