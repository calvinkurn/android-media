package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.BigDividerViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.NormalNotificationViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.SectionTitleViewHolder

class NotificationTypeFactoryImpl : BaseAdapterTypeFactory(), NotificationTypeFactory {

    override fun type(sectionTitleUiModel: SectionTitleUiModel): Int {
        return SectionTitleViewHolder.LAYOUT
    }

    override fun type(bigDividerUiModel: BigDividerUiModel): Int {
        return BigDividerViewHolder.LAYOUT
    }

    override fun type(notificationUiModel: NotificationUiModel): Int {
        return NormalNotificationViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SectionTitleViewHolder.LAYOUT -> SectionTitleViewHolder(parent)
            BigDividerViewHolder.LAYOUT -> BigDividerViewHolder(parent)
            NormalNotificationViewHolder.LAYOUT -> NormalNotificationViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}