package com.tokopedia.content.common.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.databinding.ItemFeedAccountTypeBinding
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
class FeedAccountTypeViewHolder(
    private val binding: ItemFeedAccountTypeBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ContentAccountUiModel) {
        binding.apply {
            tvAccountName.text = item.name
            ivAvatar.loadImageCircle(item.iconUrl)
            ivBadge.loadImage(item.badge)

            ivBadge.visibility = if(item.badge.isNotEmpty()) View.VISIBLE else View.GONE

            root.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ): FeedAccountTypeViewHolder {
            return FeedAccountTypeViewHolder(
                ItemFeedAccountTypeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    interface Listener {
        fun onClick(item: ContentAccountUiModel)
    }
}