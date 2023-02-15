package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel

class SettingBannerViewHolder(itemView : View?)
    : AbstractViewHolder<SettingListAddCardModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_setting_banner_view_holder
    }

    override fun bind(element: SettingListAddCardModel?) {

    }

}
