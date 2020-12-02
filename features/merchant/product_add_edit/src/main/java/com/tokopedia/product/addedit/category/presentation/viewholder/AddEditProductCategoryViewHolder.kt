package com.tokopedia.product.addedit.category.presentation.viewholder

import android.graphics.Color
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
import kotlinx.android.synthetic.main.item_category_parent.view.*

class AddEditProductCategoryViewHolder(
        itemView: View,
        private val listener: CategoryItemViewHolderListener,
        private val categories: List<CategoryUiModel>?,
        private val categoryAdapter: AddEditProductCategoryAdapter,
): RecyclerView.ViewHolder(itemView) {

    fun bindData(category: CategoryUiModel?) {
        itemView.run {
            category?.let { category ->

                tvCategoryNameParent.text = category.categoryName
                val hasChild = isParent(category)

                checkParent(hasChild)
                setRecyclerView(category)
                setIndentation(category.categoryLevel)

                itemCategory.setOnClickListener {
                    if (hasChild) {
                        category.isSelected = !category.isSelected
                        categories?.let {
                            listener.selectCategoryItem(category, hasChild, categoryAdapter)
                        }
                        checkCategorySelected(category.isSelected)
                    } else {
                        listener.selectCategoryItem(category.categoryId)
                        rbCategory.isChecked = !rbCategory.isChecked
                    }
                }

                rbCategory.setOnClickListener {
                    listener.selectCategoryItem(category.categoryId)
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
            val adapter = AddEditProductCategoryAdapter(listener)
            rvLevelCategory.adapter = adapter
            rvLevelCategory.layoutManager = LinearLayoutManager(itemView.context)
            val categoryLevel = getCategoryById(category.categoryId)
            categoryLevel?.child?.let {
                adapter.updateCategories(it)
                adapter.putIntoTempCategories()
            }
        }
    }

    private fun checkCategorySelected(isSelected: Boolean) {
        itemView.run {
            if (!isSelected) {
                tvCategoryNameParent.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                rvLevelCategory.hide()
                ivCategoryParent.loadImageWithoutPlaceholder(R.drawable.product_add_edit_ic_chevron_down)
            } else {
                tvCategoryNameParent.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                rvLevelCategory.show()
                ivCategoryParent.loadImageWithoutPlaceholder(R.drawable.product_add_edit_ic_chevron_up)
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
        fun selectCategoryItem(category: CategoryUiModel, isHasChild: Boolean, adapter: AddEditProductCategoryAdapter)
        fun selectCategoryItem(categoryId: String)
    }
}