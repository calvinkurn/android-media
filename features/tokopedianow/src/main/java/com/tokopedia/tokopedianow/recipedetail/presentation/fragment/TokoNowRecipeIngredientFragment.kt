package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeIngredientBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowRecipeIngredientFragment: Fragment() {

    companion object {
        fun newInstance(): TokoNowRecipeIngredientFragment {
            return TokoNowRecipeIngredientFragment()
        }
    }

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeIngredientBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowRecipeIngredientBinding.inflate(inflater)
        return binding?.root
    }
}