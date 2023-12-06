package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCreatorBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCreatorLoadingBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle

/**
 * Created by meyta.taliti on 25/09/23.
 */
internal class CreatorCardViewHolder private constructor() {

    class Item private constructor(
        private val binding: ItemFeedBrowseCreatorBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AuthorWidgetModel) {
            binding.ivCover.loadImage(item.coverUrl)
            binding.tvAuthorName.text = item.name
            binding.ivAuthorProfilePic.loadImageCircle(item.avatarUrl)
            binding.tvTotalViews.text = item.totalViewFmt

            binding.containerAvatar.setOnClickListener {
                listener.onAuthorClicked(this, item)
            }

            binding.clickAreaAuthor.setOnClickListener {
                listener.onAuthorClicked(this, item)
            }

            binding.ivCover.setOnClickListener {
                listener.onChannelClicked(this, item)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Item {
                return Item(
                    ItemFeedBrowseCreatorBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                    listener,
                )
            }
        }

        interface Listener {
            fun onChannelClicked(
                viewHolder: Item,
                item: AuthorWidgetModel
            )

            fun onAuthorClicked(
                viewHolder: Item,
                item: AuthorWidgetModel
            )
        }
    }

    class Placeholder private constructor(
        binding: ItemFeedBrowseCreatorLoadingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Placeholder {
                return Placeholder(
                    ItemFeedBrowseCreatorLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }
}
