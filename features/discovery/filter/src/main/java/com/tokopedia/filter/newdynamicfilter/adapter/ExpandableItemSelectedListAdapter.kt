package com.tokopedia.filter.newdynamicfilter.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.design.item.DeletableItemView
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.NumberParseHelper
import com.tokopedia.filter.newdynamicfilter.helper.RatingHelper
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView

import java.util.ArrayList

class ExpandableItemSelectedListAdapter(private val filterView: DynamicFilterView) : RecyclerView.Adapter<ExpandableItemSelectedListAdapter.ViewHolder>() {

    private var selectedOptionsList: List<Option> = ArrayList()

    override fun getItemCount(): Int {
        return selectedOptionsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(parent.context).inflate(R.layout.selected_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(selectedOptionsList[position], position)
    }

    fun setSelectedOptionsList(selectedOptionsList: List<Option>) {
        this.selectedOptionsList = selectedOptionsList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var selectedItem: DeletableItemView? = itemView.findViewById(R.id.selected_item)

        fun bind(option: Option, position: Int) {
            if (Option.KEY_RATING.equals(option.key)) {
                val ratingCount = NumberParseHelper.safeParseInt(option.name)
                selectedItem?.setItemDrawable(RatingHelper.getRatingDrawable(ratingCount))
            } else {
                selectedItem?.setItemName(option.name)
            }

            selectedItem?.setOnDeleteListener {
                filterView.removeSelectedOption(option)
                (selectedOptionsList as MutableList).removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
        }
    }
}
