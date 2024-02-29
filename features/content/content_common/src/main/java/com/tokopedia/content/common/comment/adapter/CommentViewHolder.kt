package com.tokopedia.content.common.comment.adapter

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.view.updateLayoutParams
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.comment.MentionedSpanned
import com.tokopedia.content.common.comment.TagMentionBuilder
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.isChild
import com.tokopedia.content.common.databinding.ItemCommentEmptyBinding
import com.tokopedia.content.common.databinding.ItemCommentExpandableBinding
import com.tokopedia.content.common.databinding.ItemCommentShimmeringBinding
import com.tokopedia.content.common.databinding.ItemContentCommentBinding
import com.tokopedia.content.common.util.setSafeOnClickListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
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

        private val space8 = itemView.resources.getDimensionPixelSize(unifyR.dimen.layout_lvl1)
        private val space24 = itemView.resources.getDimensionPixelSize(unifyR.dimen.layout_lvl3)
        private val space32 = itemView.resources.getDimensionPixelSize(unifyR.dimen.layout_lvl4)
        private val space48 = itemView.resources.getDimensionPixelSize(unifyR.dimen.layout_lvl6)

        fun bind(item: CommentUiModel.Item) {
            with(binding) {
                ivCommentPhoto.updateLayoutParams {
                    width = if (item.commentType.isChild) space24 else space32
                    height = if (item.commentType.isChild) space24 else space32
                }

                root.setPadding(
                    if (item.commentType.isChild) space48 else space8,
                    root.paddingTop,
                    root.paddingTop,
                    root.paddingBottom
                )

                ivCommentPhoto.loadImage(item.photo)
                ivCommentPhoto.setOnClickListener {
                    listener.onProfileClicked(item.appLink)
                }

                tvCommentContent.text = TagMentionBuilder
                    .getMentionTag(
                        item = item,
                        mentionColor,
                        parentColor,
                        mentionListener,
                        parentListener,
                        context = itemView.context
                    )
                tvCommentContent.movementMethod = LinkMovementMethod.getInstance()
                tvCommentTime.text = item.createdTime

                tvCommentReply.setOnClickListener {
                    listener.onReplyClicked(item)
                }

                grItemComment.referencedIds.forEach {
                    root.findViewById<View>(it).setOnLongClickListener {
                        listener.onLongClicked(item)
                        true
                    }
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

        private val impressHolder by lazyThreadSafetyNone {
            ImpressHolder()
        }
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

            binding.root.setSafeOnClickListener {
                listener.onClicked(item, absoluteAdapterPosition)
            }

            binding.root.addOnImpressionListener(impressHolder) {
                listener.onImpressedExpandable()
            }
        }

        interface Listener {
            fun onClicked(item: CommentUiModel.Expandable, position: Int)
            fun onImpressedExpandable()
        }
    }
}
