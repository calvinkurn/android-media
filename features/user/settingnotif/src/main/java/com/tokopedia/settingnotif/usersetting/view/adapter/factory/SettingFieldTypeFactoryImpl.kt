package com.tokopedia.settingnotif.usersetting.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.data.pojo.*
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.*
import com.tokopedia.settingnotif.usersetting.view.listener.ActivationItemListener
import com.tokopedia.settingnotif.usersetting.view.listener.SectionItemListener
import com.tokopedia.user.session.UserSessionInterface

class SettingFieldTypeFactoryImpl(
        private val sectionListener: SectionItemListener,
        private val activationListener: ActivationItemListener,
        private val userSession: UserSessionInterface
) : BaseAdapterTypeFactory(), SettingFieldTypeFactory {

    override fun type(notificationActivation: NotificationActivation): Int = ActivationItemViewHolder.LAYOUT
    override fun type(settingSections: SettingSections): Int = SettingSectionViewHolder.LAYOUT
    override fun type(sellerSection: SellerSection): Int = SellerSectionViewHolder.LAYOUT
    override fun type(changeSection: ChangeSection): Int = ChangeItemViewHolder.LAYOUT
    override fun type(parentSetting: ParentSetting): Int = ParentSettingViewHolder.LAYOUT
    override fun type(childSetting: ChildSetting): Int = ChildSettingViewHolder.LAYOUT
    override fun type(smsSection: SmsSection): Int = SmsSectionViewHolder.LAYOUT

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
            SellerSectionViewHolder.LAYOUT -> SellerSectionViewHolder(sectionListener, parent)
            ChangeItemViewHolder.LAYOUT -> ChangeItemViewHolder(userSession, parent)
            ActivationItemViewHolder.LAYOUT -> ActivationItemViewHolder(activationListener, parent)
            SmsSectionViewHolder.LAYOUT -> SmsSectionViewHolder(parent)
            else -> createViewHolder(parent, type)
        }
    }

}