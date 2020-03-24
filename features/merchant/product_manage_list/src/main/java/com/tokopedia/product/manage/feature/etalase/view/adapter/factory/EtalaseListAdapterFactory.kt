package com.tokopedia.product.manage.feature.etalase.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.etalase.data.model.EtalaseViewModel
import com.tokopedia.product.manage.feature.etalase.view.adapter.viewholder.EtalaseViewHolder

class EtalaseListAdapterFactory(
    private val listener: EtalaseViewHolder.OnClickListener
): BaseAdapterTypeFactory(), EtalaseListTypeFactory {

    override fun type(viewModel: EtalaseViewModel): Int = EtalaseViewHolder.LAYOUT

    override fun createViewHolder(itemView: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            EtalaseViewHolder.LAYOUT -> EtalaseViewHolder(itemView, listener)
            else -> super.createViewHolder(itemView, type)
        }
    }
}