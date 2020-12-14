package com.tokopedia.product.addedit.specification.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.specification.presentation.adapter.viewholder.SpecificationValueViewHolder

class SpecificationValueAdapter (
        private val onSpecificationValueAdapterClickListener: OnSpecificationValueAdapterClickListener
) :
        RecyclerView.Adapter<SpecificationValueViewHolder>(),
        SpecificationValueViewHolder.OnSpecificationValueViewHolderClickListener {

    interface OnSpecificationValueAdapterClickListener {
        fun onSpecificationValueTextClicked(position: Int)
    }

    private var items: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationValueViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_specification_value, parent, false)
        return SpecificationValueViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SpecificationValueViewHolder, position: Int) {
        val item = getData(position)
        holder.bindData(item, item.reversed())
    }

    fun setData(items: List<String>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addData(item: String) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun getData(position: Int): String {
        return items.getOrNull(position).orEmpty()
    }

    override fun onSpecificationValueTextClicked(position: Int) {
        onSpecificationValueAdapterClickListener.onSpecificationValueTextClicked(position)
    }
}