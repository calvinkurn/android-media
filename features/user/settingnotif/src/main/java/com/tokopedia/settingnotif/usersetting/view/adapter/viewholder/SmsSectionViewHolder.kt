package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.SmsSection

class SmsSectionViewHolder(itemView: View?): AbstractViewHolder<SmsSection>(itemView) {

    private val context by lazy { itemView?.context }

    override fun bind(element: SmsSection?) {}

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_sms_section
    }

}