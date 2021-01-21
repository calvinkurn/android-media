package com.tokopedia.topads.common.view.adapter.etalase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.view.adapter.etalase.viewholder.EtalaseViewHolder
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseViewModel

/**
 * Author errysuprayogi on 11,November,2019
 */


class EtalaseAdapter(val typeFactory: EtalaseAdapterTypeFactory,
                     var items: MutableList<EtalaseViewModel>) : RecyclerView.Adapter<EtalaseViewHolder<EtalaseViewModel>>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtalaseViewHolder<EtalaseViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as EtalaseViewHolder<EtalaseViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: EtalaseViewHolder<EtalaseViewModel>, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data: MutableList<EtalaseViewModel>) {
        items = data
        notifyDataSetChanged()
    }
}
