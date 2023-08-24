package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.databinding.ItemAddCardPaymentBinding
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import com.tokopedia.payment.setting.list.view.listener.SettingListActionListener

class SettingListAddCardViewHolder(
    view: View,
    private val actionListener: SettingListActionListener
) : AbstractViewHolder<SettingListAddCardModel>(view) {

    private val binding = ItemAddCardPaymentBinding.bind(itemView)

    override fun bind(element: SettingListAddCardModel?) {
        binding.buttonAddCard.setOnClickListener {
            actionListener.onClickAddCard()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_add_card_payment
    }
}
