package com.tokopedia.imagepicker_insta.common.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.common.databinding.ItemFeedAccountTypeBinding
import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel
import com.tokopedia.media.loader.loadImageCircle

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
class FeedAccountTypeViewHolder(
    private val binding: ItemFeedAccountTypeBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedAccountUiModel) {
        binding.apply {
            tvAccountName.text = item.name
            ivAvatar.loadImageCircle(item.iconUrl)

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
        fun onClick(item: FeedAccountUiModel)
    }
}