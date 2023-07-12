package com.tokopedia.tokopedianow.category.presentation.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2Component
import com.tokopedia.tokopedianow.category.di.module.CategoryL2ContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2Differ
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2AdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2ViewModel
import javax.inject.Inject

class TokoNowCategoryL2Fragment : BaseCategoryFragment() {

    companion object {
        fun newInstance(
            categoryL1: String,
            categoryL2: String,
            queryParamMap: HashMap<String, String>?
        ): TokoNowCategoryL2Fragment {
            return TokoNowCategoryL2Fragment().apply {
                this.categoryIdL1 = categoryL1
                this.categoryIdL2 = categoryL2
                this.currentCategoryId = categoryL2
                this.queryParamMap = queryParamMap
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: TokoNowCategoryL2ViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[TokoNowCategoryL2ViewModel::class.java]
    }

    override fun createAdapterTypeFactory(): CategoryL2AdapterTypeFactory {
        return CategoryL2AdapterTypeFactory(
            chooseAddressListener = createChooseAddressWidgetCallback(),
            tokoNowView = createTokoNowViewCallback()
        )
    }

    override fun createAdapterDiffer() = CategoryL2Differ()

    override fun initInjector() {
        DaggerCategoryL2Component.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryL2ContextModule(CategoryL2ContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun createChooseAddressWidgetCallback(): TokoNowChooseAddressWidgetCallback {
        return TokoNowChooseAddressWidgetCallback {

        }
    }
}
