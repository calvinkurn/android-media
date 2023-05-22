package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import com.tokopedia.payment.setting.list.view.listener.SettingListActionListener
import kotlinx.android.synthetic.main.item_add_card_payment.view.*

class SettingListAddCardViewHolder(view: View?, private val actionListener: SettingListActionListener) :
    AbstractViewHolder<SettingListAddCardModel>(view) {

    override fun bind(element: SettingListAddCardModel?) {
        itemView.buttonAddCard?.setOnClickListener {
            actionListener.onClickAddCard()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_add_card_payment
    }
}
