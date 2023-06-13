package com.tokopedia.tokopedianow.recipesearch.presentation.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.BottomSheetUtil.configureMaxHeight
import com.tokopedia.tokopedianow.common.util.BottomSheetUtil.setMaxHeight
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowRecipeSearchIngredientBinding
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_INGREDIENT_ID
import com.tokopedia.tokopedianow.recipesearch.di.component.DaggerRecipeSearchComponent
import com.tokopedia.tokopedianow.recipesearch.presentation.adapter.RecipeSearchIngredientAdapter
import com.tokopedia.tokopedianow.recipesearch.presentation.viewholder.RecipeSearchIngredientViewHolder
import com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel.TokoNowRecipeSearchIngredientViewModel
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.EXTRA_SELECTED_FILTER
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.ArrayList
import javax.inject.Inject

class TokoNowRecipeSearchIngredientBottomSheet: BottomSheetUnify() {

    companion object {
        private val TAG = TokoNowRecipeSearchIngredientBottomSheet::class.simpleName

        fun newInstance(selectedFilters: ArrayList<SelectedFilter>): TokoNowRecipeSearchIngredientBottomSheet {
            return TokoNowRecipeSearchIngredientBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_SELECTED_FILTER, selectedFilters)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)
            .get(TokoNowRecipeSearchIngredientViewModel::class.java)
    }

    private val adapter by lazy {
        RecipeSearchIngredientAdapter(
            listener = recipeSearchIngredientCallback()
        )
    }

    private var binding by autoClearedNullable<BottomsheetTokopedianowRecipeSearchIngredientBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedFilters = arguments
            ?.getParcelableArrayList(EXTRA_SELECTED_FILTER) ?: ArrayList()

        configureMaxHeight()
        observeLiveData()
        showProgressBar()

        viewModel.getIngredients()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initView() {
        binding = BottomsheetTokopedianowRecipeSearchIngredientBinding.inflate(LayoutInflater.from(context))

        clearContentPadding = true
        isFullpage = false
        showKnob = true
        showCloseIcon = false
        isHideable = true

        setTitle(getString(R.string.tokopedianow_recipe_search_ingredient_title_bottomsheet))
        setChild(binding?.root)
        setUi()
    }

    private fun observeLiveData() {
        observe(viewModel.ingredientItems) {
            adapter.submitList(ArrayList(it))
            showHideSearchButton()
            hideProgressBar()
            setMaxHeight()
        }
    }

    private fun setUi() {
        binding?.apply {
            tpSubtitle.text = getString(R.string.tokopedianow_recipe_search_ingredient_subtitle_bottomsheet)
            rvIngredients.layoutManager = LinearLayoutManager(context)
            rvIngredients.adapter = adapter
            rvIngredients.itemAnimator = null
        }
    }

    private fun recipeSearchIngredientCallback() = object : RecipeSearchIngredientViewHolder.RecipeSearchIngredientListener {
        override fun onSelectIngredient(isChecked: Boolean, id: String, title: String) {
            viewModel.onSelectIngredient(id, isChecked, title)
        }
    }

    private fun showHideSearchButton() {
        binding?.apply {
            val selectedIngredients = viewModel.selectedFilters.filter { it.parentId == PARAM_INGREDIENT_ID }
            val isShown = selectedIngredients.size.isMoreThanZero()
            if (isShown) {
                ubSearchBtn.setOnClickListener {
                    clickSearchBtn()
                }
                ubSearchBtn.text = getString(
                    R.string.tokopedianow_recipe_search_ingredient_button_bottomsheet,
                    selectedIngredients.size
                )
                ubSearchBtn.show()
                cvSearchBtnBackground.show()
            } else {
                ubSearchBtn.hide()
                cvSearchBtnBackground.hide()
            }
        }
    }

    private fun clickSearchBtn() {
        val intent = Intent()
        val selectedFilters = viewModel.selectedFilters
        intent.putParcelableArrayListExtra(EXTRA_SELECTED_FILTER, selectedFilters)
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            dismiss()
            finish()
        }
    }

    private fun showProgressBar() {
        binding?.progressBar?.show()
    }

    private fun hideProgressBar() {
        binding?.progressBar?.hide()
    }

    private fun injectDependencies() {
        DaggerRecipeSearchComponent
            .builder()
            .baseAppComponent((requireContext().applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }
}