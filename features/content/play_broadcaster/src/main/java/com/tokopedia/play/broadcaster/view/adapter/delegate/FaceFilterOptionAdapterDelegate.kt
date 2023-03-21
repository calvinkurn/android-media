package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.FaceFilterOptionViewHolder
import com.tokopedia.play_common.R as commonR

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
class FaceFilterOptionAdapterDelegate(
    private val listener: FaceFilterOptionViewHolder.Listener,
) : TypedAdapterDelegate<FaceFilterUiModel, FaceFilterUiModel, FaceFilterOptionViewHolder>(
    commonR.layout.view_play_empty) {

    override fun onBindViewHolder(item: FaceFilterUiModel, holder: FaceFilterOptionViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): FaceFilterOptionViewHolder {
        return FaceFilterOptionViewHolder.create(parent, listener)
    }
}
