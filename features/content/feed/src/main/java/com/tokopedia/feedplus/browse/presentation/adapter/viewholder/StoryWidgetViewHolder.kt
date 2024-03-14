package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseStoryBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseStoryLoadingBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.stories.widget.StoriesStatus
import com.tokopedia.stories.widget.StoriesWidgetLayout
import com.tokopedia.stories.widget.domain.StoriesWidgetState

/**
 * Created by meyta.taliti on 25/09/23.
 */
internal class StoryWidgetViewHolder private constructor() {

    class Item private constructor(
        private val binding: ItemFeedBrowseStoryBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StoryNodeModel) {
            binding.ivThumbnail.loadImage(item.thumbnailUrl) {
                centerCrop()
                isCircular(true)
            }
            binding.tvName.text = item.name
            binding.storiesLayout.setState {
                it.copy(
                    status = if (item.hasUnseenStory) {
                        StoriesStatus.HasUnseenStories
                    } else {
                        StoriesStatus.AllStoriesSeen
                    },
                    shopId = item.id,
                    appLink = item.appLink
                )
            }
            binding.storiesLayout.setListener(object : StoriesWidgetLayout.Listener {
                override fun onClickedWhenHasStories(
                    view: StoriesWidgetLayout,
                    state: StoriesWidgetState
                ) {
                    listener.onWidgetClicked(this@Item, item)
                }

                override fun onImpressed(view: StoriesWidgetLayout, state: StoriesWidgetState) {
                }
            })

            binding.root.addImpressionListener {
                listener.onWidgetImpressed(this, item)
            }

            binding.root.setOnClickListener {
                listener.onWidgetClicked(this, item)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Item {
                return Item(
                    ItemFeedBrowseStoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener
                )
            }
        }

        interface Listener {

            fun onWidgetImpressed(
                viewHolder: Item,
                item: StoryNodeModel
            )
            fun onWidgetClicked(
                viewHolder: Item,
                item: StoryNodeModel
            )
        }
    }

    class Placeholder private constructor(
        binding: ItemFeedBrowseStoryLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Placeholder {
                return Placeholder(
                    ItemFeedBrowseStoryLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}
