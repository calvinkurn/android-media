package com.tokopedia.talk.producttalk.view.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.CommentTalkAdapter
import com.tokopedia.talk.common.adapter.CommentTalkTypeFactoryImpl
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import kotlinx.android.synthetic.main.talk_item.view.*

class ProductTalkThreadViewHolder(val v: View,
                                    val listener: TalkItemListener) :
        AbstractViewHolder<TalkThreadViewModel>(v) {


    interface TalkItemListener {
        fun onReplyTalkButtonClick(allowReply: Boolean)
        fun onMenuButtonClicked(menu: TalkState)
    }

    companion object {
        val LAYOUT = R.layout.product_talk_item
    }

    val thread:View = itemView.findViewById(R.id.thread_head)
    val avatar: ImageView = thread.findViewById(R.id.prof_pict)
    val userName : TextView = thread.findViewById(R.id.username)
    val timestamp : TextView = thread.findViewById(R.id.timestamp)
    val menu : View = thread.findViewById(R.id.menu)
    val content : TextView = thread.findViewById(R.id.talk_content)
    val commentRecyclerView: RecyclerView = itemView.findViewById(R.id.list_child)
    val replyButton: View = itemView.findViewById(R.id.replyButton)
    val menuButton: ImageView = itemView.menu

    private lateinit var adapter: CommentTalkAdapter

    override fun bind(element: TalkThreadViewModel) {
        element?.run {

            if (!element.listChild.isEmpty()) {
                val typeFactoryImpl = CommentTalkTypeFactoryImpl()
                adapter = CommentTalkAdapter(typeFactoryImpl, element.listChild)
                commentRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager
                        .VERTICAL, false)
                commentRecyclerView.adapter = adapter
                commentRecyclerView.visibility = View.VISIBLE
            } else {
                commentRecyclerView.visibility = View.GONE
            }

            ImageHandler.loadImageCircle2(avatar.context, avatar, element.headThread.avatar)
            userName.text = element.headThread.name
            content.text = element.headThread.comment
            timestamp.text = element.headThread.timestamp

            replyButton.setOnClickListener {
                listener.onReplyTalkButtonClick(element.headThread.menu.allowReply)
            }

            setupMenuButton(element.headThread.menu)

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
