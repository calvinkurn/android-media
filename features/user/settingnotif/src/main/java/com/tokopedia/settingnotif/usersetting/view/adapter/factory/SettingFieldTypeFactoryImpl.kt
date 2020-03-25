package com.tokopedia.settingnotif.usersetting.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.*
import com.tokopedia.settingnotif.usersetting.view.listener.SectionItemListener
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.*

class SettingFieldTypeFactoryImpl(
        private val listener: SectionItemListener
) : BaseAdapterTypeFactory(), SettingFieldTypeFactory {

    override fun type(settingSections: SettingSections): Int = SettingSectionViewHolder.LAYOUT
    override fun type(parentSetting: ParentSetting): Int = ParentSettingViewHolder.LAYOUT
    override fun type(childSetting: ChildSetting): Int = ChildSettingViewHolder.LAYOUT
    override fun type(settingSections: NotificationActivation): Int = PushNotifActivationViewHolder.LAYOUT
    override fun type(settingSections: SellerSection): Int = SellerSectionViewHolder.LAYOUT
    override fun type(parentSetting: SmsSection): Int = SmsSectionViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SettingSectionViewHolder.LAYOUT -> SettingSectionViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun createViewHolder(
            parent: View,
            type: Int,
            settingListener: SettingViewHolder.SettingListener
    ): AbstractViewHolder<*> {
        return when (type) {
            ParentSettingViewHolder.LAYOUT -> ParentSettingViewHolder(parent, settingListener)
            ChildSettingViewHolder.LAYOUT -> ChildSettingViewHolder(parent, settingListener)
            PushNotifActivationViewHolder.LAYOUT -> PushNotifActivationViewHolder(parent)
            SellerSectionViewHolder.LAYOUT -> SellerSectionViewHolder(listener, parent)
            SmsSectionViewHolder.LAYOUT -> SmsSectionViewHolder(parent)
            else -> createViewHolder(parent, type)
        }
    }

}