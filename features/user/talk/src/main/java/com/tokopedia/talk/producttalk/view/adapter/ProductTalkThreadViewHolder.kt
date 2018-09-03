package com.tokopedia.talk

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class ProductTalkThreadViewHolder(val v: View) :
        AbstractViewHolder<TalkThreadViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.thread_talk
    }

    val thread:View = itemView.findViewById(R.id.thread_head)
    val avatar: ImageView = thread.findViewById(R.id.prof_pict)
    val userName : TextView = thread.findViewById(R.id.username)
    val timestamp : TextView = thread.findViewById(R.id.timestamp)
    val menu : View = thread.findViewById(R.id.menu)
    val content : TextView = thread.findViewById(R.id.talk_content)
    val listChild: RecyclerView = itemView.findViewById(R.id.list_child)

    override fun bind(element: TalkThreadViewModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
