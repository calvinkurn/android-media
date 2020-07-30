package com.tokopedia.settingnotif.usersetting.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.data.pojo.*
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder

typealias VisitableSettings = Visitable<SettingFieldTypeFactory>

interface SettingFieldTypeFactory : AdapterTypeFactory {
    fun type(notificationActivation: NotificationActivation): Int
    fun type(settingSections: SettingSections): Int
    fun type(sellerSection: SellerSection): Int
    fun type(changeSection: ChangeSection): Int
    fun type(parentSetting: ParentSetting): Int
    fun type(childSetting: ChildSetting): Int
    fun type(smsSection: SmsSection): Int
    fun createViewHolder(
            parent: View,
            type: Int,
            settingListener: SettingViewHolder.SettingListener
    ): AbstractViewHolder<*>
}