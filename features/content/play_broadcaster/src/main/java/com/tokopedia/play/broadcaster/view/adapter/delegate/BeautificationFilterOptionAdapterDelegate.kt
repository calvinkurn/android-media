package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.beautification.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.BeautificationFilterOptionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.BeautificationFilterOptionAdapter
import com.tokopedia.play_common.R as commonR

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
class BeautificationFilterOptionAdapterDelegate private constructor() {

    class FaceFilter(
        private val listener: BeautificationFilterOptionViewHolder.FaceFilter.Listener,
    ) : TypedAdapterDelegate<
        BeautificationFilterOptionAdapter.Model.FaceFilter,
        BeautificationFilterOptionAdapter.Model,
        BeautificationFilterOptionViewHolder.FaceFilter
    >(commonR.layout.view_play_empty) {
        override fun onBindViewHolder(
            item: BeautificationFilterOptionAdapter.Model.FaceFilter,
            holder: BeautificationFilterOptionViewHolder.FaceFilter
        ) {
            holder.bind(item.data)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): BeautificationFilterOptionViewHolder.FaceFilter {
            return BeautificationFilterOptionViewHolder.FaceFilter.create(parent, listener)
        }
    }

    class Preset(
        private val listener: BeautificationFilterOptionViewHolder.Preset.Listener,
    ) : TypedAdapterDelegate<
        BeautificationFilterOptionAdapter.Model.Preset,
        BeautificationFilterOptionAdapter.Model,
        BeautificationFilterOptionViewHolder.Preset
        >(commonR.layout.view_play_empty) {
        override fun onBindViewHolder(
            item: BeautificationFilterOptionAdapter.Model.Preset,
            holder: BeautificationFilterOptionViewHolder.Preset
        ) {
            holder.bind(item.data)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): BeautificationFilterOptionViewHolder.Preset {
            return BeautificationFilterOptionViewHolder.Preset.create(parent, listener)
        }
    }
}
