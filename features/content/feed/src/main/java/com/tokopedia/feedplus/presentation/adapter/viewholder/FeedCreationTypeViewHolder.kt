package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedContentCreationTypeBinding
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.iconunify.getIconUnifyDrawable

/**
 * Created By : Shruti Agarwal on Feb 02, 2023
 */
class FeedCreationTypeViewHolder(
    private val binding: ItemFeedContentCreationTypeBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ContentCreationTypeItem) {
        binding.apply {
            tvName.text = item.name
            if (!item.imageSrc.isNullOrEmpty()) {
                ivIcon.setImageUrl(item.imageSrc)
            } else {
                item.drawableIconId?.let {
                    ivIcon.setImageDrawable(
                        getIconUnifyDrawable(
                            itemView.context,
                            it
                        )
                    )
                }
            }

            root.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener
        ): FeedCreationTypeViewHolder {
            return FeedCreationTypeViewHolder(
                ItemFeedContentCreationTypeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    interface Listener {
        fun onClick(item: ContentCreationTypeItem)
    }
}
