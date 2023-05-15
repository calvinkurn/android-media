package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryShowcaseAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryShowcaseAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryShowcaseBinding
import com.tokopedia.utils.view.binding.viewBinding

class CategoryShowcaseViewHolder(
    itemView: View
): AbstractViewHolder<CategoryShowcaseUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_showcase
    }

    private var binding: ItemTokopedianowCategoryShowcaseBinding? by viewBinding()

    private val adapter by lazy {
        CategoryShowcaseAdapter(
            typeFactory = CategoryShowcaseAdapterTypeFactory(),
            differ = CategoryDiffer()
        )
    }

    override fun bind(element: CategoryShowcaseUiModel) {
        binding?.setCategoryList(element)
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.setCategoryList(data: CategoryShowcaseUiModel) {
        rvCategory.run {
            adapter = this@CategoryShowcaseViewHolder.adapter
            layoutManager = GridLayoutManager(context, 3)
        }

        adapter.submitList(data.categoryListUiModel.orEmpty())
    }
}
