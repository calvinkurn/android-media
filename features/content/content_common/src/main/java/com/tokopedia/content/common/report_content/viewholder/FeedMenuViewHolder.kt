package com.tokopedia.content.common.report_content.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.databinding.ItemFeedThreeDotsMenuBinding
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Shruti Agarwal on Feb 02, 2023
 */
class FeedMenuViewHolder(
    private val binding: ItemFeedThreeDotsMenuBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ContentMenuItem) {
        binding.apply {
            val context = itemView.context

            tvName.text = context.getString(item.name)
            val textColorInt = if (item.type == ContentMenuIdentifier.Report) {
                unifyR.color.Unify_RN500
            } else {
                unifyR.color.Unify_NN950
            }
            tvName.setTextColor(
                MethodChecker.getColor(
                    itemView.context,
                    textColorInt
                )
            )

            val iconColorInt = when (item.type) {
                ContentMenuIdentifier.Report, ContentMenuIdentifier.Delete -> {
                    unifyR.color.Unify_RN500
                }
                else -> {
                    unifyR.color.Unify_NN900
                }
            }
            val iconDrawable = getIconUnifyDrawable(
                context,
                item.iconUnify,
                MethodChecker.getColor(
                    itemView.context,
                    iconColorInt
                )
            )
            if (iconDrawable != null) ivIcon.setImageDrawable(iconDrawable)

            root.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener
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
        fun onClick(item: ContentMenuItem)
    }
}
