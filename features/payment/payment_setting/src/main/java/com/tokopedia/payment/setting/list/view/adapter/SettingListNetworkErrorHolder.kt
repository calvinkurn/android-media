package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import kotlinx.android.synthetic.main.item_empty_view_payment.view.*

class SettingListNetworkErrorHolder(view : View?, val listenerEmptyViewHolder: SettingListEmptyViewHolder.ListenerEmptyViewHolder?) : AbstractViewHolder<ErrorNetworkModel>(view) {

    override fun bind(element: ErrorNetworkModel?) {
        itemView?.buttonAddCard?.setOnClickListener{
            listenerEmptyViewHolder?.onClickAddCard()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_empty_view_payment
    }
}