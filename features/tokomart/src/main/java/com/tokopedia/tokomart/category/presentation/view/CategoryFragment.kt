package com.tokopedia.tokomart.category.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.tokomart.category.di.CategoryComponent
import com.tokopedia.tokomart.category.presentation.typefactory.CategoryTypeFactoryImpl
import com.tokopedia.tokomart.category.presentation.viewmodel.CategoryViewModel
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryFragment
import javax.inject.Inject

class CategoryFragment: BaseSearchCategoryFragment() {

    companion object {

        @JvmStatic
        fun create(): CategoryFragment {
            return CategoryFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var categoryViewModel: CategoryViewModel

    override val toolbarPageName = "TokoNow Category"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        activity?.let {
            categoryViewModel = ViewModelProvider(it, viewModelFactory).get(CategoryViewModel::class.java)
        }
    }

    override fun createNavToolbarIconBuilder() = IconBuilder()
            .addShare()
            .addCart()
            .addGlobalNav()

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(CategoryComponent::class.java).inject(this)
    }

    override fun createTypeFactory() = CategoryTypeFactoryImpl()

    override fun getViewModel() = categoryViewModel
}