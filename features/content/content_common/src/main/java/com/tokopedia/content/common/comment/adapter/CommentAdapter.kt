package com.tokopedia.content.common.comment.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.content.common.comment.uimodel.CommentUiModel

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentAdapter internal constructor(
    commentListener: CommentViewHolder.Item.Listener,
    expandListener: CommentViewHolder.Expandable.Listener
) :
    BaseDiffUtilAdapter<CommentUiModel>() {

    init {
        delegatesManager.addDelegate(CommentAdapterDelegate.Item(commentListener))
        delegatesManager.addDelegate(CommentAdapterDelegate.Empty())
        delegatesManager.addDelegate(CommentAdapterDelegate.Shimmering())
        delegatesManager.addDelegate(CommentAdapterDelegate.Expandable(expandListener))
    }

    override fun areItemsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel): Boolean {
        return if (oldItem is CommentUiModel.Item && newItem is CommentUiModel.Item) {
            oldItem.id == newItem.id
        } else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel): Boolean =
        oldItem == newItem
}
