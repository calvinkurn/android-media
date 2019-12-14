package com.tokopedia.notifcenter.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.NotificationUpdateFilterSectionItemViewHolder
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel

/**
 * @author : Steven 10/04/19
 */
interface NotificationUpdateFilterSectionTypeFactory{

    fun createViewHolder(
            view: View,
            viewType: Int,
            filterListener: NotificationUpdateFilterSectionItemViewHolder.FilterSectionListener
    ): AbstractViewHolder<*>

    fun type(viewModel: NotificationUpdateFilterSectionItemViewModel): Int
}