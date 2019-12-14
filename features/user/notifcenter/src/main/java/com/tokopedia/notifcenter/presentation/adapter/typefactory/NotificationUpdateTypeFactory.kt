package com.tokopedia.notifcenter.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.domain.model.EmptyUpdateState
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateItemViewModel

/**
 * @author : Steven 10/04/19
 */
interface NotificationUpdateTypeFactory{
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
    fun type(notificationUpdateDefaultViewModel: NotificationUpdateItemViewModel): Int
    fun type(emptyState: EmptyUpdateState): Int
}