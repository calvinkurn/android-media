package com.tokopedia.feedplus.browse.presentation.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChannelViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowsePlaceholderViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import kotlinx.coroutines.CoroutineScope

/**
 * Created by kenny.hadisaputra on 18/09/23
 */
class FeedBrowseAdapter(
    private val channelListener: FeedBrowseChannelViewHolder.Listener,
    private val bannerListener: FeedBrowseBannerViewHolder.Listener,
    private val scope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers
) : ListAdapter<FeedBrowseUiModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<FeedBrowseUiModel>() {
        override fun areItemsTheSame(
            oldItem: FeedBrowseUiModel,
            newItem: FeedBrowseUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FeedBrowseUiModel,
            newItem: FeedBrowseUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItem: FeedBrowseUiModel,
            newItem: FeedBrowseUiModel
        ): Any? {
            val payloadBuilder = FeedBrowsePayloads.Builder()
            if (oldItem is FeedBrowseUiModel.Channel && newItem is FeedBrowseUiModel.Channel) {
                if (oldItem.chipUiState != newItem.chipUiState) payloadBuilder.addChannelChipsChanged()
                if (oldItem.channelUiState != newItem.channelUiState) payloadBuilder.addChannelItemsChanged()
                payloadBuilder.addChannelRefresh()
            }

            val payloads = payloadBuilder.build()
            Log.d("getChangePayload", "Payloads: $payloads")
            return payloads
        }
    }
) {

    private val supportedTypes = listOf(
        FeedBrowseUiModel.Channel::class,
        FeedBrowseUiModel.Banner::class,
        FeedBrowseUiModel.Title::class,
        FeedBrowseUiModel.Placeholder::class
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (val itemClass = supportedTypes[viewType]) {
            FeedBrowseUiModel.Channel::class -> FeedBrowseChannelViewHolder.create(
                parent,
                channelListener,
                scope,
                dispatchers
            )
            FeedBrowseUiModel.Banner::class -> FeedBrowseBannerViewHolder.create(parent, bannerListener)
            FeedBrowseUiModel.Placeholder::class -> FeedBrowsePlaceholderViewHolder.create(parent)
            FeedBrowseUiModel.Title::class -> FeedBrowseTitleViewHolder.create(parent)
            else -> error("Class $itemClass is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is FeedBrowseChannelViewHolder && item is FeedBrowseUiModel.Channel -> {
                holder.bind(item)
            }
            holder is FeedBrowseBannerViewHolder && item is FeedBrowseUiModel.Banner -> {
                holder.bind(item)
            }
            holder is FeedBrowsePlaceholderViewHolder && item is FeedBrowseUiModel.Placeholder -> {
                holder.bind(item.type)
            }
            holder is FeedBrowseTitleViewHolder && item is FeedBrowseUiModel.Title -> {
                holder.bind(item)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        Log.d("onBindViewHolder", "Payloads: $payloads")
        if (payloads.isEmpty()) {
            return super.onBindViewHolder(holder, position, payloads)
        }
        val payload = payloads.filterIsInstance<FeedBrowsePayloads>().combine()
        val item = getItem(position)
        when {
            holder is FeedBrowseChannelViewHolder && item is FeedBrowseUiModel.Channel -> {
                holder.bindPayloads(item, payload)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return supportedTypes.indexOf(getItem(position)::class)
    }

    fun getSpanSizeLookup(): SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = getItem(position)
                return when (item::class) {
                    FeedBrowseUiModel.Banner::class -> 1
                    else -> 2
                }
            }
        }
    }
}
