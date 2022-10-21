package com.tokopedia.tokopedianow.recipeautocomplete.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeAutoCompleteBinding
import com.tokopedia.tokopedianow.recipeautocomplete.di.component.DaggerRecipeAutoCompleteComponent
import com.tokopedia.tokopedianow.recipeautocomplete.presentation.viewmodel.TokoNowRecipeAutoCompleteViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRecipeAutoCompleteFragment: Fragment() {

    companion object {
        fun newInstance(): Fragment {
            return TokoNowRecipeAutoCompleteFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeAutoCompleteViewModel

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeAutoCompleteBinding>()

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowRecipeAutoCompleteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeRecipeListParam()
    }

    private fun setupUi() {
        binding?.apply {
            searchBar.searchHint = getString(R.string.tokopedianow_recipe_search_hint)
            searchBar.onTextSubmitListener = { text ->
                viewModel.submitSearch(
                    title = text
                )
            }
        }
    }

    private fun observeRecipeListParam() {
        observe(viewModel.recipeListParam) { queryParam ->
            RouteManager.route(
                context,
                ApplinkConstInternalTokopediaNow.RECIPE_SEARCH + queryParam
            )
            activity?.finish()
        }
    }

    private fun injectDependencies() {
        DaggerRecipeAutoCompleteComponent
            .builder()
            .baseAppComponent((requireContext().applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }
}