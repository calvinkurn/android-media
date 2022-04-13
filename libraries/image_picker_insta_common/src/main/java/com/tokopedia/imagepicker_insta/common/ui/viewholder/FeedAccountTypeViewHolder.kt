package com.tokopedia.imagepicker_insta.common.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.common.databinding.ItemFeedAccountTypeBinding
import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
class FeedAccountTypeViewHolder(
    private val binding: ItemFeedAccountTypeBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedAccountUiModel) {

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