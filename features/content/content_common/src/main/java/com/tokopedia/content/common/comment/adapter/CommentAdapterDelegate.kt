package com.tokopedia.content.common.comment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.ItemCommentEmptyBinding
import com.tokopedia.content.common.databinding.ItemCommentShimmeringBinding
import com.tokopedia.content.common.databinding.ItemContentCommentBinding

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentAdapterDelegate {
    internal class Item(private val listener: CommentViewHolder.Item.Listener) :
        BaseAdapterDelegate<CommentUiModel.Item, CommentUiModel, CommentViewHolder.Item>(R.layout.item_content_comment) {
        override fun onBindViewHolder(item: CommentUiModel.Item, holder: CommentViewHolder.Item) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): CommentViewHolder.Item {
            val view = ItemContentCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CommentViewHolder.Item(view, listener)
        }

        override fun isForViewType(
            itemList: List<CommentUiModel>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean = itemList.any { it is CommentUiModel.Item }
    }

    internal class Empty :
        TypedAdapterDelegate<CommentUiModel.Empty, CommentUiModel, CommentViewHolder.Empty>(R.layout.item_comment_empty) {
        override fun onBindViewHolder(
            item: CommentUiModel.Empty,
            holder: CommentViewHolder.Empty
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): CommentViewHolder.Empty {
            val view = ItemCommentEmptyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CommentViewHolder.Empty(view)
        }
    }

    internal class Shimmering :
        TypedAdapterDelegate<CommentUiModel.Shimmer, CommentUiModel, CommentViewHolder.Shimmering>(R.layout.item_comment_shimmering) {
        override fun onBindViewHolder(
            item: CommentUiModel.Shimmer,
            holder: CommentViewHolder.Shimmering
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): CommentViewHolder.Shimmering {
            val view = ItemCommentShimmeringBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CommentViewHolder.Shimmering(view)
        }
    }
}
