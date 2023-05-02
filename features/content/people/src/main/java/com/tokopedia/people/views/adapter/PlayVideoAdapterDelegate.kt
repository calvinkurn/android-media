package com.tokopedia.people.views.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.people.views.viewholder.PlayVideoViewHolder
import com.tokopedia.content.common.R as contentCommonR
/**
 * Created By : Jonathan Darwin on February 10, 2023
 */
class PlayVideoAdapterDelegate private constructor() {

    class Channel(
        private val listener: PlayVideoViewHolder.Channel.Listener
    ) : TypedAdapterDelegate<PlayVideoAdapter.Model.Channel, PlayVideoAdapter.Model, PlayVideoViewHolder.Channel>
        (contentCommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: PlayVideoAdapter.Model.Channel,
            holder: PlayVideoViewHolder.Channel
        ) {
            holder.bind(item.data)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayVideoViewHolder.Channel {
            return PlayVideoViewHolder.Channel.create(
                parent,
                listener
            )
        }
    }

    class Transcode(
        private val listener: PlayVideoViewHolder.Transcode.Listener
    ) : TypedAdapterDelegate<PlayVideoAdapter.Model.Transcode, PlayVideoAdapter.Model, PlayVideoViewHolder.Transcode>
        (contentCommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: PlayVideoAdapter.Model.Transcode,
            holder: PlayVideoViewHolder.Transcode
        ) {
            holder.bind(item.data)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayVideoViewHolder.Transcode {
            return PlayVideoViewHolder.Transcode.create(
                parent,
                listener
            )
        }
    }

    class Loading : TypedAdapterDelegate<PlayVideoAdapter.Model.Loading, PlayVideoAdapter.Model, PlayVideoViewHolder.Loading>
        (contentCommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: PlayVideoAdapter.Model.Loading,
            holder: PlayVideoViewHolder.Loading
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayVideoViewHolder.Loading {
            return PlayVideoViewHolder.Loading.create(parent)
        }
    }
}
