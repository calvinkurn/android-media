package com.tokopedia.vouchercreation.product.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.ItemMvcFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.SortSelection
import com.tokopedia.vouchercreation.product.list.view.viewholder.SortItemViewHolder

@SuppressLint("NotifyDataSetChanged")
class SortListAdapter :
        RecyclerView.Adapter<SortItemViewHolder>(),
        SortItemViewHolder.OnListItemClickListener {

    private var sortSelections: List<SortSelection> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortItemViewHolder {
        val binding = ItemMvcFilterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SortItemViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: SortItemViewHolder, position: Int) {
        holder.bindData(sortSelections[position])
    }

    override fun getItemCount(): Int {
        return sortSelections.size
    }

    override fun onListItemClicked(position: Int) {
        val selectedSort = sortSelections[position]
        sortSelections.filter { sortSelection ->
            sortSelection.id != selectedSort.id
        }.forEach { selection -> selection.isSelected = false }
        sortSelections[position].isSelected = true
        notifyDataSetChanged()
    }

    fun setSortSelections(sortSelections: List<SortSelection>) {
        this.sortSelections = sortSelections
        notifyDataSetChanged()
    }

    fun getSelectedSort(): List<SortSelection> {
        val selectedSort = sortSelections.filter { sortSelection ->
            sortSelection.isSelected
        }
        return selectedSort
    }
}