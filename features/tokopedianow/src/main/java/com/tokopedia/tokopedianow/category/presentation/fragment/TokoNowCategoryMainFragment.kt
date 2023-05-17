package com.tokopedia.tokopedianow.category.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.di.component.CategoryComponent
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryNavigationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryTitleCallback
import com.tokopedia.tokopedianow.category.presentation.callback.ProductRecommendationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowCategoryMenuCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowViewCallback
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryMainViewModel
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowCategoryMainFragment: TokoNowCategoryBaseFragment() {

    companion object {
        fun newInstance(): TokoNowCategoryMainFragment {
            return TokoNowCategoryMainFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowCategoryMainViewModel

    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    override val userId: String
        get() = viewModel.getUserId()

    override val categoryIdL1: String
        get() = viewModel.categoryIdL1

    override val categoryIdL2: String
        get() = String.EMPTY

    override val categoryIdL3: String
        get() = String.EMPTY

    override val currentCategoryId: String
        get() = viewModel.categoryIdL1

    override val adapter: CategoryAdapter by lazy {
        CategoryAdapter(
            typeFactory = CategoryAdapterTypeFactory(
                categoryTitleListener = createTitleCallback(),
                categoryNavigationListener = createCategoryNavigationCallback(),
                tokoNowView = createTokoNowViewCallback(),
                tokoNowChooseAddressWidgetListener = createTokoNowChooseAddressWidgetCallback(),
                tokoNowCategoryMenuListener = createTokoNowCategoryMenuCallback(),
                tokoNowProductRecommendationListener = createProductRecommendationCallback()
            ),
            differ = CategoryDiffer()
        )
    }

    override fun initInjector() = getComponent(CategoryComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategoryHeader(
            navToolbarHeight = navToolbarHeight
        )

        observer()
    }

    override fun loadMore(isAtTheBottomOfThePage: Boolean) {
        viewModel.loadMore(isAtTheBottomOfThePage)
    }

    override fun refreshLayout() {
        viewModel.refreshLayout(
            navToolbarHeight = navToolbarHeight
        )
    }

    private fun observer() {
        observeCategoryHeader()
        observeCategoryPage()
        observeIsScrollNotNeeded()
    }

    private fun observeCategoryHeader() {
        viewModel.categoryHeader.observe(viewLifecycleOwner) {
            when(it) {
                is Success -> {
                    adapter.submitList(it.data)

                    viewModel.getFirstPage()
                }
                is Fail -> {}
            }
        }
    }

    private fun observeCategoryPage() {
        viewModel.categoryPage.observe(viewLifecycleOwner) {
            when(it) {
                is Success -> {
                    adapter.submitList(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun observeIsScrollNotNeeded() {
        viewModel.isOnScrollNotNeeded.observe(viewLifecycleOwner) { isNotNeeded ->
            if (isNotNeeded) {
                binding?.rvCategory?.removeOnScrollListener(onScrollListener)
            } else {
                binding?.rvCategory?.addOnScrollListener(onScrollListener)
            }
        }
    }

    /**
     * Callback Sections
     */

    private fun createTitleCallback() = CategoryTitleCallback(
        context = context,
        warehouseId = viewModel.getWarehouseId()
    )

    private fun createCategoryNavigationCallback() = CategoryNavigationCallback()

    private fun createTokoNowViewCallback() = TokoNowViewCallback(
        fragment = this@TokoNowCategoryMainFragment
    ) {
        viewModel.refreshLayout(navToolbarHeight)
    }

    private fun createTokoNowCategoryMenuCallback() = TokoNowCategoryMenuCallback()

    private fun createTokoNowChooseAddressWidgetCallback() = TokoNowChooseAddressWidgetCallback()

    private fun createProductRecommendationCallback() = ProductRecommendationCallback(
        productRecommendationViewModel = productRecommendationViewModel,
        activity = activity,
        startActivityForResult = ::startActivityForResult
    )
}
