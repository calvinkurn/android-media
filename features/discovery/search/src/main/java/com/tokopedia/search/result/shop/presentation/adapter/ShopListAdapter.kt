package com.tokopedia.search.result.shop.presentation.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactory
import com.tokopedia.search.result.shop.presentation.diffutil.ShopListDiffUtilCallback
import java.util.ArrayList

open class ShopListAdapter(
        private val shopListTypeFactory: ShopListTypeFactory
): RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    private val list = ArrayList<Visitable<*>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

        @Suppress("UNCHECKED_CAST")
        return shopListTypeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("UNCHECKED_CAST")
        val data = list[position] as Visitable<AdapterTypeFactory>

        return data.type(shopListTypeFactory as AdapterTypeFactory)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        val data = list[position]
        holder.bind(data)
    }

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(ShopListDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }
}