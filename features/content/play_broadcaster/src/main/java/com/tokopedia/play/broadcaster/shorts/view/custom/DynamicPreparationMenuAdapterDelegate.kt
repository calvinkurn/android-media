package com.tokopedia.play.broadcaster.shorts.view.custom

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.R as commonR

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
internal class DynamicPreparationMenuAdapterDelegate(
    private val onClick: (DynamicPreparationMenu) -> Unit
) : TypedAdapterDelegate<
    DynamicPreparationMenuAdapter.Item,
    DynamicPreparationMenuAdapter.Item,
    DynamicPreparationMenuViewHolder
    >(commonR.layout.view_play_empty) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): DynamicPreparationMenuViewHolder {
        return DynamicPreparationMenuViewHolder.create(
            parent,
            onClick
        )
    }

    override fun onBindViewHolder(
        item: DynamicPreparationMenuAdapter.Item,
        holder: DynamicPreparationMenuViewHolder
    ) {
        holder.bind(item)
    }
}
