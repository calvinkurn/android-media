package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageWithId
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel

/**
 * @author : Steven 18/02/19
 */
class PlayQuizAnnouncementViewHolder(itemView: View, var listener: ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener) : AbstractViewHolder<VoteAnnouncementViewModel>(itemView) {


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.play_quiz_announcement_view_holder
    }

    private val icon: ImageView
    private val title: TextView
    private val content: TextView

    init {
        icon = itemView.findViewById(R.id.icon)
        title = itemView.findViewById(R.id.title)
        content = itemView.findViewById(R.id.content)
    }

    override fun bind(element: VoteAnnouncementViewModel) {
        val type = element.voteType.toLowerCase()
        loadImageWithId(icon, getIcon(type))

        title.text = getTitle(type)
        title.setTextColor(getTitleTextColor(type))

        content.visibility = View.GONE
        element.voteInfoViewModel?.question?.let {
            content.visibility = View.VISIBLE
            content.text = MethodChecker.fromHtml(it)
        }

        itemView.setOnClickListener { listener.onVoteComponentClicked(type, element.message) }
    }

    private fun getIcon(voteType: String?): Int {
        return when (voteType){
            VoteAnnouncementViewModel.POLLING_START -> R.drawable.ic_quiz_start
            VoteAnnouncementViewModel.POLLING_FINISHED,
            VoteAnnouncementViewModel.POLLING_END-> R.drawable.ic_quiz_end
            else -> 0
        }
    }

    private fun getTitle(voteType: String?): CharSequence? {
        return when (voteType){
            VoteAnnouncementViewModel.POLLING_START -> itemView.context.getString(R.string.play_quiz_started)
            VoteAnnouncementViewModel.POLLING_FINISHED,
            VoteAnnouncementViewModel.POLLING_END-> itemView.context.getString(R.string.play_quiz_ended)
            else -> null
        }
    }

    private fun getTitleTextColor(voteType: String?): Int {
        return when (voteType){
            VoteAnnouncementViewModel.POLLING_START -> ContextCompat.getColor(itemView.context, R.color.quiz_start)
            VoteAnnouncementViewModel.POLLING_FINISHED,
            VoteAnnouncementViewModel.POLLING_END-> ContextCompat.getColor(itemView.context, R.color.quiz_end)
            else -> 0
        }
    }
}