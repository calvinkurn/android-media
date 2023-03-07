package com.tokopedia.tokopedianow.recipehome.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipelist.base.fragment.BaseTokoNowRecipeListFragment
import com.tokopedia.tokopedianow.recipehome.di.component.DaggerRecipeHomeComponent
import com.tokopedia.tokopedianow.recipehome.presentation.viewmodel.TokoNowRecipeHomeViewModel
import javax.inject.Inject

class TokoNowRecipeHomeFragment: BaseTokoNowRecipeListFragment() {

    companion object {
        fun newInstance(): Fragment {
            return TokoNowRecipeHomeFragment()
        }

        const val PAGE_NAME = "TokoNow Recipe Home"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val searchHintData by lazy {
        listOf(HintData(
            getString(R.string.tokopedianow_recipe_search_hint)
        ))
    }

    override val enableHeaderBackground = true

    override val pageName = PAGE_NAME

    override val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(TokoNowRecipeHomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRecipeList()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun injectDependencies() {
        DaggerRecipeHomeComponent
            .builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}
