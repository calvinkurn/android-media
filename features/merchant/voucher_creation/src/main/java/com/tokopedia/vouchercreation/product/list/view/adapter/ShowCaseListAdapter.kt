package com.tokopedia.vouchercreation.product.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.ItemMvcFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.ShowCaseSelection
import com.tokopedia.vouchercreation.product.list.view.viewholder.ShowCaseItemViewHolder

@SuppressLint("NotifyDataSetChanged")
class ShowCaseListAdapter :
        RecyclerView.Adapter<ShowCaseItemViewHolder>(),
        ShowCaseItemViewHolder.OnListItemClickListener {

    private var showCaseSelections: List<ShowCaseSelection> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowCaseItemViewHolder {
        val binding = ItemMvcFilterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowCaseItemViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ShowCaseItemViewHolder, position: Int) {
        holder.bindData(showCaseSelections[position])
    }

    override fun getItemCount(): Int {
        return showCaseSelections.size
    }

    override fun onListItemClicked(isChecked: Boolean, position: Int) {
        showCaseSelections[position].isSelected = isChecked
    }

    fun setShowCaseSelections(showCaseSelections: List<ShowCaseSelection>) {
        this.showCaseSelections = showCaseSelections
        notifyDataSetChanged()
    }

    fun getSelectedShowCases(): List<ShowCaseSelection> {
        val selectedShowCases = showCaseSelections.filter { showCaseSelection ->
            showCaseSelection.isSelected
        }
        return selectedShowCases
    }
}