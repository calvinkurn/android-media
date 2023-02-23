package com.tokopedia.autocompletecomponent.suggestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class SuggestionAdapter(
    private val typeFactory: SuggestionTypeFactory,
) : RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private val list: MutableList<Visitable<*>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        (holder as AbstractViewHolder<Visitable<*>>).bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return (list[position] as Visitable<SuggestionTypeFactory>).type(typeFactory)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun replaceData(list: List<Visitable<*>>) {
        clearData()
        addAll(list)
    }

    private fun clearData() {
        val size = this.itemCount
        this.list.clear()
        notifyItemRangeRemoved(0, size)
    }

    private fun addAll(list: List<Visitable<*>>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun getLastItem(criteria: (Visitable<*>) -> Boolean): Int {
        val item = list.lastOrNull { criteria(it) }
        return item?.let { list.indexOf(item) } ?: -1
    }

}
