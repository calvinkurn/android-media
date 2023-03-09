package com.tokopedia.content.common.comment.adapter

import android.text.method.LinkMovementMethod
import androidx.core.view.updateLayoutParams
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.comment.MentionedSpanned
import com.tokopedia.content.common.comment.TagMentionBuilder
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.databinding.ItemCommentEmptyBinding
import com.tokopedia.content.common.databinding.ItemCommentExpandableBinding
import com.tokopedia.content.common.databinding.ItemCommentShimmeringBinding
import com.tokopedia.content.common.databinding.ItemContentCommentBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.content.common.R as contentR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentViewHolder {
    internal class Item(
        private val binding: ItemContentCommentBinding,
        private val listener: Listener
    ) : BaseViewHolder(binding.root) {

        private val parentListener by lazyThreadSafetyNone {
            object : MentionedSpanned.Listener {
                override fun onClicked(appLink: String) {
                    listener.onUserNameClicked(appLink)
                }
            }
        }

        private val mentionListener by lazyThreadSafetyNone {
            object : MentionedSpanned.Listener {
                override fun onClicked(appLink: String) {
                    listener.onMentionClicked(appLink)
                }
            }
        }

        private val parentColor by lazyThreadSafetyNone {
            MethodChecker.getColor(itemView.context, unifyR.color.Unify_NN950)
        }

        private val mentionColor by lazyThreadSafetyNone {
            MethodChecker.getColor(itemView.context, unifyR.color.Unify_GN500)
        }

        fun bind(item: CommentUiModel.Item) {
            with(binding) {
                val childView = item.commentType is CommentType.Child && !item.commentType.isNewlyAdded
                val layout32 = itemView.resources.getDimensionPixelSize(unifyR.dimen.layout_lvl4)
                val layout24 = itemView.resources.getDimensionPixelSize(unifyR.dimen.layout_lvl3)
                root.addOneTimeGlobalLayoutListener {
                    root.setPadding(
                        if (childView) ivCommentPhoto.width + 8.toPx() else 8.toPx(),
                        root.paddingTop,
                        root.paddingTop,
                        root.paddingBottom
                    )
                    ivCommentPhoto.updateLayoutParams {
                        width = if (childView) layout24 else layout32
                        height = if (childView) layout24 else layout32
                    }
                }

                ivCommentPhoto.loadImage(item.photo)
                ivCommentPhoto.setOnClickListener {
                    listener.onProfileClicked(item.appLink)
                }

                tvCommentContent.text = TagMentionBuilder
                    .getMentionTag(item = item, mentionColor, parentColor, mentionListener, parentListener, context = itemView.context)
                tvCommentContent.movementMethod = LinkMovementMethod.getInstance()
                tvCommentTime.text = item.createdTime

                tvCommentReply.setOnClickListener {
                    listener.onReplyClicked(item)
                }

                itemTouchHelper.setOnLongClickListener {
                    listener.onLongClicked(item)
                    true
                }
            }
        }

        interface Listener {
            fun onReplyClicked(item: CommentUiModel.Item)
            fun onLongClicked(item: CommentUiModel.Item)
            fun onMentionClicked(appLink: String)
            fun onProfileClicked(appLink: String)
            fun onUserNameClicked(appLink: String)
        }
    }

    internal class Empty(
        binding: ItemCommentEmptyBinding
    ) : BaseViewHolder(binding.root)

    internal class Shimmering(
        binding: ItemCommentShimmeringBinding
    ) : BaseViewHolder(binding.root)

    internal class Expandable(
        private val binding: ItemCommentExpandableBinding,
        private val listener: Listener
    ) : BaseViewHolder(binding.root) {
        fun bind(item: CommentUiModel.Expandable) {
            if (item.isExpanded) {
                binding.ivChevron.setImage(newIconId = IconUnify.CHEVRON_UP)
                binding.tvCommentExpandable.text =
                    getString(contentR.string.content_comment_expand_hide)
            } else {
                binding.ivChevron.setImage(newIconId = IconUnify.CHEVRON_DOWN)
                binding.tvCommentExpandable.text =
                    getString(contentR.string.content_comment_expand_visible, item.repliesCount)
            }

            binding.root.setOnClickListener {
                listener.onClicked(item, absoluteAdapterPosition)
            }
        }

        interface Listener {
            fun onClicked(item: CommentUiModel.Expandable, position: Int)
        }
    }
}
