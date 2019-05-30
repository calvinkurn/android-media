package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.NotificationUpdateFilterItemViewHolder
import com.tokopedia.navigation.presentation.view.listener.NotificationSectionFilterListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFilterTypeFactoryImpl(var listener: NotificationSectionFilterListener)
    : BaseAdapterTypeFactory(), NotificationUpdateFilterTypeFactory {


    override fun type(viewModel: NotificationUpdateFilterItemViewModel): Int {
        return NotificationUpdateFilterItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>

        if(type == NotificationUpdateFilterItemViewHolder.LAYOUT) {
            viewHolder = NotificationUpdateFilterItemViewHolder(parent, listener)
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}