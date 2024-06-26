package com.tokopedia.productcard.compact.productcardcarousel.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.compact.productcardcarousel.presentation.adapter.differ.ProductCardCompactCarouselDiffer
import com.tokopedia.productcard.compact.productcardcarousel.presentation.adapter.typefactory.ProductCardCompactCarouselTypeFactoryImpl

class ProductCardCompactCarouselAdapter(
    differ: ProductCardCompactCarouselDiffer,
    private val typeFactory: ProductCardCompactCarouselTypeFactoryImpl
) : ListAdapter<Visitable<*>, AbstractViewHolder<Visitable<*>>>(differ) {

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun getItemViewType(position: Int): Int {
        return typeFactory.type(getItem(position))
    }
}
