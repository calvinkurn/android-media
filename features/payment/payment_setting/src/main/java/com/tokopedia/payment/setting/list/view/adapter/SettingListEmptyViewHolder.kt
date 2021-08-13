package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import kotlinx.android.synthetic.main.item_empty_view_payment.view.*

class SettingListEmptyViewHolder(view : View?, val listenerEmptyViewHolder: ListenerEmptyViewHolder?) :
        AbstractViewHolder<Visitable<SettingListPaymentAdapterTypeFactory>>(view) {

    override fun bind(element: Visitable<SettingListPaymentAdapterTypeFactory>?) {
        itemView.buttonAddCard?.setOnClickListener{
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