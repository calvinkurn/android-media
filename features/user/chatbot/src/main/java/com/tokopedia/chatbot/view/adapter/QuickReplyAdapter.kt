package com.tokopedia.chatbot.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.design.component.Dialog

/**
 * @author by yfsx on 08/05/18.
 */

class QuickReplyAdapter(private var quickReplyListViewModel: QuickReplyListViewModel = QuickReplyListViewModel(),
                        private val listener: QuickReplyListener) : RecyclerView.Adapter<QuickReplyAdapter.Holder>() {

    private val END_CHAT = "end chat"

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        return Holder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.chatbot_item_quick_reply, viewGroup, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val model = quickReplyListViewModel.quickReplies[position]
        holder.text.text = model.text
        holder.text.setOnClickListener {
            if(model.text.equals(END_CHAT,true)){
                val mContext = holder.itemView.context
                val dialog = Dialog(mContext as Activity, Dialog.Type.PROMINANCE)
                dialog.setTitle(mContext.getString(R.string.cb_bot_end_live_chat))
                dialog.setDesc(mContext.getString(R.string.cb_bot_end_live_chat_desc))
                dialog.setBtnOk(mContext.getString(R.string.cb_bot_yes_end_text))
                dialog.setBtnCancel(mContext.getString(R.string.cb_bot_cancel_text))
                dialog.setOnOkClickListener{
                    listener.onQuickReplyClicked(quickReplyListViewModel, model)
                    dialog.dismiss()
                }
                dialog.setOnCancelClickListener{
                    dialog.dismiss()
                }
                dialog.setCancelable(true)
                dialog.show()
            }else{
                listener.onQuickReplyClicked(quickReplyListViewModel, model)
            }
        }
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
