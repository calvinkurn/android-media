package com.tokopedia.tokopedianow.category.presentation.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.tokopedianow.category.di.component.CategoryComponent
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryTitleViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryMainViewModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import javax.inject.Inject

class TokoNowCategoryMainFragment: TokoNowCategoryBaseFragment() {

    companion object {
        fun newInstance(): TokoNowCategoryMainFragment {
            return TokoNowCategoryMainFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowCategoryMainViewModel

    init {
        adapter = CategoryAdapter(
            typeFactory = CategoryAdapterTypeFactory(
                tokoNowView = createTokoNowViewCallback(),
                tokoNowChooseAddressWidgetListener = createChooseAddressCallback(),
                tokoNowCategoryMenuListener = createCategoryMenuCallback(),
                categoryTitleListener = createCategoryTitleCallback()
            )
        )
    }

    override fun initInjector() = getComponent(CategoryComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFirstPage(
            navToolbarHeight = navToolbarHeight
        )

        observer()
    }

    private fun createTokoNowViewCallback(): TokoNowView {
        return object : TokoNowView {
            override fun getFragmentPage(): Fragment = this@TokoNowCategoryMainFragment
            override fun getFragmentManagerPage(): FragmentManager = childFragmentManager
            override fun refreshLayoutPage() { /* nothing to do */ }
            override fun getScrollState(adapterPosition: Int): Parcelable? = null
            override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) { /* nothing to do */ }
        }
    }

    private fun createChooseAddressCallback(): TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener {
        return object : TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener {
            override fun onChooseAddressWidgetRemoved() {
                if (binding?.rvCategory?.isComputingLayout == false) {

                }
            }

            override fun onClickChooseAddressWidgetTracker() { /* nothing to do */ }
        }
    }

    private fun createCategoryMenuCallback(): TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {
        return object : TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {
            override fun onCategoryMenuWidgetRetried() { }

            override fun onSeeAllCategoryClicked() { }

            override fun onCategoryMenuItemClicked(
                data: TokoNowCategoryMenuItemUiModel,
                itemPosition: Int
            ) { }

            override fun onCategoryMenuItemImpressed(
                data: TokoNowCategoryMenuItemUiModel,
                itemPosition: Int
            ) { }

            override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) { }

        }
    }

    private fun createCategoryTitleCallback(): CategoryTitleViewHolder.CategoryTitleListener {
        return object : CategoryTitleViewHolder.CategoryTitleListener {
            override fun onClickAnotherCategory() {
                RouteManager.route(
                    context,
                    ApplinkConstInternalTokopediaNow.CATEGORY_LIST,
                    viewModel.warehouseId
                )
            }
        }
    }

    private fun observer() {
        viewModel.categoryFirstPage.observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }
    }
}
