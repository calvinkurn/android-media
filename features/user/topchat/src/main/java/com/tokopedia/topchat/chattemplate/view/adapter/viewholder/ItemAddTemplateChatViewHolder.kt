package com.tokopedia.topchat.chattemplate.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract

/**
 * Created by stevenfredian on 11/29/17.
 */
class ItemAddTemplateChatViewHolder(
    var view: View, private var viewListener: TemplateChatContract.View
) : AbstractViewHolder<TemplateChatUiModel>(view) {

    var textView: TextView? = itemView.findViewById(R.id.caption)
    var imageView: ImageView? = itemView.findViewById(R.id.setting)

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
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_20
                )
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
                    com.tokopedia.unifyprinciples.R.color.Unify_G400
                )
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