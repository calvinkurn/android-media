package com.tokopedia.tokopedianow.category.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.tokopedianow.category.di.component.CategoryComponent
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryNavigationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryTitleCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowViewCallback
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryMainViewModel
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

    override val adapter: CategoryAdapter by lazy {
        CategoryAdapter(
            typeFactory = CategoryAdapterTypeFactory(
                tokoNowView = TokoNowViewCallback(
                    fragment = this@TokoNowCategoryMainFragment
                ),
                tokoNowChooseAddressWidgetListener = TokoNowChooseAddressWidgetCallback(),
                categoryTitleListener = CategoryTitleCallback(
                    context = context,
                    warehouseId = viewModel.getWarehouseId()
                ),
                categoryNavigationListener = CategoryNavigationCallback()
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

    private fun observer() {
        viewModel.categoryHeader.observe(viewLifecycleOwner) {
            when(it) {
                is Success -> {
                    adapter.submitList(it.data)

                    viewModel.getCategoryFirstPage()
                }
                is Fail -> {}
            }
        }

        viewModel.categoryPage.observe(viewLifecycleOwner) {
            when(it) {
                is Success -> {
                    adapter.submitList(it.data)
                }
                is Fail -> {}
            }
        }
    }
}
