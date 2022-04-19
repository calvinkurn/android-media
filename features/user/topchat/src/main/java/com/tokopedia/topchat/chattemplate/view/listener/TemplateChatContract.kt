package com.tokopedia.topchat.chattemplate.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatSettingAdapter
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder
import java.util.*

class TemplateChatContract {
    interface View : CustomerView {
        fun setTemplate(listTemplate: List<Visitable<*>>?)
        fun onDrag(viewHolder: ItemTemplateChatViewHolder)
        fun onEnter(message: String?, adapterPosition: Int)
        fun setChecked(b: Boolean)
        fun reArrange(from: Int, to: Int)
        val list: ArrayList<String>
        val adapter: TemplateChatSettingAdapter
        fun successRearrange()
        fun showError(errorMessage: Throwable)
        fun successSwitch()
        fun showLoading()
        fun finishLoading()
        fun revertArrange(from: Int, to: Int)
    }
}