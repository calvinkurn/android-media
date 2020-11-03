package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.View
import androidx.annotation.LayoutRes
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

    @LayoutRes
    override fun getItemViewType(
            visitables: List<Visitable<*>>,
            position: Int,
            default: Int
    ): Int {
        val item = visitables.getOrNull(position)
        if (item is NotificationUiModel) {
            return when (item.typeLink) {
                NotificationUiModel.TYPE_DEFAULT -> NormalNotificationViewHolder.LAYOUT
                else -> NormalNotificationViewHolder.LAYOUT
            }
        }
        return default
    }


    override fun createViewHolder(view: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SectionTitleViewHolder.LAYOUT -> SectionTitleViewHolder(view)
            BigDividerViewHolder.LAYOUT -> BigDividerViewHolder(view)
            NormalNotificationViewHolder.LAYOUT -> NormalNotificationViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

}