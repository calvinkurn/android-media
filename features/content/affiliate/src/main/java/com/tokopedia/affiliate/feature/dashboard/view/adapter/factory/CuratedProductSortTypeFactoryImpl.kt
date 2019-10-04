package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.CuratedProductSortViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CuratedProductSortViewModel

/**
 * Created by jegul on 2019-09-09.
 */
class CuratedProductSortTypeFactoryImpl(private val listener: OnClickListener) : BaseAdapterTypeFactory(), CuratedProductSortTypeFactory {

    interface OnClickListener {
        fun onSortClicked(sortId: Int?)
    }

    override fun type(curatedProductSortViewModel: CuratedProductSortViewModel): Int {
        return CuratedProductSortViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return if (viewType == CuratedProductSortViewHolder.LAYOUT) {
            CuratedProductSortViewHolder(view, listener::onSortClicked)
        } else {
            super.createViewHolder(view, viewType)
        }
    }
}