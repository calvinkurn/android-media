package com.tokopedia.tokopedianow.recipesearch.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipesearch.presentation.bottomsheet.TokoNowRecipeSearchIngredientBottomSheet
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.EXTRA_SELECTED_FILTER
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import java.util.ArrayList

class TokoNowRecipeSearchIngredientFragment: Fragment() {

    companion object {
        fun newInstance(selectedFilters: ArrayList<SelectedFilter>): TokoNowRecipeSearchIngredientFragment {
            return TokoNowRecipeSearchIngredientFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_SELECTED_FILTER, selectedFilters)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedIngredientsId = arguments
            ?.getParcelableArrayList<SelectedFilter>(EXTRA_SELECTED_FILTER) ?: ArrayList()
        showBottomSheet(selectedIngredientsId)
    }

    private fun showBottomSheet(selectedFilters: ArrayList<SelectedFilter>) {
        val bottomSheet = TokoNowRecipeSearchIngredientBottomSheet.newInstance(selectedFilters)
        bottomSheet.setOnDismissListener {
            activity?.finish()
        }
        bottomSheet.show(childFragmentManager)
    }
}