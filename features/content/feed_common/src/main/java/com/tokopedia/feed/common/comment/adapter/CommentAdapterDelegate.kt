package com.tokopedia.feed.common.comment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feed.common.comment.uimodel.CommentUiModel
import com.tokopedia.feed.common.databinding.ItemCommentExpandableBinding
import com.tokopedia.feed.common.databinding.ItemCommentShimmeringBinding
import com.tokopedia.feed.common.databinding.ItemContentCommentBinding
import com.tokopedia.feed.common.R as feedcommonR

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentAdapterDelegate {
    internal class Item(private val listener: CommentViewHolder.Item.Listener) :
        TypedAdapterDelegate<CommentUiModel.Item, CommentUiModel, CommentViewHolder.Item>(
            feedcommonR.layout.item_content_comment
        ) {
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
    }

    internal class Shimmering :
        TypedAdapterDelegate<CommentUiModel.Shimmer, CommentUiModel, CommentViewHolder.Shimmering>(
            feedcommonR.layout.item_comment_shimmering
        ) {
        override fun onBindViewHolder(
            item: CommentUiModel.Shimmer,
            holder: CommentViewHolder.Shimmering
        ) {
        }

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

    internal class Expandable(private val listener: CommentViewHolder.Expandable.Listener) :
        TypedAdapterDelegate<CommentUiModel.Expandable, CommentUiModel, CommentViewHolder.Expandable>(
            feedcommonR.layout.item_comment_expandable
        ) {
        override fun onBindViewHolder(
            item: CommentUiModel.Expandable,
            holder: CommentViewHolder.Expandable
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): CommentViewHolder.Expandable {
            val view = ItemCommentExpandableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CommentViewHolder.Expandable(view, listener)
        }
    }
}
