package com.tokopedia.search.result.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import java.util.*

class InspirationCarouselOptionAdapter(private val typeFactory: InspirationCarouselOptionAdapterTypeFactory)
    : RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    private val list = mutableListOf<Visitable<InspirationCarouselOptionTypeFactory>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

        return typeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addAll(list: List<Visitable<InspirationCarouselOptionTypeFactory>>) {
        this.list.addAll(list)

        notifyDataSetChanged()
    }

    fun clearData() {
        val size = list.size
        list.clear()
        notifyItemRangeRemoved(0, size)
    }
}