package com.tokopedia.carouselproductcard.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.R
import com.tokopedia.carouselproductcard.paging.list.ProductCardListDataView
import com.tokopedia.carouselproductcard.paging.list.ProductCardListViewHolder

private typealias CarouselPagingListAdapter =
    ListAdapter<Visitable<TypeFactory>, AbstractViewHolder<Visitable<TypeFactory>>>

internal class Adapter(
    private val typeFactory: TypeFactory,
): CarouselPagingListAdapter(DiffUtilItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<Visitable<TypeFactory>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        @Suppress("UNCHECKED_CAST")
        return typeFactory.onCreateViewHolder(view, viewType)
            as AbstractViewHolder<Visitable<TypeFactory>>
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<TypeFactory>>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type(typeFactory)

    override fun onViewRecycled(holder: AbstractViewHolder<Visitable<TypeFactory>>) {
        holder.onViewRecycled()
    }

    fun getItemAt(position: Int): Visitable<TypeFactory> = getItem(position)
}
