package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.sort

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.ItemTokofoodSearchQuickSortBinding
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodQuickSortUiModel

class TokofoodQuickSortAdapter(private val listener: Listener) : RecyclerView.Adapter<TokofoodQuickSortAdapter.ViewHolder>(),
    InnerListener {

    private val sortItems = mutableListOf<TokofoodQuickSortUiModel>()

    override fun onRadioButtonChecked(checkedIndex: Int) {
        var selectedUiModel: TokofoodQuickSortUiModel? = null
        val newSortItems = sortItems.mapIndexed { index, tokofoodQuickSortUiModel ->
            if (checkedIndex == index) {
                selectedUiModel = tokofoodQuickSortUiModel
                tokofoodQuickSortUiModel.copy(isSelected = true)
            } else {
                tokofoodQuickSortUiModel.copy(isSelected = false)
            }
        }
        setSortItems(newSortItems)
        listener.onSelectSortItem(selectedUiModel)
    }

    fun setSortItems(items: List<TokofoodQuickSortUiModel>) {
        sortItems.clear()
        sortItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTokofoodSearchQuickSortBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sortItems[position])
    }

    override fun getItemCount(): Int = sortItems.size

    inner class ViewHolder(
        private val binding: ItemTokofoodSearchQuickSortBinding,
        private val listener: InnerListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: TokofoodQuickSortUiModel) {
            setText(uiModel.name)
            setChecked(uiModel.isSelected)
            setRootClickListener()
            setRadioButtonCheckedListener()
        }

        private fun setText(name: String) {
            binding.tvItemTokofoodSearchQuickSort.text = name
        }

        private fun setChecked(isChecked: Boolean) {
            binding.radioItemTokofoodSearchQuickSort.isChecked = isChecked
        }

        private fun setRootClickListener() {
            binding.root.setOnClickListener {
                val isChecked = binding.radioItemTokofoodSearchQuickSort.isChecked
                binding.radioItemTokofoodSearchQuickSort.isChecked = !isChecked
                binding.radioItemTokofoodSearchQuickSort.callOnClick()
            }
        }

        private fun setRadioButtonCheckedListener() {
            binding.radioItemTokofoodSearchQuickSort.setOnClickListener {
                binding.radioItemTokofoodSearchQuickSort.isChecked = true
                listener.onRadioButtonChecked(adapterPosition)
            }
        }


    }

    interface Listener {
        fun onSelectSortItem(uiModel: TokofoodQuickSortUiModel?)
    }

}

interface InnerListener {
    fun onRadioButtonChecked(checkedIndex: Int)
}