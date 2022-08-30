package com.tokopedia.tokopedianow.recipesearch.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipesearch.presentation.bottomsheet.TokoNowRecipeSearchIngredientBottomSheet

class TokoNowRecipeSearchIngredientFragment: Fragment() {

    companion object {
        fun newInstance(): TokoNowRecipeSearchIngredientFragment {
            return TokoNowRecipeSearchIngredientFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showEducationalInfoBottomSheet()
    }

    private fun showEducationalInfoBottomSheet() {
        val bottomSheet = TokoNowRecipeSearchIngredientBottomSheet.newInstance()
        bottomSheet.setOnDismissListener {
            activity?.finish()
        }
        bottomSheet.show(childFragmentManager)
    }
}