package com.tokopedia.autocompletecomponent.universal.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactory
import java.util.ArrayList

class UniversalSearchAdapter(
    private val typeFactory: UniversalSearchTypeFactory
): RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    private var list = ArrayList<Visitable<*>>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

        @Suppress("UNCHECKED_CAST")
        return typeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("UNCHECKED_CAST")
        val data = list[position] as Visitable<UniversalSearchTypeFactory>

        return data.type(typeFactory)
    }

    fun updateList(newList: List<Visitable<*>>) {
        list.clear()
        list.addAll(newList)

        notifyItemRangeInserted(0, newList.size)
    }
}