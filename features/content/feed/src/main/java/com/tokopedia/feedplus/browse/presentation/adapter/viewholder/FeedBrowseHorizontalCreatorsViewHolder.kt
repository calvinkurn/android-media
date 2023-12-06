package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.presentation.adapter.CreatorAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.FeedBrowseHorizontalChannelsItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalCreatorsBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * Created by meyta.taliti on 16/11/23.
 */
internal class FeedBrowseHorizontalCreatorsViewHolder private constructor(
    private val binding: ItemFeedBrowseHorizontalCreatorsBinding,
    private val listener: Listener,
    private val scope: CoroutineScope,
) : RecyclerView.ViewHolder(binding.root) {

    private var retryJob: Job? = null

    private val adapter = CreatorAdapter(object : CreatorCardViewHolder.Item.Listener {
        override fun onChannelClicked(
            viewHolder: CreatorCardViewHolder.Item,
            item: AuthorWidgetModel
        ) {
            listener.onChannelClicked(this@FeedBrowseHorizontalCreatorsViewHolder, item)
        }

        override fun onAuthorClicked(
            viewHolder: CreatorCardViewHolder.Item,
            item: AuthorWidgetModel
        ) {
            listener.onAuthorClicked(this@FeedBrowseHorizontalCreatorsViewHolder, item)
        }
    })

    init {
        binding.rvCreators.adapter = adapter
        binding.rvCreators.addItemDecoration(
            FeedBrowseHorizontalChannelsItemDecoration(binding.rvCreators.resources)
        )

        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                retryJob?.cancel()
            }
        })
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalAuthors) {

        binding.errorView.stop()

//        when (item.itemState.state) {
//            is ResultState.Fail -> {
//                showContent(false)
//                binding.errorView.setOnClickListener { retry(item) }
//            }
//            ResultState.Loading -> {
//                adapter.setLoading()
//                showContent(true)
//            }
//            ResultState.Success -> {
//                showContent(true)
//                adapter.submitList(item.itemState.items)
//            }
//        }
        showContent(true)
        adapter.submitList(item.items)
    }

    private fun showContent(shouldShow: Boolean) {
        binding.rvCreators.showWithCondition(shouldShow)
        binding.errorView.showWithCondition(!shouldShow)
    }

    private fun retry(item: FeedBrowseItemListModel.HorizontalAuthors) {
        if (retryJob?.isActive == true) return
        retryJob = scope.launch {
            binding.errorView.startAnimating()
            delay(1.seconds)
            listener.onRetry(
                this@FeedBrowseHorizontalCreatorsViewHolder,
                item.slotInfo.id
            )
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
            scope: CoroutineScope,
        ) : FeedBrowseHorizontalCreatorsViewHolder {
            return FeedBrowseHorizontalCreatorsViewHolder(
                ItemFeedBrowseHorizontalCreatorsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
                scope
            )
        }
    }

    interface Listener {

        fun onChannelClicked(
            viewHolder: FeedBrowseHorizontalCreatorsViewHolder,
            item: AuthorWidgetModel
        )

        fun onAuthorClicked(
            viewHolder: FeedBrowseHorizontalCreatorsViewHolder,
            item: AuthorWidgetModel
        )

        fun onRetry(
            viewHolder: FeedBrowseHorizontalCreatorsViewHolder,
            slotId: String
        )
    }
}
