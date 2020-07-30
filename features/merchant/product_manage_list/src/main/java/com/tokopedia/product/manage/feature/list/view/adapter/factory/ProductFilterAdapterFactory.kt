package com.tokopedia.product.manage.feature.list.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel

interface ProductFilterAdapterFactory {

    fun type(viewModel: FilterTabViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}