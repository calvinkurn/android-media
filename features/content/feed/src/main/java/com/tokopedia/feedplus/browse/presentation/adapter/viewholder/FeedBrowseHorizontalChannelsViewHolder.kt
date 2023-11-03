package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChannelAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.FeedBrowseHorizontalChannelsItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalChannelsBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class FeedBrowseHorizontalChannelsViewHolder private constructor(
    private val binding: ItemFeedBrowseHorizontalChannelsBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    private var retryJob: Job? = null

    private var mScope: CoroutineScope? = null

    private val adapter = FeedBrowseChannelAdapter(
        object : FeedBrowseChannelViewHolder2.Channel.Listener {
            override fun onCardImpressed(item: PlayWidgetChannelUiModel, position: Int) {
            }

            override fun onCardClicked(item: PlayWidgetChannelUiModel, position: Int) {
            }
        },
        object : FeedBrowseChannelViewHolder2.Error.Listener {
            override fun onRetry(viewHolder: FeedBrowseChannelViewHolder2.Error) {

            }
        }
    )

    init {
        binding.rvChannels.adapter = adapter
        binding.rvChannels.setHasFixedSize(true)
        binding.rvChannels.addItemDecoration(
            FeedBrowseHorizontalChannelsItemDecoration(binding.rvChannels.resources)
        )

        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                mScope = v.findViewTreeLifecycleOwner()?.lifecycleScope
            }

            override fun onViewDetachedFromWindow(v: View) {
                retryJob?.cancel()
                mScope = null
            }
        })
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalChannels) {
        binding.errorView.stop()

        when (item.itemState.state) {
            ResultState.Loading -> {
                showContent(true)
                adapter.setLoading()
            }
            is ResultState.Success -> {
                showContent(true)
                adapter.submitList(item.itemState.items) {
                    binding.rvChannels.doOnLayout {
                        binding.rvChannels.scrollToPosition(0)
                    }
                }
            }
            is ResultState.Fail -> {
                showContent(false)
                binding.errorView.setOnClickListener { retry(item) }
            }
        }
    }

    private fun showContent(shouldShow: Boolean) {
        binding.rvChannels.showWithCondition(shouldShow)
        binding.errorView.showWithCondition(!shouldShow)
    }

    private fun retry(item: FeedBrowseItemListModel.HorizontalChannels) {
        if (retryJob?.isActive == true) return
        retryJob = mScope?.launch {
            binding.errorView.startAnimating()
            delay(1.seconds)
            listener.onRetry(
                this@FeedBrowseHorizontalChannelsViewHolder,
                item.slotId,
                item.menu,
            )
        }
    }

    fun bindPayloads(item: FeedBrowseItemListModel.HorizontalChannels, payloads: FeedBrowsePayloads) {
        if (payloads.isChannelItemsChanged()) bind(item)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ): FeedBrowseHorizontalChannelsViewHolder {
            return FeedBrowseHorizontalChannelsViewHolder(
                ItemFeedBrowseHorizontalChannelsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
            )
        }
    }

    interface Listener {
        fun onRetry(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            slotId: String,
            menu: WidgetMenuModel,
        )
    }
}
