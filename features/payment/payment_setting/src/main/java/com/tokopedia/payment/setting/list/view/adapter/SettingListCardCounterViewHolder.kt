package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.SettingListCardCounterModel
import kotlinx.android.synthetic.main.item_setting_card_counter_view_holder.view.*

class SettingListCardCounterViewHolder(itemView : View?)
    : AbstractViewHolder<SettingListCardCounterModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_setting_card_counter_view_holder
    }

    override fun bind(data: SettingListCardCounterModel) {
        itemView.settingListCardCounter.text =
            itemView.context.getString(R.string.payment_label_saved_card, data.size)
    }

}
