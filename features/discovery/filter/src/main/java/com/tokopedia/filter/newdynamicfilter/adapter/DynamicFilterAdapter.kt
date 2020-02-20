package com.tokopedia.filter.newdynamicfilter.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder

import java.util.ArrayList

class DynamicFilterAdapter(private val typeFactory: DynamicFilterTypeFactory) : RecyclerView.Adapter<DynamicFilterViewHolder>() {

    var filterList: List<Filter> = ArrayList()
        set(filterList) {
            field = filterList
            notifyDataSetChanged()
        }

    override fun getItemCount() = filterList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicFilterViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: DynamicFilterViewHolder, position: Int) {
        holder.bind(filterList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return typeFactory.type(filterList[position])
    }

    fun getItemPosition(filter: Filter): Int {
        return filterList.indexOf(filter)
    }
}
