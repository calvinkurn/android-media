package com.tokopedia.topchat.chattemplate.view.adapter.viewholder

import android.view.View
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatModel
import com.tokopedia.unifycomponents.ChipsUnify
import androidx.annotation.LayoutRes
import com.tokopedia.topchat.R

/**
 * Created by stevenfredian on 11/29/17.
 */
class TemplateChatViewHolder(
    itemView: View, private var viewListener: ChatTemplateListener
) : AbstractViewHolder<TemplateChatModel>(itemView) {

    var textHolder: ChipsUnify? = itemView.findViewById(R.id.chipsText)

    override fun bind(element: TemplateChatModel) {
        textHolder?.chipText = element.ellipsizedMessage
        textHolder?.setOnClickListener { view: View? ->
            viewListener.addTemplateString(element.message)
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_template_chat_layout
    }

}