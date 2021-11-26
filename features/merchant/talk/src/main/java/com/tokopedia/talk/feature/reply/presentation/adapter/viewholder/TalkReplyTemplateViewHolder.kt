package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.talk.databinding.ItemTalkReplyTemplateBinding
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTemplateListener

class TalkReplyTemplateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTalkReplyTemplateBinding.bind(view)

    fun bind(template: String, talkReplyTemplateListener: TalkReplyTemplateListener) {
        binding.root.apply {
            binding.replyTemplateChip.chip_text.text = template
            setOnClickListener {
                talkReplyTemplateListener.onTemplateClicked(template)
            }
        }
    }
}
