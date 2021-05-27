package com.tokopedia.kol.feature.comment.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.comment.view.custom.KolCommentNewCardView
import com.tokopedia.kol.feature.comment.view.listener.KolComment
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel
import com.tokopedia.unifycomponents.ProgressBarUnify

class KolCommentHeaderNewViewHolder(itemView: View, private val viewListener: KolComment.View) :
    AbstractViewHolder<KolCommentHeaderNewModel?>(itemView) {
    private val commentView: KolCommentNewCardView = itemView.findViewById(R.id.kcv_comment)
    private val progressBar: ProgressBarUnify = itemView.findViewById(R.id.progress_bar)
    private val commentViewListener: KolCommentNewCardView.Listener =
        object : KolCommentNewCardView.Listener {
            override fun onReport(
                reasonType: String,
                reasonDesc: String,
                id: String,
                canDeleteComment: Boolean
            ) {
            }

            override fun onAvatarClicked(profileUrl: String) {
                viewListener.openRedirectUrl(profileUrl)
            }

            override fun onMentionedProfileClicked(authorId: String) {}
            override fun onDeleteComment(commentId: String, canDeleteComment: Boolean): Boolean {
                return false
            }

            override fun onTokopediaUrlClicked(url: String) {
                viewListener.openRedirectUrl(url)
            }

            override fun onReplyClicked(mentionableUser: MentionableUserViewModel) {
                viewListener.replyToUser(mentionableUser)
            }
        }

    override fun bind(element: KolCommentHeaderNewModel?) {
        commentView.setListener(commentViewListener)
        element?.let {
            commentView.setModel(it, true)
            if (it.isLoading) progressBar.visibility = View.VISIBLE else progressBar.visibility =
                View.GONE
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.kol_new_comment_header
    }

}