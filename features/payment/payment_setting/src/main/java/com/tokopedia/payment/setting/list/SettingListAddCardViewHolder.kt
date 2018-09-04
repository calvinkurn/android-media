package com.tokopedia.payment.setting.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import kotlinx.android.synthetic.main.item_add_card_payment.view.*

class SettingListAddCardViewHolder(view : View?,
                                   val listenerEmptyViewHolder: SettingListEmptyViewHolder.ListenerEmptyViewHolder)
    : AbstractViewHolder<SettingListAddCardModel>(view) {

    override fun bind(element: SettingListAddCardModel?) {
        itemView.buttonAddCard?.setOnClickListener{
            listenerEmptyViewHolder.onClickAddCard()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_add_card_payment
    }
}
