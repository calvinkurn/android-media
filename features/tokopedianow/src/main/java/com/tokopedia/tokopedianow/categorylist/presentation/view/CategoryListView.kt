package com.tokopedia.tokopedianow.categorylist.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListChildUiModel.CategoryType
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryListBinding

class CategoryListView : LinearLayout {

    private lateinit var analytics: CategoryListAnalytics
    private lateinit var listener: CategoryListListener

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, analytics: CategoryListAnalytics, listener: CategoryListListener) : super(context) {
        this.analytics = analytics
        this.listener = listener
    }

    init {
        orientation = VERTICAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    fun setup(category: CategoryListItemUiModel) {
        category.childList.forEachIndexed { index, categoryL2 ->
            val itemView = if (categoryL2.type == CategoryType.CHILD_CATEGORY_ITEM) {
                createCategoryItemView(category, categoryL2).apply {
                    if(index == 0) setMargin(0, 0, 0, 0)
                }
            } else {
                createCategoryTextView(category, categoryL2)
            }
            addView(itemView)
        }
    }

    private fun createCategoryItemView(categoryLevel1: CategoryListItemUiModel, categoryL2: CategoryListChildUiModel): View {
        val binding = ItemTokopedianowCategoryListBinding.inflate(LayoutInflater.from(context), this, false).apply {
            textTitle.text = categoryL2.name
            textTitle.setWeight(categoryL2.textWeight)
            textTitle.setTextColor(ContextCompat.getColor(context, categoryL2.textColorId))

            categoryL2.imageUrl?.let {
                imageCategory.loadImage(it)
                imageCategory.show()
            }

            root.setOnClickListener {
                analytics.onClickLevelTwoCategory(categoryLevel1.id, categoryL2.id)
                RouteManager.route(context, categoryL2.appLink)
                listener.onClickCategoryItem()
            }
        }
        return binding.root
    }

    private fun createCategoryTextView(categoryLevel1: CategoryListItemUiModel, categoryL2: CategoryListChildUiModel): View {
        val binding = ItemTokopedianowCategoryListBinding.inflate(LayoutInflater.from(context), this, false).apply {
            val verticalMargin = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

            textTitle.text = context.getString(R.string.tokopedianow_all_category_text_format, categoryL2.name)
            textTitle.setWeight(categoryL2.textWeight)
            textTitle.setTextColor(ContextCompat.getColor(context, categoryL2.textColorId))

            root.setOnClickListener {
                analytics.onClickLihatSemuaCategory(categoryLevel1.id)
                RouteManager.route(context, categoryL2.appLink)
                listener.onClickCategoryItem()
            }

            setMargin(0, verticalMargin, 0, verticalMargin)
        }
        return binding.root
    }

    interface CategoryListListener {
        fun onClickCategoryItem()
    }
}