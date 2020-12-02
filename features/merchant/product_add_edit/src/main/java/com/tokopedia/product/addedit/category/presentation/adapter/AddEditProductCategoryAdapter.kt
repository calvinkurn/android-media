package com.tokopedia.product.addedit.category.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.category.presentation.model.CategoryUiModel
import com.tokopedia.product.addedit.category.presentation.viewholder.AddEditProductCategoryViewHolder
import kotlinx.android.synthetic.main.item_category_parent.view.*

class AddEditProductCategoryAdapter(
        private val listener: AddEditProductCategoryViewHolder.CategoryItemViewHolderListener,
) : RecyclerView.Adapter<AddEditProductCategoryViewHolder>()  {

    private val categories  =  mutableListOf<CategoryUiModel>()
    private val tempCategories = mutableListOf<CategoryUiModel>()
    private var viewHolder : AddEditProductCategoryViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddEditProductCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_category_parent, parent, false)
        viewHolder = AddEditProductCategoryViewHolder(itemView, listener, categories, this)
        return viewHolder as AddEditProductCategoryViewHolder
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: AddEditProductCategoryViewHolder, position: Int) {
        holder.bindData(categories[position])
    }


    fun updateCategories(models: List<CategoryUiModel>) {
        val diffCallback = CategoryDiffCallback(categories, models)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categories.clear()
        categories.addAll(models)
        diffResult.dispatchUpdatesTo(this)
    }

    fun putIntoTempCategories() {
        tempCategories.clear()
        tempCategories.addAll(categories)
    }

    fun setCategories(category: CategoryUiModel, isHasChild: Boolean) {
        if (isHasChild) {
            val categories = mutableListOf<CategoryUiModel>()
            categories.add(category)
//            viewHolder?.showRecyclerView(isSelected)
            updateCategories(categories)
        }
    }

    fun resetCategories() {
        updateCategories(tempCategories)
    }
}


class CategoryDiffCallback(
        private val oldCategories: List<CategoryUiModel>,
        private val newCategories: List<CategoryUiModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldCategories.size
    }

    override fun getNewListSize(): Int {
        return newCategories.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategories[oldItemPosition].categoryId === newCategories[newItemPosition].categoryId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = oldCategories[oldItemPosition]
        val newEmployee = newCategories[newItemPosition]
        return oldEmployee == newEmployee
    }
}
