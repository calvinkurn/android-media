package com.tokopedia.vouchercreation.product.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.ItemMvcFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.CategorySelection
import com.tokopedia.vouchercreation.product.list.view.viewholder.CategoryItemViewHolder

@SuppressLint("NotifyDataSetChanged")
class CategoryListAdapter :
        RecyclerView.Adapter<CategoryItemViewHolder>(),
        CategoryItemViewHolder.OnListItemClickListener {

    private var categorySelections: List<CategorySelection> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val binding = ItemMvcFilterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryItemViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bindData(categorySelections[position])
    }

    override fun getItemCount(): Int {
        return categorySelections.size
    }

    override fun onListItemClicked(isChecked: Boolean, position: Int) {
        categorySelections[position].isSelected = isChecked
    }

    fun setCategorySelections(categorySelections: List<CategorySelection>) {
        this.categorySelections = categorySelections
        notifyDataSetChanged()
    }

    fun getSelectedCategories(): List<CategorySelection> {
        val selectedCategories = categorySelections.filter { categorySelection ->
            categorySelection.isSelected
        }
        return selectedCategories
    }
}