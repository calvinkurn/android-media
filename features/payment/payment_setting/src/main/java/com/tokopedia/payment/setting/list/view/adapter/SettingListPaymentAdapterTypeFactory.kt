package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.list.model.SettingBannerModel
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import com.tokopedia.payment.setting.list.model.SettingListCardCounterModel
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.list.view.listener.SettingListActionListener

class SettingListPaymentAdapterTypeFactory(val actionListener: SettingListActionListener) : BaseAdapterTypeFactory() {

    fun type(settingListPaymentModel: SettingListPaymentModel): Int {
        return SettingListPaymentViewHolder.LAYOUT
    }

    fun type(settingListAddCardModel: SettingListAddCardModel): Int {
        return SettingListAddCardViewHolder.LAYOUT
    }

    fun type(settingBannerModel: SettingBannerModel): Int {
        return SettingBannerViewHolder.LAYOUT
    }

    fun type(settingListCardCounterModel: SettingListCardCounterModel): Int {
        return SettingListCardCounterViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return SettingListEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            SettingListPaymentViewHolder.LAYOUT -> {
                return SettingListPaymentViewHolder(parent)
            }
            SettingListEmptyViewHolder.LAYOUT -> {
                return SettingListEmptyViewHolder(parent, actionListener)
            }
            SettingListAddCardViewHolder.LAYOUT -> {
                return SettingListAddCardViewHolder(parent, actionListener)
            }
            SettingBannerViewHolder.LAYOUT -> {
                return SettingBannerViewHolder(parent, actionListener)
            }
            SettingListCardCounterViewHolder.LAYOUT -> {
                return SettingListCardCounterViewHolder(parent, actionListener)
            }
            else -> return super.createViewHolder(parent, type)
        }
    }
}
