package com.tokopedia.tokopedianow.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowProductCardCarouselDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductCardCarouselTypeFactoryImpl

class TokoNowProductCardCarouselAdapter(
    differ: TokoNowProductCardCarouselDiffer,
    private val typeFactory: TokoNowProductCardCarouselTypeFactoryImpl
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
