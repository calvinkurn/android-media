package com.tokopedia.talk.inboxtalk.view.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.CommentTalkAdapter
import com.tokopedia.talk.common.adapter.CommentTalkTypeFactoryImpl
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import kotlinx.android.synthetic.main.inbox_talk_item.view.*
import kotlinx.android.synthetic.main.inbox_talk_item_product_header.view.*
import kotlinx.android.synthetic.main.talk_item.view.*
import kotlinx.android.synthetic.main.thread_talk.view.*

/**
 * @author by nisie on 8/30/18.
 */

class InboxTalkItemViewHolder(val v: View,
                              val listener: TalkItemListener) :
        AbstractViewHolder<InboxTalkItemViewModel>(v) {

    interface TalkItemListener {
        fun onReplyTalkButtonClick(allowReply: Boolean)
        fun onMenuButtonClicked(menu: TalkState)
    }

    private val productName: TextView = itemView.productName
    private val productAvatar: ImageView = itemView.productAvatar
    private val profileAvatar: ImageView = itemView.prof_pict
    private val notification: View = itemView.notification
    private val profileName: TextView = itemView.username
    private val timestamp: TextView = itemView.timestamp
    private val menuButton: ImageView = itemView.menu
    private val talkContent: TextView = itemView.talk_content
    private val listComment: RecyclerView = itemView.list_child
    private val replyButton: TextView = itemView.replyButton


    private lateinit var adapter: CommentTalkAdapter

    companion object {
        val LAYOUT = R.layout.inbox_talk_item
    }

    override fun bind(element: InboxTalkItemViewModel?) {
        element?.run {

            if (!element.talkThread.listChild.isEmpty()) {
                val typeFactoryImpl = CommentTalkTypeFactoryImpl()
                adapter = CommentTalkAdapter(typeFactoryImpl, element.talkThread.listChild)
                listComment.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager
                        .VERTICAL, false)
                listComment.adapter = adapter
                listComment.visibility = View.VISIBLE
            } else {
                listComment.visibility = View.GONE
            }

            productName.text = MethodChecker.fromHtml(element.productHeader.productName)
            ImageHandler.LoadImage(productAvatar, element.productHeader.productAvatar)

            ImageHandler.loadImageCircle2(profileAvatar.context, profileAvatar, element.talkThread
                    .headThread.avatar)
            profileName.text = element.talkThread.headThread.name
            talkContent.text = element.talkThread.headThread.comment
            timestamp.text = element.talkThread.headThread.timestamp

            replyButton.setOnClickListener {
                listener.onReplyTalkButtonClick(element.talkThread
                        .headThread.menu.allowReply)
            }

            if (element.talkThread.headThread.isRead) notification.visibility = View.VISIBLE
            else notification.visibility = View.GONE

            setupMenuButton(element.talkThread.headThread.menu)

        }

    }

    private fun setupMenuButton(menu: TalkState) {
        if (menu.allowDelete || menu.allowFollow || menu.allowReport || menu.allowUnfollow) {
            menuButton.visibility = View.VISIBLE
        } else {
            menuButton.visibility = View.GONE
        }

        menuButton.setOnClickListener{listener.onMenuButtonClicked(menu)}
    }

}