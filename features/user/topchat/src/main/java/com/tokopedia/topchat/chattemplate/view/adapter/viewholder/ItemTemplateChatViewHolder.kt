package com.tokopedia.topchat.chattemplate.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.annotation.LayoutRes
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract

class ItemTemplateChatViewHolder(
    itemView: View, private var viewListener: TemplateChatContract.View
) : AbstractViewHolder<TemplateChatUiModel>(itemView) {

    var textHolder: TextView? = itemView.findViewById(R.id.caption)
    private val setting: View? = itemView.findViewById(R.id.setting)
    private val edit: View? = itemView.findViewById(R.id.edit)

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(element: TemplateChatUiModel) {
        textHolder?.text = element.message
        val ini = this
        setting?.setOnTouchListener { v, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                viewListener.onDrag(ini)
            }
            false
        }
        itemView.setOnClickListener { viewListener.onEnter(element.message, adapterPosition) }
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_template_chat_settings
    }

}