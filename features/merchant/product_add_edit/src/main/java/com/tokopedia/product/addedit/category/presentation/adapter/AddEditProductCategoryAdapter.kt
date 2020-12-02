package com.tokopedia.product.addedit.category.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.category.common.Constant.INCREMENT_CATEGORY_LEVEL
import com.tokopedia.product.addedit.category.presentation.model.CategoryUiModel
import com.tokopedia.product.addedit.category.presentation.viewholder.AddEditProductCategoryViewHolder
import kotlinx.android.synthetic.main.item_category_parent.view.*

class AddEditProductCategoryAdapter(
        private val listener: AddEditProductCategoryViewHolder.CategoryItemViewHolderListener,
) : RecyclerView.Adapter<AddEditProductCategoryViewHolder>()  {

    private val categories  =  mutableListOf<CategoryUiModel>()
    private val tempCategories = mutableListOf<CategoryUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddEditProductCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_category_parent, parent, false)
        return AddEditProductCategoryViewHolder(itemView, listener, categories, this)
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

    fun setCategories(category: CategoryUiModel, isParent: Boolean) {
        if (isParent) {
            val categories = mutableListOf<CategoryUiModel>()
            categories.add(category)
            updateCategories(categories)
        }
    }

    fun resetCategories() {
        updateCategories(tempCategories)
    }
}