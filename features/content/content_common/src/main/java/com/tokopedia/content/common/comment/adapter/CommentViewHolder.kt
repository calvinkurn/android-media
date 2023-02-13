package com.tokopedia.content.common.comment.adapter

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.databinding.ItemCommentEmptyBinding
import com.tokopedia.content.common.databinding.ItemCommentShimmeringBinding
import com.tokopedia.content.common.databinding.ItemContentCommentBinding
import com.tokopedia.feedcomponent.util.bold
import com.tokopedia.feedcomponent.util.buildSpannedString
import com.tokopedia.media.loader.loadImage

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentViewHolder {
    internal class Item(
        private val binding: ItemContentCommentBinding,
        private val listener: Listener
    ) : BaseViewHolder(binding.root) {

        fun bind(item: CommentUiModel.Item) {
            with(binding) {
                ivCommentPhoto.loadImage(item.photo)
                tvCommentContent.text = buildSpannedString {
                    bold { append(item.username) }
                    append("    ")
                    append(item.content)
                }
                tvCommentTime.text = item.createdTime

                tvCommentReply.setOnClickListener {
                    listener.onReplyClicked(item)
                }

                root.setOnLongClickListener {
                    listener.onLongClicked(item)
                    true
                }
            }
        }

        interface Listener {
            fun onReplyClicked(item: CommentUiModel)
            fun onLongClicked(item: CommentUiModel)
        }
    }

    internal class Empty(
        binding: ItemCommentEmptyBinding,
    ) : BaseViewHolder(binding.root) {}

    internal class Shimmering(
        binding: ItemCommentShimmeringBinding,
    ) : BaseViewHolder(binding.root) {}
}
