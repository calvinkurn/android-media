package com.tokopedia.kol.feature.comment.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserModel
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.comment.view.custom.KolCommentNewCardView
import com.tokopedia.kol.feature.comment.view.listener.KolComment
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography

class KolCommentHeaderNewViewHolder(itemView: View, private val viewListener: KolComment.View) :
    AbstractViewHolder<KolCommentHeaderNewModel?>(itemView) {
    private val commentView: KolCommentNewCardView = itemView.findViewById(R.id.kcv_comment)
    private val loadMore: Typography? = itemView.findViewById(R.id.btn_load_more)

    private val progressBar: ProgressBarUnify = itemView.findViewById(R.id.progress_bar)
    private val commentViewListener: KolCommentNewCardView.Listener =
        object : KolCommentNewCardView.Listener {

            override fun onMenuClicked(
                id: String?,
                canDeleteComment: Boolean,
                canReportComment: Boolean
            ) {
            }

            override fun onHashtagClicked(hashtag: String) {
                viewListener.onHashTagClicked(hashtag)

            }

            override fun onAvatarClicked(profileUrl: String, userId: String?) {
                viewListener.openRedirectUrl(profileUrl)
            }

            override fun onMentionedProfileClicked(authorId: String) {}

            override fun onTokopediaUrlClicked(url: String) {
                viewListener.openRedirectUrl(url)
            }

            override fun onReplyClicked(mentionableUser: MentionableUserModel) {
                viewListener.replyToUser(mentionableUser)
            }
        }

    override fun bind(element: KolCommentHeaderNewModel?) {
        commentView.setListener(commentViewListener)
        element?.let {
            if (it.isCanLoadMore) loadMore?.visible() else loadMore?.gone()

            commentView.setModel(it, true)
            if (it.isLoading) progressBar.visibility = View.VISIBLE else progressBar.visibility =
                View.GONE

            loadMore?.setOnClickListener { v: View? ->
                element.isCanLoadMore = false
                element.isLoading = true
                progressBar.visibility = View.VISIBLE
                loadMore.visibility = View.GONE
                viewListener.loadMoreComments()
            }
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.kol_new_comment_header
    }

}
