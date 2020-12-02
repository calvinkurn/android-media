package com.tokopedia.product.addedit.category.presentation.viewholder

import android.view.View
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
        private val categoryAdapter: AddEditProductCategoryAdapter
): RecyclerView.ViewHolder(itemView) {

    fun bindData(category: CategoryUiModel?) {
        itemView.run {
            category?.let { category ->
                tvCategoryNameParent.text = category.categoryName

                setRecyclerView(category)

                itemCategory.setOnClickListener {
                    setIndentation(1, this)
                    category.isSelected = !category.isSelected
                    categories?.let {
                        listener.selectItemCategory(category, !category.child.isNullOrEmpty(), categoryAdapter)
                    }
                    showRecyclerView(category.isSelected)
                }
            }
        }
    }

    private fun getCategoryById(categoryId: String): CategoryUiModel? {
        return categories?.firstOrNull { it.categoryId == categoryId }
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

    fun showRecyclerView(isSelected: Boolean) {
        itemView.run {
            if (!isSelected) {
//                tvCategoryNameParent.setTextColor(resources.getColor(R.color.font_black_primary_70))
                rvLevelCategory.hide()
                ivCategoryParent.loadImageWithoutPlaceholder(R.drawable.product_add_edit_ic_chevron_down)
            } else {
//                tvCategoryNameParent.setTextColor(resources.getColor(R.color.green_400))
                rvLevelCategory.show()
                ivCategoryParent.loadImageWithoutPlaceholder(R.drawable.product_add_edit_ic_chevron_up)
            }
        }
    }

    private fun setIndentation(level: Int, view: View) {
//        val displayMetrics = view.context?.resources?.displayMetrics?.density ?: 0.0F
//        val dp = level * 16.toFloat()
//        val marginStart = (dp * displayMetrics + 0.5).toInt()
//        val params = LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        params.marginStart = marginStart
//        view.tvCategoryName.layoutParams = params
    }

    interface CategoryItemViewHolderListener {
        fun selectItemCategory(category: CategoryUiModel, isHasChild: Boolean, adapter: AddEditProductCategoryAdapter)
    }
}