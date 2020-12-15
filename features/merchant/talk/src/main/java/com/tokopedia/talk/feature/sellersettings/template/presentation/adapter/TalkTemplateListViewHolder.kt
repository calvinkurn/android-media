package com.tokopedia.talk.feature.sellersettings.template.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.talk.feature.sellersettings.template.presentation.listener.TalkTemplateListListener
import com.tokopedia.talk.R
import kotlinx.android.synthetic.main.item_talk_template.view.*

class TalkTemplateListViewHolder(private val view: View, private val talkTemplateListListener: TalkTemplateListListener) : RecyclerView.ViewHolder(view) {

    fun bind(template: String) {
        view.apply {
            talkTemplateLayout.setBackgroundResource(R.drawable.bg_talk_template)
            talkTemplate.text = template
            talkEditTemplateIcon.setOnClickListener {
                talkTemplateListListener.onEditClicked(template, adapterPosition)
            }
        }
    }
}