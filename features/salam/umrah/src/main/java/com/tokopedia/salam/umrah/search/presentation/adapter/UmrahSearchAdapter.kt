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

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        if (visitables[holder.adapterPosition] is UmrahSearchProduct) {
            val product: UmrahSearchProduct = visitables[holder.adapterPosition] as UmrahSearchProduct
            if (!product.isSetOnClicked) {
                product.isSetOnClicked = true
                holder.itemView.setOnClickListener {
                    onClickListener.onItemClicked(product, position)
                }
            }
        }
        super.onBindViewHolder(holder, position)
    }

    override fun getItemId(position: Int): Long {
        return if (visitables[position] is UmrahSearchProduct) {
            val visitable = (visitables[position] as UmrahSearchProduct).id
            visitable.toLong()
        } else super.getItemId(position)
    }

    override fun isItemClickableByDefault(): Boolean = false

    interface OnClickListener {
        fun onItemClicked(product: UmrahSearchProduct, position: Int)
    }
}