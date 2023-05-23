package com.tokopedia.product.addedit.category.presentation.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.category.presentation.adapter.AddEditProductCategoryAdapter
import com.tokopedia.product.addedit.category.presentation.model.CategoryUiModel
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class AddEditProductCategoryViewHolder(
    itemView: View,
    private val listener: CategoryItemViewHolderListener,
    private val categories: List<CategoryUiModel>?,
    private val categoryAdapter: AddEditProductCategoryAdapter,
    private val resultCategories: MutableList<CategoryUiModel>
) : RecyclerView.ViewHolder(itemView) {

    private var itemCategory: LinearLayout? = itemView.findViewById(R.id.itemCategory)
    private var tvCategoryNameParent: Typography? = itemView.findViewById(R.id.tvCategoryNameParent)
    private var rbCategory: RadioButtonUnify? = itemView.findViewById(R.id.rbCategory)
    private var rvLevelCategory: RecyclerView? = itemView.findViewById(R.id.rvLevelCategory)
    private var spacingLevelCategory: View? = itemView.findViewById(R.id.spacingLevelCategory)
    private var ivCategoryParent: AppCompatImageView? = itemView.findViewById(R.id.ivCategoryParent)
    private var categoryChildAdapter: AddEditProductCategoryAdapter? = null

    fun bindData(category: CategoryUiModel?) {
        itemView.run {
            category?.let { category ->
                val hasChild = isParent(category)
                tvCategoryNameParent?.text = category.categoryName

                checkParent(hasChild)
                setRecyclerView(category)
                setIndentation(category.categoryLevel)

                itemCategory?.setOnClickListener {
                    if (hasChild) {
                        category.isSelected = !category.isSelected
                        listener.selectCategoryItem(category, hasChild, categoryAdapter, adapterPosition)
                        checkCategorySelected(category)
                    } else {
                        rbCategory?.isChecked = !(rbCategory?.isChecked.orFalse())
                        resultCategories.add(category)
                        listener.selectCategoryItem(category.categoryId, resultCategories)
                    }
                }

                rbCategory?.setOnClickListener {
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
            rvLevelCategory?.adapter = categoryChildAdapter
            rvLevelCategory?.layoutManager = LinearLayoutManager(itemView.context)

            getCategoryById(category.categoryId)?.child?.let {
                categoryChildAdapter?.updateCategories(it)
                categoryChildAdapter?.putIntoTempCategories()
            }
        }
    }

    private fun checkCategorySelected(category: CategoryUiModel) {
        itemView.run {
            val iconRes: Drawable?
            if (!category.isSelected) {
                tvCategoryNameParent?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950
                    )
                )
                rvLevelCategory?.hide()
                spacingLevelCategory?.hide()
                ivCategoryParent?.loadImage(com.tokopedia.iconunify.R.drawable.iconunify_chevron_down)
                val resultSize = resultCategories.size - 1
                for (level in resultSize downTo category.categoryLevel) {
                    resultCategories.removeAt(level)
                }
                categoryChildAdapter?.resetCategories()
                categoryChildAdapter?.notifyDataSetChanged()
                iconRes = getIconUnifyDrawable(context, IconUnify.CHEVRON_DOWN,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            } else {
                tvCategoryNameParent?.setTextColor(ContextCompat.getColor(context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                rvLevelCategory?.show()
                spacingLevelCategory?.show()
                resultCategories.add(category)
                iconRes = getIconUnifyDrawable(context, IconUnify.CHEVRON_UP,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            }
            ivCategoryParent?.loadImage(iconRes)
        }
    }

    private fun checkParent(isParent: Boolean) {
        itemView.run {
            if (!isParent) {
                ivCategoryParent?.hide()
                rbCategory?.show()
            } else {
                ivCategoryParent?.show()
                rbCategory?.hide()
            }
        }
    }

    private fun setIndentation(level: Int) {
        val resources = itemView.context?.resources ?: return
        val marginStart = level *
            resources.getDimensionPixelSize(com.tokopedia.product.addedit.R.dimen.dp_16) +
            LEADING_MARGIN_START
        val params = LinearLayout.LayoutParams(
            Int.ZERO,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            WEIGHT_ITEM_VIEW
        )

        params.marginStart = marginStart.toInt()
        tvCategoryNameParent?.layoutParams = params
    }

    interface CategoryItemViewHolderListener {
        fun selectCategoryItem(category: CategoryUiModel, isHasChild: Boolean, adapter: AddEditProductCategoryAdapter, position: Int)
        fun selectCategoryItem(categoryId: String, categories: List<CategoryUiModel>?)
    }

    companion object {
        private const val WEIGHT_ITEM_VIEW = 1F
        private const val LEADING_MARGIN_START = 0.5F
    }
}
