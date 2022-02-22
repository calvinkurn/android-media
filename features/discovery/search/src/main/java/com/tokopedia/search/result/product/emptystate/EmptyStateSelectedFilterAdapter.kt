package com.tokopedia.search.result.product.emptystate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.common.data.Option

class EmptyStateSelectedFilterAdapter(
    private val clickListener: EmptyStateListener
): RecyclerView.Adapter<EmptyStateSelectedFilterViewHolder>() {

    private val optionList = mutableListOf<Option>()

    fun setOptionList(optionList: List<Option>) {
        this.optionList.clear()
        this.optionList.addAll(optionList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmptyStateSelectedFilterViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(EmptyStateSelectedFilterViewHolder.LAYOUT, parent,false)

        return EmptyStateSelectedFilterViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: EmptyStateSelectedFilterViewHolder, position: Int) {
        holder.bind(optionList[position])
    }

    override fun getItemCount(): Int {
        return optionList.size
    }
}