package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.databinding.ItemSettingCardCounterViewHolderBinding
import com.tokopedia.payment.setting.list.model.SettingListCardCounterModel
import com.tokopedia.payment.setting.list.view.listener.SettingListActionListener

class SettingListCardCounterViewHolder(
    itemView: View,
    private val listener: SettingListActionListener
) : AbstractViewHolder<SettingListCardCounterModel>(itemView) {

    private val binding = ItemSettingCardCounterViewHolderBinding.bind(itemView)

    companion object {
        val LAYOUT = R.layout.item_setting_card_counter_view_holder
    }

    override fun bind(data: SettingListCardCounterModel) {
        binding.settingListCardCounter.text =
            itemView.context.getString(R.string.payment_label_saved_card, data.size)

        listener.onPaymentListImpressed()
    }
}
