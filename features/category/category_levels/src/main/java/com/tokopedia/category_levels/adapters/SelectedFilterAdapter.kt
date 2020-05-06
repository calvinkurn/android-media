package com.tokopedia.category_levels.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.category_levels.R
import com.tokopedia.category_levels.adapters.viewholder.catalogShimmer.SelectedFilterItemViewHolder
import com.tokopedia.category_levels.view.interfaces.SelectedFilterListener
import com.tokopedia.filter.common.data.Option
import java.util.*

class SelectedFilterAdapter(private val clickListener: SelectedFilterListener) : RecyclerView.Adapter<SelectedFilterItemViewHolder>() {

    private val optionList = ArrayList<Option>()

    fun setOptionList(optionList: List<Option>) {
        this.optionList.clear()
        this.optionList.addAll(optionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedFilterItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_empty_state_selected_filter_item, parent, false)
        return SelectedFilterItemViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: SelectedFilterItemViewHolder, position: Int) {
        holder.bind(optionList[position])
    }

    override fun getItemCount(): Int {
        return optionList.size
    }
}