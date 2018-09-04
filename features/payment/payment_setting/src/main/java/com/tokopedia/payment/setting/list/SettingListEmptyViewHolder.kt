package com.tokopedia.payment.setting.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.payment.setting.R
import kotlinx.android.synthetic.main.item_empty_view_payment.view.*

class SettingListEmptyViewHolder(view : View?, val listenerEmptyViewHolder: ListenerEmptyViewHolder?) : AbstractViewHolder<EmptyModel>(view) {

    override fun bind(element: EmptyModel?) {
        itemView?.buttonAddCard?.setOnClickListener{
            listenerEmptyViewHolder?.onClickAddCard()
        }
    }

    interface ListenerEmptyViewHolder {
        fun onClickAddCard()
    }

    companion object {
        val LAYOUT = R.layout.item_empty_view_payment
    }
}