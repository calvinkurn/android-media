package com.tokopedia.carouselproductcard.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

private typealias CarouselPagingListAdapter =
    ListAdapter<Visitable<TypeFactory>, AbstractViewHolder<*>>

internal class Adapter(
    private val typeFactory: TypeFactory,
): CarouselPagingListAdapter(DiffUtilItemCallback(typeFactory)) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return typeFactory.onCreateViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        try { (holder as AbstractViewHolder<Visitable<*>>).bind(getItem(position)) }
        catch(_: Throwable) { }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type(typeFactory)

    override fun onViewRecycled(holder: AbstractViewHolder<*>) {
        holder.onViewRecycled()
    }

    fun getItemAt(position: Int): Visitable<TypeFactory>? {
        if (position !in currentList.indices) return null
        return getItem(position)
    }
}
