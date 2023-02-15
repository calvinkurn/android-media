package com.tokopedia.content.common.report_content.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.databinding.ItemFeedThreeDotsMenuBinding
import com.tokopedia.feedplus.presentation.model.FeedMenuIdentifier
import com.tokopedia.feedplus.presentation.model.FeedMenuItem
import com.tokopedia.unifyprinciples.R

/**
 * Created By : Shruti Agarwal on Feb 02, 2023
 */
class FeedMenuViewHolder(
    private val binding: ItemFeedThreeDotsMenuBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedMenuItem) {
        binding.apply {
            tvName.text = item.name
            ivIcon.setImageDrawable(item.drawable)
            if (item.type == FeedMenuIdentifier.LAPORKAN){
                tvName.setTextColor(
                    MethodChecker.getColor(
                    itemView.context,
                    R.color.Unify_RN500
                ))
            }

            root.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ): FeedMenuViewHolder {
            return FeedMenuViewHolder(
                ItemFeedThreeDotsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    interface Listener {
        fun onClick(item: FeedMenuItem)
    }
}
