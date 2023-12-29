package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.typefactory.InspirationKeywordsTypeFactory

class InspirationKeywordAdapter(
    private val typeFactory: InspirationKeywordsTypeFactory
) : RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private val list = mutableListOf<Visitable<*>>()
    fun setList(listKeywords: List<InspirationKeywordDataView>){
        list.addAll(listKeywords)
        notifyItemRangeInserted(0, list.size)
    }

    val itemList: List<Visitable<*>>
        get() = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        (holder as AbstractViewHolder<Visitable<*>>).bind(list[position])
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            @Suppress("UNCHECKED_CAST")
            (holder as AbstractViewHolder<Visitable<*>>).bind(list[position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("UNCHECKED_CAST")
        return (list[position] as Visitable<InspirationKeywordsTypeFactory>).type(typeFactory)
    }
}
