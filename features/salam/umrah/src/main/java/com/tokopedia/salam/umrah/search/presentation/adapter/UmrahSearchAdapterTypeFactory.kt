package com.tokopedia.salam.umrah.search.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.salam.umrah.search.presentation.adapter.viewholder.UmrahSearchLoadingViewHolder
import com.tokopedia.salam.umrah.search.presentation.adapter.viewholder.UmrahSearchViewHolder

/**
 * @author by furqan on 20/10/2019
 */
class UmrahSearchAdapterTypeFactory(private val callback:BaseEmptyViewHolder.Callback) : BaseAdapterTypeFactory() {

    fun type(): Int = UmrahSearchViewHolder.LAYOUT
    override fun type(viewModel: LoadingModel): Int = UmrahSearchLoadingViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                UmrahSearchViewHolder.LAYOUT -> UmrahSearchViewHolder(parent)
                UmrahSearchLoadingViewHolder.LAYOUT -> UmrahSearchLoadingViewHolder(parent)
                EmptyViewHolder.LAYOUT -> EmptyViewHolder(parent,callback)
                else -> super.createViewHolder(parent, type)
            }
}