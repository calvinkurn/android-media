package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import kotlinx.android.synthetic.main.filter_checkbox_item.view.cb_filter
import kotlinx.android.synthetic.main.filter_checkbox_item.view.label_checkbox

class SomSubFilterCheckboxAdapter(private val somSubFilterListener: SomSubCheckboxFilterListener):
        RecyclerView.Adapter<SomSubFilterCheckboxAdapter.CheckboxViewHolder>(){

    private var listSubFilter = mutableListOf<SomFilterChipsUiModel>()
    private var idFilter: String = ""

    fun getSubFilterList() = listSubFilter

    fun setSubFilterList(newSubFilterList: List<SomFilterChipsUiModel>, idFilter: String) {
        this.idFilter = idFilter
        val callBack = SomSubFilterDiffUtil(listSubFilter, newSubFilterList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        this.listSubFilter.clear()
        this.listSubFilter.addAll(newSubFilterList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateCheckboxFilter(updatedState: Boolean, position: Int) {
        val isSelected = listSubFilter.getOrNull(position)
        listSubFilter.map { filterItemUiModel ->
            if (isSelected == filterItemUiModel) {
                filterItemUiModel.isSelected = updatedState
                notifyItemChanged(position)
            }
        }
    }

    fun resetCheckboxFilter() {
        listSubFilter.mapIndexed { index, filterItemUiModel ->
            if (filterItemUiModel.isSelected) {
                filterItemUiModel.isSelected = false
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.filter_checkbox_item, parent, false)
        return CheckboxViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSubFilter.size
    }

    override fun onBindViewHolder(holder: CheckboxViewHolder, position: Int) {
        val element = listSubFilter.getOrNull(position) ?: SomFilterChipsUiModel()
        holder.bind(element)
    }

    inner class CheckboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SomFilterChipsUiModel) {
            with(itemView) {
                label_checkbox.text = item.name
                cb_filter.apply {
                    setOnCheckedChangeListener(null)
                    isChecked = item.isSelected
                    skipAnimation()
                }

                setOnClickListener {
                    cb_filter.isChecked = !cb_filter.isChecked
                }
                cb_filter.setOnCheckedChangeListener { _, isChecked ->
                    checkBoxClicked(isChecked)
                }
            }
        }

        private fun checkBoxClicked(isChecked: Boolean) {
            with(itemView) {
                val id = listSubFilter[adapterPosition].id
                updateCheckboxFilter(isChecked, adapterPosition)
                somSubFilterListener.onCheckboxItemClicked(id, adapterPosition, cb_filter.isChecked)
            }
        }
    }

    interface SomSubCheckboxFilterListener {
        fun onCheckboxItemClicked(id: Int, position: Int, checked: Boolean)
    }
}