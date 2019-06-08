package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.NotificationUpdateItemViewHolder
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateTypeFactoryImpl(var notificationUpdateListener: NotificationUpdateItemListener) : BaseAdapterTypeFactory(), NotificationUpdateTypeFactory {


    override fun type(notificationUpdateDefaultViewModel: NotificationUpdateItemViewModel): Int {
        return NotificationUpdateItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>

        if(type == NotificationUpdateItemViewHolder.LAYOUT) {
            viewHolder = NotificationUpdateItemViewHolder(
                    itemView = parent,
                    listener = notificationUpdateListener
            )
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}