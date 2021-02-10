package com.tokopedia.talk.feature.sellersettings.template.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.talk.feature.sellersettings.template.presentation.listener.TalkTemplateListListener
import com.tokopedia.talk.R
import com.tokopedia.unifyprinciples.Typography

class TalkTemplateListViewHolder(private val view: View, private val talkTemplateListListener: TalkTemplateListListener) : RecyclerView.ViewHolder(view) {

    private var talkTemplateLayout: View? = null
    private var talkTemplate: Typography? = null
    private var talkEditTemplateIcon: IconUnify? = null

    fun bind(template: String) {
        view.apply {
            findViews()
            talkTemplate?.text = template
            talkEditTemplateIcon?.setOnClickListener {
                talkTemplateListListener.onEditClicked(template, adapterPosition)
            }
        }
    }

    private fun findViews() {
        talkTemplateLayout = itemView.findViewById(R.id.talkTemplateLayout)
        talkTemplate = itemView.findViewById(R.id.talkTemplate)
        talkEditTemplateIcon = itemView.findViewById(R.id.talkEditTemplateIcon)
    }
}