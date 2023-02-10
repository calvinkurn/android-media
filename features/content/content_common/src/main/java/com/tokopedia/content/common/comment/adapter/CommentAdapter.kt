package com.tokopedia.content.common.comment.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.content.common.comment.uimodel.CommentUiModel

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentAdapter internal constructor(private val commentListener: CommentViewHolder.Item.Listener) :
    BaseDiffUtilAdapter<CommentUiModel>() {

    init {
        delegatesManager.addDelegate(CommentAdapterDelegate.Item(commentListener))
    }

    override fun areItemsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel): Boolean {
        return if (oldItem is CommentUiModel.Item && newItem is CommentUiModel.Item) {
            oldItem.id == newItem.id
        } else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel): Boolean =
        oldItem == newItem
}
