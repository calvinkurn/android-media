package com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

@Suppress("UNCHECKED_CAST")
class HomeLeftCarouselAtcProductCardAdapter(
    differ: HomeLeftCarouselAtcProductCardDiffer,
    private val typeFactory: HomeLeftCarouselAtcProductCardTypeFactoryImplCompact
): ListAdapter<Visitable<*>, AbstractViewHolder<Visitable<*>>>(differ) {

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

