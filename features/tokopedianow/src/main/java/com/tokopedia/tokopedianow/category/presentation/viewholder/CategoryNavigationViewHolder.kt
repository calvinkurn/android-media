package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryNavigationAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryNavigationAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryNavigationBinding
import com.tokopedia.utils.view.binding.viewBinding

class CategoryNavigationViewHolder(
    itemView: View,
    private val listener: CategoryNavigationListener? = null,
): AbstractViewHolder<CategoryNavigationUiModel>(itemView),
    CategoryNavigationItemViewHolder.CategoryNavigationItemListener
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_navigation
    }

    private var binding: ItemTokopedianowCategoryNavigationBinding? by viewBinding()

    private val adapter by lazy {
        CategoryNavigationAdapter(
            typeFactory = CategoryNavigationAdapterTypeFactory(
                categoryNavigationItemListener = this@CategoryNavigationViewHolder
            ),
            differ = CategoryDiffer()
        )
    }

    init {
        binding?.initRecyclerView()
    }

    override fun bind(data: CategoryNavigationUiModel) {
        binding?.apply {
            when(data.state) {
                TokoNowLayoutState.SHOW -> showSuccessState(data)
                TokoNowLayoutState.LOADING -> showLoadingState()
                else -> { /* nothing to do */ }
            }
        }
    }

    override fun onCategoryItemClicked(data: CategoryNavigationItemUiModel, itemPosition: Int) {
        listener?.onCategoryNavigationItemClicked(data, itemPosition)
    }

    override fun onCategoryItemImpressed(data: CategoryNavigationItemUiModel, itemPosition: Int) {
        listener?.onCategoryNavigationItemImpressed(data, itemPosition)
    }

    private fun ItemTokopedianowCategoryNavigationBinding.showLoadingState() {
        rvCategory.hide()
        categoryShimmering.categoryShimmeringLayout.show()
    }

    private fun ItemTokopedianowCategoryNavigationBinding.showSuccessState(
        data: CategoryNavigationUiModel
    ) {
        categoryShimmering.categoryShimmeringLayout.hide()
        rvCategory.show()

        showCategoryMenu(
            data = data
        )
    }

    private fun ItemTokopedianowCategoryNavigationBinding.showCategoryMenu(
        data: CategoryNavigationUiModel
    ) {
        binding.apply {
            adapter.submitList(data.categoryListUiModel)
            root.addOnImpressionListener(data) {
                listener?.onCategoryNavigationWidgetImpression(data)
            }
        }
    }

    private fun ItemTokopedianowCategoryNavigationBinding.initRecyclerView() {
        rvCategory.run {
            adapter = this@CategoryNavigationViewHolder.adapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    interface CategoryNavigationListener {
        fun onCategoryNavigationWidgetRetried()
        fun onCategoryNavigationItemClicked(data: CategoryNavigationItemUiModel, itemPosition: Int)
        fun onCategoryNavigationItemImpressed(data: CategoryNavigationItemUiModel, itemPosition: Int)
        fun onCategoryNavigationWidgetImpression(data: CategoryNavigationUiModel)
    }
}
