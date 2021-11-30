package com.tokopedia.chatbot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.analytics.ChatbotAnalytics
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener

/**
 * @author by yfsx on 08/05/18.
 */

private const val ACTION_IMRESSION_QUICK_REPLIES = "impression quick reply"
class QuickReplyAdapter(private var quickReplyList: List<QuickReplyViewModel>,
                        private val listener: QuickReplyListener) : RecyclerView.Adapter<QuickReplyAdapter.Holder>() {

    private val END_CHAT = "end chat"

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        return Holder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.chatbot_item_quick_reply, viewGroup, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val model = quickReplyList[position]
        holder.text.text = model.text
        holder.text.setOnClickListener {
            listener.onQuickReplyClicked(model)
        }
    }

    override fun getItemCount(): Int {
        return quickReplyList.size
    }

    fun clearData() {
        quickReplyList = ArrayList()
        notifyDataSetChanged()
    }

    fun setList(quickReplylist: List<QuickReplyViewModel>) {
        this.quickReplyList = quickReplylist
        notifyDataSetChanged()
        if (quickReplylist.isNotEmpty()){
            ChatbotAnalytics.chatbotAnalytics.eventShowView(ACTION_IMRESSION_QUICK_REPLIES)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var text: TextView

        init {
            text = itemView.findViewById<View>(R.id.text) as TextView
        }
    }
}
