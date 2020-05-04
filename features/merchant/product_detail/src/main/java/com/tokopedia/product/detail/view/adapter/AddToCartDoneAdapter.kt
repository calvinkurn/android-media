package com.tokopedia.product.detail.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationCarouselViewHolder

class AddToCartDoneAdapter(
        val recyclerView: RecyclerView,
        addToCartDoneTypeFactory: AddToCartDoneTypeFactory
): BaseAdapter<AddToCartDoneTypeFactory>(addToCartDoneTypeFactory){
    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {

        super.onBindViewHolder(holder, position)
    }
}