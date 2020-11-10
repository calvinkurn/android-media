package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import kotlinx.android.synthetic.main.filter_child_checkbox_item.view.*

class SomSubFilterChildCheckBoxAdapter(private val somSubChildFilterListener: SomSubChildFilterListener)
    : RecyclerView.Adapter<SomSubFilterChildCheckBoxAdapter.SomSubFilterChildCheckViewHolder>() {

    private var subChildFilterList = mutableListOf<SomFilterChipsUiModel.ChildStatusUiModel>()
    private var idList = mutableSetOf<Int>()
    private var keyFilter = ""

    fun getSubChildFilterList() = subChildFilterList

    fun setSubFilterList(newSubChildFilterList: List<SomFilterChipsUiModel.ChildStatusUiModel>,
                         keyFilter: String) {
        this.keyFilter = keyFilter
        this.subChildFilterList.clear()
        this.subChildFilterList.addAll(newSubChildFilterList.toMutableList())
        notifyDataSetChanged()
    }

    fun updateCheckboxFilter(updatedState: Boolean, position: Int) {
        val isSelected = subChildFilterList.getOrNull(position)
        subChildFilterList.map { filterItemUiModel ->
            if (isSelected == filterItemUiModel) {
                filterItemUiModel.isChecked = updatedState
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SomSubFilterChildCheckViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.filter_child_checkbox_item, parent, false)
        return SomSubFilterChildCheckViewHolder(view)
    }

    override fun onBindViewHolder(holder: SomSubFilterChildCheckViewHolder, position: Int) {
        holder.bind(subChildFilterList[position])
    }

    override fun getItemCount(): Int = subChildFilterList.size

    inner class SomSubFilterChildCheckViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: SomFilterChipsUiModel.ChildStatusUiModel) {
            with(itemView) {
                label_checkbox.text = data.text
                cb_filter.setOnCheckedChangeListener(null)
                cb_filter.isChecked = data.isChecked

                subChildFilterList.filter { it.isChecked }.forEach {
                    idList.add(it.childId.firstOrNull() ?: 0)
                }

                label_checkbox.setOnClickListener {
                    checkBoxClicked(cb_filter.isChecked)
                }
                cb_filter.setOnCheckedChangeListener { _, isChecked ->
                    checkBoxClicked(isChecked)
                }
            }
        }

        private fun checkBoxClicked(isChecked: Boolean) {
            val id = subChildFilterList[adapterPosition].childId.firstOrNull() ?: 0
            if (isChecked) idList.add(id) else idList.remove(id)
            updateCheckboxFilter(isChecked, adapterPosition)
            somSubChildFilterListener.onCheckboxChildItemClicked(idList.toList(), adapterPosition, keyFilter)
        }
    }

    interface SomSubChildFilterListener {
        fun onCheckboxChildItemClicked(childIdList: List<Int>, childPosition: Int, keyFilter: String)
    }
}