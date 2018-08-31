package com.tokopedia.payment.setting.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class SettingListPaymentAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(settingListPaymentModel: SettingListPaymentModel) : Int {
        return SettingListPaymentViewHolder.LAYOUT;
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        if(type == SettingListPaymentViewHolder.LAYOUT){
            return SettingListPaymentViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }
}
