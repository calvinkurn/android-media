package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.ItemSectionListener
import com.tokopedia.navigation.presentation.adapter.viewholder.NotificationUpdateFilterSectionItemViewHolder
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFilterSectionTypeFactoryImpl(var listener: ItemSectionListener, var filterType: String) : BaseAdapterTypeFactory(), NotificationUpdateFilterSectionTypeFactory {


    override fun type(viewModel: NotificationUpdateFilterSectionItemViewModel): Int {
        return NotificationUpdateFilterSectionItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>

        if(type == NotificationUpdateFilterSectionItemViewHolder.LAYOUT) {
            viewHolder = NotificationUpdateFilterSectionItemViewHolder(parent, listener, filterType)
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}