package com.tokopedia.tokomart.category.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.tokomart.category.di.CategoryComponent
import com.tokopedia.tokomart.category.presentation.typefactory.CategoryTypeFactoryImpl
import com.tokopedia.tokomart.category.presentation.viewmodel.CategoryViewModel
import com.tokopedia.tokomart.searchcategory.presentation.view.BaseSearchCategoryFragment
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_DIRECTORY
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

    override fun getBaseAutoCompleteApplink() =
            super.getBaseAutoCompleteApplink() + "?" +
                    "${SearchApiConst.NAVSOURCE}=$TOKONOW_DIRECTORY"

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(CategoryComponent::class.java).inject(this)
    }

    override fun createTypeFactory() = CategoryTypeFactoryImpl(
            chooseAddressListener = this,
            titleListener = this,
            bannerListener = this,
            quickFilterListener = this,
            categoryFilterListener = this,
            productItemListener = this,
    )

    override fun getViewModel() = categoryViewModel

}