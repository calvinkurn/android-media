package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel

/**
 * @author : Steven 10/04/19
 */
interface NotificationUpdateFilterSectionTypeFactory{

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(viewModel: NotificationUpdateFilterSectionItemViewModel): Int
}