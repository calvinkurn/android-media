package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.databinding.CategoryFilterItemBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryFilterListUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoryFilterAdapter(private val listener: Listener): RecyclerView.Adapter<CategoryFilterAdapter.ViewHolder>() {

    private val categoryFilterList = mutableListOf<CategoryFilterListUiModel>()

    fun setCategoryFilterList(newItems: List<CategoryFilterListUiModel>?) {
        if (newItems.isNullOrEmpty()) return
        categoryFilterList.clear()
        categoryFilterList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateCategoryFilterSelected(position: Int) {
        categoryFilterList.mapIndexed { index, item ->
            item.isSelected = index == position
            notifyItemChanged(index)
        }
        GlobalScope.launch(Dispatchers.Main) {
            delay(DELAY_UPDATE_SELECTED)
            listener.onCategoryItemSelected(categoryFilterList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryFilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (categoryFilterList.isNotEmpty()) {
            holder.bind(categoryFilterList[position])
        }
    }

    override fun getItemCount(): Int = categoryFilterList.size

    inner class ViewHolder(private val binding: CategoryFilterItemBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item: CategoryFilterListUiModel) {
                with(binding) {
                    if (adapterPosition > Int.ZERO) {
                        dividerCategory.show()
                    } else {
                        dividerCategory.hide()
                    }
                    tvCategoryName.text = item.categoryUiModel.title
                    tvTotalMenu.text = item.totalMenu
                    icSelectedItem.isVisible = item.isSelected

                    root.setOnClickListener {
                        updateCategoryFilterSelected(adapterPosition)
                    }
                }
            }
        }

    interface Listener {
        fun onCategoryItemSelected(categoryFilterList: List<CategoryFilterListUiModel>)
    }

    companion object {
        const val DELAY_UPDATE_SELECTED = 300L
    }
}