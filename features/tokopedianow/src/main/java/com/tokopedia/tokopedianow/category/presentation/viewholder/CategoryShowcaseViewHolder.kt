package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryShowcaseAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryShowcaseDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryShowcaseAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder.CategoryShowcaseItemListener
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.util.RecyclerViewGridUtil.addProductItemDecoration
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryShowcaseBinding
import com.tokopedia.utils.view.binding.viewBinding

class CategoryShowcaseViewHolder(
    itemView: View,
    private val categoryShowcaseItemListener: CategoryShowcaseItemListener? = null,
    private val categoryShowcaseHeaderListener: TokoNowDynamicHeaderListener? = null,
    private val productCardCompactListener: ProductCardCompactView.ProductCardCompactListener? = null,
    private val productCardCompactSimilarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener? = null,
    private val parentRecycledViewPool: RecyclerView.RecycledViewPool? = null,
    private val lifecycleOwner: LifecycleOwner? = null
): AbstractViewHolder<CategoryShowcaseUiModel>(itemView) {
    companion object {
        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1
        private const val RECYCLER_VIEW_ITEM_CACHE = 3

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_showcase
    }

    private var binding: ItemTokopedianowCategoryShowcaseBinding? by viewBinding()

    private val adapter: CategoryShowcaseAdapter by lazy {
        CategoryShowcaseAdapter(
            typeFactory = CategoryShowcaseAdapterTypeFactory(
                categoryShowcaseItemListener = categoryShowcaseItemListener,
                productCardCompactListener = productCardCompactListener,
                productCardCompactSimilarProductTrackerListener = productCardCompactSimilarProductTrackerListener,
                lifecycleOwner = lifecycleOwner
            ),
            differ = CategoryShowcaseDiffer()
        )
    }

    init {
        binding?.initRecyclerView()
    }

    override fun bind(element: CategoryShowcaseUiModel) {
        binding?.apply {
            when(element.state) {
                TokoNowLayoutState.LOADING -> showLoading()
                TokoNowLayoutState.SHOW -> showShowcase(element)
                else -> { /* nothing to do */ }
            }
        }
    }

    override fun bind(element: CategoryShowcaseUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.showShowcase(element)
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
                circleSeeAll = true,
                widgetId = element.id
            )
        )
        dhvHeader.setListener(
            headerListener = categoryShowcaseHeaderListener
        )
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.setDataToRecyclerView(
        element: CategoryShowcaseUiModel
    ) {
        dhvHeader.show()
        rvCategory.show()
        divider.show()
        showcaseShimmering.categoryShimmeringLayout.hide()
        adapter.submitList(element.productListUiModels.orEmpty())
    }

    private fun ItemTokopedianowCategoryShowcaseBinding.initRecyclerView() {
        rvCategory.apply {
            addProductItemDecoration()
            adapter = this@CategoryShowcaseViewHolder.adapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
                spanSizeLookup = getLayoutManagerSpanSize()
            }
            animation = null
            setRecycledViewPool(parentRecycledViewPool)
            setItemViewCacheSize(RECYCLER_VIEW_ITEM_CACHE)
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
