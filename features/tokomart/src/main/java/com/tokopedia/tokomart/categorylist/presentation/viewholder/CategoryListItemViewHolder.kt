package com.tokopedia.tokomart.categorylist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListAdapter
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListAdapterTypeFactory
import com.tokopedia.tokomart.categorylist.presentation.adapter.decoration.TokoMartCategoryListDecoration
import com.tokopedia.tokomart.categorylist.presentation.adapter.differ.TokoMartCategoryListDiffer
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import kotlinx.android.synthetic.main.item_tokomart_category_child.view.imageCategory
import kotlinx.android.synthetic.main.item_tokomart_category_child.view.textTitle
import kotlinx.android.synthetic.main.item_tokomart_category_list.view.*

class CategoryListItemViewHolder(itemView: View) : AbstractViewHolder<CategoryListItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_category_list
    }

    private val adapter by lazy {
        TokoMartCategoryListAdapter(
            TokoMartCategoryListAdapterTypeFactory(),
            TokoMartCategoryListDiffer()
        )
    }

    override fun bind(category: CategoryListItemUiModel) {
        itemView.run {
            textTitle.text = category.title
            showImageCategory(category)
            showChildCategory(category)
        }
    }

    private fun View.showImageCategory(category: CategoryListItemUiModel) {
        category.iconUrl?.let {
            imageCategory.loadImage(it)
            imageCategory.show()
        }
    }

    private fun View.showChildCategory(category: CategoryListItemUiModel) {
        if (category.childList.isNotEmpty()) {
            with(rvChildCategory) {
                addItemDecoration(TokoMartCategoryListDecoration())
                adapter = this@CategoryListItemViewHolder.adapter
                layoutManager = LinearLayoutManager(context)
                visibility = View.VISIBLE
            }
            adapter.submitList(category.childList)
            categoryDivider.show()
        }
    }
}