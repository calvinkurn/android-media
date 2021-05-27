package com.tokopedia.kol.feature.comment.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.comment.view.custom.KolCommentNewCardView
import com.tokopedia.kol.feature.comment.view.listener.KolComment
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel

class KolCommentNewViewHolder(
    itemView: View,
    private val viewListener: KolComment.View.ViewHolder,
    private val canComment: Boolean
) : AbstractViewHolder<KolCommentNewModel?>(itemView) {
    private val commentView: KolCommentNewCardView = itemView.findViewById(R.id.kcv_comment)
    private val commentViewListener: KolCommentNewCardView.Listener =
        object : KolCommentNewCardView.Listener {
            override fun onReport(
                reasonType: String,
                reasonDesc: String,
                id: String,
                canDeleteComment: Boolean
            ) {
                viewListener.reportAction(
                    adapterPosition,
                    canDeleteComment,
                    id,
                    reasonType,
                    reasonDesc
                )
            }

            override fun onAvatarClicked(profileUrl: String) {
                viewListener.onGoToProfile(profileUrl)
            }

            override fun onMentionedProfileClicked(authorId: String) {
                viewListener.onClickMentionedProfile(authorId)
            }

            override fun onDeleteComment(commentId: String, canDeleteComment: Boolean): Boolean {
                return viewListener.onDeleteCommentKol(commentId, canDeleteComment, adapterPosition)
            }

            override fun onTokopediaUrlClicked(url: String) {}
            override fun onReplyClicked(mentionableUser: MentionableUserViewModel) {
                viewListener.replyToUser(mentionableUser)
            }
        }

    companion object {
        @JvmField
        val LAYOUT = R.layout.kol_comment_new_item
    }

    override fun bind(element: KolCommentNewModel?) {
        commentView.setListener(commentViewListener)
        element?.let {
            commentView.setModel(it, canComment)
        }
    }

}