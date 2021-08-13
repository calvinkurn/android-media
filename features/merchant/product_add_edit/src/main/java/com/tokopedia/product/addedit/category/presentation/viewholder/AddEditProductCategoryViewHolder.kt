package com.tokopedia.product.addedit.category.presentation.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.category.presentation.adapter.AddEditProductCategoryAdapter
import com.tokopedia.product.addedit.category.presentation.model.CategoryUiModel
import kotlinx.android.synthetic.main.item_category.view.*

class AddEditProductCategoryViewHolder(
        itemView: View,
        private val listener: CategoryItemViewHolderListener,
        private val categories: List<CategoryUiModel>?,
        private val categoryAdapter: AddEditProductCategoryAdapter,
        private val resultCategories: MutableList<CategoryUiModel>
): RecyclerView.ViewHolder(itemView) {

    private var categoryChildAdapter: AddEditProductCategoryAdapter? = null

    fun bindData(category: CategoryUiModel?) {
        itemView.run {
            category?.let { category ->
                val hasChild = isParent(category)
                tvCategoryNameParent.text = category.categoryName

                checkParent(hasChild)
                setRecyclerView(category)
                setIndentation(category.categoryLevel)

                itemCategory.setOnClickListener {
                    if (hasChild) {
                        category.isSelected = !category.isSelected
                        listener.selectCategoryItem(category, hasChild, categoryAdapter, adapterPosition)
                        checkCategorySelected(category)
                    } else {
                        rbCategory.isChecked = !rbCategory.isChecked
                        resultCategories.add(category)
                        listener.selectCategoryItem(category.categoryId, resultCategories)
                    }
                }

                rbCategory.setOnClickListener {
                    resultCategories.add(category)
                    listener.selectCategoryItem(category.categoryId, resultCategories)
                }
            }
        }
    }

    private fun getCategoryById(categoryId: String): CategoryUiModel? {
        return categories?.firstOrNull { it.categoryId == categoryId }
    }

    private fun isParent(category: CategoryUiModel): Boolean {
        return !category.child.isNullOrEmpty()
    }

    private fun setRecyclerView(category: CategoryUiModel) {
        itemView.run {
            categoryChildAdapter = AddEditProductCategoryAdapter(listener, resultCategories)
            rvLevelCategory.adapter = categoryChildAdapter
            rvLevelCategory.layoutManager = LinearLayoutManager(itemView.context)

            getCategoryById(category.categoryId)?.child?.let {
                categoryChildAdapter?.updateCategories(it)
                categoryChildAdapter?.putIntoTempCategories()
            }
        }
    }

    private fun checkCategorySelected(category: CategoryUiModel) {
        itemView.run {
            if (!category.isSelected) {
                tvCategoryNameParent.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
                rvLevelCategory.hide()
                spacingLevelCategory.hide()
                ivCategoryParent.loadImageWithoutPlaceholder(R.drawable.product_add_edit_ic_chevron_down)
                val resultSize = resultCategories.size - 1
                for (level in resultSize downTo category.categoryLevel) {
                    resultCategories.removeAt(level)
                }
                categoryChildAdapter?.resetCategories()
                categoryChildAdapter?.notifyDataSetChanged()
            } else {
                tvCategoryNameParent.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                rvLevelCategory.show()
                spacingLevelCategory.show()
                ivCategoryParent.loadImageWithoutPlaceholder(R.drawable.product_add_edit_ic_chevron_up)
                resultCategories.add(category)
            }
        }
    }

    private fun checkParent(isParent: Boolean) {
        itemView.run {
            if (!isParent) {
                ivCategoryParent.hide()
                rbCategory.show()
            } else {
                ivCategoryParent.show()
                rbCategory.hide()
            }
        }
    }

    private fun setIndentation(level: Int) {
        val displayMetrics = itemView.context?.resources?.displayMetrics?.density ?: 0.0F
        val dp = level * 16.toFloat()
        val marginStart = (dp * displayMetrics + 0.5).toInt()
        val params = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
        )
        params.marginStart = marginStart
        itemView.tvCategoryNameParent.layoutParams = params
    }

    interface CategoryItemViewHolderListener {
        fun selectCategoryItem(category: CategoryUiModel, isHasChild: Boolean, adapter: AddEditProductCategoryAdapter, position: Int)
        fun selectCategoryItem(categoryId: String, categories: List<CategoryUiModel>?)
    }
}