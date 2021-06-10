package com.tokopedia.hotel.search.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.search.data.model.Property

/**
 * @author by jessica on 2019-07-26
 */

class HotelSearchResultAdapter(private val onClickListener: OnClickListener, propertyAdapterTypeFactory: PropertyAdapterTypeFactory) :
        BaseListAdapter<Property, PropertyAdapterTypeFactory>(propertyAdapterTypeFactory) {

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                try {
                    val property: Property = visitables[holder.adapterPosition] as Property
                    onClickListener.onItemClicked(property, position)
                } catch (e: Exception) {
                    //
                }
            }
        }
        super.onBindViewHolder(holder, position)
    }

    override fun isItemClickableByDefault(): Boolean = false

    interface OnClickListener {
        fun onItemClicked(property: Property, position: Int)
    }
}