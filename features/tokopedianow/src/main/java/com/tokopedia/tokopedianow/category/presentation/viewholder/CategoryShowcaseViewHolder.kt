package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryShowcaseAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryShowcaseAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.util.RecyclerViewGridUtil.addProductItemDecoration
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryShowcaseBinding
import com.tokopedia.utils.view.binding.viewBinding

class CategoryShowcaseViewHolder(
    itemView: View
): AbstractViewHolder<CategoryShowcaseUiModel>(itemView) {
    companion object {
        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_showcase
    }

    private var binding: ItemTokopedianowCategoryShowcaseBinding? by viewBinding()

    init {
        binding?.initRecyclerView()
    }

    private var adapter: CategoryShowcaseAdapter? = null

    override fun bind(element: CategoryShowcaseUiModel) {
        binding?.apply {
            when(element.state) {
                TokoNowLayoutState.LOADING -> showLoading()
                TokoNowLayoutState.SHOW -> showShowcase(element)
                else -> { /* nothing to do */ }
            }
        }
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.showLoading() {
        dhvHeader.hide()
        rvCategory.hide()
        divider.hide()
        showcaseShimmering.categoryShimmeringLayout.show()
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.showShowcase(
        element: CategoryShowcaseUiModel
    ) {
        setDataToHeader(element)
        setDataToRecyclerView(element)
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.setDataToHeader(
        element: CategoryShowcaseUiModel
    ) {
        dhvHeader.setModel(
            TokoNowDynamicHeaderUiModel(
                title = element.title,
                ctaTextLink = element.seeAllAppLink,
                circleSeeAll = true
            )
        )
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.setDataToRecyclerView(
        element: CategoryShowcaseUiModel
    ) {
        dhvHeader.show()
        rvCategory.show()
        divider.show()
        showcaseShimmering.categoryShimmeringLayout.hide()
        adapter?.submitList(element.categoryListUiModel.orEmpty())
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.initRecyclerView() {
        adapter = CategoryShowcaseAdapter(
            typeFactory = CategoryShowcaseAdapterTypeFactory(),
            differ = CategoryDiffer()
        )

        rvCategory.run {
            addProductItemDecoration()
            adapter = this@CategoryShowcaseViewHolder.adapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
                spanSizeLookup = getLayoutManagerSpanSize()
            }
        }
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.getLayoutManagerSpanSize() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (rvCategory.adapter?.getItemViewType(position)) {
                CategoryShowcaseItemViewHolder.LAYOUT -> SPAN_FULL_SPACE
                else -> SPAN_COUNT
            }
        }
    }
}
