package com.tokopedia.topchat.chattemplate.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel

class ItemAddTemplateChatViewHolder(
    var view: View, private var viewListener: TemplateChatContract.View
) : AbstractViewHolder<TemplateChatUiModel>(view) {

    var textView: TextView? = itemView.findViewById(R.id.caption)
    var imageView: ImageView? = itemView.findViewById(R.id.setting)
    var layout : LinearLayout? = itemView.findViewById(R.id.layout)

    override fun bind(element: TemplateChatUiModel) {
        if (element.size() >= TEMPLATE_COUNT_THRESHOLD) {
            imageView?.setImageDrawable(
                MethodChecker.getDrawable(
                    view.context,
                    R.drawable.ic_plus_grey
                )
            )
            textView?.setTextColor(
                MethodChecker.getColor(
                    view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_20
                )
            )
            layout?.setBackgroundResource(
                R.drawable.bg_item_add_template_chat_settings_grey
            )
        } else {
            imageView?.setImageDrawable(
                MethodChecker.getDrawable(
                    view.context,
                    R.drawable.ic_topchat_plus_green
                )
            )
            textView?.setTextColor(
                MethodChecker.getColor(
                    view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
            layout?.setBackgroundResource(
                R.drawable.bg_item_add_template_chat_settings_active
            )
        }
        view.setOnClickListener { viewListener.onEnter(null, adapterPosition) }
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_add_template_chat_settings
        private const val TEMPLATE_COUNT_THRESHOLD = 5
    }

}