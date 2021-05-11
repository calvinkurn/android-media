package com.tokopedia.tokomart.search.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.tokomart.search.di.SearchComponent
import com.tokopedia.tokomart.search.presentation.typefactory.SearchTypeFactoryImpl
import com.tokopedia.tokomart.search.presentation.viewmodel.SearchViewModel
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryFragment
import javax.inject.Inject

class SearchFragment: BaseSearchCategoryFragment() {

    companion object {

        @JvmStatic
        fun create(): SearchFragment {
            return SearchFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var searchViewModel: SearchViewModel

    override val toolbarPageName = "TokoNow Search"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        activity?.let {
            searchViewModel = ViewModelProvider(it, viewModelFactory).get(SearchViewModel::class.java)
        }
    }

    override fun createNavToolbarIconBuilder() = IconBuilder()
            .addCart()
            .addGlobalNav()

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(SearchComponent::class.java).inject(this)
    }

    override fun createTypeFactory() = SearchTypeFactoryImpl(BannerComponentCallback(context))

    override fun getViewModel() = searchViewModel
}