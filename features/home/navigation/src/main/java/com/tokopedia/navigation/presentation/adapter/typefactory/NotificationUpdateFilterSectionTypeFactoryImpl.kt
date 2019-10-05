package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.NotificationUpdateFilterSectionItemViewHolder
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFilterSectionTypeFactoryImpl : BaseAdapterTypeFactory(),
        NotificationUpdateFilterSectionTypeFactory {

    override fun type(viewModel: NotificationUpdateFilterSectionItemViewModel): Int {
        return NotificationUpdateFilterSectionItemViewHolder.LAYOUT
    }

    override fun createViewHolder(
            parent: View,
            viewType: Int,
            filterListener: NotificationUpdateFilterSectionItemViewHolder.FilterSectionListener
    ): AbstractViewHolder<*> {
        return when (viewType) {
            NotificationUpdateFilterSectionItemViewHolder.LAYOUT -> NotificationUpdateFilterSectionItemViewHolder(parent, filterListener)
            else -> super.createViewHolder(parent, viewType)
        }
    }

}