package com.tokopedia.chatbot.chatbot2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotQuickReplyViewBinding

/**
 * @author by yfsx on 08/05/18.
 */

private const val ACTION_IMRESSION_QUICK_REPLIES = "impression quick reply"
class QuickReplyAdapter(
    private var quickReplyList: List<QuickReplyUiModel>,
    private val listener: QuickReplyListener,
    val sendAnalyticsFromAdapter: (impressionType: String) -> Unit
) : RecyclerView.Adapter<QuickReplyAdapter.Holder>() {

    private var isFromDynamicAttachment: Boolean = false
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val view = ItemChatbotQuickReplyViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val model = quickReplyList[position]
        holder.text.text = model.text
        holder.text.setOnClickListener {
            listener.onQuickReplyClicked(model, isFromDynamicAttachment)
        }
    }

    override fun getItemCount(): Int {
        return quickReplyList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        quickReplyList = ArrayList()
        notifyDataSetChanged()
        isFromDynamicAttachment = false
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(quickReplylist: List<QuickReplyUiModel>, isFromDynamicAttachment: Boolean = false) {
        this.quickReplyList = quickReplylist
        this.isFromDynamicAttachment = isFromDynamicAttachment
        notifyDataSetChanged()
        if (quickReplylist.isNotEmpty()) {
            sendAnalyticsFromAdapter(ACTION_IMRESSION_QUICK_REPLIES)
        }
    }

    inner class Holder(itemView: ItemChatbotQuickReplyViewBinding) : RecyclerView.ViewHolder(itemView.root) {
        internal var text: TextView

        init {
            text = itemView.text
        }
    }
}
