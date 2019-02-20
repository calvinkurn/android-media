package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder

import android.graphics.PorterDuff
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel

/**
 * @author : Steven 18/02/19
 */
class PlayImageAnnouncementViewHolder(itemView: View, var listener: ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener) : AbstractViewHolder<ImageAnnouncementViewModel>(itemView) {


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.play_image_announcement_view_holder
    }

    private val message: TextView
    private val content: ImageView

    init {
        message = itemView.findViewById(R.id.text)
        content = itemView.findViewById(R.id.content_image)
    }

    override fun bind(element: ImageAnnouncementViewModel) {
        message.text = ""
        var userName = getUserName(element.senderName, element.isAdministrator)
        message.append(userName)
        message.append(" ")
        message.append(getColoredString(element.message, ContextCompat.getColor(itemView.context, R.color.white)))
        ImageHandler.LoadImage(content, element.contentImageUrl)
        content.setOnClickListener { listener.onImageAnnouncementClicked(element.redirectUrl) }
    }

    private fun getUserName(senderName: String?, administrator: Boolean): Spannable {
        return when {
            administrator -> getColoredString(senderName, ContextCompat.getColor(itemView.context, R.color.play_admin))
            else -> getColoredString(senderName, ContextCompat.getColor(itemView.context, R.color.play_user))
        }
    }

    fun getColoredString(mString: String?, colorId:Int): Spannable {
        var spannable = SpannableString(MethodChecker.fromHtml(mString))
        spannable.setSpan(ForegroundColorSpan(colorId), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }
}