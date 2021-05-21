package com.tokopedia.tokomart.categorylist.presentation.viewholder

import android.view.View
import android.view.animation.Animation
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListAdapter
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListAdapterTypeFactory
import com.tokopedia.tokomart.categorylist.presentation.adapter.differ.TokoMartCategoryListDiffer
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokomart.common.base.viewholder.BaseExpandableViewHolder
import kotlinx.android.synthetic.main.item_tokomart_category_list.view.*

class CategoryListItemViewHolder(
    itemView: View
) : BaseExpandableViewHolder<CategoryListItemUiModel>(itemView) {

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

    private var expandAnimationListener: Animation.AnimationListener? = null

    override fun bind(data: CategoryListItemUiModel) {
        super.bind(data)
        itemView.run {
            textTitle.text = data.title
            showImageCategory(data)
            setupChildCategory()
        }
        createExpandListener(data)
    }

    override fun expandCollapseLayoutId() = R.id.rvChildCategory

    override fun expandAnimationListener() = expandAnimationListener

    private fun View.showImageCategory(category: CategoryListItemUiModel) {
        category.iconUrl?.let {
            imageCategory.loadImage(it)
        }
    }

    private fun View.setupChildCategory() {
        with(rvChildCategory) {
            adapter = this@CategoryListItemViewHolder.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun createExpandListener(category: CategoryListItemUiModel) {
        expandAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
                adapter.submitList(category.childList)
            }
        }
    }

}