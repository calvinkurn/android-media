package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.talk.databinding.ItemTalkReplyTemplateBinding
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTemplateListener

class TalkReplyTemplateViewHolder(view: View, private val binding: ItemTalkReplyTemplateBinding) : RecyclerView.ViewHolder(view) {

    fun bind(template: String, talkReplyTemplateListener: TalkReplyTemplateListener) {
        itemView.apply {
            binding.replyTemplateChip.chip_text.text = template
            setOnClickListener {
                talkReplyTemplateListener.onTemplateClicked(template)
            }
        }
    }
}
