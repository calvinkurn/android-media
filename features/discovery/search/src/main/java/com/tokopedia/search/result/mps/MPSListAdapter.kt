package com.tokopedia.search.result.mps

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.utils.ComparableId

class MPSListAdapter(
    private val mpsTypeFactory: MPSTypeFactory,
    private val listListener: ListListener,
): ListAdapter<Visitable<*>, AbstractViewHolder<Visitable<*>>>(MPSDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

        @Suppress("UNCHECKED_CAST")
        return mpsTypeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("UNCHECKED_CAST")
        val visitableItem = getItem(position) as Visitable<MPSTypeFactory>

        return visitableItem.type(mpsTypeFactory)
    }

    override fun onCurrentListChanged(
        previousList: List<Visitable<*>>,
        currentList: List<Visitable<*>>
    ) {
        super.onCurrentListChanged(previousList, currentList)

        listListener.onCurrentListChanged(previousList, currentList)
    }
}

private class MPSDiffUtil: DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean =
        if (oldItem is ComparableId && newItem is ComparableId)
            oldItem.compare(newItem)
        else
            oldItem::class.java == newItem::class.java

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean = true
}

interface ListListener {

    fun onCurrentListChanged(previousList: List<Visitable<*>>, currentList: List<Visitable<*>>)
}
