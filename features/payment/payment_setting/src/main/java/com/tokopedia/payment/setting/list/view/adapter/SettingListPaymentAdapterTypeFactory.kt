package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel

class SettingListPaymentAdapterTypeFactory(val listenerEmptyViewHolder: SettingListEmptyViewHolder.ListenerEmptyViewHolder) : BaseAdapterTypeFactory() {

    fun type(settingListPaymentModel: SettingListPaymentModel) : Int {
        return SettingListPaymentViewHolder.LAYOUT;
    }

    fun type(settingListAddCardModel: SettingListAddCardModel) : Int{
        return SettingListAddCardViewHolder.LAYOUT;
    }

    override fun type(viewModel: EmptyModel?): Int {
        return SettingListEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        if(type == SettingListPaymentViewHolder.LAYOUT){
            return SettingListPaymentViewHolder(parent)
        }else if(type == SettingListEmptyViewHolder.LAYOUT){
            return SettingListEmptyViewHolder(parent, listenerEmptyViewHolder)
        }else if(type == SettingListAddCardViewHolder.LAYOUT){
            return SettingListAddCardViewHolder(parent, listenerEmptyViewHolder)
        }
        return super.createViewHolder(parent, type)
    }
}
