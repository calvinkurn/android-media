package com.tokopedia.notifcenter.presentation.adapter.typefactory.update

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.domain.model.EmptyUpdateState
import com.tokopedia.notifcenter.presentation.adapter.typefactory.base.BaseNotificationTypeFactory

/**
 * @author : Steven 10/04/19
 */
interface NotificationUpdateTypeFactory : BaseNotificationTypeFactory {
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
    fun type(emptyState: EmptyUpdateState): Int
}