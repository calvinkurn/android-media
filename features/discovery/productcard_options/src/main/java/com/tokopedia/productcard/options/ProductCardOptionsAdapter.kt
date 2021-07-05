package com.tokopedia.productcard.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

internal class ProductCardOptionsAdapter(private val productCardOptionsTypeFactory: ProductCardOptionsTypeFactory)
    : RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    private val list = mutableListOf<Visitable<*>>()

    fun setList(list: List<Visitable<*>>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

        @Suppress("UNCHECKED_CAST")
        return productCardOptionsTypeFactory.createViewHolder(view, viewType)
                as AbstractViewHolder<Visitable<*>>
    }

    override fun getItemViewType(position: Int): Int {
        return (list[position] as Visitable<ProductCardOptionsTypeFactory>).type(productCardOptionsTypeFactory)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(list[position])
    }
}