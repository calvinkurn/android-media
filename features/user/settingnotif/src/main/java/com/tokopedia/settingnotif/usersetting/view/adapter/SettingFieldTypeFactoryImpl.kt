package com.tokopedia.settingnotif.usersetting.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.*
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.*

class SettingFieldTypeFactoryImpl : BaseAdapterTypeFactory(), SettingFieldTypeFactory {
    override fun type(settingSections: SettingSections): Int {
        return SettingSectionViewHolder.LAYOUT
    }

    override fun type(parentSetting: ParentSetting): Int {
        return ParentSettingViewHolder.LAYOUT
    }

    override fun type(childSetting: ChildSetting): Int {
        return ChildSettingViewHolder.LAYOUT
    }

    override fun type(pushNotifierTroubleshooterSetting: PushNotifierTroubleshooterSetting): Int {
        return PushNotifCheckerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SettingSectionViewHolder.LAYOUT -> SettingSectionViewHolder(parent)
            PushNotifCheckerViewHolder.LAYOUT -> PushNotifCheckerViewHolder(parent)
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
            else -> createViewHolder(parent, type)
        }
    }

}