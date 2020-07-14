package com.tokopedia.talk_old.addtalk.view.adapter

import android.text.Html
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.R

/**
 * @author : Steven 17/09/18
 */

class QuickReplyTalkViewHolder(val v: View,
                                      val listener: PasteTemplateListener) :
        AbstractViewHolder<TalkQuickReplyItemViewModel>(v) {

    interface PasteTemplateListener {
        fun onTemplateClicked()
    }

    companion object {
        val LAYOUT = R.layout.item_quick_reply
    }

    val textHolder: TextView = itemView.findViewById(R.id.text)


    override fun bind(element: TalkQuickReplyItemViewModel) {
        element?.run {

            textHolder.text = (Html.fromHtml(element.getText()))

            textHolder.setOnClickListener { listener.onTemplateClicked() }

            textHolder.setVisibility(View.VISIBLE)
        }
    }

}