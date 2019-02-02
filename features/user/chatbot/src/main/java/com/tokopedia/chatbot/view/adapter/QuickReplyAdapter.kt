package com.tokopedia.chatbot.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener

/**
 * @author by yfsx on 08/05/18.
 */

class QuickReplyAdapter(private var quickReplyListViewModel: QuickReplyListViewModel = QuickReplyListViewModel(),
                        private val listener: QuickReplyListener) : RecyclerView.Adapter<QuickReplyAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        return Holder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_quick_reply, viewGroup, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val model = quickReplyListViewModel.quickReplies[position]
        holder.text.text = model.text
        holder.text.setOnClickListener { view -> listener.onQuickReplyClicked(quickReplyListViewModel, model) }
    }

    override fun getItemCount(): Int {
        return quickReplyListViewModel.quickReplies.size
    }

    fun clearData() {
        quickReplyListViewModel = quickReplyListViewModel.EMPTY()
        notifyDataSetChanged()
    }

    fun setList(quickReplyListViewModel: QuickReplyListViewModel) {
        this.quickReplyListViewModel = quickReplyListViewModel
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var text: TextView

        init {
            text = itemView.findViewById<View>(R.id.text) as TextView
        }
    }
}
