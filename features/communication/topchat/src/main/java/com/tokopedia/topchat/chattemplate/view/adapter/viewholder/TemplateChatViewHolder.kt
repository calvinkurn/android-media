package com.tokopedia.topchat.chattemplate.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class TemplateChatViewHolder(
    itemView: View, private var viewListener: ChatTemplateListener
) : AbstractViewHolder<TemplateChatUiModel>(itemView) {

    var textHolder: ChipsUnify? = itemView.findViewById(R.id.chipsText)

    override fun bind(element: TemplateChatUiModel) {
        textHolder?.chipText = element.ellipsizedMessage
        textHolder?.setOnClickListener { view: View? ->
            viewListener.addTemplateString(element.message)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_template_chat_layout
    }

}