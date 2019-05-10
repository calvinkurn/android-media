package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

/**
 * @author : Steven 10/04/19
 */
interface NotificationUpdateTypeFactory{

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(notificationUpdateDefaultViewModel: NotificationUpdateItemViewModel): Int
}