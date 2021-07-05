package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTemplateListener
import kotlinx.android.synthetic.main.item_talk_reply_template.view.*

class TalkReplyTemplateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(template: String, talkReplyTemplateListener: TalkReplyTemplateListener) {
        itemView.apply {
            replyTemplateChip.chip_text.text = template
            setOnClickListener {
                talkReplyTemplateListener.onTemplateClicked(template)
            }
        }
    }
}
