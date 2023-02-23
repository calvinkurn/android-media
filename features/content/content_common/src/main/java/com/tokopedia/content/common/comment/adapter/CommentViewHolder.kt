package com.tokopedia.content.common.comment.adapter

import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.databinding.ItemCommentEmptyBinding
import com.tokopedia.content.common.databinding.ItemCommentExpandableBinding
import com.tokopedia.content.common.databinding.ItemCommentShimmeringBinding
import com.tokopedia.content.common.databinding.ItemContentCommentBinding
import com.tokopedia.feedcomponent.util.bold
import com.tokopedia.feedcomponent.util.buildSpannedString
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.content.common.R as contentR

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentViewHolder {
    internal class Item(
        private val binding: ItemContentCommentBinding,
        private val listener: Listener
    ) : BaseViewHolder(binding.root) {

        private val commentInfo = mutableMapOf<String, String>()

        private val clickableSpan by lazyThreadSafetyNone {
            object : ClickableSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    tp.color = MethodChecker.getColor(itemView.context, unifyR.color.Unify_GN500)
                    tp.isUnderlineText = false
                    tp.typeface = Typeface.DEFAULT_BOLD
                }

                override fun onClick(widget: View) {
                    listener.onMentionClicked(userType = commentInfo[USER_TYPE].orEmpty(), userId = commentInfo[ID].orEmpty())
                }
            }
        }
        private fun getTagMention(item: CommentUiModel.Item): SpannedString {
            return try {
                val regex = """((?<=\{)(@\d+)\@|(@user|@seller)\@|(@.*)\@(?=\}))""".toRegex()
                val find = regex.findAll(item.content)
                var length = 10 //total escape character [{}|@]
                if (find.count() > 0) {
                    find.forEachIndexed { index, matchResult ->
                        if(index == 0) commentInfo[ID] = matchResult.value.replace("@","")
                        if(index == 1) commentInfo[USER_TYPE] = matchResult.value.replace("@","")
                        if(index == 2) commentInfo[USERNAME] = matchResult.value.removeSuffix("@")
                        length += matchResult.value.length
                    }
                    buildSpannedString {
                        bold { append(item.username + " ") }
                        append(
                            commentInfo[USERNAME],
                            clickableSpan,
                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                        )
                        append(" ")
                    }
                } else throw Exception()
            } catch (e: Exception) {
                buildSpannedString {
                    bold { append(item.username + " ") }
                    append(item.content)
                }
            }
        }

        fun bind(item: CommentUiModel.Item) {
            with(binding) {
                root.setPadding(
                    if (item.commentType is CommentType.Child) 40 else 8,
                    root.paddingTop,
                    root.paddingTop,
                    root.paddingBottom
                )

                ivCommentPhoto.loadImage(item.photo)

                tvCommentContent.text = getTagMention(item)
                tvCommentContent.movementMethod = LinkMovementMethod.getInstance()
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
            fun onReplyClicked(item: CommentUiModel.Item)
            fun onLongClicked(item: CommentUiModel.Item)
            fun onMentionClicked(userType: String, userId: String)
        }
        companion object {
            private const val ID = "id"
            private const val USER_TYPE = "userType"
            private const val USERNAME = "userName"
        }
    }

    internal class Empty(
        binding: ItemCommentEmptyBinding,
    ) : BaseViewHolder(binding.root) {}

    internal class Shimmering(
        binding: ItemCommentShimmeringBinding,
    ) : BaseViewHolder(binding.root) {}

    internal class Expandable(
        private val binding: ItemCommentExpandableBinding,
        private val listener: Listener,
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
