package com.tokopedia.salam.umrah.search.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
/**
 * @author by M on 11/11/2019
 */
class UmrahSearchAdapter(private val onClickListener: OnClickListener, umrahSearchAdapterTypeFactory: UmrahSearchAdapterTypeFactory) :
        BaseListAdapter<UmrahSearchProduct, UmrahSearchAdapterTypeFactory>(umrahSearchAdapterTypeFactory) {

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        holder.itemView.setOnClickListener {
            if(visitables[holder.adapterPosition] is UmrahSearchProduct) {
                val product: UmrahSearchProduct = visitables[holder.adapterPosition] as UmrahSearchProduct
                onClickListener.onItemClicked(product, position)
            }
        }
        super.onBindViewHolder(holder, position)
    }

    override fun isItemClickableByDefault(): Boolean = false

    interface OnClickListener {
        fun onItemClicked(product: UmrahSearchProduct, position: Int)
    }
}