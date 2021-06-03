package com.tokopedia.tokomart.categorylist.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListAdapter
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListAdapterTypeFactory
import com.tokopedia.tokomart.categorylist.presentation.adapter.decoration.TokoNowCategoryListDecoration
import com.tokopedia.tokomart.categorylist.presentation.adapter.differ.TokoMartCategoryListDiffer
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListItemViewHolder.CategoryListListener

class TokoNowCategoryList : RecyclerView {

    private lateinit var categoryAdapter: TokoMartCategoryListAdapter

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, listener: CategoryListListener) : super(context) {
        categoryAdapter = TokoMartCategoryListAdapter(
            TokoMartCategoryListAdapterTypeFactory(listener),
            TokoMartCategoryListDiffer()
        )
    }

    fun setupCategoryList(data: List<CategoryListChildUiModel>) {
        adapter = categoryAdapter
        isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(TokoNowCategoryListDecoration())
        categoryAdapter.submitList(data)
    }
}