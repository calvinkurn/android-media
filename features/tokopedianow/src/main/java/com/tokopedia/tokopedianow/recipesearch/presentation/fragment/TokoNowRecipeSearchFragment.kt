package com.tokopedia.tokopedianow.recipesearch.presentation.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipeautocomplete.presentation.fragment.TokoNowRecipeAutoCompleteFragment.Companion.QUERY_TITLE_KEY
import com.tokopedia.tokopedianow.recipelist.base.fragment.BaseTokoNowRecipeListFragment
import com.tokopedia.tokopedianow.recipesearch.di.component.DaggerRecipeSearchComponent
import com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel.TokoNowRecipeSearchViewModel
import javax.inject.Inject

class TokoNowRecipeSearchFragment: BaseTokoNowRecipeListFragment() {

    companion object {
        fun newInstance(): Fragment {
            return TokoNowRecipeSearchFragment()
        }

        private const val PAGE_NAME = "TokoNow Recipe Search"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val searchHintData by lazy {
        listOf(HintData(
            getString(R.string.tokopedianow_recipe_search_hint)
        ))
    }

    override val enableHeaderBackground = false

    override val pageName = PAGE_NAME

    override val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(TokoNowRecipeSearchViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecipeList()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun injectDependencies() {
        DaggerRecipeSearchComponent
            .builder()
            .baseAppComponent((requireContext().applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun Uri?.getTitle(): String {
        return this?.getQueryParameter(QUERY_TITLE_KEY).orEmpty()
    }

    private fun getRecipeList() {
        val uri = activity?.intent?.data
        viewModel.setTitleParam(uri.getTitle())
        viewModel.getRecipeList()
    }
}